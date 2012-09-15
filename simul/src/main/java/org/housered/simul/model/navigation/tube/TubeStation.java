package org.housered.simul.model.navigation.tube;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import org.housered.simul.model.assets.Occupant;
import org.housered.simul.model.assets.Occupiable;
import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.Collidable;
import org.housered.simul.view.GraphicsAdapter;
import org.housered.simul.view.Renderable;

public class TubeStation implements Renderable, Collidable, Occupiable
{
    private final List<TubePassengerController> queue = new LinkedList<TubePassengerController>();
    private final List<Tube> tubesInStation = new LinkedList<Tube>();
    private final Vector position;
    private final Vector size;

    public TubeStation(Vector position, Vector size)
    {
        this.position = position;
        this.size = size;
    }

    @Override
    public void render(GraphicsAdapter r)
    {
        r.setColour(Color.MAGENTA);
        r.drawRect(position, size);
        r.setColour(Color.GREEN);
        r.drawText(position.translateCopy(0, size.y * 2), 10000, String.valueOf(queue.size()));
    }

    public void queueForTube(TubePassengerController personQueueing, TubeStation targetStation)
    {
        if (tubesInStation.size() > 0)
        {
            tubesInStation.get(0).putPersonIntoTube(personQueueing);
        }
        else
        {
            queue.add(personQueueing);
        }
    }

    void tubeHasArrived(Tube tube)
    {
        for (TubePassengerController person : queue)
            tube.putPersonIntoTube(person);
        queue.clear();
    }

    @Override
    public Vector getSize()
    {
        return size;
    }

    @Override
    public Vector getPosition()
    {
        return position;
    }

    @Override
    public boolean isColliding()
    {
        return true;
    }

    @Override
    public byte getZOrder()
    {
        return BUILDING_Z_ORDER;
    }

    @Override
    public String toString()
    {
        return "TubeStation [position=" + position + ", size=" + size + "]";
    }

    @Override
    public long getId()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Vector getEntryPoint()
    {
        return getPosition().translateCopy(size.x / 2 + 1, size.y + 1);
    }

    @Override
    public Vector getExitPoint()
    {
        return getPosition().translateCopy(size.x / 2 + 1, size.y + 1);
    }

    @Override
    public void occupy(Occupant occupant)
    {
        if (occupant instanceof Tube)
        {
            tubesInStation.add((Tube) occupant);
        }
    }

    @Override
    public void exit(Occupant occupant)
    {
        if (occupant instanceof Tube)
        {
            tubesInStation.remove(occupant);
        }
    }

}
