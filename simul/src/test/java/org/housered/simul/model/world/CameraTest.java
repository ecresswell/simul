package org.housered.simul.model.world;

import static org.junit.Assert.assertEquals;

import org.housered.simul.model.location.Vector;
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

        assertEquals(-200, c.getXOffset(), 0.001d);
        assertEquals(-150, c.getYOffset(), 0.001d);
    }

    @Test
    public void shouldZoomInOnTheCentreAfterMovingTheCamera()
    {
        Camera c = new Camera(800, 600);

        c.incrementXOffset(100);
        c.zoom(0.5f);

        assertEquals(-100, c.getXOffset(), 0.001d);
        assertEquals(-150, c.getYOffset(), 0.001d);
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

    @Test
    public void shouldTranslatePositionsWithRespectToZoomAndOffset()
    {
        Camera c = new Camera(800, 600);

        assertEquals(new Vector(5, 5), c.translateIntoScreenSpace(new Vector(5, 5)));

        c.incrementXOffset(100);
        assertEquals(new Vector(105, 5), c.translateIntoScreenSpace(new Vector(5, 5)));

        c.incrementYOffset(-10);
        assertEquals(new Vector(105, -5), c.translateIntoScreenSpace(new Vector(5, 5)));

        c.zoom(0.5);
        assertEquals(new Vector(-190, -310), c.translateIntoScreenSpace(new Vector(5, 5)));
    }

    @Test
    public void shouldScaleDimensionWithRespectToZoom()
    {
        Camera c = new Camera(800, 600);

        assertEquals(new Vector(10, 10), c.scaleIntoScreenSpace(new Vector(10, 10)));

        c.incrementXOffset(13);
        c.incrementYOffset(-23);
        assertEquals(new Vector(10, 10), c.scaleIntoScreenSpace(new Vector(10, 10)));

        c.zoom(2);
        assertEquals(new Vector(5, 5), c.scaleIntoScreenSpace(new Vector(10, 10)));

        c.zoom(0.5);
        assertEquals(new Vector(10, 10), c.scaleIntoScreenSpace(new Vector(10, 10)));
    }
}
