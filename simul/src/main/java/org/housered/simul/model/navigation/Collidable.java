package org.housered.simul.model.navigation;

import org.housered.simul.model.location.Locatable;

import straightedge.geom.KPoint;

public interface Collidable extends Locatable
{
    KPoint getBounds();
    
    boolean isColliding();
}
