// The MIT License (MIT)
//
// Copyright (c) 2016 PubNative GmbH
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//

package net.pubnative.library.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.pubnative.library.demo.utils.Settings;
import net.pubnative.library.request.PubnativeRequest;
import net.pubnative.library.request.model.PubnativeAdModel;
import net.pubnative.library.widget.PubnativeContentInfoWidget;

import java.util.List;

public class NativeAdActivity extends Activity implements PubnativeRequest.Listener,
                                                          PubnativeAdModel.Listener {

    private static final String TAG = NativeAdActivity.class.getName();
    // Container fields
    private RelativeLayout mAdContainer;
    // Settings
    private CheckBox       mCustomLoaderEnabled;
    private CheckBox       mCachingEnabled;
    private CheckBox       mCoppaEnabled;
    // Loader
    private View           mCustomLoaderView;
    private View           mCustomLoaderSpiner;
    // Ad fields
    private TextView       mTitle;
    private TextView       mDescription;
    private TextView       mCTA;
    private ImageView      mIcon;
    private ImageView      mBanner;
    private ImageView      mAdChoiceIcon;
    private PubnativeAdModel mCurrentAd;
    private PubnativeContentInfoWidget mContentInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native);
        mAdContainer = (RelativeLayout) findViewById(R.id.activity_native_container_ad);
        mTitle = (TextView) findViewById(R.id.activity_native_text_title);
        mDescription = (TextView) findViewById(R.id.activity_native_text_description);
        mCTA = (TextView) findViewById(R.id.activity_native_text_cta);
        mIcon = (ImageView) findViewById(R.id.activity_native_image_icon);
        mBanner = (ImageView) findViewById(R.id.activity_native_image_banner);
        mAdChoiceIcon = (ImageView) findViewById(R.id.pubnative_adchoice_icon);
        mContentInfo = (PubnativeContentInfoWidget) findViewById(R.id.pubnative_content_info);
        mContentInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContentInfo.openLayout();
            }
        });

        mCustomLoaderEnabled = (CheckBox) findViewById(R.id.activity_native_custom_loader);
        mCachingEnabled = (CheckBox) findViewById(R.id.activity_native_caching_enable);
        mCoppaEnabled = (CheckBox) findViewById(R.id.activity_native_coppa_enable);
        mCustomLoaderView = findViewById(R.id.activity_native_container_loader);
        mCustomLoaderSpiner = findViewById(R.id.activity_native_container_loader_square);
    }

    @Override
    protected void onResume() {

        super.onResume();
        mAdContainer.setVisibility(View.GONE);
    }

    public void onRequestClick(View v) {

        Log.v(TAG, "onRequestClick");

        mCustomLoaderView.setVisibility(View.GONE);

        PubnativeRequest request = new PubnativeRequest();
        request.setParameter(PubnativeRequest.Parameters.APP_TOKEN, Settings.getAppToken());
        request.setParameter(PubnativeRequest.Parameters.ZONE_ID, Settings.getZoneId());
        if (mCoppaEnabled.isChecked()) {
            request.setCoppaMode(true);
        } else {
            request.setCoppaMode(false);
        }
        request.start(this, this);
    }
    //==============================================================================================
    // Callbacks
    //==============================================================================================

    // PubnativeRequest.Listener
    //----------------------------------------------------------------------------------------------
    @Override
    public void onPubnativeRequestSuccess(PubnativeRequest request, List<PubnativeAdModel> ads) {

        Log.v(TAG, "onPubnativeRequestSuccess");
        if (ads != null && ads.size() > 0) {
            mCurrentAd = ads.get(0);
            mTitle.setText(mCurrentAd.getTitle());
            mDescription.setText(mCurrentAd.getDescription());
            mCTA.setText(mCurrentAd.getCtaText());
            Picasso.with(this).load(mCurrentAd.getIconUrl()).into(mIcon);
            Picasso.with(this).load(mCurrentAd.getBannerUrl()).into(mBanner);
            mContentInfo.setIconUrl(mCurrentAd.getContentInfoIconUrl());
            mContentInfo.setIconClickUrl(mCurrentAd.getContentInfoLink());
            mContentInfo.setContextText(mCurrentAd.getContentInfoText());
            mContentInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContentInfo.openLayout();
                }
            });
            Log.v(TAG, "CUSTOM SPINNER " + mCustomLoaderEnabled.isChecked());
            mCurrentAd.setUseClickCaching(mCachingEnabled.isChecked());
            mCurrentAd.startTracking(mAdContainer, this);
            mAdContainer.setVisibility(View.VISIBLE);
        } else {
            mAdContainer.setVisibility(View.GONE);
            Toast.makeText(this, "ERROR: no - fill", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPubnativeRequestFailed(PubnativeRequest request, Exception ex) {

        Log.v(TAG, "onPubnativeRequestFailed: " + ex);
        Toast.makeText(this, "ERROR: " + ex, Toast.LENGTH_SHORT).show();
    }

    // PubnativeAdModel.Listener
    //----------------------------------------------------------------------------------------------
    @Override
    public void onPubnativeAdModelImpression(PubnativeAdModel pubnativeAdModel, View view) {

        Log.v(TAG, "onPubnativeAdModelImpression");
    }

    @Override
    public void onPubnativeAdModelClick(PubnativeAdModel pubnativeAdModel, View view) {

        Log.v(TAG, "onPubnativeAdModelClick");

        if(mCustomLoaderEnabled.isChecked()){
            mCurrentAd.setUseClickLoader(false);
            mCustomLoaderView.setVisibility(View.VISIBLE);
            rotateAnimation();
        }
    }

    private void rotateAnimation() {

        if(mCustomLoaderEnabled.isChecked() && mCustomLoaderView.getVisibility() == View.VISIBLE) {
            Animation rotateAnimation = new RotateAnimation(0.0f,
                                                            360.0f,
                                                            Animation.RELATIVE_TO_SELF,
                                                            0.5f,
                                                            Animation.RELATIVE_TO_SELF,
                                                            0.5f);
            rotateAnimation.setDuration(1000);
            rotateAnimation.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    // Do nothing
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    rotateAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // Do nothing
                }
            });
            mCustomLoaderSpiner.startAnimation(rotateAnimation);
        }
    }

    @Override
    public void onPubnativeAdModelOpenOffer(PubnativeAdModel pubnativeAdModel) {

        Log.v(TAG, "onPubnativeAdModelOpenOffer");
        mCustomLoaderView.setVisibility(View.GONE);
    }
}
