package org.housered.simul.model.navigation;

import static org.housered.simul.model.location.Vector.EPSILON;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;

import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.Road.Direction;
import org.junit.Before;
import org.junit.Test;

import straightedge.geom.path.PathBlockingObstacle;
import straightedge.geom.path.PathData;

public class RoadNetworkManagerTest
{
    private RoadNetworkManager network;

    @Before
    public void setUp()
    {
        network = new RoadNetworkManager(new Vector(1000, 1000));
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
        network.addRoad(new Road(new Vector(50, 50), new Vector(10, 50), Direction.NORTH));

        List<Rectangle2D.Double> obstacles = network.inverseRoads();

        assertEquals(4, obstacles.size());
        assertTrue(listFuzzyContains(obstacles, 0, 0, 1000, 49.99));
        assertTrue(listFuzzyContains(obstacles, 0, 100.01, 1000, 899.99));
        assertTrue(listFuzzyContains(obstacles, 0, 49.99, 49.99, 50.02));
        assertTrue(listFuzzyContains(obstacles, 60.01, 49.99, 939.99, 50.02));
    }

    @Test
    public void shouldReturnManyObstaclesForManyRoads()
    {
        network.addRoad(new Road(new Vector(50, 50), new Vector(10, 50), Direction.NORTH));
        network.addRoad(new Road(new Vector(250, 200), new Vector(40, 30), Direction.NORTH));

        List<Rectangle2D.Double> obstacles = network.inverseRoads();
        assertEquals(7, obstacles.size());
        assertTrue(listFuzzyContains(obstacles, 0, 0, 1000, 49.99));
        assertTrue(listFuzzyContains(obstacles, 0, 49.99, 49.99, 50.02));
        assertTrue(listFuzzyContains(obstacles, 60.01, 49.99, 939.99, 50.02));
        assertTrue(listFuzzyContains(obstacles, 0, 230.01, 1000, 769.99));
        assertTrue(listFuzzyContains(obstacles, 0, 199.99, 249.99, 30.02));
        assertTrue(listFuzzyContains(obstacles, 290.01, 199.99, 709.99, 30.02));
        assertTrue(listFuzzyContains(obstacles, 0, 100.01, 1000, 99.98));
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
