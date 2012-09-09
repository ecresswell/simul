package org.housered.simul.model.navigation.road;

import static org.housered.simul.model.navigation.road.RoadNetworkManager.ROAD_EXPANSION_MARGIN;

import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.ListIterator;

import org.housered.simul.model.location.Vector;

import straightedge.geom.KPoint;
import straightedge.geom.path.PathData;

public class RoadLaneAugmentor
{
    private final List<Road> roads;

    public RoadLaneAugmentor(List<Road> roads)
    {
        this.roads = roads;
    }

    PathData augmentPathWithLanes(PathData path)
    {
        if (path.isError())
            return path;

        int pathLength = path.getPoints().size();
        addLaneWayPoints(path, 0);
        //addLaneWayPoints(path, 2);

        return path;
    }

    void addLaneWayPoints(PathData path, int index)
    {
        KPoint startPoint = path.getPoints().get(index);
        KPoint nextPoint = path.getPoints().get(index + 1);
        Vector direction = new Vector(nextPoint.x - startPoint.x, nextPoint.y - startPoint.y);

        Road road = getRoadThatPointIsOn(startPoint);

        if (road.getOrientation() == Road.Orientation.HORIZONTAL)
        {
            if (direction.x > 0)
            {
                double yLane = road.getPosition().y + road.getSize().y / 4;
                path.getPoints().add(index + 1, new Vector(startPoint.x, yLane));
                path.getPoints().add(index + 2, new Vector(nextPoint.x, yLane));
            }
            else
            {
                double yLane = road.getPosition().y + road.getSize().y * (3f / 4);
                path.getPoints().add(index + 1, new Vector(startPoint.x, yLane));
                path.getPoints().add(index + 2, new Vector(nextPoint.x, yLane));
            }
        }
        else
        {
            if (direction.y > 0)
            {
                double xLane = road.getPosition().x + road.getSize().x * (3f / 4);
                path.getPoints().add(index + 1, new Vector(xLane, startPoint.y));
                path.getPoints().add(index + 2, new Vector(xLane, nextPoint.y));
            }
            else
            {
                double xLane = road.getPosition().x + road.getSize().x / 4;
                path.getPoints().add(index + 1, new Vector(xLane, startPoint.y));
                path.getPoints().add(index + 2, new Vector(xLane, nextPoint.y));
            }
        }
    }

    Road getRoadThatPointIsOn(KPoint point)
    {
        for (Road road : roads)
        {
            Rectangle2D.Double r = new Rectangle2D.Double(road.getPosition().x, road.getPosition().y, road.getSize().x
                    + ROAD_EXPANSION_MARGIN, road.getSize().y + ROAD_EXPANSION_MARGIN);
            if (r.contains(point.x, point.y))
                return road;

        }
        return null;
    }
}
