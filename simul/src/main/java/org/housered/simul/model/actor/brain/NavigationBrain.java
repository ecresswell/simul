package org.housered.simul.model.actor.brain;

import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.OldNavigationOrder;

public interface NavigationBrain
{
    void setTarget(OldNavigationOrder order);

    boolean hasTarget();

    /**
     * Returns the location to walk to next, i.e. direction and magnitude. Multi-node paths are
     * handled by this brain.
     */
    Vector getNextPoint();

    boolean hasArrivedAtTarget();
}
