package org.housered.simul.model.navigation.road.quadtree;

public interface ExitEarlyItemVisitor
{
    /**
     * Visit the item. Return true if the search can stop.
     */
    boolean visitItem(Object item);
}
