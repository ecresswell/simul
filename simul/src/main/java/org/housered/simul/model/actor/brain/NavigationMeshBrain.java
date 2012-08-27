package org.housered.simul.model.actor.brain;

import org.housered.simul.model.location.Position;
import org.housered.simul.model.navigation.NavigationManager;

import straightedge.geom.KPoint;
import straightedge.geom.path.PathData;

public class NavigationMeshBrain implements NavigationBrain
{
    private final NavigationManager navigationManager;
    private Position currentPosition;
    private Position target;
    private PathData path;

    public NavigationMeshBrain(NavigationManager navigationManager)
    {
        this.navigationManager = navigationManager;
        currentPosition = new Position();
    }

    @Override
    public void setTarget(Position target)
    {
        this.target = target;
        path = navigationManager.findPath(getPosition(), target);
    }

    @Override
    public boolean hasTarget()
    {
        if (getPosition().equals(target))
            target = null;

        return target != null;
    }

    @Override
    public Position getNextPoint()
    {
        KPoint kNextPoint = path.getPoints().get(0);
        Position nextPoint = new Position((float) kNextPoint.x, (float) kNextPoint.y);
        
        if (getPosition().equals(nextPoint))
        {
            if (path.getPoints().size() == 1)
            {
                //TODO: check if we are actually where we want to be
                target = null;
            }
            else
            {
                path.getPoints().remove(0);
            }
        }
        return nextPoint;
    }

    @Override
    public Position getPosition()
    {
        return currentPosition;
    }

}
