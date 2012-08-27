package org.housered.simul.model.location;

public class Vector
{
    private float x;
    private float y;

    public Vector()
    {
        x = 0;
        y = 0;
    }

    public Vector(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector subtractCopy(Vector v)
    {
        return new Vector(getX() - v.getX(), getY() - v.getY());
    }

    public Vector scaleToMagnitudeCopy(float wantedMagnitude)
    {
        if (magnitude() == 0)
            return new Vector();
        
        float scale = wantedMagnitude / magnitude();
        return new Vector(x * scale, y * scale);
    }

    public float magnitude()
    {
        return (float) Math.sqrt(x * x + y * y);
    }

    public void increment(Vector delta)
    {
        x += delta.getX();
        y += delta.getY();
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public void setX(float x)
    {
        this.x = x;
    }

    public void setY(float y)
    {
        this.y = y;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(x);
        result = prime * result + Float.floatToIntBits(y);
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vector other = (Vector) obj;
        if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
            return false;
        if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "VectorImpl [x=" + x + ", y=" + y + "]";
    }

}
