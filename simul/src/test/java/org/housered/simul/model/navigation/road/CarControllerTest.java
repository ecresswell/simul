package org.housered.simul.model.navigation.road;

import static org.junit.Assert.assertEquals;

import org.housered.simul.model.location.Vector;
import org.junit.Test;

import com.vividsolutions.jts.geom.Envelope;

public class CarControllerTest
{
    private static final double EPSILON = 0.001f;
    
    @Test
    public void shouldCreateLookAheadWithSizeOfDirectionWhenMovingToTheRight()
    {
        CarController car = CarTrackerTest.qM(new Vector(10, 10), new Vector(3, 3));
        Vector direction = new Vector(10, 0);
        
        Envelope e = CarController.getLookAheadEnvelope(car, direction);
        
        assertEquals(13, e.getMinX(), EPSILON);
        assertEquals(10, e.getMinY(), EPSILON);
        assertEquals(10, e.getWidth(), EPSILON);
        assertEquals(0, e.getHeight(), EPSILON);
    }
    
    @Test
    public void shouldCreateLookAheadWithSizeOfDirectionWhenMovingToTheLeft()
    {
        CarController car = CarTrackerTest.qM(new Vector(10, 10), new Vector(3, 3));
        Vector direction = new Vector(-10, 0);
        
        Envelope e = CarController.getLookAheadEnvelope(car, direction);
        
        assertEquals(0, e.getMinX(), EPSILON);
        assertEquals(10, e.getMinY(), EPSILON);
        assertEquals(10, e.getWidth(), EPSILON);
        assertEquals(0, e.getHeight(), EPSILON);
    }
    
    @Test
    public void shouldCreateLookAheadWithSizeOfDirectionWhenMovingUpwards()
    {
        CarController car = CarTrackerTest.qM(new Vector(10, 10), new Vector(3, 3));
        Vector direction = new Vector(0, -10);
        
        Envelope e = CarController.getLookAheadEnvelope(car, direction);
        
        assertEquals(10, e.getMinX(), EPSILON);
        assertEquals(0, e.getMinY(), EPSILON);
        assertEquals(0, e.getWidth(), EPSILON);
        assertEquals(10, e.getHeight(), EPSILON);
    }
    
    @Test
    public void shouldCreateLookAheadWithSizeOfDirectionWhenMovingDownwards()
    {
        CarController car = CarTrackerTest.qM(new Vector(10, 10), new Vector(3, 3));
        Vector direction = new Vector(0, 10);
        
        Envelope e = CarController.getLookAheadEnvelope(car, direction);
        
        assertEquals(10, e.getMinX(), EPSILON);
        assertEquals(13, e.getMinY(), EPSILON);
        assertEquals(0, e.getWidth(), EPSILON);
        assertEquals(10, e.getHeight(), EPSILON);
    }
    
    
    @Test
    public void shouldTestDiagonalCasesAsCurrentlyTheyAreWrong()
    {
        //fail();
    }
    
}
