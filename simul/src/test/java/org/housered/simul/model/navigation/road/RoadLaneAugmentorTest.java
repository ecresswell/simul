package org.housered.simul.model.navigation.road;

import static org.housered.simul.model.location.Vector.v;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.housered.simul.model.location.Vector;
import org.junit.Test;

public class RoadLaneAugmentorTest
{
    @Test
    public void shouldReturnNullIfPointIsNotOnARoad()
    {
        RoadLaneAugmentor augmentor = constructWith(new Road(v(10, 10), v(30, 10)));
        assertNull(augmentor.getRoadThatPointIsOn(Vector.v(9, 9)));
    }

    @Test
    public void shouldReturnRoadThatPointIsOn()
    {
        Road r1 = new Road(new Vector(50, 50), new Vector(10, 50));
        Road r2 = new Road(new Vector(-20, -30), new Vector(10, 20));

        RoadLaneAugmentor augmentor = constructWith(r1, r2);

        assertEquals(r1, augmentor.getRoadThatPointIsOn(v(50, 50)));
        assertEquals(r1, augmentor.getRoadThatPointIsOn(v(60, 50)));
        assertEquals(r1, augmentor.getRoadThatPointIsOn(v(55.21314, 50)));
        assertEquals(r2, augmentor.getRoadThatPointIsOn(v(-20, -25)));
        assertEquals(r2, augmentor.getRoadThatPointIsOn(v(-10, -10)));
    }

    static RoadLaneAugmentor constructWith(Road... roads)
    {
        return new RoadLaneAugmentor(Arrays.asList(roads));
    }
}
