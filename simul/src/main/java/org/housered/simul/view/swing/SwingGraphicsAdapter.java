package org.housered.simul.view.swing;

import java.awt.Color;
import java.awt.Graphics2D;

import org.housered.simul.model.location.Vector;
import org.housered.simul.model.world.Camera;
import org.housered.simul.view.GraphicsAdapter;

public class SwingGraphicsAdapter implements GraphicsAdapter
{
    private final Graphics2D g;
    private final Camera camera;

    public SwingGraphicsAdapter(Graphics2D g, Camera camera)
    {
        this.g = g;
        this.camera = camera;
    }

    @Override
    public void setColour(Color colour)
    {
        g.setColor(colour);
    }

    @Override
    public void fillRect(Vector position, Vector dimension)
    {
        drawRect(position, dimension, true);
    }

    @Override
    public void drawRect(Vector position, Vector dimension)
    {
        drawRect(position, dimension, false);
    }

    @Override
    public void fillCircle(Vector position, float radius)
    {
        IntVector p = new IntVector(camera.translateIntoScreenSpace(position));
        int r = (int) Math.round(camera.scaleIntoScreenSpace(radius));
        g.fillOval(p.x, p.y, r, r);
    }

    private void drawRect(Vector position, Vector size, boolean filled)
    {
        IntVector p = new IntVector(camera.translateIntoScreenSpace(position));
        IntVector b = new IntVector(camera.scaleIntoScreenSpace(size));

        if (filled)
            g.fillRect(p.x, p.y, b.x, b.y);
        else
            g.drawRect(p.x, p.y, b.x, b.y);
    }

    private static class IntVector
    {
        private final int x;
        private final int y;

        public IntVector(Vector Vector)
        {
            this.x = (int) Math.round(Vector.x);
            this.y = (int) Math.round(Vector.y);
        }
    }
}
