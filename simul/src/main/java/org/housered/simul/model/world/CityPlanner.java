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

import straightedge.geom.path.PathData.Result;

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
        //loadSimpleMap(world);
        //loadComplicatedMap(world);
        loadSemiComplexCity(world);
    }

    private List<House> createCityBlock(double x, double y, double width, double height, int houses)
    {
        HouseFactory houseFactory = new HouseFactory(idGenerator);
        List<House> result = new LinkedList<House>();

        //TODO: check we can actually fit square houses in the width
        double houseWidth = width / houses;
        double xCursor = x;

        for (int i = 0; i < houses; i++)
        {
            House upper = houseFactory.createHouse(xCursor, y, houseWidth, houseWidth);
            House lower = houseFactory.createHouse(xCursor, y + height - houseWidth, houseWidth, houseWidth);
            xCursor += houseWidth;

            result.add(upper);
            result.add(lower);
        }

        final double offset = 5;
        double fillWidth = height - houseWidth * 2 - offset;
        if (fillWidth <= 0)
            throw new IllegalStateException("Cannot create fill house with 0 or negative area");
        House bigLeft = houseFactory.createHouse(x, y + houseWidth, fillWidth, fillWidth);
        House bigRight = houseFactory.createHouse(x + width - fillWidth, y + houseWidth + offset, fillWidth, fillWidth);
        result.add(bigLeft);
        result.add(bigRight);

        return result;
    }
    
    private void loadSemiComplexCity(World world)
    {
        PersonFactory personFactory = new PersonFactory(idGenerator, assetManager, jobManager, navigationManager,
                gameClock, roadNetworkManager);
        HouseFactory houseFactory = new HouseFactory(idGenerator);
        WorkplaceFactory workplaceFactory = new WorkplaceFactory(idGenerator);

        int blocks = 3;
        double blockWidth = 200;
        double blockHeight = 100;
        double xCursor = 10;
        double yCursor = 10;
        
        List<House> houses = new LinkedList<House>();
        
        for (int x = 0; x < blocks; x++)
        {
            yCursor = 10;
            for (int y = 0; y < blocks; y++)
            {
                houses.addAll(createCityBlock(xCursor, yCursor, blockWidth, blockHeight, 10));
                yCursor += blockHeight * 1.2;
            }
            xCursor += blockWidth * 1.2;
        }
        
        world.addEntities(houses);
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

        List<House> block = createCityBlock(400, 400, 200, 100, 10);
        world.addEntities(block);
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
                for (int i = 0; i < 3; i++)
                {
                    Person person = personFactory.createPerson(x - 1, y - 1);
                    world.addEntity(person);
                    assetManager.createDeed(person, house);
                    Workplace randomWorkplace = workplaces.get(new Random().nextInt(workplaces.size()));
                    jobManager.createContract(person, new JobDefinition(gameClock, randomWorkplace, 7, 30,
                            TimeUnit.HOURS.toSeconds(1)));
                }
            }
        }

        Road road = new Road(new Vector(0, 325), new Vector(750, 20));
        world.addEntity(road);
    }
}
