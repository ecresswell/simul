package org.housered.simul.model.navigation.road;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.housered.simul.model.location.Vector;
import org.junit.Ignore;
import org.junit.Test;

public class CarTrackerTest
{
    @Test
    public void shouldReturnCarsInArea()
    {
        CarController car1 = qM(new Vector(10, 10), new Vector(1, 1));
        CarController car2 = qM(new Vector(15, 15), new Vector(1, 1));
        CarController car3 = qM(new Vector(4, 4), new Vector(1, 1));

        CarTracker tracker = new CarTracker();
        tracker.addCar(car1);
        tracker.addCar(car2);
        tracker.addCar(car3);

        List<CarController> results = tracker.getCars(new Vector(6, 6), new Vector(10, 10));

        assertEquals(2, results.size());
        assertTrue(results.contains(car1));
        assertTrue(results.contains(car2));
    }

    @Test
    public void shouldAllowRemovingOfCars()
    {
        CarController car = qM(new Vector(10, 10), new Vector(1, 1));

        CarTracker tracker = new CarTracker();
        tracker.addCar(car);
        assertEquals(1, tracker.getCars(new Vector(6, 6), new Vector(10, 10)).size());

        assertTrue(tracker.removeCar(car));
        assertEquals(0, tracker.getCars(new Vector(6, 6), new Vector(10, 10)).size());
    }

    @Test
    public void shouldUpdateCarPositionsWhenTicked()
    {
        CarController car1 = qM(new Vector(10, 10), new Vector(1, 1));
        CarController car2 = qM(new Vector(0, 10), new Vector(1, 1));

        CarTracker tracker = new CarTracker();
        tracker.addCar(car1);
        tracker.addCar(car2);
        assertEquals(1, tracker.getCars(new Vector(9, 9), new Vector(1, 1)).size());
        assertEquals(1, tracker.getCars(new Vector(0, 9), new Vector(1, 1)).size());

        when(car1.getPosition()).thenReturn(new Vector(50, 20));
        when(car2.getPosition()).thenReturn(new Vector(30, 5));
        assertEquals(1, tracker.getCars(new Vector(10, 10), new Vector(1, 1)).size());
        assertEquals(1, tracker.getCars(new Vector(0, 9), new Vector(1, 1)).size());

        tracker.updateCarPosition();
        assertEquals(0, tracker.getCars(new Vector(9, 9), new Vector(1, 1)).size());
        assertEquals(0, tracker.getCars(new Vector(0, 9), new Vector(1, 1)).size());
        assertEquals(1, tracker.getCars(new Vector(50, 20), new Vector(1, 1)).size());
        assertEquals(1, tracker.getCars(new Vector(30, 5), new Vector(1, 1)).size());
    }

    @Test
    public void shouldReturnDistanceBetweenCars()
    {
        CarController me = qM(new Vector(10, 10), new Vector(5, 5));
        CarController them = qM(new Vector(302, -45.1), new Vector(10, 3));

        assertEquals(299.7956971, CarTracker.getDistanceToCar(me, them), Vector.EPSILON);
    }

    @Test
    public void shouldReturnClosestCarToTheRay()
    {
        CarController car1 = qM(new Vector(10, 10), new Vector(3, 3));
        CarController car2 = qM(new Vector(15, 15), new Vector(3, 3));
        CarController car3 = qM(new Vector(4, 4), new Vector(3, 3));

        CarTracker tracker = new CarTracker();
        tracker.addCar(car1);
        tracker.addCar(car2);
        tracker.addCar(car3);

        assertEquals(car1, tracker.performRayCollision((new Vector(6, 10)), new Vector(10, 0)));
        assertEquals(car1, tracker.performRayCollision((new Vector(5, 10)), new Vector(10, 0)));
        assertEquals(car1, tracker.performRayCollision((new Vector(5, 10)), new Vector(10, 0)));
        assertEquals(null, tracker.performRayCollision((new Vector(-4, 10)), new Vector(10, 0)));

        assertEquals(car1, tracker.performRayCollision((new Vector(5, 10)), new Vector(10, 5)));
        assertEquals(car1, tracker.performRayCollision((new Vector(11, 11)), new Vector(5, 5)));
        assertEquals(car2, tracker.performRayCollision((new Vector(20, 20)), new Vector(-50, -50)));
        assertEquals(car3, tracker.performRayCollision((new Vector(7, 7)), new Vector(-10, -10)));
    }
    
    @Test
    public void shouldFireRayFromCentreOfCarAndExcludeSelf()
    {
        CarController car1 = qM(new Vector(10, 10), new Vector(3, 3));
        CarController car2 = qM(new Vector(15, 15), new Vector(3, 3));
        CarController car3 = qM(new Vector(4, 4), new Vector(3, 3));

        CarTracker tracker = new CarTracker();
        tracker.addCar(car1);
        tracker.addCar(car2);
        tracker.addCar(car3);
        
        assertEquals(car2, tracker.getClosestCar(car1, new Vector(5, 5)));
    }

    static CarController qM(Vector pos, Vector size)
    {
        CarController car = mock(CarController.class);
        when(car.getPosition()).thenReturn(pos);
        when(car.getSize()).thenReturn(size);
        return car;
    }
}
