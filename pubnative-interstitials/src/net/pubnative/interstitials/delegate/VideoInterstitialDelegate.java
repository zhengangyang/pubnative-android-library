/**
 * Copyright 2014 PubNative GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.pubnative.interstitials.delegate;

import net.pubnative.interstitials.PubNativeInterstitialsActivity;
import net.pubnative.interstitials.R;
import net.pubnative.interstitials.api.PubNativeInterstitialsType;
import net.pubnative.interstitials.widget.InterstitialView;
import net.pubnative.interstitials.widget.VideoInterstitialView;
import net.pubnative.library.inner.PubNativeWorker;
import net.pubnative.library.model.APIEndpoint;
import net.pubnative.library.model.holder.NativeAdHolder;
import net.pubnative.library.model.holder.VideoAdHolder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;

public class VideoInterstitialDelegate extends AbstractDelegate
{
    private VideoAdHolder         holder;
    private InterstitialView      interstitialView;
    private BroadcastReceiver     interstitialBroadcastReceiver;
    private VideoInterstitialView videoInterstitialView;

    public VideoInterstitialDelegate(PubNativeInterstitialsActivity act)
    {
        super(act, 1);
    }

    @Override
    public PubNativeInterstitialsType getType()
    {
        return PubNativeInterstitialsType.VIDEO_INTERSTITIAL;
    }

    @Override
    protected int getContentLayoutId()
    {
        return R.layout.pn_delegate_video_interstitial;
    }

    @Override
    public VideoAdHolder[] getAdHolders()
    {
        return new VideoAdHolder[]
        { holder };
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        holderView.setOnClickListener(this);
        videoInterstitialView = (VideoInterstitialView) holderView.findViewById(R.id.view_holder);
        interstitialBroadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context arg0, Intent arg1)
            {
                act.finish();
            }
        };
        createHolders();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        holderView.getContext().registerReceiver(interstitialBroadcastReceiver, new IntentFilter(PubNativeWorker.broadcastInterstitialDismissKey));
        videoInterstitialView.onResume();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        holderView.getContext().unregisterReceiver(interstitialBroadcastReceiver);
        videoInterstitialView.onStop();
    }

    private void createHolders()
    {
        holder = new VideoAdHolder(holderView);
        holder.bannerViewId = R.id.view_banner;
        holder.videoViewId = R.id.view_video;
        holder.playButtonViewId = -1;
        holder.fullScreenButtonViewId = -1;
        holder.muteButtonViewId = R.id.view_mute;
        holder.countDownViewId = R.id.view_count_down;
        holder.skipButtonViewId = R.id.view_skip;
        //
        interstitialView = new InterstitialView(holderView.getContext());
        holder.backViewHolder = new NativeAdHolder(interstitialView);
        holder.backViewHolder.iconViewId = R.id.view_icon;
        holder.backViewHolder.bannerViewId = R.id.view_game_image;
        holder.backViewHolder.titleViewId = R.id.view_title;
        holder.backViewHolder.ratingViewId = R.id.view_rating;
        holder.backViewHolder.descriptionViewId = R.id.view_description;
        holder.backViewHolder.downloadViewId = R.id.btn_download;
        //
        holderView.findViewById(R.id.view_banner).setOnClickListener(this);
        holderView.findViewById(R.id.view_video).setOnClickListener(this);
        interstitialView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if (v == holderView || v == interstitialView || v.getId() == R.id.view_banner || v.getId() == R.id.view_video)
        {
            showInPlayStore(holder.ad);
        }
        else
        {
            super.onClick(v);
        }
    }

    @Override
    protected APIEndpoint getAdFormat()
    {
        return APIEndpoint.VIDEO;
    }
}
