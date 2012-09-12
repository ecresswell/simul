package org.housered.simul.model.navigation.road;

import java.awt.Color;

import org.housered.simul.controller.SimulMain;
import org.housered.simul.model.actor.Actor;
import org.housered.simul.model.actor.brain.HighLevelBrain;
import org.housered.simul.model.actor.brain.NavigationBrain;
import org.housered.simul.model.location.SpeedLimiter;
import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.ActorController;
import org.housered.simul.view.GraphicsAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CarController implements ActorController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CarController.class);
    private static final double HOW_FAR_TO_LOOK_AHEAD = 4;
    private static final double CARELESSNESS_FACTOR = 10;
    private static final double MINIMUM_SPEED = 1;
    private static final double MAX_SPEED = 8;
    private final Actor actor;
    private final NavigationBrain navigation;
    private final CarTracker carTracker;
    private final SpeedLimiter speedLimiter = new SpeedLimiter();
    private final HighLevelBrain highLevel;

    private int checkInFrontSkip = 0;
    private Vector lookAheadPosition;
    private Vector lookAheadDirection;

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

            if (checkInFrontSkip == 0)
            {
                lookAheadDirection = direction.scaleToMagnitudeCopy(HOW_FAR_TO_LOOK_AHEAD);
                lookAheadPosition = getPosition().translateCopy(getSize().x / 2, getSize().y / 2);
                CarController closestCar = carTracker.getClosestCar(this, lookAheadDirection);

                double braking = 0;

                if (closestCar != null)
                {
                    double distance = CarTracker.getDistanceToCar(this, closestCar);
                    distance -= getSize().x * 2;
                    braking = (CARELESSNESS_FACTOR - distance) / CARELESSNESS_FACTOR;
                    braking = Math.max(0, braking);

                    if (braking > 0)
                    {
                        LOGGER.trace("Breaking {} - closest car to {} is {}, {} away", new Object[] {braking, this,
                                closestCar, distance});
                    }
                }

                double maxSpeed = MAX_SPEED * (1 - braking);
                maxSpeed = Math.max(maxSpeed, MINIMUM_SPEED);
                speedLimiter.setSpeedLimit(maxSpeed);
                
                checkInFrontSkip = SimulMain.carControllerDelay;
            }
            else
            {
                checkInFrontSkip--;
            }

            getPosition().translate(actualMove);
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

    @Override
    public void render(GraphicsAdapter r)
    {
        if (lookAheadPosition == null || lookAheadDirection == null)
            return;

        r.setColour(Color.CYAN);
        r.drawVector(lookAheadPosition, lookAheadDirection);
    }

    @Override
    public byte getZOrder()
    {
        return BUILDING_Z_ORDER;
    }

    @Override
    public String toString()
    {
        return "CarController [actor=" + actor + "]";
    }

    @Override
    public Vector getSize()
    {
        return actor.getSize();
    }
}
