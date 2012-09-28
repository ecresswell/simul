package org.housered.simul.model.navigation.road.graph;

public class RoadEdge
{
    private RoadNode start;
    private RoadNode end;
    private double cost;

    public RoadEdge(RoadNode start, RoadNode end, double cost)
    {
        this.start = start;
        this.end = end;
        this.cost = cost;
    }

    public RoadNode getOtherNode(RoadNode currentNode)
    {
        if (start == currentNode)
            return end;
        else if (end == currentNode)
            return start;
        else
            throw new IllegalArgumentException("Do not recognise this road node");
    }

    public double getCost()
    {
        return cost;
    }

    public RoadNode getStartNode()
    {
        return start;
    }

    public void setStartNode(RoadNode newStartNode)
    {
        start = newStartNode;
    }

    public RoadNode getEndNode()
    {
        return end;
    }

    public void setEndNode(RoadNode newEndNode)
    {
        end = newEndNode;
    }

    @Override
    public String toString()
    {
        return String.format("(%s, %s) -> (%s, %s)", start.getPosition().x, start.getPosition().y, end.getPosition().x,
                end.getPosition().y);
    }
}
