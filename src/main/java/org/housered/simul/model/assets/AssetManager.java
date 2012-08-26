package org.housered.simul.model.assets;

import java.util.List;

import org.housered.simul.model.world.Identifiable;
import org.housered.simul.model.world.Ownable;

public interface AssetManager
{
    void addIdentifiable(Identifiable entity);

    void addOwnable(Ownable asset);

    List<Ownable> getAssets(Identifiable entity);
}