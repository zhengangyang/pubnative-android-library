package net.pubnative.library.demo;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import net.pubnative.library.demo.utils.Settings;
import net.pubnative.library.interstitial.PubnativeInterstitial;
import net.pubnative.library.video.PubnativeVideo;

public class VideoAdActivity extends Activity implements PubnativeVideo.Listener {

    private static final String TAG = VideoAdActivity.class.getName();

    private RelativeLayout mLoaderContainer;
    private PubnativeVideo mVideo;

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

        mVideo = new PubnativeVideo();
        mVideo.setListener(this);
        mVideo.load(this, Settings.getAppToken());
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    //==============================================================================================
    // Callbacks
    //==============================================================================================
    // PubnativeVideo.Listener
    //----------------------------------------------------------------------------------------------
    @Override
    public void onPubnativeVideoLoadFinish(PubnativeVideo video) {

        Log.v(TAG, "onPubnativeVideoLoadFinish");
        showToast("Video load finished");
        mLoaderContainer.setVisibility(View.GONE);
        mVideo.show();
    }

    @Override
    public void onPubnativeVideoLoadFail(PubnativeVideo video, Exception exception) {

        Log.v(TAG, "onPubnativeVideoLoadFail");
        showToast("Video load failed");
        mLoaderContainer.setVisibility(View.GONE);
    }

    @Override
    public void onPubnativeVideoShow(PubnativeVideo video) {

        Log.v(TAG, "onPubnativeVideoShow");
        showToast("Video show");
    }

    @Override
    public void onPubnativeVideoHide(PubnativeVideo video) {

        Log.v(TAG, "onPubnativeVideoHide");
        showToast("Video hide");
    }

    @Override
    public void onPubnativeVideoStart(PubnativeVideo video) {

        Log.v(TAG, "onPubnativeVideoStart");
        showToast("Video start");
    }

    @Override
    public void onPubnativeVideoFinish(PubnativeVideo video) {

        Log.v(TAG, "onPubnativeVideoFinish");
        showToast("Video finish");
        mVideo.destroy();
    }

    @Override
    public void onPubnativeVideoClick(PubnativeVideo video) {

        Log.v(TAG, "onPubnativeVideoClick");
        showToast("Video click");
    }
}
