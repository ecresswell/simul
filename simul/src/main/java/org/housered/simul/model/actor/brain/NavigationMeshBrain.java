package org.housered.simul.model.actor.brain;

import org.housered.simul.model.navigation.NavigationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import straightedge.geom.KPoint;
import straightedge.geom.path.PathData;

public class NavigationMeshBrain implements NavigationBrain
{
    private static Logger LOGGER = LoggerFactory.getLogger(NavigationMeshBrain.class);
    private final NavigationManager navigationManager;
    private KPoint currentPosition;
    private KPoint target;
    private PathData path;

    public NavigationMeshBrain(NavigationManager navigationManager)
    {
        this.navigationManager = navigationManager;
        currentPosition = new KPoint();
    }

    @Override
    public void setTarget(KPoint target)
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
    public KPoint getNextPoint()
    {
        if (path.isError())
        {
            LOGGER.warn("Path is in error for {}: {}", this, path.getResult());
            target = null;
            return currentPosition;
        }

        KPoint nextPoint = path.getPoints().get(0);

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
    public KPoint getPosition()
    {
        return currentPosition;
    }

}
