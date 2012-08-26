package org.housered.simul.model.world;

/**
 * Knows the in game time, can be passed around.
 */
public interface GameClock
{
    /**
     * Returns the number of seconds since midnight.
     */
    int getTime();
}
