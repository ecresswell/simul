package org.housered.simul.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MouseListenerManager implements MouseListener
{
    private ConcurrentLinkedQueue<MouseEvent> mouseReleased = new ConcurrentLinkedQueue<MouseEvent>();

    public List<MouseEvent> unbufferMouseReleased()
    {
        List<MouseEvent> result = new ArrayList<MouseEvent>(mouseReleased);
        mouseReleased.clear();
        return result;
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        mouseReleased.add(e);
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {

    }

    @Override
    public void mouseExited(MouseEvent e)
    {

    }

}
