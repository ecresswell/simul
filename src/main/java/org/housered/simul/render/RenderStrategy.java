package org.housered.simul.render;

import java.awt.Color;

import org.housered.simul.location.Dimension;
import org.housered.simul.location.Position;

public interface RenderStrategy
{
    void setColour(Color colour);

    /**
     * Render a filled rectangle at the given world position, with the passed dimensions. The
     * position is the centre of the object.
     */
    void fillRect(Position position, Dimension dimension);
}
