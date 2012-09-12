package org.housered.simul.model.navigation.tube;

import java.util.Collections;
import java.util.List;

import org.housered.simul.view.GraphicsAdapter;
import org.housered.simul.view.Renderable;

public class TubeLine implements Renderable
{
    private final List<TubeStation> stations;

    protected TubeLine(List<TubeStation> stations)
    {
        this.stations = Collections.unmodifiableList(stations);
    }

    public List<TubeStation> getStations()
    {
        return stations;
    }

    @Override
    public void render(GraphicsAdapter r)
    {
    }

    @Override
    public byte getZOrder()
    {
        return PERSON_Z_ORDER;
    }
}
