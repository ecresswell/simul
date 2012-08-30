package org.housered.simul.model.actor.brain;

import org.housered.simul.model.location.Vector;


/**
 * Higher level brain, would make decisions such as: <br />
 * We're at home, the clock just hit X => go to work <br />
 * I'm in my car, I'm on my way to work => continue
 */
public interface HighLevelBrain 
{
    Vector decideWhereToGo();
    
    void arrivedAtTarget();
}
