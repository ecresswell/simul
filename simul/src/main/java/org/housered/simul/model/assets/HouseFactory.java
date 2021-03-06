package org.housered.simul.model.assets;

import org.housered.simul.model.location.Vector;
import org.housered.simul.model.world.IdGenerator;

public class HouseFactory
{
    private static final double DEFAULT_WIDTH = 20;
    private static final double DEFAULT_HEIGHT = 20;

    private final IdGenerator idGenerator;

    public HouseFactory(IdGenerator idGenerator)
    {
        this.idGenerator = idGenerator;
    }

    public House createHouse(double x, double y)
    {
        return createHouse(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public House createHouse(double x, double y, double width, double height)
    {
        House result = new House(idGenerator.getNextId(), new Vector(x, y), new Vector(width, height));
        result.getPosition().setCoords(x, y);
        return result;
    }
}
