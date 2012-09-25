package org.housered.simul.model.navigation.road;

import static org.housered.simul.model.location.Vector.v;

import java.awt.geom.Rectangle2D.Double;

import org.housered.simul.model.navigation.road.graph.RoadGraph;
import org.housered.simul.model.navigation.road.graph.RoadNode;

public class RoadNetworkBuilder
{
    private final double expansionFactor;
    private final double distanceBetweenLanes;

    public RoadNetworkBuilder(double expansionFactor, double distanceBetweenLanes)
    {
        this.expansionFactor = expansionFactor;
        this.distanceBetweenLanes = distanceBetweenLanes;
    }

    public RoadGraph buildNetwork(Double block)
    {
        RoadGraph graph = new RoadGraph();

        block.add(block.x - expansionFactor, block.y - expansionFactor);
        block.add(block.x + block.width + expansionFactor, block.y + block.height + expansionFactor);

        RoadNode ulL = new RoadNode(v(block.x, block.y));
        RoadNode urL = new RoadNode(v(block.x + block.width, block.y));
        RoadNode brL = new RoadNode(v(block.x + block.width, block.y + block.height));
        RoadNode blL = new RoadNode(v(block.x, block.y + block.height));

        graph.connectNodesInADirectedWay(urL, ulL);
        graph.connectNodesInADirectedWay(brL, urL);
        graph.connectNodesInADirectedWay(blL, brL);
        graph.connectNodesInADirectedWay(ulL, blL);

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

        return graph;
    }

}
