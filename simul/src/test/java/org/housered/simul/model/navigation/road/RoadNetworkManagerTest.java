package org.housered.simul.model.navigation.road;

import static org.housered.simul.model.location.Vector.EPSILON;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;

import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.RectangleInverseUtility;
import org.junit.Before;
import org.junit.Test;

import straightedge.geom.path.PathBlockingObstacle;
import straightedge.geom.path.PathData;

public class RoadNetworkManagerTest
{
    private static final double WORLD_WIDTH = 1000;
    private static final double WORLD_HEIGHT = 1000;
    private RoadNetworkManager network;

    @Before
    public void setUp()
    {
        network = new RoadNetworkManager(new Vector(WORLD_WIDTH, WORLD_HEIGHT));
    }

    @Test
    public void shouldNavigateThroughSimpleRoadSystem()
    {
        network.addRoad(new Road(new Vector(0, 0), new Vector(20, 100)));
        network.addRoad(new Road(new Vector(0, 100), new Vector(100, 20)));
        network.addRoad(new Road(new Vector(100, 100), new Vector(100, 20)));
        network.refreshNavigationMesh();

        assertEquals(PathData.Result.SUCCESS, network.findPath(new Vector(0, 0), new Vector(10, 100)).getResult());
        assertEquals(PathData.Result.SUCCESS, network.findPath(new Vector(0, 0), new Vector(100, 120)).getResult());
        assertEquals(PathData.Result.SUCCESS, network.findPath(new Vector(200, 120), new Vector(0, 0)).getResult());
    }

    @Test
    public void shouldReturnEmptyObstacleListWhenThereAreNoRoads()
    {
        assertEquals(new LinkedList<PathBlockingObstacle>(), network.createObstacles());
    }

    @Test
    public void shouldReturn4ObstaclesForASingleRoad()
    {
        List<Rectangle2D.Double> rects = new LinkedList<Rectangle2D.Double>();
        rects.add(new Rectangle2D.Double(50, 50, 10, 50));

        List<Rectangle2D.Double> obstacles = RectangleInverseUtility
                .inverseRectangles(WORLD_WIDTH, WORLD_HEIGHT, rects);

        assertEquals(4, obstacles.size());
        assertTrue(listFuzzyContains(obstacles, 0, 0, 1000, 50));
        assertTrue(listFuzzyContains(obstacles, 0, 100, 1000, 900));
        assertTrue(listFuzzyContains(obstacles, 0, 50, 50, 50));
        assertTrue(listFuzzyContains(obstacles, 60, 50, 940, 50));
    }

    @Test
    public void shouldReturnManyObstaclesForManyRoads()
    {
        List<Rectangle2D.Double> rects = new LinkedList<Rectangle2D.Double>();
        rects.add(new Rectangle2D.Double(50, 50, 10, 50));
        rects.add(new Rectangle2D.Double(250, 200, 40, 30));

        List<Rectangle2D.Double> obstacles = RectangleInverseUtility
                .inverseRectangles(WORLD_WIDTH, WORLD_HEIGHT, rects);

        assertEquals(7, obstacles.size());
        assertTrue(listFuzzyContains(obstacles, 0, 0, 1000, 50));
        assertTrue(listFuzzyContains(obstacles, 0, 50, 50, 50));
        assertTrue(listFuzzyContains(obstacles, 60, 50, 940, 50));
        assertTrue(listFuzzyContains(obstacles, 0, 230, 1000, 770));
        assertTrue(listFuzzyContains(obstacles, 0, 200, 250, 30));
        assertTrue(listFuzzyContains(obstacles, 290, 200, 710, 30));
        assertTrue(listFuzzyContains(obstacles, 0, 100, 1000, 100));
    }

    @Test
    public void shouldReturnClosestPointOnARoadToGivenPoint()
    {
        network.addRoad(new Road(new Vector(50, 50), new Vector(10, 50)));
        Vector closestRoadPoint = network.getClosestRoadPoint(new Vector(0, 0));

        assertEquals(new Vector(50, 50), closestRoadPoint);
    }

