package org.housered.simul.model.world;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;

public class GameClockImpl implements GameClock, Tickable
{
    private final long SECONDS_PER_DAY = TimeUnit.DAYS.toSeconds(1);

    private int inGameSecondsPerSecond;
    private long secondsSinceMidnight;
    private int milliseconds;
    private int days = 1;

    public GameClockImpl(long secondsSinceMidnight, int inGameSecondsPerSecond)
    {
        this.secondsSinceMidnight = (int) secondsSinceMidnight;
        this.inGameSecondsPerSecond = inGameSecondsPerSecond;
    }

    public void setSpeed(int inGameSecondsPerSecond)
    {
        this.inGameSecondsPerSecond = inGameSecondsPerSecond;
    }

    public void incrementSpeed(int delta)
    {
        inGameSecondsPerSecond += delta;
    }

    @Override
    public long getSecondsSinceGameStart()
    {
        return secondsSinceMidnight + (days - 1) * SECONDS_PER_DAY;
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
            days++;
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

    @Override
    public int getDay()
    {
        return days;
    }

    @Override
    public int getGameSecondsPerActualSecond()
    {
        return inGameSecondsPerSecond;
    }

    @Override
    public String getDigitalClock()
    {
        String hour = StringUtils.leftPad(String.valueOf(getHour()), 2, "0");
        String minute = StringUtils.leftPad(String.valueOf(getMinutes()), 2, "0");

        return hour + ":" + minute;
    }
}
