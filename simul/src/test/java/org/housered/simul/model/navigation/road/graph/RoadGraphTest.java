package org.housered.simul.model.navigation.road.graph;

import static org.housered.simul.model.location.Vector.v;
import static org.housered.simul.model.navigation.road.graph.RoadNetworkBuilderTest.assertContainsRoad;
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
    public void shouldRemoveNodeAndAllEdgesThatLinkToTheNode()
    {
        RoadGraph graph = new RoadGraph();
        RoadNode ex = new RoadNode(0, 0);
        RoadNode a = new RoadNode(1, 1);
        RoadNode b = new RoadNode(2, 0);
        graph.connectNodesInADirectedWay(a, ex, b);
        
        graph.removeNode(b);
        
        assertEquals(2, graph.getRoadNodes().size());
        assertEquals(0, ex.getEdges().size());
    }

    @Test
    public void shouldReplaceANodeAndUpdateAllItsEdgesToANewNode()
    {
        RoadNode rep = new RoadNode(10, 10);

        RoadGraph graph = new RoadGraph();
        RoadNode ex = new RoadNode(0, 0);
        RoadNode a = new RoadNode(1, 1);
        RoadNode b = new RoadNode(2, 0);
        graph.connectNodesInADirectedWay(a, ex, b);

        graph.replaceNodeWithOtherNode(ex, rep);

        assertContainsRoad(graph, 1, 1, 10, 10);
        assertContainsRoad(graph, 10, 10, 2, 0);
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

    @Test
    public void shouldUseCartesianDistanceWhenNoCostProvided()
    {
        RoadNode r1 = new RoadNode(v(1, 1));
        RoadNode r2 = new RoadNode(v(10, 15));

        RoadGraph graph = new RoadGraph();
        graph.connectNodesInADirectedWay(r1, r2);

        assertEquals(1, r1.getEdges().size());
        assertEquals(16.64331, r1.getEdges().get(0).getCost(), 0.001d);
    }
    

    @Test
    public void shouldAllowConnectionOfManyRoadNodesAtOnce()
    {
        RoadNode r1 = new RoadNode(0, 0);
        RoadNode r2 = new RoadNode(5, 2);
        RoadNode r3 = new RoadNode(6, 2);
        RoadNode r4 = new RoadNode(-6, 4);

        RoadGraph graph = new RoadGraph();
        graph.connectNodesInADirectedWay(r1, r2, r3, r4, r1);

        assertContainsRoad(graph, 0, 0, 5, 2);
        assertContainsRoad(graph, 5, 2, 6, 2);
        assertContainsRoad(graph, 6, 2, -6, 4);
        assertContainsRoad(graph, -6, 4, 0, 0);
    }
    
    @Test
    public void shouldAllowRemovalOfRoadsUsingRoadNodes()
    {
        RoadGraph graph = new RoadGraph();
        RoadNode ex = new RoadNode(0, 0);
        RoadNode a = new RoadNode(1, 1);
        RoadNode b = new RoadNode(2, 0);
        graph.connectNodesInADirectedWay(a, ex, b);
        
        graph.removeRoad(a, ex);
        
        assertEquals(3, graph.getRoadNodes().size());
        assertEquals(1, ex.getEdges().size());
        assertEquals(0, a.getEdges().size());
    }
    
    @Test
    public void shouldInsertRoadNodesIntoExistingRoad()
    {
        RoadGraph graph = new RoadGraph();
        RoadNode a = new RoadNode(0, 0);
        RoadNode b = new RoadNode(10, 0);
        
        graph.connectNodesInADirectedWay(a, b);
        
        RoadNode one = new RoadNode(4, 0);
        RoadNode two = new RoadNode(6, 1);

        graph.insertNodeIntoRoad(a.getEdges().get(0), one);
        assertContainsRoad(graph, 0, 0, 4, 0);
        assertContainsRoad(graph, 4, 0, 10, 0);
        
        graph.insertNodeIntoRoad(one.getEdges().get(0), two);
        assertContainsRoad(graph, 0, 0, 4, 0);
        assertContainsRoad(graph, 4, 0, 6, 1);
        assertContainsRoad(graph, 6, 1, 10, 0);
    }
}
