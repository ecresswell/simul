package org.housered.simul.model.navigation.road;

import java.util.List;

import org.housered.simul.model.location.Vector;
import org.housered.simul.model.world.Tickable;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.quadtree.Quadtree;

public class CarTracker
{
    private double maxX = 0;
    private double maxY = 0;
    private Quadtree cars = new Quadtree();

    public void addCar(CarController car)
    {
        if (car.getPosition().x > maxX)
            maxX = car.getPosition().x;
        if (car.getPosition().y > maxY)
            maxY = car.getPosition().y;

        cars.insert(new Envelope(new Coordinate(car.getPosition().x, car.getPosition().y)), car);
    }

    public List<CarController> getCars(Vector position, Vector size)
    {
        @SuppressWarnings("unchecked")
        List<CarController> results = cars.query(new Envelope(position.x, position.x + size.x, position.y, position.y
                + size.y));
        return results;
    }

    public void removeCar(CarController car)
    {
        //performance increase here by keeping a map of cars to their envelope
        cars.remove(new Envelope(0, maxX, 0, maxY), car);
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
}
