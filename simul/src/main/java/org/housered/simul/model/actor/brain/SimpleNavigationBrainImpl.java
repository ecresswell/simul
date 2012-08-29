package org.housered.simul.model.actor.brain;

import org.housered.simul.model.location.Vector;

public class SimpleNavigationBrainImpl implements NavigationBrain
{
    private Vector currentPosition;
    private Vector target;
    
    public SimpleNavigationBrainImpl()
    {
        currentPosition = new Vector();
    }
    
    @Override
    public void setTarget(Vector target)
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
    public Vector getNextPoint()
    {
        return target;
    }
    
    @Override
    public Vector getPosition()
    {
        return currentPosition;
    }

}
