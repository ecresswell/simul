package org.housered.simul.model.location;

public interface Position extends Vector
{
    void setX(float x);

    void setY(float y);

    int getConvertedX(float offset, float unitsPerWorldUnit);

    int getConvertedY(float offset, float unitsPerWorldUnit);
}
