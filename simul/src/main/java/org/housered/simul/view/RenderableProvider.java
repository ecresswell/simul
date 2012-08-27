package org.housered.simul.view;

import org.housered.simul.model.world.Camera;

public interface RenderableProvider
{
    Camera getCamera();
    
    void beginRender();
    
    Iterable<Renderable> getRenderables();
    
    void endRender();
}
