package org.housered.simul.model.actor;

import java.awt.Color;

import org.housered.simul.model.location.DimensionImpl;
import org.housered.simul.model.location.Locatable;
import org.housered.simul.model.location.Position;
import org.housered.simul.model.location.PositionImpl;
import org.housered.simul.model.world.Identifiable;
import org.housered.simul.model.world.Tickable;
import org.housered.simul.view.GraphicsAdapter;
import org.housered.simul.view.Renderable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Person implements Locatable, Identifiable, Renderable, Tickable
{
    private static Logger LOGGER = LoggerFactory.getLogger(Person.class);
    private final long id;
    private Mood mood = new Mood();

    public Person(long id)
    {
        this.id = id;
    }

    @Override
    public Position getCurrentPosition()
    {
        return null;
    }

    @Override
    public long getId()
    {
        return id;
    }

    @Override
    public void render(GraphicsAdapter r)
    {
        r.setColour(Color.GREEN);
        r.fillRect(new PositionImpl(0, 0), new DimensionImpl(10, 10));
    }

    @Override
    public void tick(float dt)
    {
    }
}
