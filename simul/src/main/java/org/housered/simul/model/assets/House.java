package org.housered.simul.model.assets;

import java.awt.Color;

import org.housered.simul.model.navigation.Collidable;
import org.housered.simul.model.world.Identifiable;
import org.housered.simul.model.world.Tickable;
import org.housered.simul.view.GraphicsAdapter;
import org.housered.simul.view.Renderable;

import straightedge.geom.KPoint;

public class House implements Identifiable, Renderable, Tickable, Occupiable, Collidable
{
    private final long id;
    private KPoint position = new KPoint(50, 50);
    private KPoint size = new KPoint(20, 20);
    
    public House(long id)
    {
        this.id = id;
    }

    @Override
    public void tick(float dt)
    {
        //tick over for costs or something
    }

    @Override
    public void render(GraphicsAdapter r)
    {
        r.setColour(Color.RED);
        r.drawRect(position, size);
    }

    @Override
    public long getId()
    {
        return id;
    }

    @Override
    public KPoint getPosition()
    {
        return position;
    }

    @Override
    public KPoint getBounds()
    {
        return size;
    }

    @Override
    public boolean isColliding()
    {
        return true;
    }

}
