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

        Person p1 = new Person(getNextId(), assetManager);
        Person p2 = new Person(getNextId(), assetManager);
        p1.getPosition().setX(50);
        p1.getPosition().setY(50);

        House h1 = new House(getNextId());
        House h2 = new House(getNextId());
        h2.getPosition().setX(100);
        h2.getPosition().setY(100);

        assetManager.createDeed(p1, h1);
        assetManager.createDeed(p2, h2);

        addEntity(p1);
        addEntity(p2);
        addEntity(h1);
        addEntity(h2);
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
