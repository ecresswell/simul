package org.housered.simul.model.actor.brain;

import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.NavigationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import straightedge.geom.path.PathData;

public class NavigationMeshBrain implements NavigationBrain
{
    private static Logger LOGGER = LoggerFactory.getLogger(NavigationMeshBrain.class);
    private final NavigationManager navigationManager;
    private Vector currentPosition;
    private Vector target;
    private PathData path;

    public NavigationMeshBrain(NavigationManager navigationManager)
    {
        this.navigationManager = navigationManager;
        currentPosition = new Vector();
    }

    @Override
    public void setTarget(Vector target)
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
    public Vector getNextPoint()
    {
        if (path.isError())
        {
            LOGGER.warn("Path is in error for {}: {}", this, path.getResult());
            target = null;
            return currentPosition;
        }

        //TODO: don't keep checking this
        Vector nextPoint = new Vector(path.getPoints().get(0));

        if (getPosition().equals(nextPoint))
        {
            if (path.getPoints().size() == 1)
            {
                //TODO: check if we are actually where we want to be
                LOGGER.debug("Arrived at final destination");
                target = null;
            }
            else
            {
                LOGGER.debug("Arrived at waypoint, moving to next");
                path.getPoints().remove(0);
            }
        }
        return nextPoint;
    }

    @Override
    public Vector getPosition()
    {
        return currentPosition;
    }
}
