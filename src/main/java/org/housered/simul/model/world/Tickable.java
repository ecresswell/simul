package org.housered.simul.model.world;

public interface Tickable
{
    /**
     * Time passed in seconds.
     */
    void tick(float dt);
}
