package net.pubnative.library.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import net.pubnative.library.demo.utils.Settings;
import net.pubnative.library.interstitial.PubnativeInterstitial;

public class InterstitialAdActivity extends Activity implements PubnativeInterstitial.Listener {

    private static final String TAG = InterstitialAdActivity.class.getName();
    private RelativeLayout mLoaderContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial);
        mLoaderContainer = (RelativeLayout) findViewById(R.id.activity_native_container_loader);
    }

    public void onRequestClick(View v) {

        Log.v(TAG, "onRequestClick");
        mLoaderContainer.setVisibility(View.VISIBLE);
        PubnativeInterstitial interstitial = new PubnativeInterstitial();
        interstitial.setListener(this);
        interstitial.load(this, Settings.getAppToken(), Settings.getZoneId());
    }

    //==============================================================================================
    // Callbacks
    //==============================================================================================
    // PubnativeInterstitial.Listener
    //----------------------------------------------------------------------------------------------
    @Override
    public void onPubnativeInterstitialLoadFinish(PubnativeInterstitial interstitial) {
        Log.v(TAG, "onPubnativeInterstitialLoadFinish");
        interstitial.show();
    }

    @Override
    public void onPubnativeInterstitialLoadFail(PubnativeInterstitial interstitial, Exception exception) {
        Log.v(TAG, "onPubnativeInterstitialLoadFail", exception);
        mLoaderContainer.setVisibility(View.GONE);
    }

    @Override
    public void onPubnativeInterstitialShow(PubnativeInterstitial interstitial) {
        Log.v(TAG, "onPubnativeInterstitialShow");
        mLoaderContainer.setVisibility(View.GONE);
    }

    @Override
    public void onPubnativeInterstitialImpressionConfirmed(PubnativeInterstitial interstitial) {
        Log.v(TAG, "onPubnativeInterstitialImpressionConfirmed");
    }

    @Override
    public void onPubnativeInterstitialClick(PubnativeInterstitial interstitial) {
        Log.v(TAG, "onPubnativeInterstitialClick");
    }

    @Override
    public void onPubnativeInterstitialHide(PubnativeInterstitial interstitial) {
        Log.v(TAG, "onPubnativeInterstitialHide");
    }
}
