package org.housered.simul.model.actor.brain;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.housered.simul.model.actor.Actor;
import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.NavigationManager;
import org.housered.simul.model.navigation.NavigationOrder;
import org.housered.simul.model.navigation.WalkNavigationOrder;
import org.housered.simul.model.navigation.road.RoadManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import straightedge.geom.path.PathData;

public class NavigationMeshBrain
{
    private static final int THREADS = 1;
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(THREADS);

    private enum NavigationState
    {
        NO_TARGET, MOVING, ARRIVED, WAITING
    }

    private static Logger LOGGER = LoggerFactory.getLogger(NavigationMeshBrain.class);
    private final NavigationManager navigationManager;
    private final Actor actor;
    private Vector target;
    private PathData path;
    private NavigationState state = NavigationState.NO_TARGET;

    private FutureTask<PathData> currentTask;

    public NavigationMeshBrain(NavigationManager navigationManager, Actor actor)
    {
        this.navigationManager = navigationManager;
        this.actor = actor;
    }

    public void setTarget(NavigationOrder order)
    {
        target = ((WalkNavigationOrder) order).getTarget();

        currentTask = new FutureTask<PathData>(new Callable<PathData>() {
            @Override
            public PathData call() throws Exception
            {
                return navigationManager.findPath(actor.getPosition(), target);
            }
        });

        EXECUTOR_SERVICE.execute(currentTask);
    }

    private void startMoving()
    {
        try
        {
            path = currentTask.get();
        }
        catch (InterruptedException e)
        {
            throw new IllegalStateException(e);
        }
        catch (ExecutionException e)
        {
            throw new IllegalStateException(e);
        }

        if (path.isError())
        {
            LOGGER.error("Could not find path (for {}) between {} and {} - {}",
                    new Object[] {"WALK", actor.getPosition(), target, path.getResult()});
            return;
        }

        //the first point is where we are at the moment
        path.getPoints().remove(0);
        state = NavigationState.MOVING;
        currentTask = null;
    }

    public boolean hasTarget()
    {
        if (currentTask != null && currentTask.isDone())
        {
            startMoving();
        }

        return state == NavigationState.MOVING || state == NavigationState.ARRIVED;
    }

    public Vector getNextPoint()
    {
        if (path.isError())
        {
            LOGGER.warn("Path is in error for {}: {}", this, path.getResult());
            target = null;
            return actor.getPosition();
        }

        if (state == NavigationState.ARRIVED)
            return actor.getPosition();

        //TODO: don't keep checking this
        Vector nextPoint = new Vector(path.getPoints().get(0));

        if (actor.getPosition().equals(nextPoint))
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
                LOGGER.trace("Arrived at waypoint {}, moving to next {}", path.getPoints().get(0), path.getPoints()
                        .get(1));
                path.getPoints().remove(0);
            }
        }
        return nextPoint;
    }

    public boolean hasArrivedAtTarget()
    {
        if (state == NavigationState.ARRIVED)
        {
            state = NavigationState.WAITING;
            return true;
        }
        return false;
    }
}
