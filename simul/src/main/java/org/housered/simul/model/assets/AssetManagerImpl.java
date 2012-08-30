package org.housered.simul.model.assets;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.housered.simul.model.world.Identifiable;

/**
 * Responsible for knowing who owns what, and can be asked about these relationships.
 */
public class AssetManagerImpl implements AssetManager
{
    private Map<Identifiable, Set<Occupiable>> relationships = new HashMap<Identifiable, Set<Occupiable>>();
    private Set<Occupiable> allBuildings = new HashSet<Occupiable>();

    @Override
    public void createDeed(Identifiable owner, Occupiable asset)
    {
        if (relationships.get(owner) == null)
            relationships.put(owner, new HashSet<Occupiable>());

        relationships.get(owner).add(asset);
        allBuildings.add(asset);
    }

    @Override
    public Set<Occupiable> getAssets(Identifiable entity)
    {
        return relationships.get(entity);
    }

    @Override
    public Set<Occupiable> getAssets()
    {
        return allBuildings;
    }

    @Override
    public void addOccupiable(Occupiable asset)
    {
        allBuildings.add(asset);
    }
}
