package org.housered.simul.model.navigation.road.graph;

import static org.housered.simul.model.location.Vector.v;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

public class RoadGraphTest
{
    @Test
    public void shouldCreateASimpleDirectedLine()
    {
        RoadGraph graph = new RoadGraph();

        RoadNode n1 = new RoadNode(v(10, 10));
        RoadNode n2 = new RoadNode(v(15, 10));
        double cost = 5;

        graph.connectNodesInADirectedWay(n1, n2, cost);

        assertEquals(n2, n1.neighbors().iterator().next());
        assertEquals(false, n2.neighbors().iterator().hasNext());
        assertEquals(cost, n1.traverseCost(n2), 0.0001d);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenTryingToConnectNodeToItself()
    {
        RoadGraph graph = new RoadGraph();

        RoadNode node = new RoadNode(v(10, 10));

        graph.connectNodesInADirectedWay(node, node, 5);
    }

    @Test
    public void shouldCreateACyclicalDirectedGraph()
    {
        RoadGraph graph = new RoadGraph();

        RoadNode n1 = new RoadNode(v(10, 10));
        RoadNode n2 = new RoadNode(v(15, 10));
        RoadNode n3 = new RoadNode(v(15, 15));

        graph.connectNodesInADirectedWay(n1, n2, 1);
        graph.connectNodesInADirectedWay(n2, n3, 2);
        graph.connectNodesInADirectedWay(n3, n1, 3);

        List<RoadNode> neighbours = Lists.newArrayList(n1.neighbors());
        assertEquals(1, neighbours.size());
        assertEquals(1, n1.traverseCost(n2), 0.001d);
        assertEquals(n2, neighbours.get(0));

        neighbours = Lists.newArrayList(n2.neighbors());
        assertEquals(1, neighbours.size());
        assertEquals(2, n2.traverseCost(n3), 0.001d);
        assertEquals(n3, neighbours.get(0));

        neighbours = Lists.newArrayList(n3.neighbors());
        assertEquals(1, neighbours.size());
        assertEquals(3, n3.traverseCost(n1), 0.001d);
        assertEquals(n1, neighbours.get(0));
    }
}
