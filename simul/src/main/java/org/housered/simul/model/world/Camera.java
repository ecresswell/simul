package org.housered.simul.model.world;

import static org.housered.simul.model.location.Vector.v;

import org.housered.simul.model.location.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Camera
{
    private static Logger LOGGER = LoggerFactory.getLogger(Camera.class);
    static final float DEFAULT_CAMERA_MOVE = 5;
    static final float DEFAULT_CAMERA_ZOOM_IN = 0.95f;
    static final float DEFAULT_CAMERA_ZOOM_OUT = 1.05f;
    static final float MINMIUM_ZOOM = 0.05f;

    private final int screenWidth;
    private final int screenHeight;
    private double xOffset;
    private double yOffset;
    private double zoom = 1;

    public Camera(int screenWidth, int screenHeight)
    {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    void incrementXOffset(double delta)
    {
        this.xOffset += delta;
    }

    void incrementYOffset(double delta)
    {
        this.yOffset += delta;
    }

    void zoom(double delta)
    {
        double gameWidthLost = (double) screenWidth * (zoom - zoom * delta);
        double gameHeightLost = (double) screenHeight * (zoom - zoom * delta);

        zoom *= delta;

        if (zoom < MINMIUM_ZOOM)
        {
            zoom = MINMIUM_ZOOM;
            return;
        }

        incrementXOffset(-gameWidthLost / 2);
        incrementYOffset(-gameHeightLost / 2);
    }

    /**
     * The offset in game units
     */
    public double getXOffset()
    {
        return xOffset;
    }

    public double getYOffset()
    {
        return yOffset;
    }

    public double getUnitsPerWorldUnit()
    {
        return (double) 1 / zoom;
    }

    public Vector translateIntoScreenSpace(Vector gameUnitPosition)
    {
        return gameUnitPosition.translateCopy(xOffset, yOffset).scaleCopy(getUnitsPerWorldUnit());
    }

    public Vector scaleIntoScreenSpace(Vector gameUnitDimension)
    {
        return gameUnitDimension.scaleCopy(getUnitsPerWorldUnit());
    }

    public double scaleIntoScreenSpace(double length)
    {
        return length * getUnitsPerWorldUnit();
    }
    
    public int getScreenWidth()
    {
        return screenWidth;
    }
    
    public int getScreenHeight()
    {
        return screenWidth;
    }

    public Vector translateIntoWorldSpace(int x, int y)
    {
        double newX = ((double) x) / getUnitsPerWorldUnit() - xOffset;
        double newY = ((double) y) / getUnitsPerWorldUnit() - yOffset;
        return v(newX, newY);
    }
}
