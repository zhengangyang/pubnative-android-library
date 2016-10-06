package net.pubnative.interstitials.demo.activity;

import net.pubnative.library.model.APIEndpoint;

import org.droidparts.util.ui.ViewUtils;

import android.view.View;

public class InFeedVideoActivity extends VideoBannerActivity
{
    @Override
    protected APIEndpoint getAdFormat()
    {
        return APIEndpoint.VIDEO;
    }

    @Override
    protected View makeView()
    {
        View v = super.makeView();
        ViewUtils.setGone(true, v.findViewById(holder.playButtonViewId));
        holder.playButtonViewId = -1;
        return v;
    }
}
