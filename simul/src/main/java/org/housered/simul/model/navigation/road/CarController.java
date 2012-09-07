package org.housered.simul.model.navigation.road;

import java.util.List;

import org.housered.simul.model.actor.Actor;
import org.housered.simul.model.actor.brain.HighLevelBrain;
import org.housered.simul.model.actor.brain.NavigationBrain;
import org.housered.simul.model.location.SpeedLimiter;
import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.ActorController;

public class CarController implements ActorController
{
    private final Actor actor;
    private final NavigationBrain navigation;
    private final CarTracker carTracker;
    private final SpeedLimiter speedLimiter = new SpeedLimiter();
    private final HighLevelBrain highLevel;

    public CarController(Actor actor, HighLevelBrain highLevel, NavigationBrain navigation, CarTracker carTracker)
    {
        this.actor = actor;
        this.highLevel = highLevel;
        this.navigation = navigation;
        this.carTracker = carTracker;

        speedLimiter.setSpeedLimit(5);
    }

    @Override
    public void tick(float dt)
    {
        speedLimiter.startNewTick(dt);

        if (navigation.hasTarget())
        {
            Vector targetPoint = navigation.getNextPoint();
            Vector direction = targetPoint.translateCopy(actor.getPosition().negateCopy());
            Vector actualMove = speedLimiter.incrementPosition(direction);
            getPosition().translate(actualMove);

            List<CarController> cars = carTracker.getCars(getPosition(), actualMove.scaleToMagnitudeCopy(5));
            float maxSpeed = 5 - (float) cars.size() / 1;
            maxSpeed = Math.max(maxSpeed, 0.01f);
            speedLimiter.setSpeedLimit(maxSpeed);
        }

        if (navigation.hasArrivedAtTarget())
        {
            highLevel.arrivedAtTarget();
        }
    }

    @Override
    public Vector getPosition()
    {
        return actor.getPosition();
    }

    @Override
    public void giveDirectControl()
    {
        carTracker.addCar(this);
    }

    @Override
    public void relinquishControl()
    {
        carTracker.removeCar(this);
    }
}
