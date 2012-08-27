package org.housered.simul.model.navigation;

import java.awt.Color;

import org.housered.simul.model.location.Dimension;
import org.housered.simul.model.location.Locatable;
import org.housered.simul.model.location.Position;
import org.housered.simul.view.GraphicsAdapter;
import org.housered.simul.view.Renderable;

public class BoundingBox implements Locatable, Renderable
{
    private Position position;
    private Dimension size;
    
    public BoundingBox(Position position, Dimension size)
    {
        this.position = position;
        this.size = size;
    }
    
    @Override
    public Position getPosition()
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
