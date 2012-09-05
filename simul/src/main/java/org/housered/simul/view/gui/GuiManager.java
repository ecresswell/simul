package org.housered.simul.view.gui;

import java.awt.Color;

import org.housered.simul.model.world.GameClock;
import org.housered.simul.model.world.GameObject;
import org.housered.simul.view.GraphicsAdapter;
import org.housered.simul.view.Renderable;

public class GuiManager implements Renderable, GameObject
{
    private final GameClock gameClock;

    public GuiManager(GameClock gameClock)
    {
        this.gameClock = gameClock;
        
    }
    
    @Override
    public void render(GraphicsAdapter r)
    {
        r.setColour(Color.white);
        r.drawAbsoluteText(20, 20, gameClock.getDigitalClock());
    }
    
    @Override
    public byte getZOrder()
    {
        return GUI_Z_ORDER;
    }
}
