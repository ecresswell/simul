package org.housered.simul.model.navigation.tube;

import java.awt.Color;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.housered.simul.model.world.GameClock;
import org.housered.simul.model.world.Tickable;
import org.housered.simul.view.GraphicsAdapter;
import org.housered.simul.view.Renderable;

public class TubeLine implements Renderable, Tickable
{
    private static final long WAIT_TIME = 1;
    private final GameClock gameClock;
    private final List<TubeStation> stations;
    private final List<Tube> tubes = new LinkedList<Tube>();
    private double tubeMaxSpeed = 5;

    protected TubeLine(List<TubeStation> stations, GameClock gameClock)
    {
        this.gameClock = gameClock;
        this.stations = Collections.unmodifiableList(stations);
    }

    public List<TubeStation> getStations()
    {
        return stations;
    }

    public List<Tube> getTubes()
    {
        return tubes;
    }

    public void addTube(TubeStation station)
    {
        if (!stations.contains(station))
            throw new IllegalArgumentException("Can't add a tube to this station");

        Tube newTube = new Tube(station, this, gameClock);
        newTube.goTowardsTubeStation(getNextStationForTube(newTube, station));
        tubes.add(newTube);
    }

    void arrivedAtStation(Tube tube, TubeStation station)
    {
        TubeStation nextStation = getNextStationForTube(tube, station);
        tube.waitAndOpenDoors(WAIT_TIME);
        tube.goTowardsTubeStation(nextStation);
        station.tubeHasArrived(tube);
    }

    TubeStation getNextStationForTube(Tube tube, TubeStation currentStation)
    {
        int index = stations.indexOf(currentStation);
        int next = index == stations.size() - 1 ? 0 : index + 1;

        return stations.get(next);
    }

    @Override
    public void tick(float dt)
    {
        for (Tube tube : tubes)
            tube.tick(dt);
    }

    @Override
    public void render(GraphicsAdapter r)
    {
        r.setColour(Color.pink);

        for (int i = 0; i < stations.size() - 1; i++)
        {
            r.drawLine(stations.get(i).getPosition(), stations.get(i + 1).getPosition());
        }
        r.drawLine(stations.get(0).getPosition(), stations.get(stations.size() - 1).getPosition());
    }

    @Override
    public byte getZOrder()
    {
        return PERSON_Z_ORDER;
    }

    public double getTubeSpeed()
    {
        return tubeMaxSpeed;
    }

}
