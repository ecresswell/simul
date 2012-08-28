package org.housered.simul.model.navigation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import straightedge.geom.KPoint;
import straightedge.geom.KPolygon;
import straightedge.geom.path.NodeConnector;
import straightedge.geom.path.PathBlockingObstacle;
import straightedge.geom.path.PathBlockingObstacleImpl;
import straightedge.geom.path.PathData;
import straightedge.geom.path.PathFinder;

/**
 * Knows about all collidable objects, helps with navigating around them.
 */
public class NavigationManager
{
    private static final float MAX_CONNECTION_DISTANCE = 1000f;
    private static Logger LOGGER = LoggerFactory.getLogger(NavigationManager.class);

    private final KPoint worldBounds;
    private Set<Collidable> collidables = new HashSet<Collidable>();

    private PathFinder pathfinder = new PathFinder();
    private ArrayList<PathBlockingObstacle> obstacles = new ArrayList<PathBlockingObstacle>();
    private NodeConnector<PathBlockingObstacle> nodeConnector = new NodeConnector<PathBlockingObstacle>();

    public NavigationManager(KPoint worldBounds)
    {
        this.worldBounds = worldBounds;
    }

    public void addCollidable(Collidable collidable)
    {
        if (!isInsideWorldBounds(collidable))
        {
            LOGGER.error("Attempt to add collidable outside the bounds - {}", collidable);
        }

        collidables.add(collidable);
        refreshNavigationMesh();
    }

    public PathData findPath(KPoint start, KPoint end)
    {
        long startTime = System.currentTimeMillis();
        KPoint kStart = new KPoint(start.getX(), start.getY());
        KPoint kEnd = new KPoint(end.getX(), end.getY());

        PathData result = pathfinder.calc(kStart, kEnd, MAX_CONNECTION_DISTANCE, nodeConnector, obstacles);

        LOGGER.debug("Path calculation took {} ms", System.currentTimeMillis() - startTime);
        return result;
    }

    void refreshNavigationMesh()
    {
        long start = System.currentTimeMillis();

        obstacles.clear();
        nodeConnector = new NodeConnector<PathBlockingObstacle>();
        for (Collidable c : collidables)
        {
            KPoint b = c.getBounds();
            KPoint topLeft = c.getPosition().copy();
            KPoint topRight = topLeft.translateCopy(b.x, 0);
            KPoint bottomRight = topLeft.translateCopy(b.x, b.y);
            KPoint bottomLeft = topLeft.translateCopy(0, b.y);

            KPolygon poly = new KPolygon(topLeft, topRight, bottomRight, bottomLeft);
            obstacles.add(PathBlockingObstacleImpl.createObstacleFromInnerPolygon(poly));
        }

        for (PathBlockingObstacle o : obstacles)
        {
            nodeConnector.addObstacle(o, obstacles, MAX_CONNECTION_DISTANCE);
        }

        LOGGER.debug("Refresh of nav mesh took {} ms", System.currentTimeMillis() - start);
    }

    boolean isInsideWorldBounds(Collidable c)
    {
        if (c.getPosition().getX() < 0 || c.getPosition().getY() < 0)
            return false;
        if (c.getPosition().getX() + c.getBounds().getX() > worldBounds.getX()
                || c.getPosition().getY() + c.getBounds().getY() > worldBounds.getY())
            return false;
        return true;
    }
}
