package org.housered.simul.model.actor.brain;

import straightedge.geom.KPoint;

public class SimpleNavigationBrainImpl implements NavigationBrain
{
    private KPoint currentPosition;
    private KPoint target;
    
    public SimpleNavigationBrainImpl()
    {
        currentPosition = new KPoint();
    }
    
    @Override
    public void setTarget(KPoint target)
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
    public KPoint getNextPoint()
    {
        return target;
    }
    
    @Override
    public KPoint getPosition()
    {
        return currentPosition;
    }

}
