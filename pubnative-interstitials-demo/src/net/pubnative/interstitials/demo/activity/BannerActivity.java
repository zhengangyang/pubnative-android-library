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
package net.pubnative.interstitials.demo.activity;

import net.pubnative.interstitials.demo.Contract;
import net.pubnative.interstitials.demo.R;
import net.pubnative.interstitials.demo.misc.ResizingRoundingReshaper;
import net.pubnative.library.PubNative;
import net.pubnative.library.model.APIEndpoint;
import net.pubnative.library.model.holder.NativeAdHolder;
import net.pubnative.library.model.request.AdRequest;
import net.pubnative.library.model.response.NativeAd;

import org.droidparts.annotation.inject.InjectView;

import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RatingBar;

public class BannerActivity extends AbstractDemoActivity implements
        OnClickListener
{
    @InjectView(id = R.id.banner1, click = true)
    private View           banner1View;
    @InjectView(id = R.id.banner2, click = true)
    private View           banner2View;
    private NativeAdHolder banner1Holder;
    private NativeAdHolder banner2Holder;

    @Override
    public void onPreInject()
    {
        setContentView(R.layout.activity_banner);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        changeStarsColor();
        makeHolders();
        initPubNative();
        showBanner1();
        showBanner2();
    }

    private void changeStarsColor()
    {
        RatingBar ratingBar = (RatingBar) findViewById(R.id.view_banner_rating);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(1).setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_ATOP);
    }

    private void initPubNative()
    {
        int iconSidePx = getResources().getDimensionPixelSize(R.dimen.icon_size);
        int radiusPx = getResources().getDimensionPixelSize(R.dimen.icon_corner_radius);
        PubNative.setImageReshaper(new ResizingRoundingReshaper(this, iconSidePx, radiusPx));
    }

    private void makeHolders()
    {
        banner1Holder = new NativeAdHolder(banner1View);
        banner1Holder.iconViewId = R.id.view_banner_icon;
        banner1Holder.titleViewId = R.id.view_banner_title;
        banner1Holder.ratingViewId = R.id.view_banner_rating;
        banner1Holder.downloadViewId = R.id.view_install;
        banner2Holder = new NativeAdHolder(banner2View);
        banner2Holder.iconViewId = R.id.view_banner_icon;
    }

    private void showBanner1()
    {
        AdRequest req = new AdRequest(Contract.APP_TOKEN, APIEndpoint.NATIVE);
        req.fillInDefaults(this);
        req.setIconSize(300, 300);
        PubNative.showAd(req, banner1Holder);
    }

    private void showBanner2()
    {
        AdRequest req = new AdRequest(Contract.APP_TOKEN, APIEndpoint.NATIVE);
        req.fillInDefaults(this);
        req.setIconSize(300, 300);
        PubNative.showAd(req, banner2Holder);
    }

    @Override
    public void onClick(View view)
    {
        NativeAd ad = null;
        if (view == banner1View)
        {
            ad = banner1Holder.ad;
        }
        else
            if (view == banner2View)
            {
                ad = banner2Holder.ad;
            }
        PubNative.showInPlayStoreViaDialog(this, ad);
    }

    @Override
    protected NativeAdHolder[] getAdHolders()
    {
        return null;
    }
}
