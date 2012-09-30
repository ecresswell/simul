package org.housered.simul.model.actor.brain;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.housered.simul.model.actor.Actor;
import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.NavigationOrder;
import org.housered.simul.model.navigation.road.CarNavigationOrder;
import org.housered.simul.model.navigation.road.RoadManager;
import org.housered.simul.model.navigation.road.graph.RoadNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoadNavigationBrain
{
    private static final int THREADS = 1;
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(THREADS);

    private enum NavigationState
    {
        NO_TARGET, MOVING, ARRIVED, WAITING
    }

    private static Logger LOGGER = LoggerFactory.getLogger(NavigationMeshBrain.class);
    private final Actor actor;
    private final RoadManager networkManager;
    private RoadNode target;
    private List<RoadNode> path;
    private NavigationState state = NavigationState.NO_TARGET;

    private FutureTask<List<RoadNode>> currentTask;

    public RoadNavigationBrain(RoadManager networkManager, Actor actor)
    {
        this.networkManager = networkManager;
        this.actor = actor;
    }

    public void setTarget(NavigationOrder order)
    {
        final RoadNode start = ((CarNavigationOrder) order).getStart();
        target = ((CarNavigationOrder) order).getEnd();

        currentTask = new FutureTask<List<RoadNode>>(new Callable<List<RoadNode>>() {
            @Override
            public List<RoadNode> call() throws Exception
            {
                return networkManager.findPath(start, target);
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

        if (path == null)
        {
            LOGGER.error("Could not find path (for {}) between {} and {}", new Object[] {"CAR", actor.getPosition(),
                    target});
            return;
        }

        //the first point is where we are at the moment
        path.remove(0);
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
        if (path == null)
        {
            LOGGER.warn("Path is in error for {}", this);
            target = null;
            return actor.getPosition();
        }

        if (state == NavigationState.ARRIVED)
            return actor.getPosition();

        //TODO: don't keep checking this
        RoadNode nextNode = path.get(0);

        if (actor.getPosition().equals(nextNode.getPosition()))
        {
            if (path.size() == 1)
            {
                //TODO: check if we are actually where we want to be
                LOGGER.trace("Arrived at final destination, {}", path.get(0));
                state = NavigationState.ARRIVED;
                target = null;
            }
            else
            {
                LOGGER.trace("Arrived at waypoint {}, moving to next {}", path.get(0), path.get(1));
                path.remove(0);
            }
        }
        
        return nextNode.getPosition();
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
