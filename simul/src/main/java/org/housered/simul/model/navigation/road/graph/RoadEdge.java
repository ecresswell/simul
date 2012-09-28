package org.housered.simul.model.navigation.road.graph;


public class RoadEdge
{
    private RoadNode a;
    private RoadNode b;
    private double cost;

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

    public RoadNode getStartNode()
    {
        return a;
    }
    
    public void setStartNode(RoadNode newStartNode)
    {
        a = newStartNode;
    }

    public RoadNode getEndNode()
    {
        return b;
    }
    
    public void setEndNode(RoadNode newEndNode)
    {
        b = newEndNode;
    }
}
