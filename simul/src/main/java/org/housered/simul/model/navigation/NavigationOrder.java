package org.housered.simul.model.navigation;

import org.housered.simul.model.location.Vector;

public class NavigationOrder
{
    private final Vector target;
    private final NavigationType type;

    public enum NavigationType
    {
        WALK, CAR
    }

    public NavigationOrder(Vector target, NavigationType type)
    {
        this.target = target;
        this.type = type;
    }

    public Vector getTarget()
    {
        return target;
    }

    public NavigationType getType()
    {
        return type;
    }

    @Override
    public String toString()
    {
        return "NavigationOrder [target=" + target + ", type=" + type + "]";
    }
}
