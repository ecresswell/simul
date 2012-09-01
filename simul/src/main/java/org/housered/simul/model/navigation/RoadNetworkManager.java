package org.housered.simul.model.navigation;

import static java.util.Arrays.asList;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.housered.simul.model.location.Vector;

import straightedge.geom.KPoint;
import straightedge.geom.KPolygon;
import straightedge.geom.path.PathBlockingObstacle;
import straightedge.geom.path.PathBlockingObstacleImpl;
import straightedge.geom.path.PathData;
import straightedge.geom.path.PathData.Result;

public class RoadNetworkManager
{
    private static final Set<Result> BAD_RESULTS = new HashSet<PathData.Result>(asList(Result.ERROR1, Result.ERROR2,
            Result.ERROR3, Result.ERROR4, Result.NO_RESULT));
    private List<Road> roads = new LinkedList<Road>();
    private final Vector worldBounds;

    public RoadNetworkManager(Vector worldBounds)
    {
        this.worldBounds = worldBounds;
    }

    public void addRoad(Road road)
    {
        roads.add(road);
    }

    List<PathBlockingObstacle> createObstacles()
    {
        List<PathBlockingObstacle> obstacles = new LinkedList<PathBlockingObstacle>();

        if (roads.size() == 0)
            return obstacles;

        for (Rectangle2D.Double r : inverseRoads())
        {
            Vector topLeft = new Vector(r.getMinX(), r.getMinY());
            Vector topRight = new Vector(r.getMaxX(), r.getMinY());
            Vector bottomRight = new Vector(r.getMaxX(), r.getMaxY());
            Vector bottomLeft = new Vector(r.getMinX(), r.getMaxY());

            KPolygon poly = new KPolygon(topLeft, topRight, bottomRight, bottomLeft);
            obstacles.add(PathBlockingObstacleImpl.createObstacleFromInnerPolygon(poly));
        }

        return obstacles;
    }

    List<Rectangle2D.Double> inverseRoads()
    {
        List<Rectangle2D.Double> rects = new LinkedList<Rectangle2D.Double>();
        for (Road road : roads)
        {
            rects.add(new Rectangle2D.Double(road.getPosition().x, road.getPosition().y, road.getSize().x, road
                    .getSize().y));
        }

        List<Rectangle2D.Double> outputs = new ArrayList<Rectangle2D.Double>();
        outputs.add(new Rectangle2D.Double(0, 0, worldBounds.x, worldBounds.y));

        for (Rectangle2D.Double r : rects)
        {
            List<Rectangle2D.Double> newOutputs = new ArrayList<Rectangle2D.Double>();

            for (Rectangle2D.Double output : outputs)
                newOutputs.addAll(slice(output, r));
            outputs = newOutputs;
        }

        return outputs;
    }

    List<Rectangle2D.Double> slice(Rectangle2D.Double r, Rectangle2D.Double mask)
    {
        List<Rectangle2D.Double> rects = new ArrayList<Rectangle2D.Double>();

        Rectangle2D.Double intersection = new Rectangle2D.Double();
        Rectangle2D.Double.intersect(r, mask, intersection);

        if (!intersection.isEmpty())
        {
            rects.add(new Rectangle2D.Double(r.x, r.y, r.width, intersection.y - r.y));
            rects.add(new Rectangle2D.Double(r.x, intersection.y + intersection.height, r.width, (r.y + r.height)
                    - (intersection.y + intersection.height)));
            rects.add(new Rectangle2D.Double(r.x, intersection.y, intersection.x - r.x, intersection.height));
            rects.add(new Rectangle2D.Double(intersection.x + intersection.width, intersection.y, (r.x + r.width)
                    - (intersection.x + intersection.width), intersection.height));

            for (Iterator<Rectangle2D.Double> iter = rects.iterator(); iter.hasNext();)
                if (iter.next().isEmpty())
                    iter.remove();
        }
        else
            rects.add(r);

        return rects;
    }

    Vector getClosestRoadPoint(Vector point)
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

    Vector getClosestPoint(Vector point, KPoint... vs)
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
}
