package net.pubnative.library.request;

import java.util.ArrayList;

import net.pubnative.library.model.NativeAdModel;

public interface AdRequestListener
{
    /**
     * Invoked when ad request is started
     * @param request Request object used for making the request
     */
    void onAdRequestStarted(AdRequest request);

    /**
     * Invoked when ad request is completed
     * @param request Request object used for making the request
     * @param ads     List of ads received
     */
    void onAdRequestFinished(AdRequest request, ArrayList<? extends NativeAdModel> ads);

    /**
     * Invoked when ad request fails
     * @param request Request object used for making the request
     * @param ex      Exception that caused the failure
     */
    void onAdRequestFailed(AdRequest request, Exception ex);
}
