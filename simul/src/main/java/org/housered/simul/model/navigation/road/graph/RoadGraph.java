package org.housered.simul.model.navigation.road.graph;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.housered.simul.view.GraphicsAdapter;
import org.housered.simul.view.Renderable;

public class RoadGraph implements Renderable
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

    public Collection<RoadNode> getRoadNodes()
    {
        return roadNodes;
    }

    @Override
    public void render(GraphicsAdapter r)
    {
        r.setColour(Color.gray);
        for (RoadNode node : roadNodes)
        {
            r.fillCircle(node.getPosition(), 3);

            for (RoadEdge edge : node.getEdges())
            {
                r.drawLine(edge.getStartNode().getPosition(), edge.getEndNode().getPosition());
            }
        }
    }

    @Override
    public byte getZOrder()
    {
        return ROAD_Z_ORDER;
    }
}
