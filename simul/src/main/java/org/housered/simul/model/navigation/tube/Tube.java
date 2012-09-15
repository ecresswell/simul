package org.housered.simul.model.navigation.tube;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.housered.simul.model.assets.Occupant;
import org.housered.simul.model.assets.Occupiable;
import org.housered.simul.model.location.Locatable;
import org.housered.simul.model.location.SpeedLimiter;
import org.housered.simul.model.location.Vector;
import org.housered.simul.model.world.GameClock;
import org.housered.simul.model.world.Tickable;
import org.housered.simul.view.GraphicsAdapter;
import org.housered.simul.view.Renderable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tube implements Renderable, Locatable, Tickable, Occupant
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Tube.class);
    private final SpeedLimiter speedLimiter = new SpeedLimiter();
    private final TubeLine line;
    private final GameClock gameClock;
    private Vector position;
    private TubeStation targetStation;
    private TubeStation currentStation;
    private long waitingUntil = -1;
    private List<TubePassengerController> occupants = new LinkedList<TubePassengerController>();

    Tube(TubeStation station, TubeLine line, GameClock gameClock)
    {
        this.line = line;
        this.gameClock = gameClock;
        this.position = station.getPosition().copy();
    }

    void goTowardsTubeStation(TubeStation station)
    {
        LOGGER.trace("{} moving towards {}", this, station);
        speedLimiter.setSpeedLimit(line.getTubeSpeed());
        currentStation = targetStation;
        targetStation = station;
    }

    void waitAndOpenDoors(long waitInGameMinutes)
    {
        waitingUntil = gameClock.getSecondsSinceGameStart() + waitInGameMinutes * 60;
    }

    @Override
    public void tick(float dt)
    {
        if (waitingUntil < gameClock.getSecondsSinceGameStart())
        {
            waitingUntil = -1;
            if (currentStation != null)
                currentStation.exit(this);
        }
        else
        {
            return;
        }

        speedLimiter.startNewTick(dt);

        Vector move = targetStation.getPosition().translateCopy(getPosition().negateCopy());
        Vector actualMove = speedLimiter.incrementPosition(move);
        getPosition().translate(actualMove);

        if (getPosition().equals(targetStation.getPosition()))
        {
            LOGGER.trace("{} arrived at station {}", this, targetStation);
            targetStation.occupy(this);
            line.arrivedAtStation(this, targetStation);

            ListIterator<TubePassengerController> i = occupants.listIterator();
            while (i.hasNext())
            {
                TubePassengerController person = i.next();
                if (person.arrivedAtStationDoYouWishToAlight(this, currentStation))
                    i.remove();
            }
        }
    }

    void putPersonIntoTube(TubePassengerController person)
    {
        occupants.add(person);
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
        r.setColour(Color.green);
        r.drawText(position, 100, String.valueOf(occupants.size()));
    }

    @Override
    public byte getZOrder()
    {
        return BUILDING_Z_ORDER;
    }

    @Override
    public String toString()
    {
        return "Tube [position=" + position + ", targetStation=" + targetStation + ", line=" + line + "]";
    }
}
