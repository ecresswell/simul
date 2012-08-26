package org.housered.simul.location;

public class PositionImpl implements Position
{
    private float x;
    private float y;
    
    public PositionImpl(float x, float y)
    {
        setX(x);
        setY(y);
    }
    
    @Override
    public void setX(float x)
    {
        this.x = x;
    }

    @Override
    public void setY(float y)
    {
        this.y = y;
    }

    @Override
    public float getX()
    {
        return x;
    }

    @Override
    public float getY()
    {
        return y;
    }

    @Override
    public float getConvertedXAsFloat(float offset, float unitsPerWorldUnit)
    {
        return this.x * unitsPerWorldUnit - offset;
    }

    @Override
    public float getConvertedYAsFloat(float offset, float unitsPerWorldUnit)
    {
        return this.y * unitsPerWorldUnit - offset;
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
