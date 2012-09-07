package org.housered.simul.model.actor;

import java.awt.Color;

import org.housered.simul.model.actor.brain.HighLevelBrain;
import org.housered.simul.model.actor.brain.HighLevelBrainImpl;
import org.housered.simul.model.actor.brain.NavigationBrain;
import org.housered.simul.model.actor.brain.NavigationMeshBrain;
import org.housered.simul.model.assets.AssetManager;
import org.housered.simul.model.location.SpeedLimiter;
import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.ActorController;
import org.housered.simul.model.navigation.NavigationManager;
import org.housered.simul.model.navigation.NavigationOrder;
import org.housered.simul.model.navigation.PedestrianController;
import org.housered.simul.model.navigation.NavigationOrder.NavigationType;
import org.housered.simul.model.navigation.road.CarController;
import org.housered.simul.model.navigation.road.RoadNetworkManager;
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
    private final ActorController carController;
    private final ActorController pedestrianController;
    private HighLevelBrain highLevel;
    private NavigationBrain navigation;

    private boolean invisible;
    private boolean inACar;

    public Person(long id, AssetManager assetManager, JobManager jobManager, NavigationManager navigationManager,
            GameClock gameClock, RoadNetworkManager roadNetworkManager)
    {
        this.id = id;
        highLevel = new HighLevelBrainImpl(this, assetManager, jobManager, gameClock, roadNetworkManager);
        navigation = new NavigationMeshBrain(navigationManager, roadNetworkManager);

        carController = new CarController(this, navigation, roadNetworkManager);
        pedestrianController = new PedestrianController(this, highLevel, navigation);
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

        //draw in the middle
        if (inACar)
        {
            r.setColour(Color.ORANGE);
            r.drawRect(getPosition(), new Vector(3, 3));
        }
        else
        {
            r.setColour(Color.GREEN);
            r.fillCircle(getPosition(), 3);
        }
    }

    @Override
    public byte getZOrder()
    {
        return PERSON_Z_ORDER;
    }

    @Override
    public void tick(float dt)
    {
        NavigationOrder target = highLevel.decideWhereToGo();

        if (target != null)
        {
            if (target.getType() == NavigationType.CAR)
                inACar = true;
            else if (target.getType() == NavigationType.WALK)
                inACar = false;

            navigation.setTarget(target);
            LOGGER.trace("[{}]Moving towards target - {}", new Object[] {this, target});
        }

//        if (inACar)
//            carController.tick(dt);
//        else
            pedestrianController.tick(dt);
    }

    @Override
    public void setInvisible(boolean invisible)
    {
        this.invisible = invisible;
    }

    @Override
    public String toString()
    {
        return "Person [id=" + id + "]";
    }
}
