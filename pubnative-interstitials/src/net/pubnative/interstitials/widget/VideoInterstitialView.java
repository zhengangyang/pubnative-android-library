package net.pubnative.interstitials.widget;

import net.pubnative.interstitials.R;
import net.pubnative.interstitials.util.ScreenUtil;
import net.pubnative.library.inner.PubNativeWorker;
import net.pubnative.library.util.ViewUtil;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.TextureView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class VideoInterstitialView extends RelativeLayout
{
    private TextureView       videoView;
    private ImageView         bannerView;
    private BroadcastReceiver onVideoPreparedReceiver;

    public VideoInterstitialView(Context context)
    {
        super(context);
        init();
    }

    public VideoInterstitialView(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);
        init();
    }

    public VideoInterstitialView(Context context, AttributeSet attributeSet, int defStyle)
    {
        super(context, attributeSet, defStyle);
        init();
    }

    public void init()
    {
        inflate(getContext(), R.layout.pn_view_video_interstitial, this);
        videoView = (TextureView) findViewById(R.id.view_video);
        bannerView = (ImageView) findViewById(R.id.view_banner);
        onVideoPreparedReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context arg0, Intent arg1)
            {
                applyOrientation();
            }
        };
    }

    public void onResume()
    {
        getContext().registerReceiver(onVideoPreparedReceiver, new IntentFilter(PubNativeWorker.broadcastOnVideoPreparedKey));
    }

    public void onStop()
    {
        getContext().unregisterReceiver(onVideoPreparedReceiver);
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        applyOrientation();
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        applyOrientation();
    }

    private void applyOrientation()
    {
        boolean isPortrait = ScreenUtil.isPortrait(getContext());
        if (isPortrait)
        {
            ViewUtil.setSize(videoView, bannerView.getWidth(), bannerView.getHeight());
        }
        else
        {
            DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
            ViewUtil.setSize(videoView, metrics.widthPixels, metrics.heightPixels);
        }
    }
}
