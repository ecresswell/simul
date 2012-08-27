package org.housered.simul.model.location;

public class Position extends Vector
{
    public Position(float x, float y)
    {
        super(x, y);
    }

    public Position()
    {
        super();
    }

    public int getConvertedX(double offset, double unitsPerWorldUnit)
    {
        return (int) Math.round((getX() - offset) * unitsPerWorldUnit);
    }

    public int getConvertedY(double offset, double unitsPerWorldUnit)
    {
        return (int) Math.round((getY() - offset) * unitsPerWorldUnit);
    }
}
