package org.housered.simul.controller;

public class FrameMetrics
{
    private long frameNumber;
    private long sleepAmount;

    void logSleep(long sleepAmount)
    {
        frameNumber++;
        this.sleepAmount += sleepAmount;
    }

    long getFrameNumber()
    {
        return frameNumber;
    }

    double getAverageSleepAmount()
    {
        return (double) sleepAmount / frameNumber;
    }
}
