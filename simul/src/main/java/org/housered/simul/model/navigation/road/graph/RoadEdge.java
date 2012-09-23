package org.housered.simul.model.navigation.road.graph;


public class RoadEdge
{
    private final RoadNode a;
    private final RoadNode b;
    private final double cost;

    public RoadEdge(RoadNode start, RoadNode end, double cost)
    {
        this.a = start;
        this.b = end;
        this.cost = cost;
    }

    public RoadNode getOtherNode(RoadNode currentNode)
    {
        if (a == currentNode)
            return b;
        else if (b == currentNode)
            return a;
        else
            throw new IllegalArgumentException("Do not recognise this road node");
    }

    public double getCost()
    {
        return cost;
    }
}
