package org.housered.simul.model.assets;

import java.awt.Color;

import org.housered.simul.model.location.Vector;
import org.housered.simul.model.world.Tickable;
import org.housered.simul.view.GraphicsAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class House extends AbstractOccupiable implements Tickable
{
    private static final Logger LOGGER = LoggerFactory.getLogger(House.class);

    public House(long id, Vector position, Vector size)
    {
        super(id, position, size);
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
        r.setColour(Color.GREEN);
        r.drawText(position, 100, String.valueOf(occupants.size()));
    }

    @Override
    public byte getZOrder()
    {
        return BUILDING_Z_ORDER;
    }

    @Override
    public String toString()
    {
        return "House [id=" + id + ", position=" + position + ", size=" + size + "]";
    }

    @Override
    public Vector getEntryPoint()
    {
        return getPosition().translateCopy(size.x / 2 + 1, size.y + 1);
    }

    @Override
    public Vector getExitPoint()
    {
        return getPosition().translateCopy(size.x / 2 + 1, size.y + 1);
    }
}
