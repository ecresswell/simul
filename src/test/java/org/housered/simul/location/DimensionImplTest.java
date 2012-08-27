package org.housered.simul.location;

import static org.junit.Assert.assertEquals;

import org.housered.simul.model.location.Dimension;
import org.junit.Test;

public class DimensionImplTest
{
    @Test
    public void shouldConvertWorldPositionIntoRenderablePosition()
    {
        Dimension d = new Dimension(55, 50);

        //10 pixels per world unit
        assertEquals(550, d.getConvertedWidth(10));
        assertEquals(500, d.getConvertedHeight(10));
    }
}
