package org.housered.simul.model.actor.brain;

import java.util.Set;

import org.housered.simul.model.actor.Actor;
import org.housered.simul.model.assets.AssetManager;
import org.housered.simul.model.assets.Occupiable;

public class HighLevelBrainImpl implements HighLevelBrain
{
    private Actor actor;
    private AssetManager assetManager;

    private Occupiable currentTarget;
    
    public HighLevelBrainImpl(Actor actor, AssetManager assetManager)
    {
        this.actor = actor;
        this.assetManager = assetManager;
    }
    
    @Override
    public Occupiable decideWhereToGo()
    {
        if (currentTarget == null)
        {
            //if we're not at home, go home
            Set<Occupiable> ourBelongings = assetManager.getAssets(actor);

            if (ourBelongings.isEmpty())
                return null;

            //choose a random thing we occupy
            currentTarget = ourBelongings.iterator().next();
            return currentTarget;
        }

        //we've already decided where to go
        return null;
    }

}
