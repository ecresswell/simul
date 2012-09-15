package org.housered.simul.model.navigation.road;

import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.housered.simul.model.location.Vector;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.quadtree.Quadtree;

public class CarTracker
{
    private final Quadtree cars = new Quadtree();
    private final Map<CarController, Envelope> positions = new HashMap<CarController, Envelope>();
    private double maxX = 0;
    private double maxY = 0;

    public void addCar(CarController car)
    {
        if (car.getPosition().x > maxX)
            maxX = car.getPosition().x;
        if (car.getPosition().y > maxY)
            maxY = car.getPosition().y;

        Envelope e = new Envelope(car.getPosition().x, car.getPosition().x + car.getSize().x, car.getPosition().y,
                car.getPosition().y + car.getSize().y);

        cars.insert(e, car);
        positions.put(car, e);
    }

    public CarController performRayCollision(Vector origin, Vector direction)
    {
        Envelope e = new Envelope(origin.x, origin.x + direction.x, origin.y, origin.y + direction.y);

        RayTestingItemVisitor v = new RayTestingItemVisitor(origin, direction);
        cars.query(e, v);

        return v.getClosestCar();
    }

    public CarController getClosestCar(Vector origin, final CarController car, Vector direction)
    {
        Envelope e = new Envelope(origin.x, origin.x + direction.x, origin.y, origin.y + direction.y);

        RayTestingItemVisitor v = new RayTestingItemVisitor(origin, direction, car);
        cars.query(e, v);

        return v.getClosestCar();
    }

    public List<CarController> getCars(Vector position, Vector size)
    {
        Envelope e = new Envelope(position.x, position.x + size.x, position.y, position.y + size.y);
        @SuppressWarnings("unchecked")
        List<CarController> results = cars.query(e);
        return results;
    }

    public List<CarController> getCars(Envelope query)
    {
        @SuppressWarnings("unchecked")
        List<CarController> results = cars.query(query);
        return results;
    }

    public boolean removeCar(CarController car)
    {
        Envelope e = positions.get(car);
        if (e == null)
            return false;

        return cars.remove(positions.get(car), car);
    }

    public void updateCarPosition()
    {
        @SuppressWarnings("unchecked")
        List<CarController> allCars = cars.queryAll();

        //performance increase here by... modifying the source?
        //need to do an update operation instead of remove/add
        for (CarController car : allCars)
        {
            removeCar(car);
            addCar(car);
        }
    }

    /**
     * Returns the distance between centre of cars.
     */
    static double getDistanceToCar(CarController me, CarController them)
    {
        Vector centre = me.getPosition().translateCopy(me.getSize().x / 2, me.getSize().y / 2);
        return getDistanceToCar(centre, them);
    }

    static double getDistanceToCar(Vector me, CarController them)
    {
        Vector themCentre = them.getPosition().translateCopy(them.getSize().x / 2, them.getSize().y / 2);
        Vector difference = me.translateCopy(themCentre.negateCopy());
        return difference.magnitude();
    }
}
