package org.housered.simul.location;

import static org.junit.Assert.assertEquals;

import org.housered.simul.model.location.Position;
import org.junit.Test;

public class PositionImplTest
{
    @Test
    public void shouldConvertWorldPositionIntoRenderablePosition()
    {
        Position p = new Position(10, 20);

        //but camera has 10 pixels per game unit, and is looking at (-50, -50) (game unit)
        assertEquals(600, p.getConvertedX(-50, 10));
        assertEquals(700, p.getConvertedY(-50, 10));
    }
}
