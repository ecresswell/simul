package org.housered.simul.model.actor.brain;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import org.housered.simul.model.actor.Actor;
import org.housered.simul.model.assets.AssetManager;
import org.housered.simul.model.assets.Occupiable;
import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.NavigationOrder;
import org.housered.simul.model.navigation.RoadNetworkManager;
import org.housered.simul.model.navigation.NavigationOrder.NavigationType;
import org.housered.simul.model.work.CommercialBuilding;
import org.housered.simul.model.work.CommercialManager;
import org.housered.simul.model.world.GameClock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HighLevelBrainImpl implements HighLevelBrain
{
    private enum State
    {
        AT_HOME, AT_WORK, GOING_HOME, GOING_TO_WORK, GOING_TO_CAR_FOR_WORK, GOING_TO_CAR_FOR_HOME
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(HighLevelBrainImpl.class);
    private static final long GAME_SECONDS_AT_WORK = TimeUnit.HOURS.toSeconds(1);
    private final Actor actor;
    private final AssetManager assetManager;
    private final CommercialManager commercialManager;
    private final RoadNetworkManager roadManager;
    private final GameClock gameClock;

    private Queue<NavigationOrder> orders = new LinkedList<NavigationOrder>();
    private Occupiable currentTarget;
    private long currentOccupiedStartTime;
    private State state;
    private boolean releaseNextOrder;

    public HighLevelBrainImpl(Actor actor, AssetManager assetManager, CommercialManager commercialManager,
            GameClock gameClock, RoadNetworkManager roadManager)
    {
        this.actor = actor;
        this.assetManager = assetManager;
        this.commercialManager = commercialManager;
        this.gameClock = gameClock;
        this.roadManager = roadManager;
    }

    @Override
    public NavigationOrder decideWhereToGo()
    {
        if (currentTarget == null
                || (state == State.AT_WORK && gameClock.getSecondsSinceGameStart() - currentOccupiedStartTime > GAME_SECONDS_AT_WORK))
        {
            LOGGER.trace("Heading home via the car");
            Set<Occupiable> assets = assetManager.getAssets(actor);

            if (!assets.isEmpty())
            {
                if (currentTarget != null)
                    currentTarget.exit(actor);

                state = State.GOING_HOME;
                currentTarget = assets.iterator().next();

                queueOrder(roadManager.getClosestRoadPoint(actor.getPosition()), NavigationType.WALK);
                queueOrder(roadManager.getClosestRoadPoint(currentTarget.getEntryPoint()), NavigationType.CAR);
                queueOrder(currentTarget.getEntryPoint(), NavigationType.WALK);

                return orders.remove();
            }
            LOGGER.warn("{} is homeless", actor);
        }
        else if (state == State.AT_HOME)// && gameClock.getHour() == 8)
        {
            LOGGER.trace("Time to go to work via the car");
            Set<CommercialBuilding> placesOfWork = commercialManager.getPlacesOfWork(actor);

            if (!placesOfWork.isEmpty())
            {
                currentTarget.exit(actor);
                state = State.GOING_TO_WORK;
                currentTarget = placesOfWork.iterator().next();

                queueOrder(roadManager.getClosestRoadPoint(actor.getPosition()), NavigationType.WALK);
                queueOrder(roadManager.getClosestRoadPoint(currentTarget.getEntryPoint()), NavigationType.CAR);
                queueOrder(currentTarget.getEntryPoint(), NavigationType.WALK);

                return orders.remove();
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
        LOGGER.debug("Arrived at {}", actor.getPosition());

        if (orders.size() > 0)
            releaseNextOrder = true;
        else
        {
            if (state == State.GOING_HOME)
            {
                LOGGER.debug("Arrived at home");
                currentTarget.occupy(actor);
                state = State.AT_HOME;
                currentOccupiedStartTime = gameClock.getSecondsSinceGameStart();
            }
            else if (state == State.GOING_TO_WORK)
            {
                LOGGER.debug("Arrived at work");
                currentTarget.occupy(actor);
                state = State.AT_WORK;
                currentOccupiedStartTime = gameClock.getSecondsSinceGameStart();
            }
        }
    }

    private void queueOrder(Vector targetPoint, NavigationType type)
    {
        orders.add(new NavigationOrder(targetPoint, type));
    }
}
