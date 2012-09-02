package org.housered.simul.model.location;

import straightedge.geom.KPoint;

public class Vector extends KPoint
{
    public static final double EPSILON = 0.000001f;

    public Vector()
    {
    }

    public Vector(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector(KPoint old)
    {
        this.x = old.x;
        this.y = old.y;
    }

    public Vector copy()
    {
        return new Vector(this);
    }

    @Override
    public Vector translateCopy(double xIncrement, double yIncrement)
    {
        return new Vector(x + xIncrement, y + yIncrement);
    }

    public Vector translateCopy(Vector pointIncrement)
    {
        return new Vector(x + pointIncrement.x, y + pointIncrement.y);
    }

    public Vector negateCopy()
    {
        return new Vector(-x, -y);
    }

    public Vector scaleCopy(double scale)
    {
        return new Vector(x * scale, y * scale);
    }

    @Override
    public String toString()
    {
        return "Vector [x=" + x + ", y=" + y + "]";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        if (!nearlyEqual(x, other.x, EPSILON) || !nearlyEqual(y, other.y, EPSILON))
            return false;

        return true;
    }

    public static boolean nearlyEqual(double a, double b, double epsilon)
    {
        final double absA = Math.abs(a);
        final double absB = Math.abs(b);
        final double diff = Math.abs(a - b);

        if (a == b)
        { // shortcut, handles infinities
            return true;
        }
        else if (a * b == 0)
        { // a or b or both are zero
            // relative error is not meaningful here
            return diff < (epsilon * epsilon);
        }
        else
        { // use relative error
            return diff / (absA + absB) < epsilon;
        }
    }

}
