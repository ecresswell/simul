package org.housered.simul.model.world;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.housered.simul.controller.KeyInputManager;
import org.housered.simul.controller.MouseListenerManager;
import org.housered.simul.model.actor.brain.DisplayBrainImpl;
import org.housered.simul.model.actor.brain.DisplayBrainImpl.DisplayState;
import org.housered.simul.model.assets.AssetManager;
import org.housered.simul.model.assets.Occupiable;
import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.Collidable;
import org.housered.simul.model.navigation.NavigationManager;
import org.housered.simul.model.navigation.RenderableBoundingBox;
import org.housered.simul.model.navigation.road.Road;
import org.housered.simul.model.navigation.road.RoadManager;
import org.housered.simul.model.navigation.tube.TubeManager;
import org.housered.simul.model.work.JobManager;
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
    private JobManager jobManager = new JobManager();
    private RoadManager roadNetwork = new RoadManager();
    private TubeManager tubeManager = new TubeManager();
    private GuiManager guiManager;
    private GameClockImpl gameClock;

    private KeyInputManager inputManager = new KeyInputManager();
    private MouseListenerManager mouseManager = new MouseListenerManager();
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
        addEntity(roadNetwork);
        addEntity(roadNetwork.getRoadGraph());
        addEntity(tubeManager);

        CityPlanner cityPlanner = new CityPlanner(this, gameClock, assetManager, jobManager, navigationManager,
                roadNetwork, tubeManager);
        cityPlanner.loadLevel(this);

        navigationManager.refreshNavigationMesh();
    }

    public void addEntity(GameObject entity)
    {
        if (entity instanceof Identifiable)
            LOGGER.debug("Add entity with id {} - {}", ((Identifiable) entity).getId(), entity);
        else
            LOGGER.trace("Add non-identifiable object - {}", entity);

        if (entity instanceof Renderable)
        {
            renderables.add((Renderable) entity);
            Collections.sort(renderables, new RenderableComparator());
        }
        if (entity instanceof Tickable)
            tickables.add((Tickable) entity);
        if (entity instanceof Collidable)
            if (((Collidable) entity).isColliding())
                navigationManager.addColliableWithoutNavMeshRefresh((Collidable) entity);
        if (entity instanceof Occupiable)
            assetManager.addOccupiable((Occupiable) entity);
    }

    public void addEntities(GameObject... entities)
    {
        for (GameObject entity : entities)
            addEntity(entity);
    }

    public void addEntities(Collection<? extends GameObject> entities)
    {
        for (GameObject entity : entities)
            addEntity(entity);
    }

    @Override
    public void beginRender()
    {
    }

    @Override
    public Iterable<Renderable> getZOrderedRenderables()
    {
        synchronized (gameClock)
        {
            return renderables;
        }
    }

    @Override
    public void endRender()
    {
    }

    @Override
    public void tick(float dt)
    {
        synchronized (gameClock)
        {
            processKeyboardInput();
            processMouseInput();
            gameClock.tick(dt);

            for (Tickable tickable : tickables)
            {
                tickable.tick(dt * gameClock.getGameSecondsPerActualSecond() * SLOW_DOWN_REAL_TIME_FACTOR);
            }
        }
    }

    private void processMouseInput()
    {
        //        LOGGER.debug("{}, {}", e.getX() * unitsPerWorldUnit - xClickOffset, e.getY() * unitsPerWorldUnit
        //                - yClickOffset);

        List<MouseEvent> events = mouseManager.unbufferMouseReleased();

        final int xOffset = -4;
        final int yOffset = -27;

        for (MouseEvent e : events)
        {
            int x = e.getX() + xOffset;
            int y = e.getY() + yOffset;
            Vector position = camera.translateIntoWorldSpace(x, y);
            Vector size = Vector.v(2, 2);
            addEntity(new RenderableBoundingBox(position, size));
            LOGGER.debug("Add entity at {} ({})", position);
        }
    }

    private void processKeyboardInput()
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
            gameClock.setSpeed(10);
        }
        if (inputManager.isKeyDown(KeyEvent.VK_2))
        {
            gameClock.setSpeed(60);
        }
        if (inputManager.isKeyDown(KeyEvent.VK_3))
        {
            gameClock.setSpeed(180);
        }
        if (inputManager.isKeyDown(KeyEvent.VK_4))
        {
            gameClock.setSpeed(600);
        }

        if (inputManager.isKeyDown(KeyEvent.VK_G))
        {
            System.out.println("//DISPERSE");
            DisplayBrainImpl.displayState = DisplayState.DISPERSE;
        }
        else if (inputManager.isKeyDown(KeyEvent.VK_H))
        {
            DisplayBrainImpl.displayState = DisplayState.HI;
        }
        else if (inputManager.isKeyDown(KeyEvent.VK_T))
        {
            DisplayBrainImpl.displayState = DisplayState.TIRAMISU;
        }
        else if (inputManager.isKeyDown(KeyEvent.VK_Y))
        {
            DisplayBrainImpl.displayState = DisplayState.SMILEY;
        }
    }

    public long getNextId()
    {
        return nextId.getAndIncrement();
    }

    public KeyInputManager getInputManager()
    {
        return inputManager;
    }

    public MouseListenerManager getMouseManager()
    {
        return mouseManager;
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

    public double getWorldWidth()
    {
        return WORLD_WIDTH;
    }

    public double getWorldHeight()
    {
        return WORLD_HEIGHT;
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
