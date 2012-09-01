package org.housered.simul.model.navigation;

import java.awt.Color;

import org.housered.simul.model.location.BoundingBox;
import org.housered.simul.model.location.Vector;
import org.housered.simul.view.GraphicsAdapter;
import org.housered.simul.view.Renderable;

public class Road implements Renderable, BoundingBox
{
    enum Direction
    {
        NORTH, EAST, SOUTH, WEST
    }

    private Vector position;
    private Vector size;
    private Direction direction;

    public Road(Vector position, Vector size, Direction direction)
    {
        this.position = position;
        this.size = size;
        this.direction = direction;
    }

    public Direction getTrafficDirection()
    {
        return direction;
    }

    @Override
    public void render(GraphicsAdapter r)
    {
        r.setColour(Color.gray);
        r.fillRect(position, size);
    }

    @Override
    public byte getZOrder()
    {
        return ROAD_Z_ORDER;
    }

    @Override
    public Vector getPosition()
    {
        return position;
    }

    @Override
    public Vector getSize()
    {
        return size;
    }

}
