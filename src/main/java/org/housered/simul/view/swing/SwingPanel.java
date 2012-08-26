package org.housered.simul.view.swing;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import org.housered.simul.view.GraphicsAdapter;
import org.housered.simul.view.Renderable;
import org.housered.simul.view.RenderableProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwingPanel extends JPanel
{
    private static final long serialVersionUID = 368187900828450340L;
    private static Logger LOGGER = LoggerFactory.getLogger(SwingPanel.class);

    private final RenderableProvider renderables;

    public SwingPanel(RenderableProvider renderables)
    {
        this.renderables = renderables;
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);

        Graphics2D g2 = (Graphics2D) g;

        renderables.beginRender();
        GraphicsAdapter strategy = new SwingGraphicsAdapter(g2, 0, 0, 1);
        
        for (Renderable r : renderables.getRenderables())
        {
            r.render(strategy);
        }

        renderables.endRender();
    }
}
