package org.housered.simul.model.navigation;

public interface NavigationOrder
{
    public enum NavigationType
    {
        WALK, CAR, TUBE
    }
    
    NavigationType getType();
}
