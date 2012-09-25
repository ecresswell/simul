package org.housered.simul.model.world;

import static org.housered.simul.model.location.Vector.v;
import static org.housered.simul.model.navigation.RectangleInverseUtility.inverseRectangles;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
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
import org.housered.simul.model.navigation.road.Road;
import org.housered.simul.model.navigation.road.RoadManager;
import org.housered.simul.model.navigation.road.RoadNetworkBuilder;
import org.housered.simul.model.navigation.road.graph.RoadGraph;
import org.housered.simul.model.navigation.road.graph.RoadNode;
import org.housered.simul.model.navigation.tube.Tube;
import org.housered.simul.model.navigation.tube.TubeLine;
import org.housered.simul.model.navigation.tube.TubeLineBuilder;
import org.housered.simul.model.navigation.tube.TubeManager;
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
    private final RoadManager roadNetworkManager;
    private final JobManager jobManager;
    private final TubeManager tubeManager;

    public CityPlanner(IdGenerator idGenerator, GameClock gameClock, AssetManager assetManager, JobManager jobManager,
            NavigationManager navigationManager, RoadManager roadNetworkManager, TubeManager tubeManager)
    {
        this.idGenerator = idGenerator;
        this.gameClock = gameClock;
        this.assetManager = assetManager;
        this.jobManager = jobManager;
        this.navigationManager = navigationManager;
        this.roadNetworkManager = roadNetworkManager;
        this.tubeManager = tubeManager;
    }

    public void loadLevel(World world)
    {
        //        loadSimpleMap(world);
        //loadComplicatedMap(world);
        loadSemiComplexCity(world);
        //        loadSpecialMap(world);
    }

    private void loadSpecialMap(World world)
    {
        PersonFactory personFactory = createPersonFactory();

        Random r = new Random();
        for (int i = 0; i < 5000; i++)
        {
            Person person = personFactory.createPerson(200 + r.nextInt(200), 200 + r.nextInt(200));
            world.addEntity(person);
        }
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
        //result.add(bigLeft);
        //result.add(bigRight);

        return result;
    }

    private void loadSemiComplexCity(World world)
    {
        WorkplaceFactory workplaceFactory = new WorkplaceFactory(idGenerator);

        int numberOfPeople = 0;
        double pavementOffset = 5;
        int blocks = 3;
        double blockWidth = 200;
        double blockHeight = 100;

        double xCursor = 10;
        double yCursor = 10;

        List<Rectangle2D.Double> blockBounds = new LinkedList<Rectangle2D.Double>();

        //houses
        List<House> houses = new LinkedList<House>();
        for (int x = 0; x < blocks; x++)
        {
            yCursor = 10;
            for (int y = 0; y < blocks; y++)
            {
                blockBounds.add(new Rectangle2D.Double(xCursor - pavementOffset, yCursor - pavementOffset, blockWidth
                        + pavementOffset * 2, blockHeight + pavementOffset * 2));
                houses.addAll(createCityBlock(xCursor, yCursor, blockWidth, blockHeight, 10));
                yCursor += blockHeight * 1.2;
            }
            xCursor += blockWidth * 1.2;
        }

        //work buildings
        List<Workplace> workplaces = new LinkedList<Workplace>();
        xCursor = 10;
        blocks = 2;
        double yCursorOriginal = yCursor;

        for (int x = 0; x < blocks; x++)
        {
            for (int y = 0; y < blocks; y++)
            {
                blockBounds.add(new Rectangle2D.Double(xCursor - pavementOffset, yCursor - pavementOffset, blockWidth
                        + pavementOffset * 2, blockHeight + pavementOffset * 2));
                Workplace workplace = workplaceFactory.createWorkplace(xCursor, yCursor, blockWidth, blockHeight);
                workplaces.add(workplace);
                yCursor += blockHeight * 1.2;
            }
            yCursor = yCursorOriginal;
            xCursor += blockWidth * 2.2;
        }

        blockBounds.add(new Double(300, 400, 40, 40));
        
        RoadNetworkBuilder roadBuilder = new RoadNetworkBuilder(3, 3);
        RoadGraph graph = roadBuilder.buildNetwork(blockBounds.toArray(new Double[0]));
        roadNetworkManager.setRoadNetwork(graph);
        world.addEntities(graph);

        //tubes
        TubeLineBuilder builder = new TubeLineBuilder(gameClock);
        TubeLine line = builder.addTubeStation(100, 60, 10, 10).addTubeStation(600, 60, 10, 10)
                .addTubeStation(600, 280, 10, 10).addTubeStation(310, 410, 20, 20).addTubeStation(100, 280, 10, 10)
                .buildLine();
        line.addTube(line.getStations().get(0));
        line.addTube(line.getStations().get(2));
        world.addEntities(line);
        world.addEntities(line.getStations());
        world.addEntities(line.getTubes());
        tubeManager.addTubeLine(line);

        List<Person> people = createAndAssignPeople(numberOfPeople, world, houses, workplaces);

        world.addEntities(workplaces);
        world.addEntities(houses);
        world.addEntities(people);
    }

    private List<Person> createAndAssignPeople(int numPeople, World world, List<House> houses, List<Workplace> works)
    {
        PersonFactory personFactory = createPersonFactory();

        List<Person> people = new LinkedList<Person>();
        Random r = new Random();

        for (int i = 0; i < numPeople; i++)
        {
            House randomHouse = houses.get(r.nextInt(houses.size()));
            Person person = personFactory.createPerson(randomHouse.getEntryPoint().x, randomHouse.getEntryPoint().y);
            assetManager.createDeed(person, randomHouse);

            Workplace randomWork = works.get(r.nextInt(works.size()));
            JobDefinition job = new JobDefinition(gameClock, randomWork, 7, 30, TimeUnit.HOURS.toSeconds(1));
            jobManager.createContract(person, job.createJob());

            people.add(person);
        }

        return people;
    }

    private void loadSimpleMap(World world)
    {
        PersonFactory personFactory = createPersonFactory();
        HouseFactory houseFactory = new HouseFactory(idGenerator);
        WorkplaceFactory workplaceFactory = new WorkplaceFactory(idGenerator);

        House house = houseFactory.createHouse(200, 200);
        House house2 = houseFactory.createHouse(200, 280);
        Workplace workplace = workplaceFactory.createWorkplace(400, 200);
        Workplace workplace2 = workplaceFactory.createWorkplace(400, 280);

        Random r = new Random();
        for (int i = 0; i < 100; i++)
        {
            Person person = personFactory.createPerson(r.nextInt(200), r.nextInt(200));
            if (r.nextDouble() < 0.5f)
            {
                jobManager.createContract(person,
                        new JobDefinition(gameClock, workplace, 7, 30, TimeUnit.MINUTES.toSeconds(10)));
            }
            else
            {
                jobManager.createContract(person,
                        new JobDefinition(gameClock, workplace2, 7, 30, TimeUnit.MINUTES.toSeconds(20)));
            }
            if (r.nextDouble() < 0.5f)
            {
                assetManager.createDeed(person, house);
            }
            else
            {
                assetManager.createDeed(person, house2);
            }

            world.addEntity(person);
        }

        RoadNode topLeftL = new RoadNode(v(180, 240));
        RoadNode topRightL = new RoadNode(v(500, 240));
        RoadNode bottomLeftL = new RoadNode(v(180, 260));
        RoadNode bottomRightL = new RoadNode(v(500, 260));
        roadNetworkManager.addRoad(topLeftL, topRightL, 10);
        roadNetworkManager.addRoad(topRightL, bottomRightL, 5);
        roadNetworkManager.addRoad(bottomRightL, bottomLeftL, 10);
        roadNetworkManager.addRoad(bottomLeftL, topLeftL, 5);

        RoadNode topLeftR = new RoadNode(v(185, 245));
        RoadNode topRightR = new RoadNode(v(495, 245));
        RoadNode bottomLeftR = new RoadNode(v(185, 255));
        RoadNode bottomRightR = new RoadNode(v(495, 255));
        roadNetworkManager.addRoad(topRightR, topLeftR, 10);
        roadNetworkManager.addRoad(bottomRightR, topRightR, 5);
        roadNetworkManager.addRoad(bottomLeftR, bottomRightR, 10);
        roadNetworkManager.addRoad(topLeftR, bottomLeftR, 5);

        roadNetworkManager.addRoad(topLeftR, topLeftL, 1);
        roadNetworkManager.addRoad(topRightL, topRightR, 1);
        roadNetworkManager.addRoad(bottomLeftR, bottomLeftL, 1);
        roadNetworkManager.addRoad(bottomRightL, bottomRightR, 1);

        TubeLineBuilder builder = new TubeLineBuilder(gameClock);
        TubeLine line = builder.addTubeStation(180, 225, 10, 10).addTubeStation(420, 225, 10, 10)
                .addTubeStation(420, 265, 10, 10).addTubeStation(180, 265, 10, 10).buildLine();
        line.addTube(line.getStations().get(0));
        line.addTube(line.getStations().get(1));
        world.addEntities(line);
        world.addEntities(line.getStations());
        world.addEntities(line.getTubes());
        tubeManager.addTubeLine(line);

        world.addEntities(house, workplace, workplace2, house2);
    }

    private void loadComplicatedMap(World world)
    {
        PersonFactory personFactory = createPersonFactory();
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

    private PersonFactory createPersonFactory()
    {
        return new PersonFactory(idGenerator, assetManager, jobManager, navigationManager, gameClock,
                roadNetworkManager, tubeManager);
    }
}
