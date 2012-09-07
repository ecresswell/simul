package org.housered.simul.model.navigation.road;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.housered.simul.model.location.Vector;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.quadtree.Quadtree;

public class CarTracker
{
    private static final double ENVELOPE_MARGIN = 0;
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

        Envelope e = new Envelope(car.getPosition().x - ENVELOPE_MARGIN, car.getPosition().x + ENVELOPE_MARGIN * 2,
                car.getPosition().y - ENVELOPE_MARGIN, car.getPosition().y + ENVELOPE_MARGIN * 2);

        cars.insert(e, car);
        positions.put(car, e);
    }

    public List<CarController> getCars(Vector position, Vector size)
    {
        Envelope e = new Envelope(position.x, position.x + size.x, position.y, position.y + size.y);
        @SuppressWarnings("unchecked")
        List<CarController> results = cars.query(e);
        return results;
    }

    public boolean removeCar(CarController car)
    {
        //performance increase here by keeping a map of cars to their envelope
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
}
