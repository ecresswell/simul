package org.housered.simul.model.location;

public class Dimension extends Vector
{
    public Dimension(float width, float height)
    {
        super(width, height);
    }

    public Dimension()
    {
        super();
    }

    public float getWidth()
    {
        return getX();
    }

    public float getHeight()
    {
        return getY();
    }

    public int getConvertedWidth(double unitsPerWorldUnit)
    {
        return (int) Math.round(getWidth() * unitsPerWorldUnit);
    }

    public int getConvertedHeight(double unitsPerWorldUnit)
    {
        return (int) Math.round(getHeight() * unitsPerWorldUnit);
    }

    public void setWidth(float width)
    {
        setX(width);
    }

    public void setHeight(float height)
    {
        setY(height);
    }
}
