package org.housered.simul.model.location;

import straightedge.geom.KPoint;

public class Vector extends KPoint
{
    public Vector()
    {
    }

    public Vector(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector(KPoint old)
    {
        this.x = old.x;
        this.y = old.y;
    }

    public Vector copy()
    {
        return new Vector(this);
    }

    @Override
    public Vector translateCopy(double xIncrement, double yIncrement)
    {
        return new Vector(x + xIncrement, y + yIncrement);
    }

    public Vector translateCopy(Vector pointIncrement)
    {
        return new Vector(x + pointIncrement.x, y + pointIncrement.y);
    }

    public Vector negateCopy()
    {
        return new Vector(-x, -y);
    }

    public Vector scaleCopy(double scale)
    {
        return new Vector(x * scale, y * scale);
    }
}
