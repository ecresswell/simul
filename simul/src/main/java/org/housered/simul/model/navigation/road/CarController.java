package org.housered.simul.model.navigation.road;

import org.housered.simul.model.actor.Actor;
import org.housered.simul.model.actor.brain.NavigationBrain;
import org.housered.simul.model.navigation.ActorController;

public class CarController implements ActorController
{
    private final Actor person;
    private final RoadNetworkManager roadManager;
    private final NavigationBrain navigation;

    public CarController(Actor person, NavigationBrain navigation, RoadNetworkManager roadManager)
    {
        this.person = person;
        this.navigation = navigation;
        this.roadManager = roadManager;
    }

    @Override
    public void giveDirectControl()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void tick(float dt)
    {
        // TODO Auto-generated method stub

    }
}
