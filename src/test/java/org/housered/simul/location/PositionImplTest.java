package org.housered.simul.location;

import static org.junit.Assert.assertEquals;

import org.housered.simul.model.location.Position;
import org.housered.simul.model.location.PositionImpl;
import org.junit.Test;

public class PositionImplTest
{
    @Test
    public void shouldConvertWorldPositionIntoRenderablePosition()
    {
        Position p = new PositionImpl(10, 20);

        //but camera is has 10 pixels per game unit, and is looking at (-50, -50) pixels
        assertEquals(150, p.getConvertedX(-50, 10));
        assertEquals(250, p.getConvertedY(-50, 10));
    }
}
