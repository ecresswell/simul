package org.housered.simul.world;

public interface Tickable
{
    /**
     * Time passed in seconds.
     */
    void tick(float dt);
}
