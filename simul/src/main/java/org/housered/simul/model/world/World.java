package org.housered.simul.model.world;

import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.housered.simul.model.actor.PersonFactory;
import org.housered.simul.model.assets.AssetManager;
import org.housered.simul.model.assets.AssetManagerImpl;
import org.housered.simul.model.assets.HouseFactory;
import org.housered.simul.model.assets.Occupiable;
import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.BoundingBox;
import org.housered.simul.model.navigation.Collidable;
import org.housered.simul.model.navigation.NavigationManager;
import org.housered.simul.view.Renderable;
import org.housered.simul.view.RenderableProvider;
import org.housered.simul.view.gui.GuiManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class World implements RenderableProvider, Tickable, IdGenerator
{
    private static final int WORLD_WIDTH = 800;
    private static final int WORLD_HEIGHT = 600;
    private static final float SLOW_DOWN_REAL_TIME_FACTOR = 0.1f;
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

        PersonFactory personFactory = new PersonFactory(this, assetManager, navigationManager, gameClock);
        HouseFactory houseFactory = new HouseFactory(this);

        for (int x = 50; x < 600; x += 50)
        {
            for (int y = 50; y < 600; y += 50)
            {
                addEntity(houseFactory.createHouse(x, y));
                addEntity(personFactory.createPerson(x - 1, y - 1));
            }
        }
        
        for (int i = 0; i < 100; i++)
        {
            addEntity(personFactory.createPerson(325, 325));
        }
        
        navigationManager.refreshNavigationMesh();
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
            navigationManager.addColliableWithoutNavMeshRefresh((Collidable) entity);
        if (entity instanceof Occupiable)
            assetManager.addOccupiable((Occupiable) entity);
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
            tickable.tick(dt * gameClock.getGameSecondsPerActualSecond() * SLOW_DOWN_REAL_TIME_FACTOR);
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

    public long getNextId()
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

    public void informAverageSleepAmount(double averageSleep)
    {
        LOGGER.debug("Average sleep is {}", averageSleep);
    }
}
