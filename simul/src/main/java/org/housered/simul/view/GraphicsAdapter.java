package org.housered.simul.view;

import java.awt.Color;

import straightedge.geom.KPoint;

public interface GraphicsAdapter
{
    void setColour(Color colour);

    /**
     * Render a filled rectangle at the given world position, with the passed dimensions. The
     * position is the top left.
     */
    void fillRect(KPoint position, KPoint dimension);
    
    void drawRect(KPoint position, KPoint dimension);
    
    void fillCircle(KPoint position, float radius);
}
