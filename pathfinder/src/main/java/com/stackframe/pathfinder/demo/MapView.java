// Copyright (C) 2002-2010 StackFrame, LLC http://www.stackframe.com/
// This software is provided under the GNU General Public License, version 2.
package com.stackframe.pathfinder.demo;

import java.awt.AlphaComposite;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import javax.swing.JPanel;

public class MapView extends JPanel implements MouseListener {

    private static final int width = 12;
    private Map map;
    private Location start, goal;
    private List<Location> path;

    public MapView(Map map, Location start, Location goal) {
        this.map = map;
        this.start = start;
        this.goal = goal;
        setBackground(Color.gray);
        addMouseListener(this);
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled) {
            addMouseListener(this);
        } else {
            removeMouseListener(this);
        }
    }

    protected double getCellWidth() {
        return (double)getWidth() / (double)map.getXSize();
    }

    protected double getCellHeight() {
        return (double)getHeight() / (double)map.getYSize();
    }

    public void mouseClicked(MouseEvent e) {
        int x = (int)(e.getX() / getCellWidth());
        int y = (int)(e.getY() / getCellHeight());
        Location loc = map.getLocation(x, y);
        if (e.isControlDown()) {
            loc.setBlocked(true);
        } else if (e.isShiftDown()) {
            loc.setBlocked(false);
            loc.setHeight(0);
        } else {
            loc.setHeight(100);
        }

        repaint();
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void setMap(Map map) {
        this.map = map;
        repaint();
    }

    public void setStart(Location start) {
        this.start = start;
        repaint();
    }

    public void setGoal(Location goal) {
        this.goal = goal;
        repaint();
    }

    public void setPath(List<Location> path) {
        this.path = path;
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width * map.getXSize(), width * map.getYSize());
    }

    private Ellipse2D buildCircle(int x, int y, double radius) {
        double a = x * getCellWidth() + (getCellWidth() - radius) / 2.0;
        double b = y * getCellHeight() + (getCellHeight() - radius) / 2.0;
        return new Ellipse2D.Double(a, b, radius, radius);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        for (int i = 0; i < map.getXSize(); i++) {
            for (int j = 0; j < map.getYSize(); j++) {
                Location t = map.getLocation(i, j);
                if (t.getBlocked()) {
                    g2.setColor(Color.black);
                } else if (t.getHeight() == 0) {
                    g2.setColor(Color.gray);
                } else {
                    g2.setColor(new Color(0, 0, 255));
                }

                Shape cell = new Rectangle2D.Double(i * getCellWidth(), j * getCellHeight(), getCellWidth(), getCellHeight());
                g2.fill(cell);
            }
        }

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double pathWidth = Math.min(getCellWidth() / 2.0, getCellHeight() / 2.0);

        g2.setColor(Color.green);
        Shape sCircle = buildCircle(start.getX(), start.getY(), pathWidth);
        g2.fill(sCircle);

        g2.setColor(Color.red);
        sCircle = buildCircle(goal.getX(), goal.getY(), pathWidth);
        g2.fill(sCircle);

        pathWidth = Math.min(getCellWidth() / 3.0, getCellHeight() / 3.0);

        if (path != null) {
            g2.setColor(Color.white);
            int pathLength = path.size();
            float minAlpha = 0.2f;
            for (int i = 0; i < pathLength; i++) {
                Location loc = path.get(i);
                float alpha = minAlpha + ((float)i / (float)pathLength) * (1.0f - minAlpha);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                sCircle = buildCircle(loc.getX(), loc.getY(), pathWidth);
                g2.fill(sCircle);
            }
        }
    }

}
