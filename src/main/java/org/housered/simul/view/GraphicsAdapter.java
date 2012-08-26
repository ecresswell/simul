package org.housered.simul.view;

import java.awt.Color;

import org.housered.simul.model.location.Dimension;
import org.housered.simul.model.location.Position;

public interface GraphicsAdapter
{
    void setColour(Color colour);

    /**
     * Render a filled rectangle at the given world position, with the passed dimensions. The
     * position is the top left.
     */
    void fillRect(Position position, Dimension dimension);
    
    void drawRect(Position position, Dimension dimension);
}
