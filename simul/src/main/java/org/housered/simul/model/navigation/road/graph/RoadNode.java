package org.housered.simul.model.navigation.road.graph;

import java.util.ArrayList;
import java.util.List;

import org.housered.simul.model.location.Locatable;
import org.housered.simul.model.location.Vector;

import com.stackframe.pathfinder.Node;

public class RoadNode implements Node<RoadNode>, Locatable
{
    private final List<RoadEdge> roads = new ArrayList<RoadEdge>();
    private final Vector position;

    public RoadNode(Vector position)
    {
        this.position = position;
    }

    void addRoad(RoadEdge road)
    {
        roads.add(road);
    }

    @Override
    public double pathCostEstimate(RoadNode goal)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double traverseCost(RoadNode dest)
    {
        for (RoadEdge road : roads)
        {
            if (road.getOtherNode(this) == dest)
                return road.getCost();
        }

        throw new IllegalStateException("Node is not connected to requested node");
    }

    @Override
    public Iterable<RoadNode> neighbors()
    {
        List<RoadNode> neighbours = new ArrayList<RoadNode>(roads.size());

        for (RoadEdge road : roads)
            neighbours.add(road.getOtherNode(this));

        return neighbours;
    }

    @Override
    public Vector getPosition()
    {
        return position;
    }

    public List<RoadEdge> getEdges()
    {
        return roads;
    }

}
