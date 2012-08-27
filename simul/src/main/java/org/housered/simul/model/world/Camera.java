package org.housered.simul.model.world;

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

    void zoom(float delta)
    {
        //want to change the x and y offsets by half the amount we just lost
        //        float changeInZoom = unitsPerWorldUnit - (unitsPerWorldUnit + unitsPerWorldUnit * delta);
        //        float gameUnitWidthLoss = screenWidth * (1 /changeInZoom);
        //        float gameUnitHeightLoss = screenHeight * (1 /changeInZoom);

        //        float gameWidthLost = screenWidth / unitsPerWorldUnit - screenWidth / (unitsPerWorldUnit + unitsPerWorldUnit * delta);
        //        float gameHeightLost = screenHeight / unitsPerWorldUnit - screenHeight / (unitsPerWorldUnit + unitsPerWorldUnit * delta);

        //incrementXOffset(gameWidthLost / 2);
        //incrementYOffset(gameHeightLost / 2);

        double gameWidthLost = (double) screenWidth * (zoom - zoom * delta);
        double gameHeightLost = (double) screenHeight * (zoom - zoom * delta);

        zoom *= delta;

        if (zoom < MINMIUM_ZOOM)
        {
            zoom = MINMIUM_ZOOM;
            return;
        }

        incrementXOffset(gameWidthLost / 2);
        incrementYOffset(gameHeightLost / 2);
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
        //LOGGER.debug("'{}'", 1 / zoom);
        return (double) 1 / zoom;
    }
}
