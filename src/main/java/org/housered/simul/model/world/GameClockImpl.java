package org.housered.simul.model.world;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameClockImpl implements GameClock, Tickable
{
    private static Logger LOGGER = LoggerFactory.getLogger(GameClockImpl.class);
    private int inGameSecondsPerSecond;
    private float secondsSinceMidnight;

    public GameClockImpl(long secondsSinceMidnight, int inGameSecondsPerSecond)
    {
        this.secondsSinceMidnight = secondsSinceMidnight;
        this.inGameSecondsPerSecond = inGameSecondsPerSecond;
    }

    public void setSpeed(int inGameSecondsPerSecond)
    {
        this.inGameSecondsPerSecond = inGameSecondsPerSecond;
    }

    @Override
    public int getTime()
    {
        return Math.round(secondsSinceMidnight);
    }

    @Override
    public void tick(float dt)
    {
        secondsSinceMidnight += inGameSecondsPerSecond * dt;

        if (secondsSinceMidnight > TimeUnit.DAYS.toSeconds(1))
        {
            secondsSinceMidnight = 0;
        }

        long hours = TimeUnit.SECONDS.toHours(Math.round(secondsSinceMidnight));
        long minutes = TimeUnit.SECONDS.toMinutes(Math.round(secondsSinceMidnight)) - TimeUnit.HOURS.toMinutes(hours);
        LOGGER.debug("The time is: {}:{}", hours, minutes);
    }
}
