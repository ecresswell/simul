package org.housered.simul.view.swing;

import javax.swing.JFrame;

import org.housered.simul.view.RenderableProvider;

public class SwingFrame extends JFrame
{
    private static final long serialVersionUID = 2000214150960328157L;

    public SwingFrame(int width, int height, RenderableProvider renderables)
    {
        add(new SwingPanel(renderables));

        setTitle("Simul");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(width, height);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }
}
