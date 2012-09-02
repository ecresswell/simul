package org.housered.simul.model.work;

import org.housered.simul.model.assets.Occupiable;
import org.housered.simul.model.world.GameClock;

public class JobDefinition
{
    protected final int startHour;
    protected final int startMinute;
    protected final long jobLengthInSeconds;
    protected final Occupiable jobLocation;
    protected final GameClock gameClock;

    public JobDefinition(GameClock gameClock, Occupiable jobLocation, int startHour, int startMinute,
            long jobLengthInSeconds)
    {
        this.gameClock = gameClock;
        this.jobLocation = jobLocation;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.jobLengthInSeconds = jobLengthInSeconds;
    }

    public int getStartHour()
    {
        return startHour;
    }

    public int getStartMinute()
    {
        return startMinute;
    }

    public long getJobLengthInSeconds()
    {
        return jobLengthInSeconds;
    }

    public Job createJob()
    {
        return new Job(gameClock, getJobLocation(), startHour, startMinute, jobLengthInSeconds);
    }

    public Occupiable getJobLocation()
    {
        return jobLocation;
    }
}
