package net.pubnative.library.renderer;

public interface AdRendererListener
{
    /**
     * Invoked when ad rendering starts
     * @param renderer Renderer object used to render the ad
     */
    void onAdRenderStarted(AdRenderer renderer);

    /**
     * Invokded when ad rendering fails
     * @param renderer Renderer object used to render the ad
     * @param e        Exception that caused the failure
     */
    void onAdRenderFailed(AdRenderer renderer, Exception e);

    /**
     * Invoked when ad rendering is finished
     * @param renderer Renderer object used to render the ad
     */
    void onAdRenderFinished(AdRenderer renderer);
}
