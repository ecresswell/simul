package org.housered.simul.model.actor;

import org.housered.simul.model.assets.AssetManager;
import org.housered.simul.model.navigation.NavigationManager;
import org.housered.simul.model.navigation.road.RoadManager;
import org.housered.simul.model.navigation.tube.TubeManager;
import org.housered.simul.model.work.JobManager;
import org.housered.simul.model.world.GameClock;
import org.housered.simul.model.world.IdGenerator;

public class PersonFactory
{
    private final IdGenerator idGenerator;
    private final AssetManager assetManager;
    private final NavigationManager navigationManager;
    private final GameClock gameClock;
    private final RoadManager roadNetworkManager;
    private final JobManager jobManager;
    private final TubeManager tubeManager;

    public PersonFactory(IdGenerator idGenerator, AssetManager assetManager, JobManager jobManager,
            NavigationManager navigationManager, GameClock gameClock, RoadManager roadNetworkManager,
            TubeManager tubeManager)
    {
        this.idGenerator = idGenerator;
        this.assetManager = assetManager;
        this.jobManager = jobManager;
        this.navigationManager = navigationManager;
        this.gameClock = gameClock;
        this.roadNetworkManager = roadNetworkManager;
        this.tubeManager = tubeManager;
    }

    public Person createPerson(double x, double y)
    {
        Person person = new Person(idGenerator.getNextId(), assetManager, jobManager, navigationManager, gameClock,
                roadNetworkManager, tubeManager);
        person.getPosition().setCoords(x, y);
        return person;
    }
}
