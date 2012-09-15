package org.housered.simul.model.navigation.tube;

import static org.housered.simul.model.location.Vector.v;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TubeManagerTest
{
    @Test
    public void shouldReturnClosestTubeStationBasedOnStationMidPoint()
    {
        TubeStation t1 = new TubeStation(v(10, 10), v(20, 20));
        TubeStation t2 = new TubeStation(v(10, 40), v(20, 20));
        TubeLine line = new TubeLineBuilder(null).addTubeStation(t1).addTubeStation(t2).buildLine();

        TubeManager tubeManager = new TubeManager();
        tubeManager.addTubeLine(line);

        assertEquals(t1, tubeManager.getClosestTubeStation(v(9, 9)));
        assertEquals(t1, tubeManager.getClosestTubeStation(v(12, 12)));
        assertEquals(t2, tubeManager.getClosestTubeStation(v(12, 45)));
        assertEquals(t2, tubeManager.getClosestTubeStation(v(10, 35.000001)));
        assertEquals(t1, tubeManager.getClosestTubeStation(v(10, 34.999999)));
    }
}
