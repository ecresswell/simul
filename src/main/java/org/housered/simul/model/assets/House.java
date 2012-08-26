package org.housered.simul.model.assets;

import java.awt.Color;

import org.housered.simul.model.location.Dimension;
import org.housered.simul.model.location.DimensionImpl;
import org.housered.simul.model.location.Position;
import org.housered.simul.model.location.PositionImpl;
import org.housered.simul.model.world.Identifiable;
import org.housered.simul.model.world.Tickable;
import org.housered.simul.view.GraphicsAdapter;
import org.housered.simul.view.Renderable;

public class House implements Identifiable, Renderable, Tickable
{
    private final long id;
    private Position position = new PositionImpl();
    private Dimension size = new DimensionImpl();
    
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

}
