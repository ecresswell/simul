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
    
    public RoadGraph buildNetwork(Double... blocks)
    {
        RoadGraph graph = new RoadGraph();
        
        for (Double block : blocks)
            buildNetwork(graph, block);
        
        return graph;
    }
    
    private void buildNetwork(RoadGraph graph, Double block)
    {

        block.add(block.x - expansionFactor, block.y - expansionFactor);
        block.add(block.x + block.width + expansionFactor, block.y + block.height + expansionFactor);

        RoadNode ul = new RoadNode(v(block.x, block.y));
        RoadNode ur = new RoadNode(v(block.x + block.width, block.y));
        RoadNode br = new RoadNode(v(block.x + block.width, block.y + block.height));
        RoadNode bl = new RoadNode(v(block.x, block.y + block.height));

        graph.connectNodesInADirectedWay(ur, ul);
        graph.connectNodesInADirectedWay(br, ur);
        graph.connectNodesInADirectedWay(bl, br);
        graph.connectNodesInADirectedWay(ul, bl);

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
        graph.connectNodesInADirectedWay(top1, ul);
        graph.connectNodesInADirectedWay(ul, top1);
        graph.connectNodesInADirectedWay(top3, right2);
        graph.connectNodesInADirectedWay(right2, top3);
        graph.connectNodesInADirectedWay(top4, ur);
        graph.connectNodesInADirectedWay(ur, top4);
        graph.connectNodesInADirectedWay(right3, bottom3);
        graph.connectNodesInADirectedWay(bottom3, right3);
        graph.connectNodesInADirectedWay(bottom4, br);
        graph.connectNodesInADirectedWay(br, bottom4);
        graph.connectNodesInADirectedWay(bottom2, left3);
        graph.connectNodesInADirectedWay(left3, bottom2);
        graph.connectNodesInADirectedWay(bottom1, bl);
        graph.connectNodesInADirectedWay(bl, bottom1);
    }

}
