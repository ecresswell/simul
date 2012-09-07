package org.housered.simul.model.navigation.road;

import static org.housered.simul.model.location.Vector.EPSILON;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;

import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.RectangleInverseUtility;
import org.housered.simul.model.navigation.road.Road;
import org.housered.simul.model.navigation.road.RoadNetworkManager;
import org.housered.simul.model.navigation.road.Road.Direction;
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

        List<Rectangle2D.Double> obstacles = RectangleInverseUtility.inverseRectangles(WORLD_WIDTH, WORLD_HEIGHT, rects);

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

        List<Rectangle2D.Double> obstacles = RectangleInverseUtility.inverseRectangles(WORLD_WIDTH, WORLD_HEIGHT, rects);
                
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
        network.addRoad(new Road(new Vector(50, 50), new Vector(10, 50), Direction.NORTH));
        Vector closestRoadPoint = network.getClosestRoadPoint(new Vector(0, 0));

        assertEquals(new Vector(50, 50), closestRoadPoint);
    }

    @Test
    public void shouldReturnClosestPointOnARoadToGivenPointInAMoreComplicatedSetting()
    {
        network.addRoad(new Road(new Vector(50, 50), new Vector(10, 50), Direction.NORTH));
        network.addRoad(new Road(new Vector(-20, -20), new Vector(10, 10), Direction.NORTH));
        Vector closestRoadPoint = network.getClosestRoadPoint(new Vector(0, 0));

        assertEquals(new Vector(-10, -10), closestRoadPoint);
    }

    @Test
    public void shouldReturnClosestPointOnARoadToGivenPointWhenClosestIsNotACorner()
    {
        network.addRoad(new Road(new Vector(50, 50), new Vector(10, 50), Direction.NORTH));
        network.addRoad(new Road(new Vector(-20, -20), new Vector(50, 10), Direction.NORTH));
        Vector closestRoadPoint = network.getClosestRoadPoint(new Vector(0, 0));

        assertEquals(new Vector(0, -10), closestRoadPoint);
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
