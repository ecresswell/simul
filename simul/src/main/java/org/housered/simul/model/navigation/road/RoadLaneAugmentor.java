package org.housered.simul.model.navigation.road;

import static org.housered.simul.model.navigation.road.RoadNetworkManager.ROAD_EXPANSION_MARGIN;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.housered.simul.model.location.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import straightedge.geom.KPoint;
import straightedge.geom.path.PathData;

public class RoadLaneAugmentor
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RoadLaneAugmentor.class);
    private final List<Road> roads;

    public RoadLaneAugmentor(List<Road> roads)
    {
        this.roads = roads;
    }

    PathData augmentPathWithLanes(PathData path)
    {
        if (path.isError())
            return path;

        ArrayList<KPoint> laneWayPoints = calculateLaneWayPoints(path);

        laneWayPoints.add(0, path.getPoints().get(0));
        laneWayPoints.add(laneWayPoints.size(), path.getPoints().get(path.getPoints().size() - 1));

        path.getPoints().clear();
        path.getPoints().addAll(laneWayPoints);

        return path;
    }

    ArrayList<KPoint> calculateLaneWayPoints(PathData path)
    {
        ArrayList<KPoint> augmentedList = new ArrayList<KPoint>();

        for (int index = 0; index < path.getPoints().size() - 1; index++)
        {
            KPoint startPoint = path.getPoints().get(index);
            KPoint nextPoint = path.getPoints().get(index + 1);
            Vector direction = new Vector(nextPoint.x - startPoint.x, nextPoint.y - startPoint.y);

            Road road = getRoadThatPointIsOn(startPoint);

            if (road == null)
            {
                LOGGER.warn("Null road");
                continue;
            }

            if (road.getOrientation() == Road.Orientation.HORIZONTAL)
            {
                if (direction.x > 0)
                {
                    double yLane = road.getPosition().y + road.getSize().y / 4;
                    augmentedList.add(new Vector(startPoint.x, yLane));
                    augmentedList.add(new Vector(nextPoint.x, yLane));
                }
                else
                {
                    double yLane = road.getPosition().y + road.getSize().y * (3f / 4);
                    augmentedList.add(new Vector(startPoint.x, yLane));
                    augmentedList.add(new Vector(nextPoint.x, yLane));
                }
            }
            else
            {
                if (direction.y > 0)
                {
                    double xLane = road.getPosition().x + road.getSize().x * (3f / 4);
                    augmentedList.add(new Vector(xLane, startPoint.y));
                    augmentedList.add(new Vector(xLane, nextPoint.y));
                }
                else
                {
                    double xLane = road.getPosition().x + road.getSize().x / 4;
                    augmentedList.add(new Vector(xLane, startPoint.y));
                    augmentedList.add(new Vector(xLane, nextPoint.y));
                }
            }
        }

        return augmentedList;
    }

    Road getRoadThatPointIsOn(KPoint point)
    {
        for (Road road : roads)
        {
            Rectangle2D.Double r = new Rectangle2D.Double(road.getPosition().x - ROAD_EXPANSION_MARGIN,
                    road.getPosition().y - ROAD_EXPANSION_MARGIN, road.getSize().x + ROAD_EXPANSION_MARGIN * 2,
                    road.getSize().y + ROAD_EXPANSION_MARGIN * 2);
            if (r.contains(point.x, point.y))
                return road;

        }
        return null;
    }
}
