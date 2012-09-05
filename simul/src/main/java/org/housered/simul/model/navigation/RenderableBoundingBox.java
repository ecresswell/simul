package org.housered.simul.model.navigation;

import java.awt.Color;

import org.housered.simul.model.location.BoundingBox;
import org.housered.simul.model.location.Vector;
import org.housered.simul.model.world.GameObject;
import org.housered.simul.view.GraphicsAdapter;
import org.housered.simul.view.Renderable;

public class RenderableBoundingBox implements Renderable, BoundingBox, GameObject
{
    private Vector position;
    private Vector size;

    public RenderableBoundingBox(Vector position, Vector size)
    {
        this.position = position;
        this.size = size;
    }
    
    @Override
    public byte getZOrder()
    {
        return BUILDING_Z_ORDER;
    }

    @Override
    public Vector getPosition()
    {
        return position;
    }

    @Override
    public void render(GraphicsAdapter r)
    {
        r.setColour(Color.red);
        r.drawRect(position, size);
    }

    @Override
    public Vector getSize()
    {
        return size;
    }
}
