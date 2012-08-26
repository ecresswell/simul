package org.housered.simul.model.location;

public class VectorImpl implements Vector
{
    private float x;
    private float y;

    public VectorImpl()
    {
        x = 0;
        y = 0;
    }

    public VectorImpl(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector subtract(Vector v)
    {
        return new VectorImpl(getX() - v.getX(), getY() - v.getY());
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
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
}
