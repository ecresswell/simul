package org.housered.simul.model.actor.brain;

import org.housered.simul.model.location.Position;

public interface NavigationBrain
{
    void setTarget(Position position);
    
    boolean hasTarget();

    /**
     * Returns the location to walk to next, i.e. direction and magnitude. Multi-node paths are
     * handled by this brain.
     */
    Position getNextPoint();
    
    Position getPosition();
}
