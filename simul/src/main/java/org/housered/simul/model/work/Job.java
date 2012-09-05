package org.housered.simul.model.work;

import java.util.Random;

import org.housered.simul.model.assets.Occupiable;
import org.housered.simul.model.world.GameClock;

public class Job extends JobDefinition
{
    private static final Random RANDOM = new Random();
    private long timeGotToWork;

    public Job(GameClock gameClock, Occupiable jobLocation, int startHour, int startMinute, long jobLengthInSeconds)
    {
        super(gameClock, jobLocation, startHour, startMinute, jobLengthInSeconds);
    }

    public boolean shouldGoToWork()
    {
        return RANDOM.nextFloat() < 0.1f;
    }

    public boolean shouldLeaveWork()
    {
        return gameClock.getSecondsSinceGameStart() - timeGotToWork > getJobLengthInSeconds();
    }

    public void arrivedAtWork()
    {
        timeGotToWork = gameClock.getSecondsSinceGameStart();
    }

    public void arrivedAtHome()
    {

    }
}
