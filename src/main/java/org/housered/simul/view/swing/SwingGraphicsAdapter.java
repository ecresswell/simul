package org.housered.simul.view.swing;

import java.awt.Color;
import java.awt.Graphics2D;

import org.housered.simul.model.location.Dimension;
import org.housered.simul.model.location.Position;
import org.housered.simul.view.GraphicsAdapter;

public class SwingGraphicsAdapter implements GraphicsAdapter
{
    private final Graphics2D g;
    private final int offsetX;
    private final int offsetY;
    private final float unitsPerWorldUnit;

    public SwingGraphicsAdapter(Graphics2D g, int offsetX, int offsetY, float unitsPerWorldUnit)
    {
        this.g = g;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.unitsPerWorldUnit = unitsPerWorldUnit;
    }

    @Override
    public void setColour(Color colour)
    {
        g.setColor(colour);
    }
    
    @Override
    public void fillRect(Position position, Dimension dimension)
    {
        int x = position.getConvertedX(offsetX, unitsPerWorldUnit);
        int y = position.getConvertedY(offsetY, unitsPerWorldUnit);
        int width = dimension.getConvertedWidth(unitsPerWorldUnit);
        int height = dimension.getConvertedHeight(unitsPerWorldUnit);
        g.fillRect(x, y, width, height);
    }
}
