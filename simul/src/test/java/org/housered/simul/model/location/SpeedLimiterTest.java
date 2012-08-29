package org.housered.simul.model.location;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SpeedLimiterTest
{
    @Test
    public void shouldLimitSpeed()
    {
        SpeedLimiter limiter = new SpeedLimiter();
        limiter.setSpeedLimit(10);
        limiter.startNewTick(1);

        Vector actual = limiter.incrementPosition(new Vector(13, 13));

        assertEquals(7.071, actual.getX(), 0.0001);
        assertEquals(7.071, actual.getY(), 0.0001);
    }

    @Test
    public void shouldLimitSpeedFromMultipleCalls()
    {
        SpeedLimiter limiter = new SpeedLimiter();
        limiter.setSpeedLimit(10);
        limiter.startNewTick(1);

        Vector start = limiter.incrementPosition(new Vector(5, 5));
        assertEquals(5, start.x, 0.0001);
        assertEquals(5, start.y, 0.0001);

        Vector actual = limiter.incrementPosition(new Vector(5, 5));

        assertEquals(2.071, actual.getX(), 0.0001);
        assertEquals(2.071, actual.getY(), 0.0001);
    }

    @Test
    public void shouldLimitSpeedTakingIntoAccountDt()
    {
        SpeedLimiter limiter = new SpeedLimiter();
        limiter.setSpeedLimit(10);
        limiter.startNewTick(0.1f);

        Vector actual = limiter.incrementPosition(new Vector(13, 13));

        assertEquals(0.7071, actual.getX(), 0.0001);
        assertEquals(0.7071, actual.getY(), 0.0001);
    }
}
