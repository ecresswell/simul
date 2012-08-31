package org.housered.simul.model.assets;

import java.util.HashSet;
import java.util.Set;

import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.Collidable;
import org.housered.simul.model.world.Identifiable;
import org.housered.simul.view.Renderable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractOccupiable implements Renderable, Identifiable, Occupiable, Collidable
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractOccupiable.class);

    protected final long id;
    protected final Vector position;
    protected final Vector size;
    protected final Set<Occupant> occupants = new HashSet<Occupant>();

    protected AbstractOccupiable(long id, Vector position, Vector size)
    {
        this.id = id;
        this.position = position;
        this.size = size;
    }

    @Override
    public Vector getPosition()
    {
        return position;
    }

    @Override
    public Vector getBounds()
    {
        return size;
    }

    @Override
    public boolean isColliding()
    {
        return true;
    }

    @Override
    public void occupy(Occupant occupant)
    {
        boolean newOccupant = occupants.add(occupant);

        if (!newOccupant)
            LOGGER.warn("{} already contains {}", this, occupant);
    }

    @Override
    public void exit(Occupant occupant)
    {
        boolean occupantExists = occupants.remove(occupant);

        if (!occupantExists)
            LOGGER.warn("{} did not contain {}", this, occupant);

    }

    @Override
    public long getId()
    {
        return id;
    }

}
