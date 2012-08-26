package org.housered.simul.model.location;

public interface Position extends Vector
{
    void setX(float x);

    void setY(float y);

    float getConvertedXAsFloat(float offset, float unitsPerWorldUnit);

    float getConvertedYAsFloat(float offset, float unitsPerWorldUnit);

    int getConvertedX(int offset, float unitsPerWorldUnit);

    int getConvertedY(int offset, float unitsPerWorldUnit);
}
