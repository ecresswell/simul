package org.housered.simul.model.world;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CameraTest
{
    @Test
    public void shouldMoveAround()
    {
        Camera c = new Camera(800, 600);

        c.incrementXOffset(100);
        c.incrementYOffset(300);

        assertEquals(100, c.getXOffset(), 0.001d);
        assertEquals(300, c.getYOffset(), 0.001d);
    }

    @Test
    public void shouldZoomInOnTheCentre()
    {
        Camera c = new Camera(800, 600);

        c.zoom(0.5f);

        assertEquals(200, c.getXOffset(), 0.001d);
        assertEquals(150, c.getYOffset(), 0.001d);
    }

    @Test
    public void shouldZoomInOnTheCentreAfterMovingTheCamera()
    {
        Camera c = new Camera(800, 600);

        c.incrementXOffset(100);
        c.zoom(0.5f);

        assertEquals(300, c.getXOffset(), 0.001d);
        assertEquals(150, c.getYOffset(), 0.001d);
    }

    @Test
    public void shouldZoomInAndReturnCorrectUnitsPerWorldUnit()
    {
        Camera c = new Camera(800, 600);

        c.zoom(0.5f);
        assertEquals(2, c.getUnitsPerWorldUnit(), 0.001d);

        c.zoom(0.5f);
        assertEquals(4, c.getUnitsPerWorldUnit(), 0.001d);
        
        c.zoom(4f);
        assertEquals(1, c.getUnitsPerWorldUnit(), 0.001d);
        
        c.zoom(0.454f);
        assertEquals(2.2026, c.getUnitsPerWorldUnit(), 0.001d);
    }
}
