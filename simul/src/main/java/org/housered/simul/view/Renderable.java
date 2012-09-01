package org.housered.simul.view;

public interface Renderable
{
    public static final byte ROAD_Z_ORDER = 1;
    public static final byte BUILDING_Z_ORDER = 10;
    public static final byte PERSON_Z_ORDER = 14;
    public static final byte GUI_Z_ORDER = 20;
    
    void render(GraphicsAdapter r);

    /**
     * Things get rendered in Z-order, low to high.
     */
    byte getZOrder();
}
