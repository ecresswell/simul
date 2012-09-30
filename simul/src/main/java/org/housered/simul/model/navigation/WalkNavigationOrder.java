package org.housered.simul.model.navigation;

import org.housered.simul.model.location.Vector;

public class WalkNavigationOrder implements NavigationOrder
{
    private final Vector target;

    public WalkNavigationOrder(Vector target)
    {
        this.target = target;

    }

    @Override
    public NavigationType getType()
    {
        return NavigationType.WALK;
    }

    public Vector getTarget()
    {
        return target;
    }

}
