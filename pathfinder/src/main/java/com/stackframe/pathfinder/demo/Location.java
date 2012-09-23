// Copyright (C) 2002-2010 StackFrame, LLC http://www.stackframe.com/
// This software is provided under the GNU General Public License, version 2.
package com.stackframe.pathfinder.demo;

import com.stackframe.pathfinder.Node;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Location implements Node<Location>, Serializable {

    private final int x;
    private final int y;
    private int height;
    private boolean blocked;
    private transient List<Location> neighbors;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
        neighbors = new ArrayList<Location>();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean getBlocked() {
        return blocked;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getDistance(Location dest) {
        double a = dest.x - x;
        double b = dest.y - y;
        return Math.sqrt(a * a + b * b);
    }

    public double pathCostEstimate(Location goal) {
        return getDistance(goal) * 0.99;
    }

    public double traverseCost(Location target) {
        double distance = getDistance(target);
        double diff = target.getHeight() - getHeight();
        return Math.abs(diff) + distance;
    }

    public Iterable<Location> neighbors() {
        List<Location> realNeighbors = new ArrayList<Location>();
        if (!blocked) {
            for (Location loc : neighbors) {
                if (!loc.blocked) {
                    realNeighbors.add(loc);
                }
            }
        }

        return realNeighbors;
    }

    public void addNeighbor(Location l) {
        neighbors.add(l);
    }

    @Override
    public String toString() {
        return "{x=" + x + ",y=" + y + "}";
    }

    private synchronized void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        neighbors = new ArrayList<Location>();
    }

}
