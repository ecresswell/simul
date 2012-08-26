package org.housered.simul.model.world;

import java.util.LinkedList;
import java.util.List;

import org.housered.simul.model.actor.Person;
import org.housered.simul.view.Renderable;
import org.housered.simul.view.RenderableProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class World implements RenderableProvider, Tickable
{
    private static Logger LOGGER = LoggerFactory.getLogger(World.class);

    private List<Renderable> renderables = new LinkedList<Renderable>();
    private List<Tickable> tickables = new LinkedList<Tickable>();

    public void loadLevel()
    {
        addEntity(new Person());
    }

    private void addEntity(Identifiable entity)
    {
        LOGGER.debug("Add entity with id {} - {}", entity.getId(), entity);
        if (entity instanceof Renderable)
            renderables.add((Renderable) entity);
        if (entity instanceof Tickable)
            tickables.add((Tickable) entity);
    }

    @Override
    public void beginRender()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public Iterable<Renderable> getRenderables()
    {
        return renderables;
    }

    @Override
    public void endRender()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void tick(float dt)
    {
        for (Tickable tickable : tickables)
        {
            tickable.tick(dt);
        }
    }
}
