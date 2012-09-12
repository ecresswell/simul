package org.housered.simul.model.navigation.tube;

import java.util.ArrayList;
import java.util.List;

import org.housered.simul.model.location.Vector;
import org.housered.simul.model.world.GameClock;

public class TubeLineBuilder
{
    private final GameClock gameClock;
    private List<TubeStation> stations = new ArrayList<TubeStation>();

    public TubeLineBuilder(GameClock gameClock)
    {
        this.gameClock = gameClock;

    }

    public TubeLine buildLine()
    {
        return new TubeLine(stations, gameClock);
    }

    public TubeLineBuilder addTubeStation(TubeStation station)
    {
        stations.add(station);
        return this;
    }

    public TubeLineBuilder addTubeStation(double x, double y, double width, double height)
    {
        stations.add(new TubeStation(new Vector(x, y), new Vector(width, height)));
        return this;
    }
}
