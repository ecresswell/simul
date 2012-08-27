package org.housered.simul.model.navigation;

import java.util.HashSet;
import java.util.Set;

import org.housered.simul.model.location.Dimension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Knows about all collidable objects, helps with navigating around them.
 */
public class NavigationManager
{
    private static Logger LOGGER = LoggerFactory.getLogger(NavigationManager.class);
    private final Dimension worldBounds;
    private Set<Collidable> collidables = new HashSet<Collidable>();

    public NavigationManager(Dimension worldBounds)
    {
        this.worldBounds = worldBounds;
    }

    public void addCollidable(Collidable collidable)
    {
        if (!isInsideWorldBounds(collidable))
        {
            LOGGER.error("Attempt to add collidable outside the bounds - {}", collidable);
        }
        
        collidables.add(collidable);
        refreshNavigationMesh();
    }
    
    void refreshNavigationMesh()
    {
        
    }

    boolean isInsideWorldBounds(Collidable c)
    {
        if (c.getPosition().getX() < 0 || c.getPosition().getY() < 0)
            return false;
        if (c.getPosition().getX() + c.getBounds().getWidth() > worldBounds.getWidth()
                || c.getPosition().getY() + c.getBounds().getHeight() > worldBounds.getHeight())
            return false;
        return true;
    }
}
