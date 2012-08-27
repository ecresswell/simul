package org.housered.simul.model.world;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameClockImpl implements GameClock, Tickable
{
    private static Logger LOGGER = LoggerFactory.getLogger(GameClockImpl.class);
    private int inGameSecondsPerSecond;
    private int secondsSinceMidnight;
    private int milliseconds;

    public GameClockImpl(long secondsSinceMidnight, int inGameSecondsPerSecond)
    {
        this.secondsSinceMidnight = (int) secondsSinceMidnight;
        this.inGameSecondsPerSecond = inGameSecondsPerSecond;
    }

    public void setSpeed(int inGameSecondsPerSecond)
    {
        this.inGameSecondsPerSecond = inGameSecondsPerSecond;
    }

    @Override
    public int getSecondsSinceMidnight()
    {
        return Math.round(secondsSinceMidnight);
    }

    @Override
    public void tick(float dt)
    {
        milliseconds += inGameSecondsPerSecond * dt * 1000;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds);

        if (seconds > 0)
        {
            secondsSinceMidnight += seconds;
            milliseconds -= seconds * 1000;
        }

        if (secondsSinceMidnight >= TimeUnit.DAYS.toSeconds(1))
        {
            secondsSinceMidnight -= TimeUnit.DAYS.toSeconds(1);
        }
    }

    @Override
    public int getHour()
    {
        return (int) TimeUnit.SECONDS.toHours(Math.round(secondsSinceMidnight));
    }

    @Override
    public int getMinutes()
    {
        return (int) (TimeUnit.SECONDS.toMinutes(Math.round(secondsSinceMidnight)) - TimeUnit.HOURS
                .toMinutes(getHour()));
    }

    @Override
    public int getSeconds()
    {
        return (int) (secondsSinceMidnight - TimeUnit.MINUTES.toSeconds(getMinutes()) - TimeUnit.HOURS
                .toSeconds(getHour()));
    }
}
