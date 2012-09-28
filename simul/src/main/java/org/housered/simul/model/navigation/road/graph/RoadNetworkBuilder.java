package org.housered.simul.model.navigation.road.graph;

import static org.housered.simul.model.location.Vector.v;

import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoadNetworkBuilder
{
    private final Map<Double, BlockGroup<BlockGroup<RoadNode>>> keyPoints = new HashMap<Double, BlockGroup<BlockGroup<RoadNode>>>();
    private final double expansionFactor;
    private final double distanceBetweenLanes;
    private final RoadGraph graph = new RoadGraph();

    public RoadNetworkBuilder(double expansionFactor, double distanceBetweenLanes)
    {
        this.expansionFactor = expansionFactor;
        this.distanceBetweenLanes = distanceBetweenLanes;
    }

    public RoadGraph getGraph()
    {
        return graph;
    }

    public RoadGraph buildNetwork(Double... blocks)
    {
        for (Double block : blocks)
            addBlock(block);

        return graph;
    }

    void addBlock(Double block)
    {

        block.add(block.x - expansionFactor, block.y - expansionFactor);
        block.add(block.x + block.width + expansionFactor, block.y + block.height + expansionFactor);

        RoadNode tl = new RoadNode(v(block.x, block.y));
        RoadNode tr = new RoadNode(v(block.x + block.width, block.y));
        RoadNode br = new RoadNode(v(block.x + block.width, block.y + block.height));
        RoadNode bl = new RoadNode(v(block.x, block.y + block.height));

        graph.connectNodesInADirectedWay(tr, tl);
        graph.connectNodesInADirectedWay(br, tr);
        graph.connectNodesInADirectedWay(bl, br);
        graph.connectNodesInADirectedWay(tl, bl);

        //outside road should contain junction points
        Double outerBlock = (Double) block.clone();
        outerBlock.add(block.x - distanceBetweenLanes, block.y - distanceBetweenLanes);
        outerBlock.add(block.x + block.width + distanceBetweenLanes, block.y + block.height + distanceBetweenLanes);

        RoadNode top1 = new RoadNode(outerBlock.x, outerBlock.y);
        RoadNode top2 = new RoadNode(outerBlock.x + distanceBetweenLanes, outerBlock.y);
        RoadNode top3 = new RoadNode(outerBlock.x + outerBlock.width - distanceBetweenLanes, outerBlock.y);
        RoadNode top4 = new RoadNode(outerBlock.x + outerBlock.width, outerBlock.y);

        RoadNode right2 = new RoadNode(outerBlock.x + outerBlock.width, outerBlock.y + distanceBetweenLanes);
        RoadNode right3 = new RoadNode(outerBlock.x + outerBlock.width, outerBlock.y + outerBlock.height
                - distanceBetweenLanes);

        RoadNode bottom1 = new RoadNode(outerBlock.x, outerBlock.y + outerBlock.height);
        RoadNode bottom2 = new RoadNode(outerBlock.x + distanceBetweenLanes, outerBlock.y + outerBlock.height);
        RoadNode bottom3 = new RoadNode(outerBlock.x + outerBlock.width - distanceBetweenLanes, outerBlock.y
                + outerBlock.height);
        RoadNode bottom4 = new RoadNode(outerBlock.x + outerBlock.width, outerBlock.y + outerBlock.height);

        RoadNode left2 = new RoadNode(outerBlock.x, outerBlock.y + distanceBetweenLanes);
        RoadNode left3 = new RoadNode(outerBlock.x, outerBlock.y + outerBlock.height - distanceBetweenLanes);

        graph.connectNodesInADirectedWay(top1, top2, top3, top4);
        graph.connectNodesInADirectedWay(top4, right2, right3, bottom4);
        graph.connectNodesInADirectedWay(bottom4, bottom3, bottom2, bottom1);
        graph.connectNodesInADirectedWay(bottom1, left3, left2, top1);

        //junctions
        graph.connectNodesInADirectedWay(top2, left2);
        graph.connectNodesInADirectedWay(left2, top2);
        graph.connectNodesInADirectedWay(top1, tl);
        graph.connectNodesInADirectedWay(tl, top1);
        graph.connectNodesInADirectedWay(top3, right2);
        graph.connectNodesInADirectedWay(right2, top3);
        graph.connectNodesInADirectedWay(top4, tr);
        graph.connectNodesInADirectedWay(tr, top4);
        graph.connectNodesInADirectedWay(right3, bottom3);
        graph.connectNodesInADirectedWay(bottom3, right3);
        graph.connectNodesInADirectedWay(bottom4, br);
        graph.connectNodesInADirectedWay(br, bottom4);
        graph.connectNodesInADirectedWay(bottom2, left3);
        graph.connectNodesInADirectedWay(left3, bottom2);
        graph.connectNodesInADirectedWay(bottom1, bl);
        graph.connectNodesInADirectedWay(bl, bottom1);

        BlockGroup<RoadNode> topLeft = new BlockGroup<RoadNode>(top1, top2, tl, left2);
        BlockGroup<RoadNode> topRight = new BlockGroup<RoadNode>(top3, top4, right2, tr);
        BlockGroup<RoadNode> bottomRight = new BlockGroup<RoadNode>(br, right3, bottom4, bottom3);
        BlockGroup<RoadNode> bottomLeft = new BlockGroup<RoadNode>(left3, bl, bottom2, bottom1);
        keyPoints.put(block, new BlockGroup<BlockGroup<RoadNode>>(topLeft, topRight, bottomRight, bottomLeft));
    }

    void attachBlockToRight(Double existingBlock, Double newBlock)
    {
        addBlock(newBlock);
        BlockGroup<BlockGroup<RoadNode>> existingPoints = keyPoints.get(existingBlock);
        BlockGroup<BlockGroup<RoadNode>> newPoints = keyPoints.get(newBlock);
    }

    BlockGroup<BlockGroup<RoadNode>> getKeyPoints(Double block)
    {
        return keyPoints.get(block);
    }

    static class BlockGroup<T>
    {
        private T topleft;
        private T topRight;
        private T bottomRight;
        private T bottomLeft;

        public BlockGroup(T topleft, T topRight, T bottomRight, T bottomLeft)
        {
            this.setTopleft(topleft);
            this.setTopRight(topRight);
            this.setBottomRight(bottomRight);
            this.setBottomLeft(bottomLeft);

        }

        public T getTopleft()
        {
            return topleft;
        }

        public void setTopleft(T topleft)
        {
            this.topleft = topleft;
        }

        public T getTopRight()
        {
            return topRight;
        }

        public void setTopRight(T topRight)
        {
            this.topRight = topRight;
        }

        public T getBottomRight()
        {
            return bottomRight;
        }

        public void setBottomRight(T bottomRight)
        {
            this.bottomRight = bottomRight;
        }

        public T getBottomLeft()
        {
            return bottomLeft;
        }

        public void setBottomLeft(T bottomLeft)
        {
            this.bottomLeft = bottomLeft;
        }
    }

}