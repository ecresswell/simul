package org.housered.simul.model.actor.brain;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.housered.simul.model.actor.Actor;
import org.housered.simul.model.assets.AssetManager;
import org.housered.simul.model.assets.Occupiable;
import org.housered.simul.model.location.Vector;
import org.housered.simul.model.world.GameClock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HighLevelBrainImpl implements HighLevelBrain
{
    private static final Logger LOGGER = LoggerFactory.getLogger(HighLevelBrainImpl.class);
    private static final long GAME_SECONDS_UNTIL_BORED = TimeUnit.MINUTES.toSeconds(6);
    private final Actor actor;
    private final AssetManager assetManager;
    private final GameClock gameClock;

    private Occupiable currentTarget;
    private boolean occupying = false;
    private long currentOccupiedStartTime;

    public HighLevelBrainImpl(Actor actor, AssetManager assetManager, GameClock gameClock)
    {
        this.actor = actor;
        this.assetManager = assetManager;
        this.gameClock = gameClock;
    }

    @Override
    public Vector decideWhereToGo()
    {
        if (occupying && gameClock.getSecondsSinceGameStart() > currentOccupiedStartTime + GAME_SECONDS_UNTIL_BORED)
        {
            LOGGER.debug("[{}] has decided to frolick", actor);
            //we're bored, let's bounce
            currentTarget.exit(actor);
            occupying = false;
            currentTarget = null;
        }

        if (currentTarget == null)
        {
            //let's go somewhere
            Set<Occupiable> allBuildings = assetManager.getAssets();

            if (allBuildings.isEmpty())
                return null;

            currentTarget = getRandomBuilding(allBuildings);
            return currentTarget.getEntryPoint();
        }

        //we've already decided where to go
        return null;
    }

    @Override
    public void arrivedAtTarget()
    {
        if (occupying)
            return;

        currentTarget.occupy(actor);
        currentOccupiedStartTime = gameClock.getSecondsSinceGameStart();
        occupying = true;
    }

    private Occupiable getRandomBuilding(Set<Occupiable> all)
    {
        int random = new Random().nextInt(all.size());

        int i = 0;
        for (Occupiable occupiable : all)
        {
            if (i == random)
                return occupiable;
            i += 1;
        }
        
        return null;
    }
}
