package org.housered.simul.model.work;

import java.awt.Color;

import org.housered.simul.model.assets.AbstractOccupiable;
import org.housered.simul.model.location.Vector;
import org.housered.simul.model.world.Tickable;
import org.housered.simul.view.GraphicsAdapter;


public class Workplace extends AbstractOccupiable implements Tickable, CommercialBuilding
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
    }

    @Override
    public Vector getEntryPoint()
    {
        return position.translateCopy(-1, -1);
    }

    @Override
    public Vector getExitPoint()
    {
        return position.translateCopy(-1, -1);
    }

    @Override
    public void tick(float dt)
    {
        // TODO Auto-generated method stub
    }

}
