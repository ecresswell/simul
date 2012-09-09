package org.housered.simul.model.navigation;

import org.housered.simul.model.location.Locatable;
import org.housered.simul.model.world.Tickable;
import org.housered.simul.view.Renderable;

public interface ActorController extends Tickable, Locatable, Renderable
{
    void giveDirectControl();
    
    void relinquishControl();
}
