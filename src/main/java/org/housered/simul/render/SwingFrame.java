package org.housered.simul.render;

import javax.swing.JFrame;

public class SwingFrame extends JFrame
{
    private static final long serialVersionUID = 2000214150960328157L;

    public SwingFrame()
    {
        add(new SwingPanel());

        setTitle("Simul");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }
}
