package org.housered.simul.model.assets;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.housered.simul.model.world.Identifiable;

/**
 * Responsible for knowing who owns what, and can be asked about these relationships.
 */
public class AssetManager
{
    private static final Set<Occupiable> NO_ASSETS = Collections.unmodifiableSet(new HashSet<Occupiable>());
    private Map<Identifiable, Set<Occupiable>> deeds = new HashMap<Identifiable, Set<Occupiable>>();
    private Set<Occupiable> allBuildings = new HashSet<Occupiable>();

    public void createDeed(Identifiable owner, Occupiable asset)
    {
        if (deeds.get(owner) == null)
            deeds.put(owner, new HashSet<Occupiable>());

        deeds.get(owner).add(asset);
        allBuildings.add(asset);
    }

    public Set<Occupiable> getAssets(Identifiable entity)
    {
        Set<Occupiable> assets = deeds.get(entity);
        if (assets == null)
            return NO_ASSETS;
        return assets;
    }

    public Set<Occupiable> getAssets()
    {
        return allBuildings;
    }

    public void addOccupiable(Occupiable asset)
    {
        allBuildings.add(asset);
    }
}
