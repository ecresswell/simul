package org.housered.simul.model.navigation.road;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.housered.simul.model.location.Vector;
import org.junit.Test;

public class CarTrackerTest
{
    @Test
    public void shouldReturnCarsInArea()
    {
        CarController car1 = mock(CarController.class);
        when(car1.getPosition()).thenReturn(new Vector(10, 10));
        CarController car2 = mock(CarController.class);
        when(car2.getPosition()).thenReturn(new Vector(15, 15));
        CarController car3 = mock(CarController.class);
        when(car3.getPosition()).thenReturn(new Vector(5, 5));

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
        CarController car = mock(CarController.class);
        when(car.getPosition()).thenReturn(new Vector(10, 10));

        CarTracker tracker = new CarTracker();
        tracker.addCar(car);
        assertEquals(1, tracker.getCars(new Vector(6, 6), new Vector(10, 10)).size());

        tracker.removeCar(car);
        assertEquals(0, tracker.getCars(new Vector(6, 6), new Vector(10, 10)).size());
    }

    @Test
    public void shouldUpdateCarPositionsWhenTicked()
    {
        CarController car1 = mock(CarController.class);
        CarController car2 = mock(CarController.class);
        when(car1.getPosition()).thenReturn(new Vector(10, 10));
        when(car2.getPosition()).thenReturn(new Vector(0, 10));
        
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
}
