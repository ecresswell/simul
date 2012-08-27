package org.housered.simul.controller;

import org.housered.simul.model.world.Camera;
import org.housered.simul.model.world.World;
import org.housered.simul.view.swing.SwingFrame;

public class SimulMain
{
    private static final long DELAY = 20;

    private World world;
    private SwingFrame swingFrame;
    private boolean running;

    public SimulMain()
    {
        int width = 800;
        int height = 600;
        Camera camera = new Camera(width, height);

        world = new World(camera);
        world.loadLevel();

        swingFrame = new SwingFrame(width, height, world);
        swingFrame.addKeyListener(world.getInputManager());

        running = true;
        run();
    }

    private void run()
    {
        long beforeTime, sleep, timeDiff, currentTime;
        long dt = 0;

        beforeTime = System.currentTimeMillis();

        while (running)
        {
            swingFrame.repaint();
            world.tick((float) dt / 1000);

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

            if (sleep < 0)
                sleep = 2;
            try
            {
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
