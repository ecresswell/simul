package org.housered.simul.model.work;

import org.housered.simul.model.assets.Occupiable;
import org.housered.simul.model.world.GameClock;

public class Job extends JobDefinition
{
    private long timeGotToWork;
    
    public Job(GameClock gameClock, Occupiable jobLocation, int startHour, int startMinute, long jobLengthInSeconds)
    {
        super(gameClock, jobLocation, startHour, startMinute, jobLengthInSeconds);
    }

    public boolean shouldGoToWork()
    {
        return true;
    }
    
    public boolean shouldLeaveWork()
    {
        return gameClock.getSecondsSinceGameStart() - timeGotToWork > getJobLengthInSeconds();
    }
    
    public void arrivedAtWork()
    {
        timeGotToWork = gameClock.getSecondsSinceGameStart();
    }
    
    public void leftWork()
    {
        
    }
}
