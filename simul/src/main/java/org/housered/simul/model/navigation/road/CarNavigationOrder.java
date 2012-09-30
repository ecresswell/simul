package org.housered.simul.model.navigation.road;

import org.housered.simul.model.navigation.NavigationOrder;
import org.housered.simul.model.navigation.road.graph.RoadNode;

public class CarNavigationOrder implements NavigationOrder
{
    private final RoadNode start;
    private final RoadNode end;

    public CarNavigationOrder(RoadNode start, RoadNode end)
    {
        this.start = start;
        this.end = end;

    }

    @Override
    public NavigationType getType()
    {
        return NavigationType.CAR;
    }

    public RoadNode getStart()
    {
        return start;
    }

    public RoadNode getEnd()
    {
        return end;
    }
}
