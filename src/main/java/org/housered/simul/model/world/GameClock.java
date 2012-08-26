package org.housered.simul.model.world;

/**
 * Knows the in game time, can be passed around.
 */
public interface GameClock
{
    /**
     * Returns the number of hours since midnight.
     */
    int getHour();

    /**
     * Returns the number of minutes past the hour.
     */
    int getMinutes();

    /**
     * Returns the number of seconds past the minute. Is that even a phrase? Who knows.
     */
    int getSeconds();

    /**
     * Returns the number of seconds since midnight.
     */
    int getSecondsSinceMidnight();
}
