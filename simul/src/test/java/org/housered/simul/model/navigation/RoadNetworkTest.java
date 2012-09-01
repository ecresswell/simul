package org.housered.simul.model.navigation;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.Road.Direction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import straightedge.geom.KPoint;
import straightedge.geom.path.KNode;
import straightedge.geom.path.PathData;
import straightedge.geom.path.PathData.Result;

public class RoadNetworkTest
{
    private RoadNetworkManager network;

    @Before
    public void setUp()
    {
        network = new RoadNetworkManager();
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

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionIfThereAreNoRoads()
    {
        PathData data = new PathData(new ArrayList<KPoint>(), new ArrayList<KNode>());
        network.makePathObeyRoads(data);
    }

    @Test
    public void shouldThrowExceptionOnPathsNotInSuccess()
    {
        PathData[] notSuccessful = {new PathData(Result.ERROR1), new PathData(Result.ERROR2),
                new PathData(Result.ERROR3), new PathData(Result.ERROR4), new PathData(Result.NO_RESULT)};

        for (PathData data : notSuccessful)
        {
            boolean threwException = false;
            try
            {
                network.makePathObeyRoads(data);
            }
            catch (IllegalArgumentException e)
            {
                threwException = true;
            }
            if (!threwException)
            {
                Assert.fail("Path should cause an exception");
            }
        }
    }

    private static ArrayList<KPoint> points(Vector... points)
    {
        ArrayList<KPoint> result = new ArrayList<KPoint>();
        for (Vector v : points)
        {
            result.add(v);
        }
        return result;
    }
}
