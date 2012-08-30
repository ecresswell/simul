package org.housered.simul.model.assets;

import java.util.Set;

import org.housered.simul.model.world.Identifiable;

public interface AssetManager
{
    void addOccupiable(Occupiable asset);
    
    void createDeed(Identifiable owner, Occupiable asset);

    Set<Occupiable> getAssets(Identifiable entity);
    
    Set<Occupiable> getAssets();
}