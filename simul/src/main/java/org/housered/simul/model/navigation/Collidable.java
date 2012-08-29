package org.housered.simul.model.navigation;

import org.housered.simul.model.location.Locatable;
import org.housered.simul.model.location.Vector;

public interface Collidable extends Locatable
{
    Vector getBounds();
    
    boolean isColliding();
}
