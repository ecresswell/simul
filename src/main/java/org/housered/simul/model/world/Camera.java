package org.housered.simul.model.world;

public class Camera
{
    static final float DEFAULT_CAMERA_MOVE = 5;
    static final float DEFAULT_CAMERA_ZOOM = 0.05f;
    private float xOffset;
    private float yOffset;
    private float unitsPerWorldUnit = 1;
    
    void incrementXOffset(float delta)
    {
        this.xOffset += delta;
    }
    
    void incrementYOffset(float delta)
    {
        this.yOffset += delta;
    }
    
    void zoomIn(float delta)
    {
        unitsPerWorldUnit += unitsPerWorldUnit * delta;
    }
    
    public float getXOffset()
    {
        return xOffset;
    }

    public float getYOffset()
    {
        return yOffset;
    }

    public float getUnitsPerWorldUnit()
    {
        return unitsPerWorldUnit;
    }
}
