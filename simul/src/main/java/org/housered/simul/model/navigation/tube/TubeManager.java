package org.housered.simul.model.navigation.tube;

import java.util.ArrayList;
import java.util.List;

import org.housered.simul.model.location.Vector;
import org.housered.simul.model.world.Tickable;

/**
 * Functions as the TFL, advising people how to get places.
 * @author Ed
 */
public class TubeManager implements Tickable
{
    private final List<TubeLine> lines = new ArrayList<TubeLine>();

    public void addTubeLine(TubeLine tubeLine)
    {

    }

    public TubeStation getClosestTubeStation(Vector currentPosition, Vector target)
    {
        return null;
    }

    @Override
    public void tick(float dt)
    {
        for (TubeLine line : lines)
            line.tick(dt);
    }
}
