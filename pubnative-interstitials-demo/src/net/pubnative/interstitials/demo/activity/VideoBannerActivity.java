package net.pubnative.interstitials.demo.activity;

import net.pubnative.interstitials.demo.R;
import net.pubnative.interstitials.widget.InterstitialView;
import net.pubnative.library.model.APIEndpoint;
import net.pubnative.library.model.holder.NativeAdHolder;
import net.pubnative.library.model.holder.VideoAdHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

public class VideoBannerActivity extends AbstractSingleHolderListActivity<View> implements
        OnClickListener
{
    protected VideoAdHolder holder;
    private View            interstitialView;

    @Override
    protected APIEndpoint getAdFormat()
    {
        return APIEndpoint.VIDEO;
    }

    @Override
    protected View makeView()
    {
        View view = LayoutInflater.from(this).inflate(R.layout.view_video_banner, null);
        holder = new VideoAdHolder(view);
        holder.bannerViewId = R.id.view_banner;
        holder.videoViewId = R.id.view_video;
        holder.playButtonViewId = R.id.view_play;
        holder.fullScreenButtonViewId = R.id.view_full_screen;
        holder.muteButtonViewId = R.id.view_mute;
        holder.countDownViewId = R.id.view_count_down;
        holder.skipButtonViewId = R.id.view_skip;
        //
        interstitialView = new InterstitialView(this);
        holder.backViewHolder = new NativeAdHolder(interstitialView);
        holder.backViewHolder.iconViewId = R.id.view_icon;
        holder.backViewHolder.bannerViewId = R.id.view_game_image;
        holder.backViewHolder.titleViewId = R.id.view_title;
        holder.backViewHolder.ratingViewId = R.id.view_rating;
        holder.backViewHolder.descriptionViewId = R.id.view_description;
        holder.backViewHolder.downloadViewId = R.id.btn_download;
        //
        view.findViewById(R.id.view_banner).setOnClickListener(this);
        view.findViewById(R.id.view_video).setOnClickListener(this);
        interstitialView.setOnClickListener(this);
        //
        return view;
    }

    @Override
    protected VideoAdHolder[] getAdHolders()
    {
        return new VideoAdHolder[]
        { holder };
    }

    @Override
    public void onClick(View v)
    {
        if (v == interstitialView || v.getId() == R.id.view_banner || v.getId() == R.id.view_video)
        {
            showInPlayStore(holder.ad);
        }
    }
}
