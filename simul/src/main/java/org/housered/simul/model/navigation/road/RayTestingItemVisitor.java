package org.housered.simul.model.navigation.road;

import java.awt.geom.Rectangle2D;

import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.road.quadtree.ExitEarlyItemVisitor;

public class RayTestingItemVisitor implements ExitEarlyItemVisitor
{
    private final Vector origin;
    private final Vector direction;
    private final CarController ignoredCar;
    private final double exitEarlyDistanceSquared;
    private CarController minimumCar;
    private double minimumDistanceSquared;

    public RayTestingItemVisitor(Vector origin, Vector direction)
    {
        this(origin, direction, null, 0);
    }

    public RayTestingItemVisitor(Vector origin, Vector direction, CarController car, double exitEarlyDistance)
    {
        this.origin = origin;
        this.direction = direction;
        this.ignoredCar = car;
        this.exitEarlyDistanceSquared = exitEarlyDistance * exitEarlyDistance;
    }

    public CarController getClosestCar()
    {
        return minimumCar;
    }

    @Override
    public boolean visitItem(Object item)
    {
        CarController c = (CarController) item;

        if (c == ignoredCar)
            return false;

        Rectangle2D.Double d = new Rectangle2D.Double(c.getPosition().x, c.getPosition().y, c.getSize().x,
                c.getSize().y);

        if (d.intersectsLine(origin.x, origin.y, origin.x + direction.x, origin.y + direction.y))
        {
            Vector cOrigin = new Vector(d.getCenterX(), d.getCenterY());
            double distanceSq = cOrigin.distanceSq(origin);

            if (minimumCar == null || distanceSq < minimumDistanceSquared)
            {
                minimumCar = c;
                minimumDistanceSquared = distanceSq;
                
                
                if (minimumDistanceSquared < exitEarlyDistanceSquared)
                    return true;
            }
        }
        
        return false;
    }
}
