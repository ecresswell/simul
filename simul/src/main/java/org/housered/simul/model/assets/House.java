package org.housered.simul.model.assets;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.Collidable;
import org.housered.simul.model.world.Identifiable;
import org.housered.simul.model.world.Tickable;
import org.housered.simul.view.GraphicsAdapter;
import org.housered.simul.view.Renderable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class House implements Identifiable, Renderable, Tickable, Occupiable, Collidable
{
    private static final Logger LOGGER = LoggerFactory.getLogger(House.class);
    private final long id;
    private final Set<Occupant> occupants = new HashSet<Occupant>();
    private Vector position = new Vector(50, 50);
    private Vector size = new Vector(20, 20);

    public House(long id)
    {
        this.id = id;
    }

    @Override
    public void tick(float dt)
    {
        //tick over for costs or something
    }

    @Override
    public void render(GraphicsAdapter r)
    {
        r.setColour(Color.RED);
        r.drawRect(position, size);
    }

    @Override
    public long getId()
    {
        return id;
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
    public String toString()
    {
        return "House [id=" + id + ", position=" + position + ", size=" + size + "]";
    }

    @Override
    public Vector getEntryPoint()
    {
        return getPosition().translateCopy(-1, -1);
    }

    @Override
    public Vector getExitPoint()
    {
        return getPosition().translateCopy(-1, -1);
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
}
