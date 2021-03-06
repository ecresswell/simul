package org.housered.simul.model.actor.brain;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import org.housered.simul.model.actor.Actor;
import org.housered.simul.model.assets.AssetManager;
import org.housered.simul.model.assets.Occupiable;
import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.NavigationOrder;
import org.housered.simul.model.navigation.WalkNavigationOrder;
import org.housered.simul.model.navigation.road.CarNavigationOrder;
import org.housered.simul.model.navigation.road.RoadManager;
import org.housered.simul.model.navigation.road.graph.RoadNode;
import org.housered.simul.model.navigation.tube.TubeManager;
import org.housered.simul.model.navigation.tube.TubeNavigationOrder;
import org.housered.simul.model.navigation.tube.TubeStation;
import org.housered.simul.model.work.Job;
import org.housered.simul.model.work.JobManager;
import org.housered.simul.model.world.GameClock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HighLevelBrainImpl implements HighLevelBrain
{
    private enum State
    {
        AT_HOME, AT_WORK, GOING_HOME, GOING_TO_WORK
    }

    private static final double CHANCE_OF_CAR = 0.5;
    private static final Logger LOGGER = LoggerFactory.getLogger(HighLevelBrainImpl.class);
    private static final Random R = new Random();
    private final Actor actor;
    private final RoadManager roadManager;
    private final AssetManager assetManager;
    private final JobManager jobManager;
    private final TubeManager tubeManager;
    private final GameClock gameClock;

    private Queue<NavigationOrder> orders = new LinkedList<NavigationOrder>();
    private Occupiable currentTarget;
    private State state;
    private boolean releaseNextOrder;

    private Job job;
    private Occupiable home;

    public HighLevelBrainImpl(Actor actor, AssetManager assetManager, JobManager jobManager, GameClock gameClock,
            RoadManager roadManager, TubeManager tubeManager)
    {
        this.actor = actor;
        this.assetManager = assetManager;
        this.jobManager = jobManager;
        this.gameClock = gameClock;
        this.roadManager = roadManager;
        this.tubeManager = tubeManager;
    }

    @Override
    public NavigationOrder decideWhereToGo()
    {
        updateJobAndHome();

        if (currentTarget == null || (state == State.AT_WORK && job.shouldLeaveWork()))
        {
            if (home != null)
            {
                if (R.nextFloat() < CHANCE_OF_CAR)
                {
                    LOGGER.trace("Heading home via the car");
                    if (currentTarget != null)
                        currentTarget.exit(actor);

                    state = State.GOING_HOME;
                    actor.setInvisible(false);
                    currentTarget = home;

                    queueWalk(roadManager.getClosestRoadPoint(actor.getPosition()).getPosition());
                    queueCar(roadManager.getClosestRoadPoint(actor.getPosition()),
                            roadManager.getClosestRoadPoint(currentTarget.getEntryPoint()));
                    queueWalk(currentTarget.getEntryPoint());

                    return orders.remove();
                }
                else
                {
                    LOGGER.trace("Heading home via the tube");

                    if (currentTarget != null)
                        currentTarget.exit(actor);

                    state = State.GOING_HOME;
                    actor.setInvisible(false);
                    currentTarget = home;

                    TubeStation closestToMe = tubeManager.getClosestTubeStation(actor.getPosition());
                    TubeStation closestToHome = tubeManager.getClosestTubeStation(currentTarget.getEntryPoint());

                    queueWalk(closestToMe.getEntryPoint());
                    queueTube(closestToHome);
                    queueWalk(currentTarget.getEntryPoint());

                    return orders.remove();
                }
            }
            LOGGER.warn("{} is homeless", actor);
        }
        else if (state == State.AT_HOME && job.shouldGoToWork())
        {
            if (job != null)
            {
                if (R.nextFloat() < CHANCE_OF_CAR)
                {
                    LOGGER.trace("Time to go to work via the car");
                    currentTarget.exit(actor);
                    state = State.GOING_TO_WORK;
                    actor.setInvisible(false);
                    currentTarget = job.getJobLocation();

                    queueWalk(roadManager.getClosestRoadPoint(actor.getPosition()).getPosition());
                    queueCar(roadManager.getClosestRoadPoint(actor.getPosition()),
                            roadManager.getClosestRoadPoint(currentTarget.getEntryPoint()));
                    queueWalk(currentTarget.getEntryPoint());

                    return orders.remove();
                }
                else
                {
                    LOGGER.trace("Going to work via the tube");

                    if (currentTarget != null)
                        currentTarget.exit(actor);
                    state = State.GOING_TO_WORK;
                    actor.setInvisible(false);
                    currentTarget = job.getJobLocation();

                    TubeStation closestToMe = tubeManager.getClosestTubeStation(actor.getPosition());
                    TubeStation closestToWork = tubeManager.getClosestTubeStation(currentTarget.getEntryPoint());
                    
                    queueWalk(closestToMe.getEntryPoint());
                    queueTube(closestToWork);
                    queueWalk(currentTarget.getEntryPoint());

                    return orders.remove();
                }
            }
            LOGGER.warn("{} is unemployed", actor);
        }
        if (releaseNextOrder)
        {
            releaseNextOrder = false;
            return orders.remove();
        }

        //we've already decided where to go
        return null;
    }

    @Override
    public void arrivedAtTarget()
    {
        LOGGER.trace("Arrived at {}", actor.getPosition());

        if (orders.size() > 0)
            releaseNextOrder = true;
        else
        {
            if (state == State.GOING_HOME)
            {
                LOGGER.trace("Arrived at home");

                currentTarget.occupy(actor);
                job.arrivedAtHome();
                state = State.AT_HOME;
                actor.setInvisible(true);
            }
            else if (state == State.GOING_TO_WORK)
            {
                LOGGER.trace("Arrived at work");

                currentTarget.occupy(actor);
                job.arrivedAtWork();
                state = State.AT_WORK;
                actor.setInvisible(true);
            }
        }
    }

    private void queueWalk(Vector target)
    {
        orders.add(new WalkNavigationOrder(target));
    }

    private void queueCar(RoadNode start, RoadNode end)
    {
        orders.add(new CarNavigationOrder(start, end));
    }

    private void queueTube(TubeStation target)
    {
        orders.add(new TubeNavigationOrder(target));
    }

    private void updateJobAndHome()
    {
        job = jobManager.getJobs(actor).iterator().next();
        home = assetManager.getAssets(actor).iterator().next();
    }
}
