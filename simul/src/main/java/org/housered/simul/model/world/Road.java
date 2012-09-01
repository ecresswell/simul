package org.housered.simul.model.world;

import java.awt.Color;

import org.housered.simul.model.location.BoundingBox;
import org.housered.simul.model.location.Vector;
import org.housered.simul.view.GraphicsAdapter;
import org.housered.simul.view.Renderable;

public class Road implements Renderable, BoundingBox
{
    private Vector position;
    private Vector size;
    
    @Override
    public void render(GraphicsAdapter r)
    {
        r.setColour(Color.gray);
        r.fillRect(position, size);
    }
    
    @Override
    public byte getZOrder()
    {
        return ROAD_Z_ORDER;
    }

    @Override
    public Vector getPosition()
    {
        return position;
    }

    @Override
    public Vector getSize()
    {
        return size;
    }

}
