package org.housered.simul.view;

import java.awt.Color;

import org.housered.simul.model.location.Vector;

public interface GraphicsAdapter
{
    void setColour(Color colour);

    /**
     * Render a filled rectangle at the given world position, with the passed dimensions. The
     * position is the top left.
     */
    void fillRect(Vector position, Vector dimension);
    
    void drawRect(Vector position, Vector dimension);
    
    void fillCircle(Vector position, float radius);
    
    int getScreenWidth();
    
    int getScreenHeight();
    
    void drawAbsoluteRect(int x, int y, int width, int height);
    
    void drawAbsoluteText(int x, int y, String text);
}
