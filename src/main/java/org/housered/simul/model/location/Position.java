package org.housered.simul.model.location;

public interface Position extends Vector
{
    void setX(float x);

    void setY(float y);

    int getConvertedX(double offset, double unitsPerWorldUnit);

    int getConvertedY(double offset, double unitsPerWorldUnit);
}
