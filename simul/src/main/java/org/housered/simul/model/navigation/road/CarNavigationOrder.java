package org.housered.simul.model.navigation.road;

import org.housered.simul.model.navigation.NavigationOrder;

public class CarNavigationOrder implements NavigationOrder
{
    
    
    @Override
    public NavigationType getType()
    {
        return NavigationType.CAR;
    }
}
