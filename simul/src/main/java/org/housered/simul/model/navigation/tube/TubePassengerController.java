package org.housered.simul.model.navigation.tube;

import org.housered.simul.model.actor.Actor;
import org.housered.simul.model.actor.brain.HighLevelBrain;
import org.housered.simul.model.location.Vector;
import org.housered.simul.model.navigation.ActorController;
import org.housered.simul.model.navigation.NavigationOrder;
import org.housered.simul.model.navigation.NavigationOrder.NavigationType;
import org.housered.simul.view.GraphicsAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TubePassengerController implements ActorController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TubePassengerController.class);
    private final HighLevelBrain highLevel;
    private final Actor actor;
    private TubeStation targetStation;
    private final TubeManager tubeManager;
    private TubeStation startStation;

    public TubePassengerController(Actor actor, HighLevelBrain highLevel, TubeManager tubeManager)
    {
        this.actor = actor;
        this.highLevel = highLevel;
        this.tubeManager = tubeManager;

    }

    @Override
    public void giveDirectControl(NavigationOrder target)
    {
        if (target.getType() != NavigationType.TUBE)
            throw new IllegalArgumentException("Can't do other things");

        targetStation = ((TubeNavigationOrder) target).getTarget();
        startStation = tubeManager.getClosestTubeStation(getPosition());
    }

    @Override
    public void tick(float dt)
    {
        if (startStation != null)
        {
            LOGGER.trace("Queueing for a tube at {}", targetStation);
            startStation.queueForTube(this, targetStation);
            startStation = null;
        }
    }

    public boolean arrivedAtStationDoYouWishToAlight(Tube currentTube, TubeStation station)
    {
        if (station == targetStation)
        {
            LOGGER.trace("Arrived at {}", station);
            getPosition().setCoords(station.getExitPoint());
            highLevel.arrivedAtTarget();
            return true;
        }
        return false;
    }

    @Override
    public void relinquishControl()
    {

    }

    @Override
    public Vector getPosition()
    {
        return actor.getPosition();
    }

    @Override
    public void render(GraphicsAdapter r)
    {
    }

    @Override
    public byte getZOrder()
    {
        return BUILDING_Z_ORDER;
    }

    @Override
    public Vector getSize()
    {
        return actor.getSize();
    }

}
