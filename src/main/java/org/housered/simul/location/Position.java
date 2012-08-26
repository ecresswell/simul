package org.housered.simul.location;

public interface Position
{
    void setX(float x);

    void setY(float y);

    float getX();

    float getY();

    float getConvertedXAsFloat(float offset, float unitsPerWorldUnit);

    float getConvertedYAsFloat(float offset, float unitsPerWorldUnit);

    int getConvertedX(int offset, float unitsPerWorldUnit);

    int getConvertedY(int offset, float unitsPerWorldUnit);
}
