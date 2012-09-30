package org.housered.simul.model.navigation.road;

import static org.housered.simul.model.location.Vector.v;
import static org.housered.simul.model.navigation.road.CarTrackerTest.qM;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.housered.simul.model.location.Vector;
import org.junit.Test;

public class RayTestingItemVisitorTest
{
    @Test
    public void shouldFindFirstCarInARay()
    {
        RayTestingItemVisitor v = new RayTestingItemVisitor(v(0, 0), v(10, 10));

        CarController car1 = qM(new Vector(5, 5), new Vector(1, 1));
        CarController car2 = qM(new Vector(10, 10), new Vector(1, 1));

        v.visitItem(car2);
        v.visitItem(car1);

        assertEquals(car1, v.getClosestCar());
    }

    @Test
    public void shouldReturnNullWhenNothingInRay()
    {
        RayTestingItemVisitor v = new RayTestingItemVisitor(v(0, 0), v(4.9, 4.9));

        CarController car1 = qM(new Vector(5, 5), new Vector(1, 1));

        v.visitItem(car1);

        assertEquals(null, v.getClosestCar());
    }
    
    @Test
    public void shouldIgnoreTheGivenCar()
    {
        CarController car1 = qM(new Vector(5, 5), new Vector(1, 1));
        CarController car2 = qM(new Vector(10, 10), new Vector(1, 1));
        
        RayTestingItemVisitor v = new RayTestingItemVisitor(v(0, 0), v(10, 10), car1, 0);

        v.visitItem(car2);
        v.visitItem(car1);

        assertEquals(car2, v.getClosestCar());
    }
    
    @Test
    public void shouldReturnFalseWhenMinimumDistanceHasBeenHit()
    {
        CarController car1 = qM(new Vector(5, 5), new Vector(1, 1));
        CarController car2 = qM(new Vector(10, 10), new Vector(1, 1));
        
        RayTestingItemVisitor v = new RayTestingItemVisitor(v(9, 9), v(10, 10), car1, 3);

        assertFalse(v.visitItem(car1));
        assertTrue(v.visitItem(car2));
        
        assertEquals(car2, v.getClosestCar());
    }
}
