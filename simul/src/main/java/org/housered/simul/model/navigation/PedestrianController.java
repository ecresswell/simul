package org.housered.simul.model.navigation;

import org.housered.simul.model.actor.Actor;
import org.housered.simul.model.actor.brain.HighLevelBrain;
import org.housered.simul.model.actor.brain.NavigationBrain;
import org.housered.simul.model.location.SpeedLimiter;
import org.housered.simul.model.location.Vector;
import org.housered.simul.view.GraphicsAdapter;

public class PedestrianController implements ActorController
{
    private final Actor actor;
    private final NavigationBrain navigation;
    private final HighLevelBrain highLevel;
    private final SpeedLimiter speedLimiter = new SpeedLimiter();

    public PedestrianController(Actor actor, HighLevelBrain highLevel, NavigationBrain navigation)
    {
        this.actor = actor;
        this.highLevel = highLevel;
        this.navigation = navigation;

        speedLimiter.setSpeedLimit(3);
    }

    @Override
    public void tick(float dt)
    {
        speedLimiter.startNewTick(dt);

        if (navigation.hasTarget())
        {
            Vector targetPoint = navigation.getNextPoint();
            Vector direction = targetPoint.translateCopy(actor.getPosition().negateCopy());
            incrementPosition(direction);
        }

        if (navigation.hasArrivedAtTarget())
        {
            highLevel.arrivedAtTarget();
        }
    }

    private void incrementPosition(Vector delta)
    {
        Vector move = speedLimiter.incrementPosition(delta);
        actor.getPosition().translate(move);
    }

    @Override
    public Vector getPosition()
    {
        return actor.getPosition();
    }

    @Override
    public void giveDirectControl(NavigationOrder target)
    {
    }

    @Override
    public void relinquishControl()
    {
    }

    @Override
    public void render(GraphicsAdapter r)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public byte getZOrder()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    @Override
    public Vector getSize()
    {
        return actor.getSize();
    }
}
