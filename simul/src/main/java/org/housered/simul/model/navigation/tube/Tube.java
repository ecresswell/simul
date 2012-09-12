package org.housered.simul.model.navigation.tube;

import java.awt.Color;

import org.housered.simul.model.location.Locatable;
import org.housered.simul.model.location.SpeedLimiter;
import org.housered.simul.model.location.Vector;
import org.housered.simul.model.world.GameClock;
import org.housered.simul.model.world.Tickable;
import org.housered.simul.view.GraphicsAdapter;
import org.housered.simul.view.Renderable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tube implements Renderable, Locatable, Tickable
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Tube.class);
    private final SpeedLimiter speedLimiter = new SpeedLimiter();
    private final TubeLine line;
    private final GameClock gameClock;
    private Vector position;
    private TubeStation targetStation;
    private boolean waiting = true;
    private long waitingUntil = -1;

    Tube(TubeStation station, TubeLine line, GameClock gameClock)
    {
        this.line = line;
        this.gameClock = gameClock;
        this.position = station.getPosition().copy();
    }

    void goTowardsTubeStation(TubeStation station)
    {
        LOGGER.debug("{} moving towards {}", this, station);
        speedLimiter.setSpeedLimit(line.getTubeSpeed());
        targetStation = station;
    }

    void waitAndOpenDoors(long waitInGameMinutes)
    {
        waiting = true;
        waitingUntil = gameClock.getSecondsSinceGameStart() + waitInGameMinutes * 60;
    }

    @Override
    public void tick(float dt)
    {
        if (waitingUntil == -1)
        {
            waiting = false;
        }
        else if (waitingUntil < gameClock.getSecondsSinceGameStart())
        {
            waiting = false;
            waitingUntil = -1;
        }

        if (waiting)
            return;

        speedLimiter.startNewTick(dt);

        Vector move = targetStation.getPosition().translateCopy(getPosition().negateCopy());
        Vector actualMove = speedLimiter.incrementPosition(move);
        getPosition().translate(actualMove);

        if (getPosition().equals(targetStation.getPosition()))
        {
            LOGGER.debug("{} arrived at station {}", this, targetStation);
            waiting = true;
            line.arrivedAtStation(this, targetStation);
        }
    }

    @Override
    public Vector getPosition()
    {
        return position;
    }

    @Override
    public void render(GraphicsAdapter r)
    {
        r.setColour(Color.white);
        r.fillCircle(position, 10);
    }

    @Override
    public byte getZOrder()
    {
        return BUILDING_Z_ORDER;
    }

    @Override
    public String toString()
    {
        return "Tube [position=" + position + ", targetStation=" + targetStation + ", line=" + line + ", waiting="
                + waiting + "]";
    }
}
