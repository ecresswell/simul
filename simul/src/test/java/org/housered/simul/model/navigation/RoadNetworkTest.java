package org.housered.simul.model.navigation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;

import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.Road.Direction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import straightedge.geom.path.PathBlockingObstacle;

public class RoadNetworkTest
{
    private RoadNetworkManager network;

    @Before
    public void setUp()
    {
        network = new RoadNetworkManager(new Vector(1000, 1000));
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
        assertTrue(obstacles.contains(new Rectangle2D.Double(0, 0, 1000, 50)));
        assertTrue(obstacles.contains(new Rectangle2D.Double(0, 100, 1000, 900)));
        assertTrue(obstacles.contains(new Rectangle2D.Double(0, 50, 50, 50)));
        assertTrue(obstacles.contains(new Rectangle2D.Double(60, 50, 940, 50)));
    }

    @Test
    public void shouldReturnManyObstaclesForManyRoads()
    {
        network.addRoad(new Road(new Vector(50, 50), new Vector(10, 50), Direction.NORTH));
        network.addRoad(new Road(new Vector(250, 200), new Vector(40, 30), Direction.NORTH));

        List<Rectangle2D.Double> obstacles = network.inverseRoads();
        assertEquals(7, obstacles.size());
        assertTrue(obstacles.contains(new Rectangle2D.Double(0, 0, 1000, 50)));
        assertTrue(obstacles.contains(new Rectangle2D.Double(0, 50, 50, 50)));
        assertTrue(obstacles.contains(new Rectangle2D.Double(60, 50, 940, 50)));
        assertTrue(obstacles.contains(new Rectangle2D.Double(0, 230, 1000, 770)));
        assertTrue(obstacles.contains(new Rectangle2D.Double(0, 200, 250, 30)));
        assertTrue(obstacles.contains(new Rectangle2D.Double(290, 200, 710, 30)));
        assertTrue(obstacles.contains(new Rectangle2D.Double(0, 200, 250, 30)));
        System.out.println(obstacles);
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
}
