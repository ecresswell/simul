package org.housered.simul.model.navigation.road;

import java.awt.Color;

import org.housered.simul.model.actor.Actor;
import org.housered.simul.model.actor.brain.HighLevelBrain;
import org.housered.simul.model.actor.brain.NavigationBrain;
import org.housered.simul.model.location.SpeedLimiter;
import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.ActorController;
import org.housered.simul.view.GraphicsAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Envelope;

public class CarController implements ActorController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CarController.class);
    private static final double HOW_FAR_TO_LOOK_AHEAD = 20;
    private static final double MINIMUM_LOOK_AHEAD_WIDTH = 5;
    private static final double CARELESSNESS_FACTOR = 10;
    private final Actor actor;
    private final NavigationBrain navigation;
    private final CarTracker carTracker;
    private final SpeedLimiter speedLimiter = new SpeedLimiter();
    private final HighLevelBrain highLevel;

    private Vector lookAheadPosition;
    private Vector lookAheadSize;
    private boolean actuallyBreaking;

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

            Envelope e = getLookAheadEnvelope(this, direction.scaleToMagnitudeCopy(HOW_FAR_TO_LOOK_AHEAD),
                    MINIMUM_LOOK_AHEAD_WIDTH);
            lookAheadPosition = new Vector(e.getMinX(), e.getMinY());
            lookAheadSize = new Vector(e.getWidth(), e.getHeight());

            CarController closestCar = carTracker.getClosestCar(this, direction, MINIMUM_LOOK_AHEAD_WIDTH);

            double breaking = 0;
            actuallyBreaking = false;

            if (closestCar != null)
            {
                double distance = CarTracker.getDistanceToCar(this, closestCar);
                breaking = (CARELESSNESS_FACTOR - distance) / CARELESSNESS_FACTOR;
                breaking = Math.max(0, breaking);

                if (breaking > 0)
                {
                    LOGGER.trace("Breaking {} - closest car to {} is {}, {} away", new Object[] {breaking, this,
                            closestCar, distance});
                    actuallyBreaking = true;
                }
            }

            double maxSpeed = 5 * (1 - breaking);
            //maxSpeed = Math.max(maxSpeed, 0.01f);
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

    @Override
    public void render(GraphicsAdapter r)
    {
        if (lookAheadPosition == null || lookAheadSize == null)
            return;

        r.setColour(Color.CYAN);
//        r.fillCircle(actor.getPosition(), actor.getSize().x);
    }

    @Override
    public byte getZOrder()
    {
        return BUILDING_Z_ORDER;
    }

    static Envelope getLookAheadEnvelope(CarController car, Vector direction, double minWidth)
    {
        double otherX = direction.x;
        double otherY = direction.y;
        Vector position = car.getPosition();

        if (otherX >= 0 && otherX < minWidth)
            otherX = minWidth;
        else if (otherX < 0 && otherX > -minWidth)
            otherX = -minWidth;

        if (otherY >= 0 && otherY < minWidth)
            otherY = minWidth;
        else if (otherY < 0 && otherY > -minWidth)
            otherY = -minWidth;

        Vector size = car.getSize();
        double xOffset = direction.x < 0 ? size.x : 0;
        double yOffset = direction.y < 0 ? size.y : 0;

        return new Envelope(position.x + xOffset, position.x + otherX + xOffset, position.y + yOffset, position.y
                + otherY + yOffset);
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
