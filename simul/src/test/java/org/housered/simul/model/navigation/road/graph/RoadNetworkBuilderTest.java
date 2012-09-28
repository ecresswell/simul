package org.housered.simul.model.navigation.road.graph;

import static org.housered.simul.model.location.Vector.v;
import static org.housered.simul.model.navigation.road.graph.RoadNetworkBuilderTest.assertContainsRoad;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.awt.geom.Rectangle2D;

import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.road.graph.RoadNetworkBuilder.BlockGroup;
import org.junit.Test;

public class RoadNetworkBuilderTest
{
    @Test
    public void shouldAttachBlockToRightOfExistingBlock()
    {
        Rectangle2D.Double blockA = new Rectangle2D.Double(0, 0, 10, 10);
        Rectangle2D.Double blockB = new Rectangle2D.Double(20, 0, 10, 10);

        RoadNetworkBuilder b = new RoadNetworkBuilder(1, 3);
        b.addBlock(blockA);
        b.attachBlockToRight(blockA, blockB);
        RoadGraph g = b.getGraph();

        //just check the interlinks
        //        assertContainsRoad(g, 14, -4, 30, -4);
        //        assertContainsRoad(g, 30, -1, 14, -1);
        //        assertContainsRoad(g, 14, 11, 30, 11);
        //        assertContainsRoad(g, 30, 14, 14, 14);
    }

    @Test
    public void shouldAddRoadsGoingBothWaysAroundABlock()
    {
        Rectangle2D.Double block = new Rectangle2D.Double(0, 0, 10, 10);

        RoadNetworkBuilder b = new RoadNetworkBuilder(1, 3);
        RoadGraph g = b.buildNetwork(block);

        //inner road
        assertContainsRoad(g, 11, -1, -1, -1);
        assertContainsRoad(g, -1, -1, -1, 11);
        assertContainsRoad(g, -1, 11, 11, 11);
        assertContainsRoad(g, 11, 11, 11, -1);

        //outside road, with junction points
        assertContainsRoad(g, -1, -4, 11, -4);
        assertContainsRoad(g, 14, -1, 14, 11);
        assertContainsRoad(g, 11, 14, -1, 14);
        assertContainsRoad(g, -4, 11, -4, -1);

        assertContainsRoad(g, -4, -4, -1, -4);
        assertContainsRoad(g, 11, -4, 14, -4);
        assertContainsRoad(g, 14, -4, 14, -1);
        assertContainsRoad(g, 14, 11, 14, 14);
        assertContainsRoad(g, 14, 14, 11, 14);
        assertContainsRoad(g, -1, 14, -4, 14);
        assertContainsRoad(g, -4, 14, -4, 11);
        assertContainsRoad(g, -4, -1, -4, -4);

        //junction roads
        assertContainsBothWaysRoad(g, -4, -4, -1, -1);
        assertContainsBothWaysRoad(g, -1, -4, -4, -1);
        assertContainsBothWaysRoad(g, 14, -4, 11, -1);
        assertContainsBothWaysRoad(g, 11, -4, 14, -1);
        assertContainsBothWaysRoad(g, 14, 14, 11, 11);
        assertContainsBothWaysRoad(g, 14, 11, 11, 14);
        assertContainsBothWaysRoad(g, -4, 14, -1, 11);
        assertContainsBothWaysRoad(g, -1, 14, -4, 11);

        assertEquals(16, g.getRoadNodes().size());
    }

    @Test
    public void shouldReplaceNodesInTheBlockGroupsToo()
    {
        Rectangle2D.Double block = new Rectangle2D.Double(0, 0, 10, 10);

        RoadNetworkBuilder b = new RoadNetworkBuilder(1, 3);
        RoadGraph g = b.buildNetwork(block);
        BlockGroup<BlockGroup<RoadNode>> keyPoints = b.getKeyPoints(block);

        assertEquals(getNodeClosestTo(g, 11, 11), keyPoints.getBottomRight().getTopLeft());

        RoadNode replacement = new RoadNode(11, 11);
        b.replaceNodeWithOtherNode(keyPoints.getBottomRight().getTopLeft(), replacement);

        assertEquals(replacement, keyPoints.getBottomRight().getTopLeft());
    }

    public static void assertContainsRoad(RoadGraph graph, double sx, double sy, double ex, double ey)
    {
        assertContainsRoad(graph, v(sx, sy), v(ex, ey));
    }

    public static void assertContainsBothWaysRoad(RoadGraph graph, double sx, double sy, double ex, double ey)
    {
        assertContainsRoad(graph, v(sx, sy), v(ex, ey));
        assertContainsRoad(graph, v(ex, ey), v(sx, sy));
    }

    public static void assertContainsRoad(RoadGraph graph, Vector start, Vector end)
    {
        for (RoadNode n : graph.getRoadNodes())
            if (n.getPosition().equals(start))
                for (RoadEdge e : n.getEdges())
                    if (e.getOtherNode(n).getPosition().equals(end))
                        return;

        fail("Could not find road");
    }

    public static RoadNode getNodeClosestTo(RoadGraph graph, double x, double y)
    {
        double minDistance = 0;
        RoadNode minNode = null;

        for (RoadNode node : graph.getRoadNodes())
        {
            double distance = node.getPosition().distance(x, y);
            if (minNode == null || distance < minDistance)
            {
                minNode = node;
                minDistance = distance;
            }
        }

        return minNode;
    }

    @Test
    public void shouldReturnNodeClosestToPoint()
    {
        RoadNode a = new RoadNode(0, 0);
        RoadNode b = new RoadNode(5, 2);
        RoadGraph graph = new RoadGraph();
        graph.connectNodesInADirectedWay(a, b);

        assertEquals(a, getNodeClosestTo(graph, 0, 0));
        assertEquals(b, getNodeClosestTo(graph, 5, 1));
        assertEquals(a, getNodeClosestTo(graph, -111113, Double.MIN_VALUE));
    }
}