    @Test
    public void shouldReturnClosestPointOnARoadToGivenPointInAMoreComplicatedSetting()
    {
        network.addRoad(new Road(new Vector(50, 50), new Vector(10, 50)));
        network.addRoad(new Road(new Vector(-20, -30), new Vector(10, 20)));
        Vector closestRoadPoint = network.getClosestRoadPoint(new Vector(0, 0));

        assertEquals(new Vector(-10, -10), closestRoadPoint);
    }

    @Test
    public void shouldReturnClosestPointOnARoadToGivenPointWhenClosestIsNotACorner()
    {
        network.addRoad(new Road(new Vector(50, 50), new Vector(10, 50)));
        network.addRoad(new Road(new Vector(-20, -20), new Vector(50, 10)));
        Vector closestRoadPoint = network.getClosestRoadPoint(new Vector(0, 0));

        assertEquals(new Vector(0, -10), closestRoadPoint);
    }

    @Test
    public void shouldAugmentSimpleRoadPathDataToAbideByLanesGoingEast()
    {
        network.addRoad(new Road(new Vector(10, 10), new Vector(50, 10)));
        network.refreshNavigationMesh();

        PathData path = network.findPath(new Vector(12, 10), new Vector(55, 10));

        assertFalse(path.isError());
        assertEquals(Vector.v(12, 10), path.getPoints().get(0));
        assertEquals(Vector.v(12, 12.5), path.getPoints().get(1));
        assertEquals(Vector.v(55, 12.5), path.getPoints().get(2));
        assertEquals(Vector.v(55, 10), path.getPoints().get(3));
        assertEquals(4, path.getPoints().size());
    }

    @Test
    public void shouldAugmentSimpleRoadPathDataToAbideByLanesGoingWest()
    {
        network.addRoad(new Road(new Vector(10, 10), new Vector(50, 10)));
        network.refreshNavigationMesh();

        PathData path = network.findPath(new Vector(57, 12), new Vector(15, 14));

        assertFalse(path.isError());
        assertEquals(Vector.v(57, 12), path.getPoints().get(0));
        assertEquals(Vector.v(57, 17.5), path.getPoints().get(1));
        assertEquals(Vector.v(15, 17.5), path.getPoints().get(2));
        assertEquals(Vector.v(15, 14), path.getPoints().get(3));
        assertEquals(4, path.getPoints().size());
    }

    @Test
    public void shouldAugmentSimpleRoadPathDataToAbideByLanesGoingSouth()
    {
        network.addRoad(new Road(new Vector(5, 10), new Vector(20, 50)));
        network.refreshNavigationMesh();

        PathData path = network.findPath(new Vector(6, 11), new Vector(8, 32));

        assertFalse(path.isError());
        assertEquals(Vector.v(6, 11), path.getPoints().get(0));
        assertEquals(Vector.v(20, 11), path.getPoints().get(1));
        assertEquals(Vector.v(20, 32), path.getPoints().get(2));
        assertEquals(Vector.v(8, 32), path.getPoints().get(3));
        assertEquals(4, path.getPoints().size());
    }

    @Test
    public void shouldAugmentSimpleRoadPathDataToAbideByLanesGoingNorth()
    {
        network.addRoad(new Road(new Vector(5, 10), new Vector(20, 50)));
        network.refreshNavigationMesh();

        PathData path = network.findPath(new Vector(15, 49), new Vector(8, 32));

        assertFalse(path.isError());
        assertEquals(Vector.v(15, 49), path.getPoints().get(0));
        assertEquals(Vector.v(10, 49), path.getPoints().get(1));
        assertEquals(Vector.v(10, 32), path.getPoints().get(2));
        assertEquals(Vector.v(8, 32), path.getPoints().get(3));
        assertEquals(4, path.getPoints().size());
    }

