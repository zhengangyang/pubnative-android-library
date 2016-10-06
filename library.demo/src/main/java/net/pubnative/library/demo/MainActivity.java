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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onNativeClick(View view) {

        Log.v(TAG, "onNativeClick");
        // Launch native activity
        startActivity(new Intent(this, NativeAdActivity.class));
    }

    public void onSettingsClick(View view) {

        Log.v(TAG, "onSettingsClick");
        // Launch native activity
        startActivity(new Intent(this, SettingsActivity.class));
    }

    public void onInterstitialClick(View view) {

        Log.v(TAG, "onInterstitialClick");
        // Launch interstitial activity
        startActivity(new Intent(this, InterstitialAdActivity.class));
    }

    public void onBannerClick(View view) {

        Log.v(TAG, "onBannerClick");
        // Launch interstitial activity
        startActivity(new Intent(this, BannerActivity.class));
    }

    public void onInFeedBannerClick(View view) {

        Log.v(TAG, "onInFeedBannerClick");
        // Launch InFeedBanner activity
        startActivity(new Intent(this, InFeedBannerActivity.class));
    }

    public void onVideoClick(View view) {

        Log.v(TAG, "onVideoClick");
        // Launch VideoAd activity
        startActivity(new Intent(this, VideoAdActivity.class));
    }

    public void onFeedVideoClick(View view) {

        Log.v(TAG, "onFeedVideoClick");
        // Launch FeedVideo activity
        startActivity(new Intent(this, InFeedVideoActivity.class));
    }

    public void onAssetLayoutClick(View view) {

        Log.v(TAG, "onAssetLayoutClick");
        // Launch AssetLayout activity
        startActivity(new Intent(this, AssetLayoutActivity.class));
    }
}
