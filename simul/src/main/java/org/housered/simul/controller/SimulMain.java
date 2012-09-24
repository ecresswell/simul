package org.housered.simul.controller;

import org.housered.simul.model.world.Camera;
import org.housered.simul.model.world.World;
import org.housered.simul.view.swing.SwingFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimulMain
{
    public static int carControllerDelay = 0;
    private static final int MAX_CAR_CONTROLLER_DELAY = 5;
    private static final Logger LOGGER = LoggerFactory.getLogger(SimulMain.class);
    private static final long DELAY = 20;

    private World world;
    private SwingFrame swingFrame;
    private boolean running;

    public SimulMain()
    {
        int width = 800;
        int height = 600;
        final Camera camera = new Camera(width, height);

        world = new World(camera);
        world.loadLevel();

        swingFrame = new SwingFrame(width, height, world);
        swingFrame.addKeyListener(world.getInputManager());
        swingFrame.addMouseListener(world.getMouseManager());

        running = true;
        run();
    }

    private void run()
    {
        FrameMetrics metrics = new FrameMetrics();
        long beforeTime, sleep, timeDiff, currentTime;
        long dt = 0;

        beforeTime = System.currentTimeMillis();

        while (running)
        {
            swingFrame.repaint();
            world.tick((float) dt / 1000);

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;
            world.informAverageSleepAmount(metrics.getAverageSleepAmount());

            if (sleep < 0)
            {
                sleep = 1;
                if (carControllerDelay < MAX_CAR_CONTROLLER_DELAY)
                {
                    carControllerDelay++;
                    LOGGER.debug("Increasing car controller delay to {}", carControllerDelay);
                }
            }
            else if (sleep > 10 && carControllerDelay > 0)
            {
                carControllerDelay--;
                LOGGER.debug("Reducing car controller delay to {}", carControllerDelay);
            }
            try
            {
                metrics.logSleep(sleep);
                Thread.sleep(sleep);
            }
            catch (InterruptedException e)
            {
                System.out.println("interrupted");
            }
            currentTime = System.currentTimeMillis();
            dt = currentTime - beforeTime;
            beforeTime = currentTime;
        }
    }

    public static void main(String[] args)
    {
        new SimulMain();
    }
}
