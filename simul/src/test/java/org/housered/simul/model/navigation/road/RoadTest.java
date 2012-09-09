package org.housered.simul.model.navigation.road;

import static org.junit.Assert.*;

import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.road.Road.Orientation;
import org.junit.Test;

public class RoadTest
{
    @Test
    public void shouldDeriveOrientationFromSize()
    {
        assertEquals(Orientation.HORIZONTAL, Road.calculateOrientation(Vector.v(10, 3)));
        assertEquals(Orientation.HORIZONTAL, Road.calculateOrientation(Vector.v(3.1, 3)));
        assertEquals(Orientation.VERTICAL, Road.calculateOrientation(Vector.v(2.99, 3)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForSquareRoad()
    {
        Road.calculateOrientation(Vector.v(3, 3));
    }
}
