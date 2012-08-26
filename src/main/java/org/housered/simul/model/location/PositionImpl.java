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
    public float getConvertedXAsFloat(float offset, float unitsPerWorldUnit)
    {
        return getX() * unitsPerWorldUnit - offset;
    }

    @Override
    public float getConvertedYAsFloat(float offset, float unitsPerWorldUnit)
    {
        return getY() * unitsPerWorldUnit - offset;
    }

    @Override
    public int getConvertedX(int offset, float unitsPerWorldUnit)
    {
        return Math.round(getConvertedXAsFloat(offset, unitsPerWorldUnit));
    }

    @Override
    public int getConvertedY(int offset, float unitsPerWorldUnit)
    {
        return Math.round(getConvertedYAsFloat(offset, unitsPerWorldUnit));
    }
}
