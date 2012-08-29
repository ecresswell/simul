package org.housered.simul.model.location;


public class SpeedLimiter
{
    private double maxDistancePerSecond;
    private double distanceMovedThisTick;
    private float dt;

    public void setSpeedLimit(float maxDistancePerSecond)
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
        return scaleToMagnitudeCopy(delta, wantedMag);
    }

    private Vector scaleToMagnitudeCopy(Vector direction, double wantedMagnitude)
    {
        if (direction.magnitude() == 0)
            return new Vector();

        double scale = wantedMagnitude / direction.magnitude();
        return new Vector(direction.x * scale, direction.y * scale);
    }
}
