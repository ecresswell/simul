package org.housered.simul.actor;

import java.awt.Color;

import org.housered.simul.location.DimensionImpl;
import org.housered.simul.location.Locatable;
import org.housered.simul.location.Position;
import org.housered.simul.location.PositionImpl;
import org.housered.simul.render.RenderStrategy;
import org.housered.simul.render.Renderable;
import org.housered.simul.world.Identifiable;

public class Person implements Locatable, Identifiable, Renderable
{
    private Mood mood;

    @Override
    public Position getCurrentPosition()
    {
        return null;
    }

    @Override
    public long getId()
    {
        return 0;
    }

    @Override
    public void render(RenderStrategy r)
    {
        r.setColour(Color.RED);
        r.fillRect(new PositionImpl(0, 0), new DimensionImpl(10, 10));
    }
}
