package org.housered.simul.model.navigation.road;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.road.graph.RoadGraph;
import org.housered.simul.model.navigation.road.graph.RoadNode;
import org.housered.simul.model.world.GameObject;
import org.housered.simul.model.world.Tickable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import straightedge.geom.KPoint;
import straightedge.geom.path.KNode;
import straightedge.geom.path.PathData;
import straightedge.geom.path.PathData.Result;

import com.stackframe.pathfinder.AStar;
import com.stackframe.pathfinder.PathFinder;

public class RoadManager implements Tickable, GameObject
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RoadManager.class);
    private final CarTracker carTracker;
    private final PathFinder<RoadNode> pathfinder = new AStar<RoadNode>();
    private RoadGraph graph = new RoadGraph();

    public RoadManager()
    {
        carTracker = new CarTracker();
    }

    public CarTracker getCarTracker()
    {
        return carTracker;
    }

    @Deprecated
    public PathData findPath(Vector start, Vector end)
    {

        RoadNode startNode = getClosestRoadPoint(start);
        RoadNode endNode = getClosestRoadPoint(end);

        List<RoadNode> path = findPath(startNode, endNode);
        
        if (path != null && path.size() == 1)
            path.add(path.get(0));

        if (path == null)
            return new PathData(Result.ERROR1);

        ArrayList<KPoint> pathAsPoints = new ArrayList<KPoint>();

        for (RoadNode node : path)
            pathAsPoints.add(node.getPosition());
        
        return new PathData(pathAsPoints, new ArrayList<KNode>());
    }

    public synchronized List<RoadNode> findPath(RoadNode start, RoadNode target)
    {
        long startTime = System.currentTimeMillis();
        
        List<RoadNode> path = pathfinder.findPath(graph.getRoadNodes(), start, Arrays.asList(target));
        
        LOGGER.trace("Path calculation took {} ms - between {} and {} => {}", new Object[] {
                System.currentTimeMillis() - startTime, start, target, path});
        
        if (path != null && path.size() == 1)
            path.add(target);
        
        return path;
    }

    public void addRoad(RoadNode start, RoadNode end, double cost)
    {
        graph.connectNodesInADirectedWay(start, end, cost);
    }
    
    public void setRoadNetwork(RoadGraph graph)
    {
        this.graph = graph;
    }

    public RoadNode getClosestRoadPoint(Vector point)
    {
        double minDistance = Double.MAX_VALUE;
        RoadNode minDistanceNode = null;

        for (RoadNode node : graph.getRoadNodes())
        {
            double distance = node.getPosition().distance(point);

            if (minDistanceNode == null || distance < minDistance)
            {
                minDistanceNode = node;
                minDistance = distance;
            }
        }

        return minDistanceNode;
    }

    @Override
    public void tick(float dt)
    {
        carTracker.updateCarPosition();
    }

    public RoadGraph getRoadGraph()
    {
        return graph;
    }

}
