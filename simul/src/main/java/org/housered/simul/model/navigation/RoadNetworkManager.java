package org.housered.simul.model.navigation;

import static java.util.Arrays.asList;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.housered.simul.model.location.Vector;

import straightedge.geom.KPoint;
import straightedge.geom.path.PathData;
import straightedge.geom.path.PathData.Result;

public class RoadNetworkManager
{
    private static final Set<Result> BAD_RESULTS = new HashSet<PathData.Result>(asList(Result.ERROR1, Result.ERROR2,
            Result.ERROR3, Result.ERROR4, Result.NO_RESULT));
    private List<Road> roads = new LinkedList<Road>();

    public void addRoad(Road road)
    {
        roads.add(road);
    }

    public PathData makePathObeyRoads(PathData pathData)
    {
        if (BAD_RESULTS.contains(pathData.getResult()))
            throw new IllegalArgumentException();
        throw new IllegalStateException();
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
