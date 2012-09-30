package org.housered.simul.model.actor.brain;

import org.housered.simul.model.navigation.NavigationOrder;
import org.housered.simul.model.navigation.road.CarNavigationOrder;
import org.housered.simul.model.navigation.road.graph.RoadNode;

public class RoadNavigationBrain
{
    public void setTarget(NavigationOrder order)
    {
        if (!(order instanceof CarNavigationOrder))
            throw new IllegalArgumentException("Wrong order type");
    }
    
    public boolean hasTarget()
    {
        return false;
    }
    
    public boolean hasArrivedAtTarget()
    {
        return false;
    }
    
    public RoadNode getNextNode()
    {
        return null;
    }
}
