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
        drawRect(position, dimension, true);
    }

    @Override
    public void drawRect(Position position, Dimension dimension)
    {
        drawRect(position, dimension, false);
    }
    
    @Override
    public void fillCircle(Position position, float radius)
    {
        int x = position.getConvertedX(offsetX, unitsPerWorldUnit);
        int y = position.getConvertedY(offsetY, unitsPerWorldUnit);
        int convertedRadius = Math.round(radius * unitsPerWorldUnit);
        g.fillOval(x, y, convertedRadius, convertedRadius);
    }

    private void drawRect(Position position, Dimension size, boolean filled)
    {
        int x = position.getConvertedX(offsetX, unitsPerWorldUnit);
        int y = position.getConvertedY(offsetY, unitsPerWorldUnit);
        int width = size.getConvertedWidth(unitsPerWorldUnit);
        int height = size.getConvertedHeight(unitsPerWorldUnit);

        if (filled)
            g.fillRect(x, y, width, height);
        else
            g.drawRect(x, y, width, height);
    }
}
