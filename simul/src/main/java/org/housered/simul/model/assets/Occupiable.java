package org.housered.simul.model.assets;

import org.housered.simul.model.location.Locatable;
import org.housered.simul.model.world.Identifiable;

/**
 * A building or area of some sort that we can be in. This could include our home, work, a park, a
 * certain bench, but would not include random bits of the road etc.
 */
public interface Occupiable extends Identifiable, Locatable
{

}