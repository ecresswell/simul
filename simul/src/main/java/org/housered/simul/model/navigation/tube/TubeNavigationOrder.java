package org.housered.simul.model.navigation.tube;

import org.housered.simul.model.navigation.NavigationOrder;

public class TubeNavigationOrder implements NavigationOrder
{
    private final TubeStation target;

    public TubeNavigationOrder(TubeStation target)
    {
        this.target = target;

    }

    @Override
    public NavigationType getType()
    {
        return NavigationType.TUBE;
    }

    public TubeStation getTarget()
    {
        return target;
    }

}
