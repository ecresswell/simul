package org.housered.simul.model.navigation;

import java.awt.Color;

import org.housered.simul.model.location.Locatable;
import org.housered.simul.view.GraphicsAdapter;
import org.housered.simul.view.Renderable;

import straightedge.geom.KPoint;

public class BoundingBox implements Locatable, Renderable
{
    private KPoint position;
    private KPoint size;
    
    public BoundingBox(KPoint position, KPoint size)
    {
        this.position = position;
        this.size = size;
    }
    
    @Override
    public KPoint getPosition()
    {
        return position;
    }

    @Override
    public void render(GraphicsAdapter r)
    {
        r.setColour(Color.red);
        r.drawRect(position, size);        
    }
}
