package org.housered.simul.model.work;

import java.awt.Color;

import org.housered.simul.model.assets.AbstractOccupiable;
import org.housered.simul.model.location.Vector;
import org.housered.simul.model.world.Tickable;
import org.housered.simul.view.GraphicsAdapter;

public class Workplace extends AbstractOccupiable implements Tickable
{
    public Workplace(long id, Vector position, Vector size)
    {
        super(id, position, size);
    }

    @Override
    public void render(GraphicsAdapter r)
    {
        r.setColour(Color.blue);
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
    public Vector getEntryPoint()
    {
        return getPosition().translateCopy(size.x / 2 + 1, size.y + 1);
    }

    @Override
    public Vector getExitPoint()
    {
        return getPosition().translateCopy(size.x / 2 + 1, size.y + 1);
    }

    @Override
    public void tick(float dt)
    {
        // TODO Auto-generated method stub
    }
}
