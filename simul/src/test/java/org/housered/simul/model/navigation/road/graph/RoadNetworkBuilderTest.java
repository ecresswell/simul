package org.housered.simul.model.navigation.road.graph;

import static org.housered.simul.model.location.Vector.v;
import static org.housered.simul.model.navigation.road.graph.RoadNetworkBuilderTest.assertContainsRoad;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.road.graph.RoadNetworkBuilder.BlockGroup;
import org.housered.simul.model.world.Camera;
import org.housered.simul.view.swing.SwingGraphicsAdapter;
import org.junit.Test;

import com.sun.corba.se.impl.orbutil.graph.Graph;
import com.sun.imageio.plugins.common.ImageUtil;

public class RoadNetworkBuilderTest
{
    @Test
    public void shouldAttachBlockToRightOfExistingBlock() throws IOException
    {
        Rectangle2D.Double blockA = new Rectangle2D.Double(0, 0, 10, 10);
        Rectangle2D.Double blockB = new Rectangle2D.Double(20, 0, 10, 10);
        
        RoadNetworkBuilder b = new RoadNetworkBuilder(1, 3);
        RoadGraph g = b.getGraph();
        
        b.addBlock(blockA);
        outputGraph(g, "before");
        b.attachBlockToRight(blockA, blockB);
        outputGraph(g, "after");

        //just check the interlinks
        assertContainsRoad(g, 14, -4, 31, -4);
        assertContainsRoad(g, 31, -1, 14, -1);
        assertContainsRoad(g, 14, 11, 31, 11);
        assertContainsRoad(g, 31, 14, 14, 14);
        
        BlockGroup<BlockGroup<RoadNode>> aPoints = b.getKeyPoints(blockA);
        BlockGroup<BlockGroup<RoadNode>> bPoints = b.getKeyPoints(blockB);
        
        assertGroupsEqual(aPoints.getTopRight(), bPoints.getTopLeft());
        assertGroupsEqual(aPoints.getBottomRight(), bPoints.getBottomLeft());
    }
    
    private void assertGroupsEqual(BlockGroup<RoadNode> a, BlockGroup<RoadNode> b)
    {
        assertEquals(a.getTopRight(), b.getTopRight());
        assertEquals(a.getBottomRight(), b.getBottomRight());
        assertEquals(a.getTopLeft(), b.getTopLeft());
        assertEquals(a.getBottomLeft(), b.getBottomLeft());
    }
    
    public void outputGraph(RoadGraph graph, String fileName) throws IOException
    {
        int size = 800;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Camera camera = new Camera(size, size);
        camera.incrementXOffset(390);
        camera.incrementYOffset(390);
        camera.zoom(0.1);
        graph.render(new SwingGraphicsAdapter((Graphics2D) image.getGraphics(), camera));
        File writeOut = new File(fileName + ".png");
        System.out.println(writeOut.getAbsolutePath());
        ImageIO.write(image, "PNG", writeOut);
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
        b.replace(keyPoints.getBottomRight().getTopLeft(), replacement);

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
