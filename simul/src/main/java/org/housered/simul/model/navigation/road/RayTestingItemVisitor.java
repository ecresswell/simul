package org.housered.simul.model.navigation.road;

import java.awt.geom.Rectangle2D;

import org.housered.simul.model.location.Vector;

import com.vividsolutions.jts.index.ItemVisitor;

public class RayTestingItemVisitor implements ItemVisitor
{
    private final Vector origin;
    private final Vector direction;
    private final CarController ignoredCar;
    private CarController minimumCar;
    private double minimumDistanceSquared;

    public RayTestingItemVisitor(Vector origin, Vector direction)
    {
        this(origin, direction, null);
    }

    public RayTestingItemVisitor(Vector origin, Vector direction, CarController car)
    {
        this.origin = origin;
        this.direction = direction;
        this.ignoredCar = car;
    }

    public CarController getClosestCar()
    {
        return minimumCar;
    }

    @Override
    public void visitItem(Object item)
    {
        CarController c = (CarController) item;
        
        if (c == ignoredCar)
            return;
        
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
            }
        }
    }
}
