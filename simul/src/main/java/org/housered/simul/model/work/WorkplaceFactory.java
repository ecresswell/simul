package org.housered.simul.model.work;

import org.housered.simul.model.location.Vector;
import org.housered.simul.model.world.IdGenerator;

public class WorkplaceFactory
{
    private static final double DEFAULT_WIDTH = 20;
    private static final double DEFAULT_HEIGHT = 20;

    private final IdGenerator idGenerator;

    public WorkplaceFactory(IdGenerator idGenerator)
    {
        this.idGenerator = idGenerator;
    }

    public Workplace createWorkplace(double x, double y)
    {
        return createWorkplace(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public Workplace createWorkplace(double x, double y, double width, double height)
    {
        Workplace result = new Workplace(idGenerator.getNextId(), new Vector(x, y), new Vector(width, height));
        result.getPosition().setCoords(x, y);
        return result;
    }
}
