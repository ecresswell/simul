package org.housered.simul.model.navigation.road.graph;

import static java.util.Arrays.asList;
import static org.housered.simul.model.location.Vector.v;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.road.graph.RoadNetworkBuilder.BlockGroup;
import org.housered.simul.model.world.Camera;
import org.housered.simul.view.swing.SwingGraphicsAdapter;
import org.junit.Test;

public class RoadNetworkBuilderTest
{
    private static boolean renderGraphs = true;

    @Test
    public void shouldAttachBlockToRightOfExistingBlock() throws IOException
    {
        Rectangle2D.Double blockA = new Rectangle2D.Double(0, 0, 10, 10);
        Rectangle2D.Double blockB = new Rectangle2D.Double(20, 0, 10, 10);

        RoadNetworkBuilder b = new RoadNetworkBuilder(1, 3);
        RoadGraph g = b.getGraph();

        b.addBlock(blockA);
        //        outputGraph(g, "before");
        b.attachBlockToRight(blockA, blockB);
        //        outputGraph(g, "after");

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

    @Test
    public void shouldAttachBlockToBottomOfExistingBlock() throws IOException
    {
        Rectangle2D.Double blockA = new Rectangle2D.Double(0, 0, 10, 10);
        Rectangle2D.Double blockB = new Rectangle2D.Double(20, 0, 10, 10);
        Rectangle2D.Double blockC = new Rectangle2D.Double(0, 20, 10, 10);

        RoadNetworkBuilder b = new RoadNetworkBuilder(1, 3);
        RoadGraph g = b.getGraph();

        b.addBlock(blockA);
        b.attachBlockToRight(blockA, blockB);
        //        outputGraph(g, "before");
        b.attachBlockToBottom(blockA, blockC);
        //        outputGraph(g, "after");

        assertContainsRoad(g, -4, 31, -4, 14);
        assertContainsRoad(g, -1, 14, -1, 31);
        assertContainsRoad(g, 14, 14, 14, 31);
        assertContainsRoad(g, 11, 31, 11, 14);

        BlockGroup<BlockGroup<RoadNode>> aPoints = b.getKeyPoints(blockA);
        BlockGroup<BlockGroup<RoadNode>> bPoints = b.getKeyPoints(blockB);
        BlockGroup<BlockGroup<RoadNode>> cPoints = b.getKeyPoints(blockC);

        assertGroupsEqual(aPoints.getBottomLeft(), cPoints.getTopLeft());
        assertGroupsEqual(aPoints.getBottomRight(), cPoints.getTopRight());
        assertGroupsEqual(aPoints.getBottomRight(), bPoints.getBottomLeft());
    }

    @Test
    public void shouldManageTheOrderingOfBlocksToCreateANetwork() throws IOException
    {
        RoadNetworkBuilder b = new RoadNetworkBuilder(1, 3);
        RoadGraph graph = b.buildNetwork(v(10, 10),
                asList(v(0, 0), v(20, 0), v(40, 0), v(0, 40), v(20, 40), v(40, 20), v(40, 40), v(0, 20), v(20, 20)));

        outputGraph(graph, "graph");
    }

    @Test
    public void shouldAttachBlockToBottomAndLeftOfExistingNetworkWhenDoingRightFirst() throws IOException
    {
        Rectangle2D.Double blockA = new Rectangle2D.Double(0, 0, 10, 10);
        Rectangle2D.Double blockB = new Rectangle2D.Double(20, 0, 10, 10);
        Rectangle2D.Double blockC = new Rectangle2D.Double(0, 20, 10, 10);
        Rectangle2D.Double blockD = new Rectangle2D.Double(20, 20, 10, 10);

        RoadNetworkBuilder b = new RoadNetworkBuilder(1, 3);
        RoadGraph g = b.getGraph();

        b.addBlock(blockA);
        b.attachBlockToRight(blockA, blockB);
        b.attachBlockToBottom(blockA, blockC);
        //        outputGraph(g, "before");
        b.attachBlockToRight(blockC, blockD);
        b.attachBlockToBottom(blockB, blockD);
        //        outputGraph(g, "after");

        assertContainsRoad(g, 14, 14, 11, 14);
        assertContainsRoad(g, 14, 11, 14, 14);
    }

    @Test
    public void shouldAttachBlockToBottomAndLeftOfExistingNetworkWhenDoingBottomFirst() throws IOException
    {
        Rectangle2D.Double blockA = new Rectangle2D.Double(0, 0, 10, 10);
        Rectangle2D.Double blockB = new Rectangle2D.Double(20, 0, 10, 10);
        Rectangle2D.Double blockC = new Rectangle2D.Double(0, 20, 10, 10);
        Rectangle2D.Double blockD = new Rectangle2D.Double(20, 20, 10, 10);

        RoadNetworkBuilder b = new RoadNetworkBuilder(1, 3);
        RoadGraph g = b.getGraph();

        b.addBlock(blockA);
        b.attachBlockToRight(blockA, blockB);
        b.attachBlockToBottom(blockA, blockC);
        //        outputGraph(g, "before");
        b.attachBlockToBottom(blockB, blockD);
        b.attachBlockToRight(blockC, blockD);
        //        outputGraph(g, "after");

        assertContainsRoad(g, 14, 14, 11, 14);
        assertContainsRoad(g, 14, 11, 14, 14);
    }

    private void assertGroupsEqual(BlockGroup<RoadNode> a, BlockGroup<RoadNode> b)
    {
        assertSame(a.getTopRight(), b.getTopRight());
        assertSame(a.getBottomRight(), b.getBottomRight());
        assertSame(a.getTopLeft(), b.getTopLeft());
        assertSame(a.getBottomLeft(), b.getBottomLeft());
    }

    public void outputGraph(RoadGraph graph, String fileName) throws IOException
    {
        if (!renderGraphs)
            return;

        int size = 800;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Camera camera = new Camera(size, size);
        camera.incrementXOffset(370);
        camera.incrementYOffset(370);
        camera.zoom(0.1);
        graph.render(new SwingGraphicsAdapter((Graphics2D) image.getGraphics(), camera));
        File writeOut = new File(fileName + ".png");
        System.out.println(writeOut.getAbsolutePath());
        ImageIO.write(image, "PNG", writeOut);
    }

    @Test
    public void shouldAddRoadsGoingBothWaysAroundABlock() throws IOException
    {
        Rectangle2D.Double block = new Rectangle2D.Double(0, 0, 10, 10);

        RoadNetworkBuilder b = new RoadNetworkBuilder(1, 3);
        b.addBlock(block);
        RoadGraph g = b.getGraph();
        //        outputGraph(g, "block");

        //inner road
        assertContainsRoad(g, 11, -1, -1, -1);
        assertContainsRoad(g, -1, -1, -1, 11);
        assertContainsRoad(g, -1, 11, 11, 11);
        assertContainsRoad(g, 11, 11, 11, -1);
        //onwards
        assertContainsRoad(g, -1, -4, -1, -1);
        assertContainsRoad(g, -1, -1, -4, -1);
        assertContainsRoad(g, 11, -1, 11, -4);
        assertContainsRoad(g, 14, -1, 11, -1);
        assertContainsRoad(g, 11, 11, 14, 11);
        assertContainsRoad(g, 11, 14, 11, 11);
        assertContainsRoad(g, -4, 11, -1, 11);
        assertContainsRoad(g, -1, 11, -1, 14);

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
        b.addBlock(block);
        RoadGraph g = b.getGraph();
        BlockGroup<BlockGroup<RoadNode>> keyPoints = b.getKeyPoints(block);

        assertEquals(getNodeClosestTo(g, 11, 11), keyPoints.getBottomRight().getTopLeft());

        RoadNode replacement = new RoadNode(11, 11);
        b.replace(keyPoints.getBottomRight().getTopLeft(), replacement);

        assertEquals(replacement, keyPoints.getBottomRight().getTopLeft());
    }

    @Test
    public void shouldArrangeBlocksInto2DArray()
    {
        List<Vector> blocks = Arrays.asList(v(10, 10), v(10, 0), v(0, 0), v(0, 10));

        List<List<Vector>> results = RoadNetworkBuilder.orderBlockPositions(blocks);

        assertEquals(2, results.get(0).size());
        assertEquals(2, results.get(1).size());
        assertEquals(2, results.size());
        assertEquals(v(0, 0), results.get(0).get(0));
        assertEquals(v(10, 0), results.get(1).get(0));
        assertEquals(v(0, 10), results.get(0).get(1));
        assertEquals(v(10, 10), results.get(1).get(1));
    }

    @Test
    public void shouldAddSmallSideRoad() throws IOException
    {
        Rectangle2D.Double block = new Rectangle2D.Double(0, 0, 10, 10);

        RoadNetworkBuilder b = new RoadNetworkBuilder(1, 3);
        b.addBlock(block);
        BlockGroup<BlockGroup<RoadNode>> keyPoints = b.getKeyPoints(block);
        b.addSideRoad(keyPoints.getBottomLeft().getTopRight(), keyPoints.getBottomRight().getTopLeft(), v(5, 11),
                v(5, 8), v(8, 8), v(8, 11));

        RoadGraph g = b.getGraph();
        outputGraph(g, "block");

        //new roads
        assertContainsRoad(g, 5, 11, 5, 8);
        assertContainsRoad(g, 5, 8, 8, 8);
        assertContainsRoad(g, 8, 8, 8, 11);

        //existing flow
        assertContainsRoad(g, -1, 11, 5, 11);
        assertContainsRoad(g, 5, 11, 8, 11);
        assertContainsRoad(g, 8, 11, 11, 11);
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
