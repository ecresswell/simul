package org.housered.simul.model.location;

public interface Dimension extends Vector
{
    float getWidth();

    float getHeight();

    void setWidth(float width);

    void setHeight(float height);

    int getConvertedWidth(double unitsPerWorldUnit);

    int getConvertedHeight(double unitsPerWorldUnit);
}
