package org.housered.simul.model.navigation.road.quadtree;

import java.util.ArrayList;

public class ArrayListVisitor implements ExitEarlyItemVisitor
{
    private ArrayList items = new ArrayList();

    public ArrayListVisitor()
    {
    }

    public boolean visitItem(Object item)
    {
        items.add(item);
        return false;
    }

    public ArrayList getItems()
    {
        return items;
    }

}
