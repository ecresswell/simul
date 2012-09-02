package org.housered.simul.model.world;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.housered.simul.model.actor.Person;
import org.housered.simul.model.actor.PersonFactory;
import org.housered.simul.model.assets.AssetManager;
import org.housered.simul.model.assets.House;
import org.housered.simul.model.assets.HouseFactory;
import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.NavigationManager;
import org.housered.simul.model.navigation.Road;
import org.housered.simul.model.navigation.RoadNetworkManager;
import org.housered.simul.model.work.JobDefinition;
import org.housered.simul.model.work.JobManager;
import org.housered.simul.model.work.Workplace;
import org.housered.simul.model.work.WorkplaceFactory;

public class CityPlanner
{
    private final GameClock gameClock;
    private final AssetManager assetManager;
    private final NavigationManager navigationManager;
    private final IdGenerator idGenerator;
    private final RoadNetworkManager roadNetworkManager;
    private final JobManager jobManager;

    public CityPlanner(IdGenerator idGenerator, GameClock gameClock, AssetManager assetManager, JobManager jobManager,
            NavigationManager navigationManager, RoadNetworkManager roadNetworkManager)
    {
        this.idGenerator = idGenerator;
        this.gameClock = gameClock;
        this.assetManager = assetManager;
        this.jobManager = jobManager;
        this.navigationManager = navigationManager;
        this.roadNetworkManager = roadNetworkManager;
    }

    public void loadLevel(World world)
    {
        loadSimpleMap(world);
        //loadComplicatedMap(world);
    }

    private void loadSimpleMap(World world)
    {
        PersonFactory personFactory = new PersonFactory(idGenerator, assetManager, jobManager, navigationManager,
                gameClock, roadNetworkManager);
        HouseFactory houseFactory = new HouseFactory(idGenerator);
        WorkplaceFactory workplaceFactory = new WorkplaceFactory(idGenerator);

        Person person = personFactory.createPerson(50, 50);
        House house = houseFactory.createHouse(100, 100);
        Workplace workplace = workplaceFactory.createWorkplace(300, 200);

        assetManager.createDeed(person, house);
        jobManager.createContract(person, new JobDefinition(gameClock, workplace, 7, 30, TimeUnit.HOURS.toSeconds(1)));

        Road road1 = new Road(new Vector(130, 130), new Vector(20, 400));
        Road road2 = new Road(new Vector(150, 330), new Vector(400, 20));
        Road road3 = new Road(new Vector(290, 220), new Vector(20, 200));

        world.addEntities(person, house, workplace, road1, road2, road3);
    }

    private void loadComplicatedMap(World world)
    {
        PersonFactory personFactory = new PersonFactory(idGenerator, assetManager, jobManager, navigationManager,
                gameClock, roadNetworkManager);
        HouseFactory houseFactory = new HouseFactory(idGenerator);
        WorkplaceFactory workplaceFactory = new WorkplaceFactory(idGenerator);

        List<Workplace> workplaces = new LinkedList<Workplace>();

        for (int x = 600; x < 800; x += 50)
        {
            for (int y = 50; y < 600; y += 50)
            {
                Workplace workplace = workplaceFactory.createWorkplace(x, y);
                workplaces.add(workplace);
                world.addEntity(workplace);
            }
        }

        for (int x = 50; x < 600; x += 50)
        {
            for (int y = 50; y < 600; y += 50)
            {
                House house = houseFactory.createHouse(x, y);
                world.addEntity(house);
                for (int i = 0; i < 30; i++)
                {
                    Person person = personFactory.createPerson(x - 1, y - 1);
                    world.addEntity(person);
                    assetManager.createDeed(person, house);
                    Workplace randomWorkplace = workplaces.get(new Random().nextInt(workplaces.size()));
                    jobManager.createContract(person, new JobDefinition(gameClock, randomWorkplace, 7, 30,
                            TimeUnit.HOURS.toSeconds(8)));
                }
            }
        }

        Road road = new Road(new Vector(0, 325), new Vector(750, 20));
        world.addEntity(road);
    }
}
