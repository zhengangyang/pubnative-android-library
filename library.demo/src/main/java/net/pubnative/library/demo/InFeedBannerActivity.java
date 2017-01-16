package net.pubnative.library.demo;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import net.pubnative.library.demo.utils.Settings;
import net.pubnative.library.feed.banner.PubnativeFeedBanner;


public class InFeedBannerActivity extends InFeedActivity implements PubnativeFeedBanner.Listener {

    private static final String TAG = InFeedBannerActivity.class.getName();

    private PubnativeFeedBanner mFeedBanner;

    @Override
    public void request() {

        Log.v(TAG, "request");
        startLoading();

        if(mFeedBanner == null) {
            mFeedBanner = new PubnativeFeedBanner();
            mFeedBanner.setListener(this);
        }
        mFeedBanner.load(this, Settings.getAppToken(), Settings.getZoneId());
    }

    @Override
    public boolean isReady() {

        Log.v(TAG, "isReady");
        boolean result = false;
        if (mFeedBanner != null) {
            result = mFeedBanner.isReady();
        }
        return result;
    }

    @Override
    public void show(ViewGroup container) {

        Log.v(TAG, "show");

        View adView = mFeedBanner.getView();
        ViewGroup parent = (ViewGroup)adView.getParent();
        if(parent != null) {
            parent.removeView(adView);
        }
        container.addView(adView);
    }

    //==============================================================================================
    // Callbacks
    //==============================================================================================

    //----------------------------------------------------------------------------------------------
    // PubnativeFeedBanner.Listener
    //----------------------------------------------------------------------------------------------

    @Override
    public void onPubnativeFeedBannerLoadFinish(PubnativeFeedBanner feedBanner) {
        Log.v(TAG, "onPubnativeFeedBannerLoadFinish");
        stopLoading();
    }

    @Override
    public void onPubnativeFeedBannerLoadFailed(PubnativeFeedBanner feedBanner, Exception exception) {
        Log.v(TAG, "onPubnativeFeedBannerLoadFailed");
        stopLoading();
    }

    @Override
    public void onPubnativeFeedBannerImpressionConfirmed(PubnativeFeedBanner feedBanner) {
        Log.v(TAG, "onPubnativeFeedBannerLoadFailed");
    }

    @Override
    public void onPubnativeFeedBannerClick(PubnativeFeedBanner feedBanner) {
        Log.v(TAG, "onPubnativeFeedBannerLoadFailed");
    }
}
