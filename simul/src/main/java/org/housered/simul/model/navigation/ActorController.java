package org.housered.simul.model.navigation;

import org.housered.simul.model.location.Locatable;
import org.housered.simul.model.world.Tickable;

public interface ActorController extends Tickable, Locatable
{
    void giveDirectControl();
    
    void relinquishControl();
}
