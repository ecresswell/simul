package org.housered.simul.model.location;

public class DimensionImpl extends VectorImpl implements Dimension
{
    public DimensionImpl(float width, float height)
    {
        super(width, height);
    }

    public DimensionImpl()
    {
        super();
    }

    @Override
    public float getWidth()
    {
        return getX();
    }

    @Override
    public float getHeight()
    {
        return getY();
    }

    @Override
    public int getConvertedWidth(double unitsPerWorldUnit)
    {
        return (int) Math.round(getWidth() * unitsPerWorldUnit);
    }

    @Override
    public int getConvertedHeight(double unitsPerWorldUnit)
    {
        return (int) Math.round(getHeight() * unitsPerWorldUnit);
    }

    @Override
    public void setWidth(float width)
    {
        setX(width);
    }

    @Override
    public void setHeight(float height)
    {
        setY(height);
    }
}
