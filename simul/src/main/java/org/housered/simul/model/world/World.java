package org.housered.simul.model.world;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.housered.simul.model.actor.Person;
import org.housered.simul.model.actor.PersonFactory;
import org.housered.simul.model.assets.AssetManager;
import org.housered.simul.model.assets.House;
import org.housered.simul.model.assets.HouseFactory;
import org.housered.simul.model.assets.Occupiable;
import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.Collidable;
import org.housered.simul.model.navigation.NavigationManager;
import org.housered.simul.model.navigation.RenderableBoundingBox;
import org.housered.simul.model.navigation.Road;
import org.housered.simul.model.navigation.RoadNetworkManager;
import org.housered.simul.model.work.CommercialManager;
import org.housered.simul.model.work.Workplace;
import org.housered.simul.model.work.WorkplaceFactory;
import org.housered.simul.view.Renderable;
import org.housered.simul.view.RenderableProvider;
import org.housered.simul.view.gui.GuiManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class World implements RenderableProvider, Tickable, IdGenerator
{
    private static final int WORLD_WIDTH = 800;
    private static final int WORLD_HEIGHT = 600;
    private static final float SLOW_DOWN_REAL_TIME_FACTOR = 0.25f;
    private static Logger LOGGER = LoggerFactory.getLogger(World.class);

    private AtomicLong nextId = new AtomicLong();
    private List<Renderable> renderables = new ArrayList<Renderable>();
    private List<Tickable> tickables = new LinkedList<Tickable>();

    private NavigationManager navigationManager = new NavigationManager(new Vector(WORLD_WIDTH, WORLD_HEIGHT));
    private AssetManager assetManager = new AssetManager();
    private CommercialManager commercialManager = new CommercialManager();
    private RoadNetworkManager roadNetwork = new RoadNetworkManager(new Vector(WORLD_WIDTH, WORLD_HEIGHT));
    private GuiManager guiManager;
    private GameClockImpl gameClock;

    private InputManager inputManager = new InputManager();
    private final Camera camera;

    public World(Camera camera)
    {
        this.camera = camera;

        //TODO: move this 
        addEntity(new RenderableBoundingBox(new Vector(), new Vector(WORLD_WIDTH, WORLD_HEIGHT)));
    }

    public void loadLevel()
    {
        gameClock = new GameClockImpl(TimeUnit.HOURS.toSeconds(7), 60);
        guiManager = new GuiManager(gameClock);
        addEntity(guiManager);

        CityPlanner cityPlanner = new CityPlanner(this, gameClock, assetManager, commercialManager, navigationManager,
                roadNetwork);
        cityPlanner.loadLevel(this);

        roadNetwork.refreshNavigationMesh();
        navigationManager.refreshNavigationMesh();
    }

    public void addEntity(Object entity)
    {
        if (entity instanceof Identifiable)
            LOGGER.debug("Add entity with id {} - {}", ((Identifiable) entity).getId(), entity);
        else
            LOGGER.debug("Add non-identifiable object - {}", entity);

        if (entity instanceof Renderable)
        {
            renderables.add((Renderable) entity);
            Collections.sort(renderables, new RenderableComparator());
        }
        if (entity instanceof Tickable)
            tickables.add((Tickable) entity);
        if (entity instanceof Collidable)
            navigationManager.addColliableWithoutNavMeshRefresh((Collidable) entity);
        if (entity instanceof Occupiable)
            assetManager.addOccupiable((Occupiable) entity);
        if (entity instanceof Road)
            roadNetwork.addRoad((Road) entity);
    }

    public void addEntities(Object... entities)
    {
        for (Object entity : entities)
            addEntity(entity);
    }

    @Override
    public void beginRender()
    {
    }

    @Override
    public Iterable<Renderable> getZOrderedRenderables()
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
        if (inputManager.isKeyDown(KeyEvent.VK_4))
        {
            gameClock.setSpeed(6000);
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
        //LOGGER.trace("Average sleep is {}", averageSleep);
    }

    private static class RenderableComparator implements Comparator<Renderable>
    {
        @Override
        public int compare(Renderable o1, Renderable o2)
        {
            byte one = o1.getZOrder();
            byte two = o2.getZOrder();

            if (one < two)
                return -1;
            else if (one == two)
                return 0;
            else
                return 1;
        }
    }
}
