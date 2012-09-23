package org.housered.simul.model.navigation;

import org.housered.simul.model.location.Vector;
import org.housered.simul.model.world.GameObject;

@Deprecated
public class OldNavigationOrder
{
    private final GameObject targetObject;
    private final Vector target;
    private final NavigationType type;

    public enum NavigationType
    {
        WALK, CAR, TUBE
    }

    public OldNavigationOrder(Vector target, NavigationType type)
    {
        this(target, type, null);
    }

    public OldNavigationOrder(Vector target, NavigationType type, GameObject targetObject)
    {
        this.target = target;
        this.type = type;
        this.targetObject = targetObject;
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
        return "NavigationOrder [targetObject=" + targetObject + ", target=" + target + ", type=" + type + "]";
    }

    public GameObject getTargetObject()
    {
        return targetObject;
    }
}
