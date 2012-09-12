package org.housered.simul.model.navigation.tube;

import java.awt.Color;

import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.Collidable;
import org.housered.simul.view.GraphicsAdapter;
import org.housered.simul.view.Renderable;

public class TubeStation implements Renderable, Collidable
{
    private final Vector position;
    private final Vector size;

    public TubeStation(Vector position, Vector size)
    {
        this.position = position;
        this.size = size;
    }

    @Override
    public void render(GraphicsAdapter r)
    {
        r.setColour(Color.MAGENTA);
        r.drawRect(position, size);
    }

    @Override
    public Vector getSize()
    {
        return size;
    }

    @Override
    public Vector getPosition()
    {
        return position;
    }

    @Override
    public boolean isColliding()
    {
        return true;
    }

    @Override
    public byte getZOrder()
    {
        return BUILDING_Z_ORDER;
    }

    @Override
    public String toString()
    {
        return "TubeStation [position=" + position + ", size=" + size + "]";
    }

}
