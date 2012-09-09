package org.housered.simul.model.navigation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.housered.simul.model.location.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private final Vector worldBounds;
    private Set<Collidable> collidables = new HashSet<Collidable>();

    private PathFinder pathfinder = new PathFinder();
    private ArrayList<PathBlockingObstacle> obstacles = new ArrayList<PathBlockingObstacle>();
    private NodeConnector<PathBlockingObstacle> nodeConnector = new NodeConnector<PathBlockingObstacle>();

    public NavigationManager(Vector worldBounds)
    {
        this.worldBounds = worldBounds;
    }

    public void addCollidable(Collidable collidable)
    {
        addColliableWithoutNavMeshRefresh(collidable);
        refreshNavigationMesh();
    }

    public void addColliableWithoutNavMeshRefresh(Collidable collidable)
    {
        if (!isInsideWorldBounds(collidable))
        {
            LOGGER.error("Attempt to add collidable outside the bounds - {}", collidable);
        }

        collidables.add(collidable);
    }

    public PathData findPath(Vector start, Vector end)
    {
        long startTime = System.currentTimeMillis();
        Vector kStart = new Vector(start.getX(), start.getY());
        Vector kEnd = new Vector(end.getX(), end.getY());

        PathData result = pathfinder.calc(kStart, kEnd, MAX_CONNECTION_DISTANCE, nodeConnector, obstacles);

        LOGGER.trace("Path calculation took {} ms - {}", System.currentTimeMillis() - startTime, result.getResult());
        return result;
    }

    public void refreshNavigationMesh()
    {
        long start = System.currentTimeMillis();

        obstacles.clear();
        nodeConnector = new NodeConnector<PathBlockingObstacle>();
        for (Collidable c : collidables)
        {
            Vector b = c.getSize();
            Vector topLeft = c.getPosition().copy();
            Vector topRight = topLeft.translateCopy(b.x, 0);
            Vector bottomRight = topLeft.translateCopy(b.x, b.y);
            Vector bottomLeft = topLeft.translateCopy(0, b.y);

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
        if (c.getPosition().getX() + c.getSize().getX() > worldBounds.getX()
                || c.getPosition().getY() + c.getSize().getY() > worldBounds.getY())
            return false;
        return true;
    }
}
