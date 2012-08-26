package org.housered.simul.model.assets;

import java.util.Collections;
import java.util.List;

import org.housered.simul.model.world.Identifiable;
import org.housered.simul.model.world.Ownable;

/**
 * Responsible for knowing who owns what, and can be asked about these relationships.
 */
public class AssetManagerImpl implements AssetManager
{
    @Override
    public void addIdentifiable(Identifiable entity)
    {

    }

    @Override
    public void addOwnable(Ownable asset)
    {

    }

    @Override
    public List<Ownable> getAssets(Identifiable entity)
    {
        return Collections.emptyList();
    }
}
