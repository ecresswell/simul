package org.housered.simul.model.navigation;

import java.awt.Color;

import org.housered.simul.model.location.Locatable;
import org.housered.simul.model.location.Vector;
import org.housered.simul.view.GraphicsAdapter;
import org.housered.simul.view.Renderable;

public class BoundingBox implements Locatable, Renderable
{
    private Vector position;
    private Vector size;
    
    public BoundingBox(Vector position, Vector size)
    {
        this.position = position;
        this.size = size;
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
}
