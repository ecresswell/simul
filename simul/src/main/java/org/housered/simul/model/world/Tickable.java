package org.housered.simul.model.world;

public interface Tickable extends GameObject
{
    /**
     * Time passed in seconds.
     */
    void tick(float dt);
}
