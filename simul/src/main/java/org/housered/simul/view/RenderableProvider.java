package org.housered.simul.view;

import org.housered.simul.model.world.Camera;

public interface RenderableProvider
{
    Camera getCamera();

    void beginRender();

    /**
     * Returns an iterator over the ordered, by z-order low to high, renderables.
     */
    Iterable<Renderable> getZOrderedRenderables();

    void endRender();
}
