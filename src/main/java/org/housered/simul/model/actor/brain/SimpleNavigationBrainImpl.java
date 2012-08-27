package org.housered.simul.model.actor.brain;

import org.housered.simul.model.location.Position;

public class SimpleNavigationBrainImpl implements NavigationBrain
{
    private Position currentPosition;
    private Position target;
    
    public SimpleNavigationBrainImpl()
    {
        currentPosition = new Position();
    }
    
    @Override
    public void setTarget(Position target)
    {
        this.target = target;
    }

    @Override
    public boolean hasTarget()
    {
        if (currentPosition.equals(target))
            target = null;
        
        return target != null;
    }

    @Override
    public Position getNextPoint()
    {
        return target;
    }
    
    @Override
    public Position getPosition()
    {
        return currentPosition;
    }

}
