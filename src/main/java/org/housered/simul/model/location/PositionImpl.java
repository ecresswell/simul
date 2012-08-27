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
    public int getConvertedX(float offset, float unitsPerWorldUnit)
    {
        return Math.round(getX() * unitsPerWorldUnit - offset);
    }

    @Override
    public int getConvertedY(float offset, float unitsPerWorldUnit)
    {
        return Math.round(getY() * unitsPerWorldUnit - offset);
    }
}
