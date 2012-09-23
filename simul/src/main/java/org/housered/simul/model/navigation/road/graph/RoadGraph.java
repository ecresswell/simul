package org.housered.simul.model.navigation.road.graph;

import java.util.ArrayList;
import java.util.List;

public class RoadGraph
{
    private List<RoadNode> roadNodes = new ArrayList<RoadNode>();

    public void addNode(RoadNode node)
    {
        roadNodes.add(node);
    }

    public void connectNodesInADirectedWay(RoadNode start, RoadNode end, double cost)
    {
        if (start == end)
            throw new IllegalArgumentException("Can't connect node to itself");
        
        if (!roadNodes.contains(start))
        {
            addNode(start);
        }
        if (!roadNodes.contains(end))
        {
            addNode(end);
        }
        
        start.addRoad(new RoadEdge(start, end, cost));
    }
}
