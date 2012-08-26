package org.housered.simul.model.assets;

import org.housered.simul.model.world.Identifiable;

/**
 * An object that can be owned by someone.
 */
public interface Ownable extends Identifiable
{
    long getOwner();
}
