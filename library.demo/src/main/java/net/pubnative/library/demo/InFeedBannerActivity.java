package net.pubnative.library.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import net.pubnative.library.demo.utils.Settings;
import net.pubnative.library.feed.banner.PubnativeFeedBanner;


public class InFeedBannerActivity extends Activity implements PubnativeFeedBanner.Listener {

    private static final String TAG = InFeedBannerActivity.class.getName();
    private RelativeLayout mLoaderContainer;
    private RelativeLayout mFeedBannerContainer;

    private PubnativeFeedBanner mFeedBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infeed_banner);
        mLoaderContainer = (RelativeLayout) findViewById(R.id.activity_infeed_banner_container_loader);
        mFeedBannerContainer = (RelativeLayout) findViewById(R.id.activity_infeed_banner_container);
    }

    public void onRequestClick(View v) {

        Log.v(TAG, "onRequestClick");
        mLoaderContainer.setVisibility(View.VISIBLE);
        if(mFeedBanner != null) {
            mFeedBanner.destroy();
        }
        mFeedBanner = new PubnativeFeedBanner();
        mFeedBanner.setListener(this);
        mFeedBanner.load(this, Settings.getAppToken());
    }

    //==============================================================================================
    // Callbacks
    //==============================================================================================
    // PubnativeFeedBanner.Listener
    //----------------------------------------------------------------------------------------------

    @Override
    public void onPubnativeFeedBannerLoadFinish(PubnativeFeedBanner feedBanner) {
        Log.v(TAG, "onPubnativeFeedBannerLoadFinish");
        if(mFeedBanner == feedBanner) {
            feedBanner.show(mFeedBannerContainer);
            mLoaderContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPubnativeFeedBannerLoadFailed(PubnativeFeedBanner feedBanner, Exception exception) {
        Log.v(TAG, "onPubnativeFeedBannerLoadFailed");
        mLoaderContainer.setVisibility(View.GONE);
    }

    @Override
    public void onPubnativeFeedBannerShow(PubnativeFeedBanner feedBanner) {
        Log.v(TAG, "onPubnativeFeedBannerShow");
    }

    @Override
    public void onPubnativeFeedBannerImpressionConfirmed(PubnativeFeedBanner feedBanner) {
        Log.v(TAG, "onPubnativeFeedBannerImpressionConfirmed");
    }

    @Override
    public void onPubnativeFeedBannerClick(PubnativeFeedBanner feedBanner) {
        Log.v(TAG, "onPubnativeFeedBannerClick");
    }
}
