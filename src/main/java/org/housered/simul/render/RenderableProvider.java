package org.housered.simul.render;

public interface RenderableProvider
{
    void beginRender();
    
    Iterable<Renderable> getRenderables();
    
    void endRender();
}
