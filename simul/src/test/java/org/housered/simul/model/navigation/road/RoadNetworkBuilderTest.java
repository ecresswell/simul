package org.housered.simul.model.navigation.road;

import static org.housered.simul.model.location.Vector.v;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.awt.geom.Rectangle2D;

import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.road.graph.RoadEdge;
import org.housered.simul.model.navigation.road.graph.RoadGraph;
import org.housered.simul.model.navigation.road.graph.RoadNode;
import org.junit.Test;

public class RoadNetworkBuilderTest
{

    @Test
    public void shouldAddRoadsGoingBothWaysAroundABlock()
    {
        Rectangle2D.Double block = new Rectangle2D.Double(0, 0, 10, 10);

        RoadNetworkBuilder b = new RoadNetworkBuilder(1, 3);
        RoadGraph g = b.buildNetwork(block);

        //inner road
        assertContainsRoad(g, 11, -1, -1, -1);
        assertContainsRoad(g, -1, -1, -1, 11);
        assertContainsRoad(g, -1, 11, 11, 11);
        assertContainsRoad(g, 11, 11, 11, -1);

        //outside road, with junction points
        assertContainsRoad(g, -1, -4, 11, -4);
        assertContainsRoad(g, 14, -1, 14, 11);
        assertContainsRoad(g, 11, 14, -1, 14);
        assertContainsRoad(g, -4, 11, -4, -1);

        assertContainsRoad(g, -4, -4, -1, -4);
        assertContainsRoad(g, 11, -4, 14, -4);
        assertContainsRoad(g, 14, -4, 14, -1);
        assertContainsRoad(g, 14, 11, 14, 14);
        assertContainsRoad(g, 14, 14, 11, 14);
        assertContainsRoad(g, -1, 14, -4, 14);
        assertContainsRoad(g, -4, 14, -4, 11);
        assertContainsRoad(g, -4, -1, -4, -4);

        assertEquals(16, g.getRoadNodes().size());
    }

    public static void assertContainsRoad(RoadGraph graph, double sx, double sy, double ex, double ey)
    {
        assertContainsRoad(graph, v(sx, sy), v(ex, ey));
    }

    public static void assertContainsRoad(RoadGraph graph, Vector start, Vector end)
    {
        for (RoadNode n : graph.getRoadNodes())
            if (n.getPosition().equals(start))
                for (RoadEdge e : n.getEdges())
                    if (e.getOtherNode(n).getPosition().equals(end))
                        return;

        fail("Could not find road");
    }
}
