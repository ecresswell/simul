package org.housered.simul.model.actor.brain;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.housered.simul.model.actor.Actor;
import org.housered.simul.model.assets.AssetManager;
import org.housered.simul.model.assets.Occupiable;
import org.housered.simul.model.location.Vector;
import org.housered.simul.model.work.CommercialBuilding;
import org.housered.simul.model.work.CommercialManager;
import org.housered.simul.model.world.GameClock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HighLevelBrainImpl implements HighLevelBrain
{
    private enum State
    {
        AT_HOME, AT_WORK, GOING_HOME, GOING_TO_WORK
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(HighLevelBrainImpl.class);
    private static final long GAME_SECONDS_AT_WORK = TimeUnit.HOURS.toSeconds(1);
    private final Actor actor;
    private final AssetManager assetManager;
    private final CommercialManager commercialManager;
    private final GameClock gameClock;

    private Occupiable currentTarget;
    private long currentOccupiedStartTime;
    private State state;

    public HighLevelBrainImpl(Actor actor, AssetManager assetManager, CommercialManager commercialManager,
            GameClock gameClock)
    {
        this.actor = actor;
        this.assetManager = assetManager;
        this.commercialManager = commercialManager;
        this.gameClock = gameClock;
    }

    @Override
    public Vector decideWhereToGo()
    {
        if (state == State.AT_HOME)// && gameClock.getHour() == 8)
        {
            LOGGER.trace("Time to go to work");
            Set<CommercialBuilding> placesOfWork = commercialManager.getPlacesOfWork(actor);

            if (!placesOfWork.isEmpty())
            {
                currentTarget.exit(actor);
                state = State.GOING_TO_WORK;
                currentTarget = placesOfWork.iterator().next();

                return currentTarget.getEntryPoint();
            }
            LOGGER.warn("{} is unemployed", actor);
        }

        if (currentTarget == null
                || (state == State.AT_WORK && gameClock.getSecondsSinceGameStart() - currentOccupiedStartTime > GAME_SECONDS_AT_WORK))
        {
            LOGGER.trace("Heading home");
            Set<Occupiable> assets = assetManager.getAssets(actor);

            if (!assets.isEmpty())
            {
                if (currentTarget != null)
                    currentTarget.exit(actor);

                state = State.GOING_HOME;
                currentTarget = assets.iterator().next();

                return currentTarget.getEntryPoint();
            }
            LOGGER.warn("{} is homeless", actor);
        }

        //we've already decided where to go
        return null;
    }

    @Override
    public void arrivedAtTarget()
    {
        if (state == State.GOING_TO_WORK)
        {
            LOGGER.trace("Arrived at work");
            state = State.AT_WORK;
            currentTarget.occupy(actor);
            currentOccupiedStartTime = gameClock.getSecondsSinceGameStart();
        }
        else if (state == State.GOING_HOME)
        {
            LOGGER.trace("Arrived at home");
            state = State.AT_HOME;
            currentTarget.occupy(actor);
            currentOccupiedStartTime = gameClock.getSecondsSinceGameStart();
        }
    }
}
