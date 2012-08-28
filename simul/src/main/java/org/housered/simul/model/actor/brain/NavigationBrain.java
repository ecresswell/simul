package org.housered.simul.model.actor.brain;

import straightedge.geom.KPoint;

public interface NavigationBrain
{
    void setTarget(KPoint position);
    
    boolean hasTarget();

    /**
     * Returns the location to walk to next, i.e. direction and magnitude. Multi-node paths are
     * handled by this brain.
     */
    KPoint getNextPoint();
    
    KPoint getPosition();
}
