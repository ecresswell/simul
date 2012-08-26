package org.housered.simul.model.world;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.housered.simul.model.actor.Person;
import org.housered.simul.model.assets.AssetManager;
import org.housered.simul.model.assets.AssetManagerImpl;
import org.housered.simul.model.assets.House;
import org.housered.simul.view.Renderable;
import org.housered.simul.view.RenderableProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class World implements RenderableProvider, Tickable
{
    private static Logger LOGGER = LoggerFactory.getLogger(World.class);

    private AtomicLong nextId = new AtomicLong();
    private List<Renderable> renderables = new LinkedList<Renderable>();
    private List<Tickable> tickables = new LinkedList<Tickable>();

    private AssetManager assetManager = new AssetManagerImpl();
    private GameClockImpl gameClock;

    public void loadLevel()
    {
        gameClock = new GameClockImpl(TimeUnit.HOURS.toSeconds(7), 30);
        tickables.add(gameClock);

        addEntity(new Person(getNextId()));
        addEntity(new House(getNextId()));
    }

    private void addEntity(Identifiable entity)
    {
        LOGGER.debug("Add entity with id {} - {}", entity.getId(), entity);

        if (entity instanceof Renderable)
            renderables.add((Renderable) entity);
        if (entity instanceof Tickable)
            tickables.add((Tickable) entity);
        if (entity instanceof Ownable)
            assetManager.addOwnable((Ownable) entity);

        assetManager.addIdentifiable(entity);
    }

    @Override
    public void beginRender()
    {
    }

    @Override
    public Iterable<Renderable> getRenderables()
    {
        return renderables;
    }

    @Override
    public void endRender()
    {
    }

    @Override
    public void tick(float dt)
    {
        for (Tickable tickable : tickables)
        {
            tickable.tick(dt);
        }
    }

    private long getNextId()
    {
        return nextId.getAndIncrement();
    }
}
