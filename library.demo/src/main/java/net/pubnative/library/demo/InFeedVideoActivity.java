package net.pubnative.library.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import net.pubnative.library.demo.utils.Settings;
import net.pubnative.library.feed.video.PubnativeFeedVideo;


public class InFeedVideoActivity extends InFeedActivity implements PubnativeFeedVideo.Listener {

    private static final String TAG = InFeedVideoActivity.class.getSimpleName();

    private PubnativeFeedVideo mFeedVideo;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mFeedVideo = savedInstanceState.getParcelable("feedVideo");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("feedVideo", mFeedVideo);
    }

    @Override
    protected void onStop() {
        if (mFeedVideo != null) {
            mFeedVideo.hide();
        }
        super.onStop();
    }

    @Override
    protected void onResume() {

        super.onResume();
        if (mFeedVideo != null) {
            mFeedVideo.load(this, Settings.getAppToken());
        }
    }

    @Override
    public void request() {

        Log.v(TAG, "request");

        startLoading();

        if (mFeedVideo != null) {
            mFeedVideo.destroy();
        }

        mFeedVideo = new PubnativeFeedVideo();
        mFeedVideo.setListener(this);
        mFeedVideo.load(this, Settings.getAppToken());
    }

    @Override
    public boolean isReady() {

        Log.v(TAG, "isReady");

        boolean result = false;
        if (mFeedVideo != null) {
            result = mFeedVideo.isReady();
        }
        return result;
    }

    @Override
    public void show(ViewGroup container) {


        Log.v(TAG, "show");
        View adView = mFeedVideo.getView();
        ViewGroup parent = (ViewGroup)adView.getParent();
        if(parent != null) {
            parent.removeView(adView);
        }
        container.addView(adView);
    }

    //==============================================================================================
    // Callbacks
    //==============================================================================================
    // PubnativeFeedBanner.Listener
    //----------------------------------------------------------------------------------------------
    @Override
    public void onPubnativeFeedVideoLoadFinish(PubnativeFeedVideo video) {

        Log.v(TAG, "onPubnativeFeedVideoLoadFinish");
        stopLoading();
    }

    @Override
    public void onPubnativeFeedVideoLoadFail(PubnativeFeedVideo video, Exception exception) {

        Log.v(TAG, "onPubnativeFeedVideoLoadFail");
        stopLoading();
    }
}
