package org.housered.simul.view;

public interface RenderableProvider
{
    void beginRender();
    
    Iterable<Renderable> getRenderables();
    
    void endRender();
}
