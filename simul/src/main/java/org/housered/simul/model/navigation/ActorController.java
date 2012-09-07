package org.housered.simul.model.navigation;

import org.housered.simul.model.world.Tickable;

public interface ActorController extends Tickable
{
    void giveDirectControl();
}
