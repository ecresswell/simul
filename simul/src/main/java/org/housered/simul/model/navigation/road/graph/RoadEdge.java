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

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(cost);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((end == null) ? 0 : end.hashCode());
        result = prime * result + ((start == null) ? 0 : start.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RoadEdge other = (RoadEdge) obj;
        if (Double.doubleToLongBits(cost) != Double.doubleToLongBits(other.cost))
            return false;
        if (end == null)
        {
            if (other.end != null)
                return false;
        }
        else if (end != other.end)
            return false;
        if (start == null)
        {
            if (other.start != null)
                return false;
        }
        else if (start != other.start)
            return false;
        return true;
    }
    
    
}
