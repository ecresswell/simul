package org.housered.simul.model.actor;

import org.housered.simul.model.assets.AssetManager;
import org.housered.simul.model.navigation.NavigationManager;
import org.housered.simul.model.navigation.road.RoadNetworkManager;
import org.housered.simul.model.work.JobManager;
import org.housered.simul.model.world.GameClock;
import org.housered.simul.model.world.IdGenerator;

public class PersonFactory
{
    private final IdGenerator idGenerator;
    private final AssetManager assetManager;
    private final NavigationManager navigationManager;
    private final GameClock gameClock;
    private final RoadNetworkManager roadNetworkManager;
    private final JobManager jobManager;

    public PersonFactory(IdGenerator idGenerator, AssetManager assetManager, JobManager jobManager,
            NavigationManager navigationManager, GameClock gameClock, RoadNetworkManager roadNetworkManager)
    {
        this.idGenerator = idGenerator;
        this.assetManager = assetManager;
        this.jobManager = jobManager;
        this.navigationManager = navigationManager;
        this.gameClock = gameClock;
        this.roadNetworkManager = roadNetworkManager;
    }

    public Person createPerson(double x, double y)
    {
        Person person = new Person(idGenerator.getNextId(), assetManager, jobManager, navigationManager,
                gameClock, roadNetworkManager);
        person.getPosition().setCoords(x, y);
        return person;
    }
}
