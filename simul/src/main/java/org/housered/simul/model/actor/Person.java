package org.housered.simul.model.actor;

import java.awt.Color;

import org.housered.simul.model.actor.brain.HighLevelBrain;
import org.housered.simul.model.actor.brain.HighLevelBrainImpl;
import org.housered.simul.model.actor.brain.NavigationBrain;
import org.housered.simul.model.actor.brain.NavigationMeshBrain;
import org.housered.simul.model.assets.AssetManager;
import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.ActorController;
import org.housered.simul.model.navigation.NavigationManager;
import org.housered.simul.model.navigation.OldNavigationOrder;
import org.housered.simul.model.navigation.OldNavigationOrder.NavigationType;
import org.housered.simul.model.navigation.PedestrianController;
import org.housered.simul.model.navigation.road.CarController;
import org.housered.simul.model.navigation.road.RoadManager;
import org.housered.simul.model.navigation.tube.TubePassengerController;
import org.housered.simul.model.navigation.tube.TubeManager;
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
    private final ActorController tubeController;
    private HighLevelBrain highLevel;
    private Vector size = new Vector(3, 3);
    private Vector position = new Vector();

    private boolean invisible;
    private NavigationType currentType;

    public Person(long id, AssetManager assetManager, JobManager jobManager, NavigationManager navigationManager,
            GameClock gameClock, RoadManager roadNetworkManager, TubeManager tubeManager)
    {
        this.id = id;
        highLevel = new HighLevelBrainImpl(this, assetManager, jobManager, gameClock, roadNetworkManager, tubeManager);
        NavigationMeshBrain navigation = new NavigationMeshBrain(navigationManager, roadNetworkManager, this);
        carController = new CarController(this, highLevel, navigation, roadNetworkManager.getCarTracker());
        pedestrianController = new PedestrianController(this, highLevel, navigation);
        tubeController = new TubePassengerController(this, highLevel, tubeManager);
    }

    @Override
    public Vector getPosition()
    {
        return position;
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
        if (currentType == NavigationType.CAR)
        {
            carController.render(r);
            r.setColour(Color.ORANGE);
            r.drawRect(getPosition(), getSize());
        }
        else if (currentType == NavigationType.WALK)
        {
            pedestrianController.render(r);
            r.setColour(Color.GREEN);
            r.fillCircle(getPosition(), getSize().x);
        }
        else if (currentType == NavigationType.TUBE)
        {
            tubeController.render(r);
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
        OldNavigationOrder target = highLevel.decideWhereToGo();

        if (target != null)
        {
            if (target.getType() == NavigationType.CAR)
            {
                currentType = NavigationType.CAR;
                tubeController.relinquishControl();
                pedestrianController.relinquishControl();
                carController.giveDirectControl(target);
            }
            else if (target.getType() == NavigationType.WALK)
            {
                currentType = NavigationType.WALK;
                tubeController.relinquishControl();
                carController.relinquishControl();
                pedestrianController.giveDirectControl(target);
            }
            else if (target.getType() == NavigationType.TUBE)
            {
                currentType = NavigationType.TUBE;
                carController.relinquishControl();
                pedestrianController.relinquishControl();
                tubeController.giveDirectControl(target);
            }

            LOGGER.trace("[{}]Moving towards target - {}", new Object[] {this, target});
        }

        if (currentType == NavigationType.CAR)
            carController.tick(dt);
        else if (currentType == NavigationType.WALK)
            pedestrianController.tick(dt);
        else if (currentType == NavigationType.TUBE)
            tubeController.tick(dt);
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

    @Override
    public Vector getSize()
    {
        return size;
    }
}
