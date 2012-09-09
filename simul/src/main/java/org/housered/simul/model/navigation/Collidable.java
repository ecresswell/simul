package org.housered.simul.model.navigation;

import org.housered.simul.model.location.BoundingBox;

public interface Collidable extends BoundingBox
{
    boolean isColliding();
}
