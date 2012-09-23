package org.housered.simul.model.navigation.road;

import static org.housered.simul.model.location.Vector.v;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.housered.simul.model.navigation.road.graph.RoadNode;
import org.junit.Before;
import org.junit.Test;

import straightedge.geom.path.PathData;

public class RoadManagerTest
{
    private RoadManager network;

    @Before
    public void setUp()
    {
        network = new RoadManager();
    }

    @Test
    public void shouldNavigateThroughSimpleRoadSystem()
    {
        RoadNode n1 = new RoadNode(v(10, 10));
        RoadNode n2 = new RoadNode(v(15, 10));
        RoadNode n3 = new RoadNode(v(15, 15));
        network.addRoad(n1, n2, 5);
        network.addRoad(n2, n3, 5);

        List<RoadNode> path = network.findPath(n1, n3);

        assertEquals(3, path.size());
        assertEquals(n1, path.get(0));
        assertEquals(n2, path.get(1));
        assertEquals(n3, path.get(2));
    }

    @Test
    public void shouldAcceptVectorStartAndEndForTheMoment()
    {
        RoadNode n1 = new RoadNode(v(10, 10));
        RoadNode n2 = new RoadNode(v(15, 10));
        RoadNode n3 = new RoadNode(v(15, 15));
        network.addRoad(n1, n2, 5);
        network.addRoad(n2, n3, 5);

        PathData path = network.findPath(n1.getPosition(), n3.getPosition());

        assertFalse(path.isError());
        assertEquals(3, path.getPoints().size());
        assertEquals(n1.getPosition(), path.getPoints().get(0));
        assertEquals(n2.getPosition(), path.getPoints().get(1));
        assertEquals(n3.getPosition(), path.getPoints().get(2));
    }

    @Test
    public void shouldChooseShortestPath()
    {
        RoadNode n1 = new RoadNode(v(10, 10));
        RoadNode n2 = new RoadNode(v(15, 10));
        RoadNode n3 = new RoadNode(v(15, 15));
        RoadNode n4 = new RoadNode(v(12, 12));
        network.addRoad(n1, n2, 5);
        network.addRoad(n2, n3, 5);
        network.addRoad(n1, n3, 9);
        network.addRoad(n1, n4, 2);
        network.addRoad(n4, n3, 5);

        List<RoadNode> path = network.findPath(n1, n3);

        assertEquals(3, path.size());
        assertEquals(n1, path.get(0));
        assertEquals(n4, path.get(1));
        assertEquals(n3, path.get(2));
    }

    @Test
    public void shouldReturnClosestRoadNodeToAGivenPoint()
    {
        RoadNode n1 = new RoadNode(v(10, 10));
        RoadNode n2 = new RoadNode(v(15, 10));
        RoadNode n3 = new RoadNode(v(15, 15));
        RoadNode n4 = new RoadNode(v(12, 12));
        network.addRoad(n1, n2, 5);
        network.addRoad(n2, n3, 5);
        network.addRoad(n1, n3, 9);
        network.addRoad(n1, n4, 2);
        network.addRoad(n4, n3, 5);

        assertEquals(n1, network.getClosestRoadPoint(v(10, 10)));
        assertEquals(n1, network.getClosestRoadPoint(v(10.9, 10.9)));
        assertEquals(n1, network.getClosestRoadPoint(v(5, 5)));
        assertEquals(n2, network.getClosestRoadPoint(v(14, 10)));
    }
}
