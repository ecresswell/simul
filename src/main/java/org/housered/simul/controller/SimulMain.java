package org.housered.simul.controller;

import java.util.concurrent.TimeUnit;

import org.housered.simul.render.SwingFrame;
import org.housered.simul.world.World;

public class SimulMain
{
    private static final long DELAY = 5;

    private World world;
    private SwingFrame swingFrame;
    private boolean running;

    public SimulMain()
    {
        world = new World();
        world.loadLevel();
        
        swingFrame = new SwingFrame(world);

        running = true;
        run();
    }

    private void run()
    {
        long beforeTime, sleep;
        long timeDiff = 0;

        beforeTime = System.currentTimeMillis();

        while (running)
        {
            swingFrame.repaint();
            world.tick(TimeUnit.MILLISECONDS.toSeconds(timeDiff));

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
            beforeTime = System.currentTimeMillis();
        }
    }

    public static void main(String[] args)
    {
        new SimulMain();
    }
}
