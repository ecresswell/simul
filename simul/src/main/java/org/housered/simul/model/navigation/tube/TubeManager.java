package org.housered.simul.model.navigation.tube;

import java.util.ArrayList;
import java.util.List;

import org.housered.simul.model.location.Vector;
import org.housered.simul.model.world.Tickable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Functions as the TFL, advising people how to get places.
 * @author Ed
 */
public class TubeManager implements Tickable
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TubeManager.class);
    private final List<TubeLine> lines = new ArrayList<TubeLine>();

    public void addTubeLine(TubeLine tubeLine)
    {
        LOGGER.debug("Added tube line: {}", tubeLine);
        lines.add(tubeLine);
    }

    public TubeStation getClosestTubeStation(Vector point)
    {
        TubeStation closest = null;
        double minDistance = 0;

        for (TubeLine line : lines)
        {
            for (TubeStation station : line.getStations())
            {
                double distance = station.getEntryPoint().distance(point);
                
                if (closest == null || distance < minDistance)
                {
                    closest = station;
                    minDistance = distance;
                }
            }
        }

        return closest;
    }

    @Override
    public void tick(float dt)
    {
        for (TubeLine line : lines)
            line.tick(dt);
    }
}
