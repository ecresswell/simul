package org.housered.simul.model.assets;

import org.housered.simul.model.location.Locatable;
import org.housered.simul.model.location.Vector;
import org.housered.simul.model.world.Identifiable;

/**
 * A building or area of some sort that we can be in. This could include our home, work, a park, a
 * certain bench, but would not include random bits of the road etc.
 */
public interface Occupiable extends Identifiable, Locatable
{
    /**
     * This should be outside the colliable object, or people won't be able to get there.
     */
    Vector getEntryPoint();

    /**
     * This should also be outside the colliable object, or people won't be able to get there.
     */
    Vector getExitPoint();

    /**
     * Informs this occupiable that someone is now inside them, and relevant things can be applied.
     */
    void occupy(Occupant occupant);

    /**
     * This occupant has now disengaged from this occupiable.
     */
    void exit(Occupant occupant);
}
