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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.pubnative.library.demo.utils.Settings;
import net.pubnative.library.request.model.PubnativeAdModel;
import net.pubnative.library.request.PubnativeRequest;

import java.util.List;

public class NativeAdActivity extends Activity implements PubnativeRequest.Listener,
                                                          PubnativeAdModel.Listener {

    private static final String TAG = NativeAdActivity.class.getName();
    private PubnativeRequest mRequest;
    // Container fields
    private RelativeLayout   mLoaderContainer;
    private RelativeLayout   mAdContainer;
    // Ad fields
    private TextView         mTitle;
    private TextView         mDescription;
    private TextView         mCTA;
    private ImageView        mIcon;
    private ImageView        mBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native);
        mAdContainer = (RelativeLayout) findViewById(R.id.activity_native_container_ad);
        mLoaderContainer = (RelativeLayout) findViewById(R.id.activity_native_container_loader);
        mTitle = (TextView) findViewById(R.id.activity_native_text_title);
        mDescription = (TextView) findViewById(R.id.activity_native_text_description);
        mCTA = (TextView) findViewById(R.id.activity_native_text_cta);
        mIcon = (ImageView) findViewById(R.id.activity_native_image_icon);
        mBanner = (ImageView) findViewById(R.id.activity_native_image_banner);
    }

    @Override
    protected void onResume() {

        super.onResume();
        mAdContainer.setVisibility(View.GONE);
        mLoaderContainer.setVisibility(View.GONE);
    }

    public void onRequestClick(View v) {

        Log.v(TAG, "onRequestClick");
        mLoaderContainer.setVisibility(View.VISIBLE);
        PubnativeRequest request = new PubnativeRequest();
        request.setParameter(PubnativeRequest.Parameters.APP_TOKEN, Settings.getAppToken());
        request.start(this, PubnativeRequest.Endpoint.NATIVE, this);
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
            PubnativeAdModel ad = ads.get(0);
            mTitle.setText(ad.getTitle());
            mDescription.setText(ad.getDescription());
            mCTA.setText(ad.getCtaText());
            Picasso.with(this).load(ad.getIconUrl()).into(mIcon);
            Picasso.with(this).load(ad.getBannerUrl()).into(mBanner);
            ad.startTracking(mAdContainer, this);
        } else {
            Toast.makeText(this, "ERROR: no - fill", Toast.LENGTH_SHORT);
        }
        mLoaderContainer.setVisibility(View.GONE);
        mAdContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPubnativeRequestFailed(PubnativeRequest request, Exception ex) {

        Log.v(TAG, "onPubnativeRequestFailed: " + ex);
        Toast.makeText(this, "ERROR: " + ex, Toast.LENGTH_SHORT);
        mLoaderContainer.setVisibility(View.GONE);
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
        mLoaderContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPubnativeAdModelOpenOffer(PubnativeAdModel pubnativeAdModel) {

        Log.v(TAG, "onPubnativeAdModelOpenOffer");
        mLoaderContainer.setVisibility(View.GONE);
    }
}
