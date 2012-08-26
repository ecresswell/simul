package org.housered.simul.model.location;

public interface Vector
{
    float getX();

    float getY();

    void setX(float x);

    void setY(float y);

    Vector subtractCopy(Vector v);

    Vector scaleToMagnitudeCopy(float wantedMagnitude);

    float magnitude();

    void increment(Vector delta);

    boolean equals(Object obj);
}
