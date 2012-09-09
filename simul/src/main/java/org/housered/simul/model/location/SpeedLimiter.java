package org.housered.simul.model.location;


public class SpeedLimiter
{
    private double maxDistancePerSecond;
    private double distanceMovedThisTick;
    private float dt;

    public void setSpeedLimit(double maxDistancePerSecond)
    {
        this.maxDistancePerSecond = maxDistancePerSecond;
    }

    public void startNewTick(float dt)
    {
        distanceMovedThisTick = 0;
        this.dt = dt;
    }

    public Vector incrementPosition(Vector delta)
    {
        double wantedMag = Math.min(maxDistancePerSecond * dt - distanceMovedThisTick, delta.magnitude());
        wantedMag = Math.max(wantedMag, 0);
        distanceMovedThisTick += wantedMag;
        return delta.scaleToMagnitudeCopy(wantedMag);
    }
}
