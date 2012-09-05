package org.housered.simul.model.actor;

import java.awt.Color;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.housered.simul.model.actor.brain.HighLevelBrain;
import org.housered.simul.model.actor.brain.HighLevelBrainImpl;
import org.housered.simul.model.actor.brain.NavigationBrain;
import org.housered.simul.model.actor.brain.NavigationMeshBrain;
import org.housered.simul.model.assets.AssetManager;
import org.housered.simul.model.location.SpeedLimiter;
import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.NavigationManager;
import org.housered.simul.model.navigation.NavigationOrder;
import org.housered.simul.model.navigation.RoadNetworkManager;
import org.housered.simul.model.work.JobManager;
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
    private boolean invisible;

    public Person(long id, AssetManager assetManager, JobManager jobManager, NavigationManager navigationManager,
            GameClock gameClock, RoadNetworkManager roadNetworkManager)
    {
        this.id = id;
        speedLimiter.setSpeedLimit(3);
        highLevel = new HighLevelBrainImpl(this, assetManager, jobManager, gameClock, roadNetworkManager);
        navigation = new NavigationMeshBrain(navigationManager, roadNetworkManager);
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
        if (invisible)
            return;

        r.setColour(Color.GREEN);
        //draw in the middle
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

        NavigationOrder target = highLevel.decideWhereToGo();

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

    @Override
    public void setInvisible(boolean invisible)
    {
        this.invisible = invisible;
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
