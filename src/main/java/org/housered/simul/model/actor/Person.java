package org.housered.simul.model.actor;

import java.awt.Color;

import org.housered.simul.model.actor.brain.HighLevelBrain;
import org.housered.simul.model.actor.brain.NavigationBrain;
import org.housered.simul.model.assets.Occupiable;
import org.housered.simul.model.location.DimensionImpl;
import org.housered.simul.model.location.Locatable;
import org.housered.simul.model.location.Position;
import org.housered.simul.model.location.PositionImpl;
import org.housered.simul.model.location.Vector;
import org.housered.simul.model.world.Identifiable;
import org.housered.simul.model.world.Tickable;
import org.housered.simul.view.GraphicsAdapter;
import org.housered.simul.view.Renderable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Person implements Locatable, Identifiable, Renderable, Tickable
{
    private static Logger LOGGER = LoggerFactory.getLogger(Person.class);
    private final long id;

    private HighLevelBrain highLevel;
    private NavigationBrain navigation;

    public Person(long id)
    {
        this.id = id;
    }

    @Override
    public Position getPosition()
    {
        return null;
    }

    @Override
    public long getId()
    {
        return id;
    }

    @Override
    public void render(GraphicsAdapter r)
    {
        r.setColour(Color.GREEN);
        r.fillRect(new PositionImpl(0, 0), new DimensionImpl(10, 10));
    }

    @Override
    public void tick(float dt)
    {
        Occupiable target = highLevel.decideWhereToGo();

        if (target != null)
        {
            navigation.setTarget(target.getPosition());
            Position targetPoint = navigation.getNextPoint(getPosition());
            incrementPosition(targetPoint.subtract(getPosition()), dt);
        }
    }

    private void incrementPosition(Vector delta, float dt)
    {

    }
}
