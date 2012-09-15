package org.housered.simul.model.navigation.road;

import static org.housered.simul.model.location.Vector.v;
import static org.junit.Assert.assertEquals;

import org.housered.simul.model.location.Vector;
import org.junit.Test;

public class CarControllerTest
{
    @Test
    public void shouldSetRayOriginToCircularBoundsOfCarWhenMovingNorth()
    {
        CarController car = CarTrackerTest.qM(v(10, 10), v(4, 4));
        Vector direction = v(0, -10);
        
        assertEquals(v(12, 10), CarController.getRayLookAheadOrigin(car, direction));
    }
    
    @Test
    public void shouldSetRayOriginToCircularBoundsOfCarWhenMovingSouth()
    {
        CarController car = CarTrackerTest.qM(v(10, 10), v(4, 4));
        Vector direction = v(0, 10);
        
        assertEquals(v(12, 14), CarController.getRayLookAheadOrigin(car, direction));
    }
    
    @Test
    public void shouldSetRayOriginToCircularBoundsOfCarWhenMovingEast()
    {
        CarController car = CarTrackerTest.qM(v(10, 10), v(4, 4));
        Vector direction = v(10, 0);
        
        assertEquals(v(14, 12), CarController.getRayLookAheadOrigin(car, direction));
    }
    
    @Test
    public void shouldSetRayOriginToCircularBoundsOfCarWhenMovingWest()
    {
        CarController car = CarTrackerTest.qM(v(10, 10), v(4, 4));
        Vector direction = v(-10 , 0);
        
        assertEquals(v(10, 12), CarController.getRayLookAheadOrigin(car, direction));
    }
    
    @Test
    public void shouldSetRayOriginToCircularBoundsOfCarWhenMovingDiagonally()
    {
        CarController car = CarTrackerTest.qM(v(10, 10), v(4, 4));
        Vector direction = v(3, 4);
        
        assertEquals(v(13.2, 13.6), CarController.getRayLookAheadOrigin(car, direction));
    }
}
