package org.housered.simul.model.assets;

import java.util.List;

import org.housered.simul.model.world.Identifiable;

public interface AssetManager
{
    void addIdentifiable(Identifiable entity);

    void addOccupiable(Occupiable asset);

    List<Occupiable> getAssets(Identifiable entity);
}