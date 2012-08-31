package org.housered.simul.model.actor.brain;

import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.NavigationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import straightedge.geom.path.PathData;

public class NavigationMeshBrain implements NavigationBrain
{
    private enum NavigationState
    {
        NO_TARGET, MOVING, ARRIVED
    }

    private static Logger LOGGER = LoggerFactory.getLogger(NavigationMeshBrain.class);
    private final NavigationManager navigationManager;
    private Vector currentPosition;
    private Vector target;
    private PathData path;
    private NavigationState state = NavigationState.NO_TARGET;

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
        state = NavigationState.MOVING;
    }

    @Override
    public boolean hasTarget()
    {
        return state == NavigationState.MOVING || state == NavigationState.ARRIVED;
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

        if (state == NavigationState.ARRIVED)
            return currentPosition;

        //TODO: don't keep checking this
        Vector nextPoint = new Vector(path.getPoints().get(0));

        if (getPosition().equals(nextPoint))
        {
            if (path.getPoints().size() == 1)
            {
                //TODO: check if we are actually where we want to be
                LOGGER.trace("Arrived at final destination, {}", path.getPoints().get(0));
                state = NavigationState.ARRIVED;
                target = null;
            }
            else
            {
                LOGGER.trace("Arrived at waypoint {}, moving to next {}", path.getPoints().get(0),
                        path.getPoints().get(1));
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

    @Override
    public boolean hasArrivedAtTarget()
    {
        return state == NavigationState.ARRIVED;
    }
}
