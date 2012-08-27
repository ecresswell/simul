package org.housered.simul.model.navigation;

import org.housered.simul.model.location.Dimension;
import org.housered.simul.model.location.Locatable;

public interface Collidable extends Locatable
{
    Dimension getBounds();
    
    boolean isColliding();
}