    @Test
    public void shouldAugmentRoadPathDataToAbideByLanes()
    {
        network.addRoad(new Road(new Vector(5, 10), new Vector(20, 50)));
        network.addRoad(new Road(new Vector(5, 60), new Vector(40, 10)));
        network.refreshNavigationMesh();

        PathData path = network.findPath(new Vector(15, 49), new Vector(37, 65));

        //[Vector [x=15.0, y=49.0], Vector [x=20.0, y=49.0], Vector [x=20.0, y=59.99], 
        //KPoint [x=25.000000000223515, y=59.99], Vector [x=20.0, y=59.99], 
        //Vector [x=20.0, y=59.999999999776485], KPoint [x=25.009999999999998, y=59.999999999776485], 
        //Vector [x=20.0, y=59.999999999776485], Vector [x=20.0, y=65.0], Vector [x=37.0, y=65.0]]
        System.out.println(path.getPoints().toString());

        //(0)
        //[Vector [x=15.0, y=49.0], Vector [x=20.0, y=49.0], 
        //Vector [x=20.0, y=59.99], KPoint [x=25.000000000223515, y=59.99], 
        //KPoint [x=25.009999999999998, y=59.999999999776485], Vector [x=37.0, y=65.0]]

        //(0, 3)
        //[Vector [x=15.0, y=49.0], Vector [x=20.0, y=49.0], Vector [x=20.0, y=59.99], 
        //KPoint [x=25.000000000223515, y=59.99], Vector [x=20.0, y=59.99], 
        //Vector [x=20.0, y=59.999999999776485], KPoint [x=25.009999999999998, y=59.999999999776485], 
        //Vector [x=37.0, y=65.0]]

        // (0, 4)
        //[Vector [x=15.0, y=49.0], Vector [x=20.0, y=49.0], Vector [x=20.0, y=59.99], 
        //KPoint [x=25.000000000223515, y=59.99], KPoint [x=25.009999999999998, y=59.999999999776485], 
        //Vector [x=20.0, y=59.999999999776485], Vector [x=20.0, y=65.0], Vector [x=37.0, y=65.0]]
        
        // (0, 2)
        //[Vector [x=15.0, y=49.0], Vector [x=20.0, y=49.0], Vector [x=20.0, y=59.99], 
        //Vector [x=10.0, y=59.99], Vector [x=10.0, y=59.99], KPoint [x=25.000000000223515, y=59.99], 
        //KPoint [x=25.009999999999998, y=59.999999999776485], Vector [x=37.0, y=65.0]]

        assertFalse(path.isError());
        assertEquals(Vector.v(15, 49), path.getPoints().get(0));
        assertEquals(Vector.v(20, 49), path.getPoints().get(1));
        assertEquals(Vector.v(20, 59.99), path.getPoints().get(2));
        assertEquals(Vector.v(20, 59.99), path.getPoints().get(3));
        assertEquals(Vector.v(20, 59.999999999776485), path.getPoints().get(4));
        assertEquals(Vector.v(25.009999999999998, 62.5), path.getPoints().get(5));
        assertEquals(Vector.v(37, 62.5), path.getPoints().get(6));
        assertEquals(Vector.v(37, 65), path.getPoints().get(7));
        assertEquals(8, path.getPoints().size());
    }

    private boolean listFuzzyContains(List<Rectangle2D.Double> rs, double x, double y, double width, double height)
    {
        return listFuzzyContains(rs, new Rectangle2D.Double(x, y, width, height));
    }

    private boolean listFuzzyContains(List<Rectangle2D.Double> rs, Rectangle2D.Double r)
    {
        for (Rectangle2D.Double possible : rs)
        {
            if (Vector.nearlyEqual(possible.x, r.x, EPSILON) && Vector.nearlyEqual(possible.y, r.y, EPSILON)
                    && Vector.nearlyEqual(possible.width, r.width, EPSILON)
                    && Vector.nearlyEqual(possible.height, r.height, EPSILON))
                return true;
        }
        return false;
    }
}
