package org.housered.simul.model.assets;

import java.util.Set;

import org.housered.simul.model.world.Identifiable;

public interface AssetManager
{
    void createDeed(Identifiable owner, Occupiable asset);

    Set<Occupiable> getAssets(Identifiable entity);
}