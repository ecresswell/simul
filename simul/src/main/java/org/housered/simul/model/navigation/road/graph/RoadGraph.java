package org.housered.simul.model.navigation.road.graph;

import static java.lang.String.format;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.housered.simul.model.location.Vector;
import org.housered.simul.view.GraphicsAdapter;
import org.housered.simul.view.Renderable;

public class RoadGraph implements Renderable
{
    private List<RoadNode> roadNodes = new ArrayList<RoadNode>();
    private boolean checkMode = true;

    public void addNode(RoadNode node)
    {
        roadNodes.add(node);
        consistencyCheck();
    }

    public void beginBatchGraphUpdate()
    {
        checkMode = false;
    }

    public void endBatchGraphUpdate()
    {
        checkMode = true;
        consistencyCheck();
    }

    public void connectNodesInADirectedWay(RoadNode... roadNodes)
    {
        RoadNode previous = roadNodes[0];

        for (int i = 1; i < roadNodes.length; i++)
        {
            internalConnectNodesInADirectedWay(previous, roadNodes[i]);
            previous = roadNodes[i];
        }

        consistencyCheck();
    }

    void replaceNodeWithOtherNode(RoadNode existingNode, RoadNode replacementNode)
    {
        if (existingNode == replacementNode)
            return;

        if (!roadNodes.contains(replacementNode))
            roadNodes.add(replacementNode);

        for (RoadEdge nodeEdge : existingNode.getEdges())
            replacementNode.addRoad(nodeEdge);

        for (RoadNode node : roadNodes)
        {
            for (RoadEdge edge : node.getEdges())
            {
                if (edge.getStartNode() == existingNode)
                    edge.setStartNode(replacementNode);
                if (edge.getEndNode() == existingNode)
                    edge.setEndNode(replacementNode);
            }
        }

        removeNode(existingNode);

        consistencyCheck();
    }

    private void internalConnectNodesInADirectedWay(RoadNode start, RoadNode end)
    {
        double cost = start.getPosition().distance(end.getPosition());
        connectNodesInADirectedWay(start, end, cost);
    }

    public void connectNodesInADirectedWay(RoadNode start, RoadNode end, double cost)
    {
        if (start == end)
            throw new IllegalArgumentException("Can't connect node to itself");

        if (!roadNodes.contains(start))
        {
            addNode(start);
        }
        if (!roadNodes.contains(end))
        {
            addNode(end);
        }

        start.addRoad(new RoadEdge(start, end, cost));
        consistencyCheck();
    }

    public Collection<RoadNode> getRoadNodes()
    {
        return roadNodes;
    }

    @Override
    public void render(GraphicsAdapter r)
    {
        r.setColour(Color.gray);
        for (RoadNode node : roadNodes)
        {
            r.fillCircle(node.getPosition(), 1);

            for (RoadEdge edge : node.getEdges())
            {
                r.drawLine(edge.getStartNode().getPosition(), edge.getEndNode().getPosition());
            }
        }
    }

    @Override
    public byte getZOrder()
    {
        return ROAD_Z_ORDER;
    }

    public void removeNode(RoadNode nodeToRemove)
    {
        for (RoadNode node : roadNodes)
        {
            ListIterator<RoadEdge> listIterator = node.getEdges().listIterator();

            while (listIterator.hasNext())
            {
                RoadEdge edge = listIterator.next();

                if (edge.getEndNode() == nodeToRemove)
                {
                    listIterator.remove();
                }
            }
        }

        roadNodes.remove(nodeToRemove);
        consistencyCheck();
    }

    public void removeRoad(RoadNode startNode, RoadNode endNode)
    {
        ListIterator<RoadEdge> listIterator = startNode.getEdges().listIterator();

        while (listIterator.hasNext())
        {
            RoadEdge edge = listIterator.next();
            if (edge.getEndNode() == endNode)
            {
                listIterator.remove();
            }
        }
        consistencyCheck();
    }

    private void consistencyCheck()
    {
        if (!checkMode)
            return;

        Set<Vector> nodePositions = new HashSet<Vector>();
        Set<RoadEdge> edges = new HashSet<RoadEdge>();

        for (RoadNode node : roadNodes)
        {
            if (!nodePositions.add(node.getPosition()))
                throw new IllegalStateException("Overlapping road nodes");
            for (RoadEdge edge : node.getEdges())
            {
                if (!edges.add(edge))
                    throw new IllegalStateException(format("Duplicate road defined - %s", edge));
                if (node != edge.getStartNode())
                    throw new IllegalStateException("Road starting node is out of sync");
            }
        }
    }
    
    void removeDuplicateRoads()
    {
        Set<RoadEdge> edges = new HashSet<RoadEdge>();
        
        for (RoadNode node : roadNodes)
        {
            ListIterator<RoadEdge> listIterator = node.getEdges().listIterator();
            while (listIterator.hasNext())
            {
                RoadEdge edge = listIterator.next();
                if (!edges.add(edge))
                    listIterator.remove();
            }
        }
    }

    public void insertNodeIntoRoad(RoadEdge existingRoad, RoadNode nodeToInsert)
    {
        RoadNode end = existingRoad.getEndNode();
        existingRoad.setEndNode(nodeToInsert);
        
        connectNodesInADirectedWay(nodeToInsert, end);
    }
}
