package org.housered.simul.model.assets;

import org.housered.simul.model.world.IdGenerator;

public class HouseFactory
{
    private final IdGenerator idGenerator;

    public HouseFactory(IdGenerator idGenerator)
    {
        this.idGenerator = idGenerator;
    }

    public House createHouse(double x, double y)
    {
        House result = new House(idGenerator.getNextId());
        result.getPosition().setCoords(x, y);
        return result;
    }
}
