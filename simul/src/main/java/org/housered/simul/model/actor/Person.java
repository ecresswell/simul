package org.housered.simul.model.actor;

import java.awt.Color;

import org.housered.simul.model.actor.brain.HighLevelBrain;
import org.housered.simul.model.actor.brain.HighLevelBrainImpl;
import org.housered.simul.model.actor.brain.NavigationBrain;
import org.housered.simul.model.actor.brain.NavigationMeshBrain;
import org.housered.simul.model.assets.AssetManager;
import org.housered.simul.model.location.SpeedLimiter;
import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.NavigationManager;
import org.housered.simul.model.work.CommercialManager;
import org.housered.simul.model.world.GameClock;
import org.housered.simul.model.world.Tickable;
import org.housered.simul.view.GraphicsAdapter;
import org.housered.simul.view.Renderable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Person implements Renderable, Tickable, Actor
{
    private static Logger LOGGER = LoggerFactory.getLogger(Person.class);
    private final long id;

    private HighLevelBrain highLevel;
    private NavigationBrain navigation;
    private SpeedLimiter speedLimiter = new SpeedLimiter();

    public Person(long id, AssetManager assetManager, CommercialManager commercialManager,
            NavigationManager navigationManager, GameClock gameClock)
    {
        this.id = id;
        speedLimiter.setSpeedLimit(3);
        highLevel = new HighLevelBrainImpl(this, assetManager, commercialManager, gameClock);
        navigation = new NavigationMeshBrain(navigationManager);
    }

    @Override
    public Vector getPosition()
    {
        return navigation.getPosition();
    }

    @Override
    public long getId()
    {
        return id;
    }

    @Override
    public void render(GraphicsAdapter r)
    {
        r.setColour(Color.GREEN);
        r.fillCircle(getPosition(), 3);
    }
    
    @Override
    public byte getZOrder()
    {
        return PERSON_Z_ORDER;
    }

    @Override
    public void tick(float dt)
    {
        speedLimiter.startNewTick(dt);

        Vector target = highLevel.decideWhereToGo();

        if (target != null)
        {
            navigation.setTarget(target);
            LOGGER.trace("[{}]Moving towards target - {}", new Object[] {this, target});
        }

        if (navigation.hasTarget())
        {
            Vector targetPoint = navigation.getNextPoint();
            incrementPosition(targetPoint.translateCopy(getPosition().negateCopy()));
        }

        if (navigation.hasArrivedAtTarget())
        {
            highLevel.arrivedAtTarget();
        }
    }

    private void incrementPosition(Vector delta)
    {
        Vector move = speedLimiter.incrementPosition(delta);
        getPosition().translate(move);
    }

    @Override
    public String toString()
    {
        return "Person [id=" + id + "]";
    }
}
