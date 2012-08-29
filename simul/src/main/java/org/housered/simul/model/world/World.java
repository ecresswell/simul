package org.housered.simul.model.world;

import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.housered.simul.model.actor.Person;
import org.housered.simul.model.assets.AssetManager;
import org.housered.simul.model.assets.AssetManagerImpl;
import org.housered.simul.model.assets.House;
import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.BoundingBox;
import org.housered.simul.model.navigation.Collidable;
import org.housered.simul.model.navigation.NavigationManager;
import org.housered.simul.view.Renderable;
import org.housered.simul.view.RenderableProvider;
import org.housered.simul.view.gui.GuiManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class World implements RenderableProvider, Tickable
{
    private static final int WORLD_WIDTH = 800;
    private static final int WORLD_HEIGHT = 600;
    private static final float REAL_TIME_MULTIPLIER = 0.1f;
    private static Logger LOGGER = LoggerFactory.getLogger(World.class);

    private AtomicLong nextId = new AtomicLong();
    private List<Renderable> renderables = new LinkedList<Renderable>();
    private List<Tickable> tickables = new LinkedList<Tickable>();

    private NavigationManager navigationManager = new NavigationManager(new Vector(WORLD_WIDTH, WORLD_HEIGHT));
    private AssetManager assetManager = new AssetManagerImpl();
    private GameClockImpl gameClock;
    private GuiManager guiManager;

    private InputManager inputManager = new InputManager();
    private final Camera camera;

    public World(Camera camera)
    {
        this.camera = camera;

        //TODO: move this 
        addEntity(new BoundingBox(new Vector(), new Vector(WORLD_WIDTH, WORLD_HEIGHT)));
    }

    public void loadLevel()
    {
        gameClock = new GameClockImpl(TimeUnit.HOURS.toSeconds(7), 60);
        guiManager = new GuiManager(gameClock);
        addEntity(guiManager);

        Person p1 = new Person(getNextId(), assetManager, navigationManager);
        Person p2 = new Person(getNextId(), assetManager, navigationManager);
        p1.getPosition().setX(395);
        p1.getPosition().setY(295);

        House h1 = new House(getNextId());
        House h2 = new House(getNextId());
        h2.getPosition().setX(450);
        h2.getPosition().setY(500);

        assetManager.createDeed(p1, h1);
        assetManager.createDeed(p2, h2);

        addEntity(p1);
        addEntity(p2);
        addEntity(h1);
        addEntity(h2);
    }

    private void addEntity(Object entity)
    {
        if (entity instanceof Identifiable)
            LOGGER.debug("Add entity with id {} - {}", ((Identifiable) entity).getId(), entity);
        else
            LOGGER.debug("Add non-identifiable object - {}", entity);

        if (entity instanceof Renderable)
            renderables.add((Renderable) entity);
        if (entity instanceof Tickable)
            tickables.add((Tickable) entity);
        if (entity instanceof Collidable)
            navigationManager.addCollidable((Collidable) entity);
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
        processInput();
        gameClock.tick(dt);

        for (Tickable tickable : tickables)
        {
            tickable.tick(dt * gameClock.getGameSecondsPerActualSecond() * REAL_TIME_MULTIPLIER);
        }
    }

    private void processInput()
    {
        if (inputManager.isKeyDown(KeyEvent.VK_LEFT))
        {
            camera.incrementXOffset(Camera.DEFAULT_CAMERA_MOVE);
        }
        if (inputManager.isKeyDown(KeyEvent.VK_RIGHT))
        {
            camera.incrementXOffset(-Camera.DEFAULT_CAMERA_MOVE);
        }
        if (inputManager.isKeyDown(KeyEvent.VK_UP))
        {
            camera.incrementYOffset(Camera.DEFAULT_CAMERA_MOVE);
        }
        if (inputManager.isKeyDown(KeyEvent.VK_DOWN))
        {
            camera.incrementYOffset(-Camera.DEFAULT_CAMERA_MOVE);
        }
        if (inputManager.isKeyDown(KeyEvent.VK_SPACE))
        {
            camera.zoom(Camera.DEFAULT_CAMERA_ZOOM_OUT);
        }
        if (inputManager.isKeyDown(KeyEvent.VK_C))
        {
            camera.zoom(Camera.DEFAULT_CAMERA_ZOOM_IN);
        }
        if (inputManager.isKeyDown(KeyEvent.VK_1))
        {
            gameClock.setSpeed(60);
        }
        if (inputManager.isKeyDown(KeyEvent.VK_2))
        {
            gameClock.setSpeed(180);
        }
        if (inputManager.isKeyDown(KeyEvent.VK_3))
        {
            gameClock.setSpeed(600);
        }
    }

    private long getNextId()
    {
        return nextId.getAndIncrement();
    }

    public InputManager getInputManager()
    {
        return inputManager;
    }

    @Override
    public Camera getCamera()
    {
        return camera;
    }
}
