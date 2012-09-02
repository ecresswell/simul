package org.housered.simul.model.actor.brain;

import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.NavigationOrder;

public interface NavigationBrain
{
    void setTarget(NavigationOrder order);

    boolean hasTarget();

    /**
     * Returns the location to walk to next, i.e. direction and magnitude. Multi-node paths are
     * handled by this brain.
     */
    Vector getNextPoint();

    Vector getPosition();

    boolean hasArrivedAtTarget();
}
