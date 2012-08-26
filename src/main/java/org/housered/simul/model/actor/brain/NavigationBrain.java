package org.housered.simul.model.actor.brain;

import org.housered.simul.model.location.Position;

public interface NavigationBrain
{
    void setTarget(Position position);

    /**
     * Returns the location to walk to next, i.e. direction and magnitude. Multi-node paths are
     * handled by this brain.
     * @param currentPosition the current position of the actor, as we do not know
     */
    Position getNextPoint(Position currentPosition);
}
