package org.housered.simul.model.navigation.road;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.RectangleInverseUtility;
import org.housered.simul.model.world.GameObject;
import org.housered.simul.model.world.Tickable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import straightedge.geom.KPoint;
import straightedge.geom.path.NodeConnector;
import straightedge.geom.path.PathBlockingObstacle;
import straightedge.geom.path.PathData;
import straightedge.geom.path.PathFinder;

public class RoadNetworkManager implements Tickable, GameObject
{
    static final double ROAD_EXPANSION_MARGIN = 0.01d;
    private static final Logger LOGGER = LoggerFactory.getLogger(RoadNetworkManager.class);
    private static final float MAX_CONNECTION_DISTANCE = 1000f;
    private final CarTracker carTracker;
    private List<Road> roads = new LinkedList<Road>();
    private final Vector worldBounds;
    private PathFinder pathfinder = new PathFinder();
    private RoadLaneAugmentor laneAugmentor = new RoadLaneAugmentor(roads);
    private ArrayList<PathBlockingObstacle> obstacles = new ArrayList<PathBlockingObstacle>();
    private NodeConnector<PathBlockingObstacle> nodeConnector = new NodeConnector<PathBlockingObstacle>();

    public RoadNetworkManager(Vector worldBounds)
    {
        this.worldBounds = worldBounds;
        this.carTracker = new CarTracker();
    }

    public CarTracker getCarTracker()
    {
        return carTracker;
    }

    public PathData findPath(Vector start, Vector end)
    {
        long startTime = System.currentTimeMillis();
        Vector kStart = new Vector(start.getX(), start.getY());
        Vector kEnd = new Vector(end.getX(), end.getY());

        PathData result = pathfinder.calc(kStart, kEnd, MAX_CONNECTION_DISTANCE, nodeConnector, obstacles);
        LOGGER.trace("Path calculation took {} ms - {}", System.currentTimeMillis() - startTime, result.getResult());
        result = laneAugmentor.augmentPathWithLanes(result);

        return result;
    }

    public void addRoad(Road road)
    {
        roads.add(road);
    }

    public void refreshNavigationMesh()
    {
        long start = System.currentTimeMillis();

        obstacles.clear();
        nodeConnector = new NodeConnector<PathBlockingObstacle>();
        obstacles = createObstacles();

        for (PathBlockingObstacle o : obstacles)
        {
            nodeConnector.addObstacle(o, obstacles, MAX_CONNECTION_DISTANCE);
        }

        LOGGER.debug("Refresh of nav mesh took {} ms", System.currentTimeMillis() - start);
    }

    ArrayList<PathBlockingObstacle> createObstacles()
    {
        if (roads.size() == 0)
            return obstacles;

        List<Rectangle2D.Double> rects = new LinkedList<Rectangle2D.Double>();
        for (Road road : roads)
        {
            rects.add(new Rectangle2D.Double(road.getPosition().x - ROAD_EXPANSION_MARGIN, road.getPosition().y
                    - ROAD_EXPANSION_MARGIN, road.getSize().x + ROAD_EXPANSION_MARGIN * 2, road.getSize().y
                    + ROAD_EXPANSION_MARGIN * 2));
        }

        return RectangleInverseUtility.createObstacles(worldBounds.x, worldBounds.y, rects);
    }

    public Vector getClosestRoadPoint(Vector point)
    {
        double minDistance = Double.MAX_VALUE;
        Vector minDistancePoint = null;

        for (Road road : roads)
        {
            KPoint topLeft = road.getPosition();
            KPoint bottomRight = road.getPosition().translateCopy(road.getSize());
            KPoint topRight = topLeft.translateCopy(road.getSize().x, 0);
            KPoint bottomLeft = topLeft.translateCopy(0, road.getSize().y);

            KPoint topLine = point.getClosestPointOnSegment(topLeft, topRight);
            KPoint rightLine = point.getClosestPointOnSegment(topRight, bottomRight);
            KPoint bottomLine = point.getClosestPointOnSegment(bottomLeft, bottomRight);
            KPoint leftLine = point.getClosestPointOnSegment(topLeft, bottomLeft);

            Vector closest = getClosestPoint(point, topLine, rightLine, bottomLine, leftLine);
            double distance = closest.translateCopy(point.negateCopy()).magnitude();

            if (minDistancePoint == null || distance < minDistance)
            {
                minDistance = distance;
                minDistancePoint = closest;
            }
        }

        return minDistancePoint;
    }

    static Vector getClosestPoint(Vector point, KPoint... vs)
    {
        double minDistance = Double.MAX_VALUE;
        KPoint minDistancePoint = null;

        for (KPoint v : vs)
        {
            double distance = v.translateCopy(point.negateCopy()).magnitude();

            if (minDistancePoint == null || distance < minDistance)
            {
                minDistance = distance;
                minDistancePoint = v;
            }
        }

        return new Vector(minDistancePoint);
    }

   

    @Override
    public void tick(float dt)
    {
        carTracker.updateCarPosition();
    }
}
