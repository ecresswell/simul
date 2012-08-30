package org.housered.simul.model.world;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GameClockImplTest
{
    @Test
    public void shouldReturnComponentsOfTheTime()
    {
        GameClock g = new GameClockImpl(26730, 0);

        assertEquals(30, g.getSeconds());
        assertEquals(25, g.getMinutes());
        assertEquals(7, g.getHour());
        assertEquals(1, g.getDay());
        assertEquals(26730, g.getSecondsSinceGameStart());
        assertEquals("07:25", g.getDigitalClock());
    }
    
    @Test
    public void shouldRollOverTheClockAtMidnight()
    {
        GameClockImpl g = new GameClockImpl(86399, 1);
        
        assertEquals(59, g.getSeconds());
        assertEquals(59, g.getMinutes());
        assertEquals(23, g.getHour());
        assertEquals(1, g.getDay());
        assertEquals("23:59", g.getDigitalClock());
        assertEquals(86399, g.getSecondsSinceGameStart());
        
        g.tick(3);
        
        assertEquals(2, g.getSeconds());
        assertEquals(0, g.getMinutes());
        assertEquals(0, g.getHour());
        assertEquals(2, g.getDay());
        assertEquals("00:00", g.getDigitalClock());
        assertEquals(86402, g.getSecondsSinceGameStart());
    }
    
    @Test
    public void shouldReturnHumanReadableClock()
    {
        GameClock g = new GameClockImpl(86399, 3);
        
        assertEquals("23:59", g.getDigitalClock());
    }
}
