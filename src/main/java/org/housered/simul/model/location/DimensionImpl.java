package org.housered.simul.model.location;

public class DimensionImpl implements Dimension
{
    private float width;
    private float height;

    public DimensionImpl(float width, float height)
    {
        setWidth(width);
        setHeight(height);
    }

    @Override
    public float getWidth()
    {
        return width;
    }

    @Override
    public float getHeight()
    {
        return height;
    }

    @Override
    public float getConvertedWidthAsFloat(float unitsPerWorldUnit)
    {
        return width * unitsPerWorldUnit;
    }

    @Override
    public float getConvertedHeightAsFloat(float unitsPerWorldUnit)
    {
        return height * unitsPerWorldUnit;
    }

    @Override
    public int getConvertedWidth(float unitsPerWorldUnit)
    {
        return Math.round(getConvertedWidthAsFloat(unitsPerWorldUnit));
    }

    @Override
    public int getConvertedHeight(float unitsPerWorldUnit)
    {
        return Math.round(getConvertedHeightAsFloat(unitsPerWorldUnit));
    }

    @Override
    public void setWidth(float width)
    {
        this.width = width;
    }

    @Override
    public void setHeight(float height)
    {
        this.height = height;
    }

}
