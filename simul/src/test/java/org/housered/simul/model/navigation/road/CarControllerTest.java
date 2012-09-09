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
        Vector pos = new Vector(10, 10);
        Vector direction = new Vector(10, 0);
        
        Envelope e = CarController.getLookAheadEnvelope(pos, direction, 5);
        
        assertEquals(10, e.getMinX(), EPSILON);
        assertEquals(10, e.getMinY(), EPSILON);
        assertEquals(10, e.getWidth(), EPSILON);
        assertEquals(5, e.getHeight(), EPSILON);
    }
    
    @Test
    public void shouldCreateLookAheadWithSizeOfDirectionWhenMovingToTheLeft()
    {
        Vector pos = new Vector(10, 10);
        Vector direction = new Vector(-10, 0);
        
        Envelope e = CarController.getLookAheadEnvelope(pos, direction, 5);
        
        assertEquals(0, e.getMinX(), EPSILON);
        assertEquals(10, e.getMinY(), EPSILON);
        assertEquals(10, e.getWidth(), EPSILON);
        assertEquals(5, e.getHeight(), EPSILON);
    }
    
    @Test
    public void shouldCreateLookAheadWithSizeOfDirectionWhenMovingUpwards()
    {
        Vector pos = new Vector(10, 10);
        Vector direction = new Vector(0, -10);
        
        Envelope e = CarController.getLookAheadEnvelope(pos, direction, 5);
        
        assertEquals(10, e.getMinX(), EPSILON);
        assertEquals(0, e.getMinY(), EPSILON);
        assertEquals(5, e.getWidth(), EPSILON);
        assertEquals(10, e.getHeight(), EPSILON);
    }
    
    @Test
    public void shouldCreateLookAheadWithSizeOfDirectionWhenMovingDownwards()
    {
        Vector pos = new Vector(10, 10);
        Vector direction = new Vector(0, 10);
        
        Envelope e = CarController.getLookAheadEnvelope(pos, direction, 5);
        
        assertEquals(10, e.getMinX(), EPSILON);
        assertEquals(10, e.getMinY(), EPSILON);
        assertEquals(5, e.getWidth(), EPSILON);
        assertEquals(10, e.getHeight(), EPSILON);
    }
    
}
