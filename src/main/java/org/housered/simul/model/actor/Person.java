package org.housered.simul.model.actor;

import java.awt.Color;

import org.housered.simul.model.location.DimensionImpl;
import org.housered.simul.model.location.Locatable;
import org.housered.simul.model.location.Position;
import org.housered.simul.model.location.PositionImpl;
import org.housered.simul.model.world.Identifiable;
import org.housered.simul.model.world.Tickable;
import org.housered.simul.view.GraphicsAdapter;
import org.housered.simul.view.Renderable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Person implements Locatable, Identifiable, Renderable, Tickable
{
    private static Logger LOGGER = LoggerFactory.getLogger(Person.class);
    private Mood mood = new Mood();
    private int direction = 1;

    @Override
    public Position getCurrentPosition()
    {
        return null;
    }

    @Override
    public long getId()
    {
        return 0;
    }

    @Override
    public void render(GraphicsAdapter r)
    {
        int happyColour = Math.round(mood.getHappiness() * 255);
        LOGGER.debug("{},{},{}", new Object[] {happyColour, 255 - happyColour, 122});
        r.setColour(new Color(happyColour, 255 - happyColour, 122));
        r.fillRect(new PositionImpl(0, 0), new DimensionImpl(10, 10));
    }

    @Override
    public void tick(float dt)
    {
        if (mood.getHappiness() >= 1)
            direction = -1;
        else if (mood.getHappiness() <= 0)
            direction = 1;

        mood.setHappiness(mood.getHappiness() + 0.2f * dt * direction);
    }
}
