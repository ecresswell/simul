package org.housered.simul.model.location;

public interface Dimension
{
    float getWidth();

    float getHeight();

    void setWidth(float width);

    void setHeight(float height);

    float getConvertedWidthAsFloat(float unitsPerWorldUnit);

    float getConvertedHeightAsFloat(float unitsPerWorldUnit);

    int getConvertedWidth(float unitsPerWorldUnit);

    int getConvertedHeight(float unitsPerWorldUnit);
}
