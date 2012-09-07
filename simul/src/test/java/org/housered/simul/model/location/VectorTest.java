package org.housered.simul.model.location;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class VectorTest
{
    @Test
    public void shouldCreateCopyWithWantedMagnitude()
    {
        Vector v = new Vector(10, 5);
        double wantedMagnitude = 10;

        Vector scaled = v.scaleToMagnitudeCopy(wantedMagnitude);
        assertEquals(8.9442719, scaled.x, 0.001f);
        assertEquals(4.472136, scaled.y, 0.001f);
    }
}
