package org.housered.simul.model.navigation.road;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import org.housered.simul.model.location.BoundingBox;
import org.housered.simul.model.location.Vector;
import org.housered.simul.model.world.GameClock;
import org.housered.simul.model.world.GameObject;
import org.housered.simul.view.GraphicsAdapter;
import org.housered.simul.view.Renderable;

public class Road implements Renderable, BoundingBox, GameObject
{
    public enum Direction
    {
        NORTH, EAST, SOUTH, WEST
    }

    private Vector position;
    private Vector size;
    private Direction direction;

    public Road(Vector position, Vector size)
    {
        this(position, size, Direction.NORTH);
    }
    
    public Road(Rectangle2D.Double bounds)
    {
        this(new Vector(bounds.x, bounds.y), new Vector(bounds.width, bounds.height));
    }
    
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
