package org.housered.simul.model.location;

public class PositionImpl extends VectorImpl implements Position
{
    public PositionImpl(float x, float y)
    {
        super(x, y);
    }

    public PositionImpl()
    {
        super();
    }

    @Override
    public int getConvertedX(double offset, double unitsPerWorldUnit)
    {
        return (int) Math.round((getX() - offset) * unitsPerWorldUnit);
    }

    @Override
    public int getConvertedY(double offset, double unitsPerWorldUnit)
    {
        return (int) Math.round((getY() - offset) * unitsPerWorldUnit);
    }
}
