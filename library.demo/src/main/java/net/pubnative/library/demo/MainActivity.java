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
        Intent intent = new Intent(this, NativeAdActivity.class);
        startActivity(intent);
    }

    public void onSettingsClick(View view) {

        Log.v(TAG, "onSettingsClick");
        // Launch native activity
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onInterstitialClick(View view) {

        Log.v(TAG, "onInterstitialClick");
        // Launch interstitial activity
        Intent intent = new Intent(this, InterstitialAdActivity.class);
        startActivity(intent);
    }

    public void onBannerClick(View view) {

        Log.v(TAG, "onBannerClick");
        // Launch interstitial activity
        Intent intent = new Intent(this, BannerActivity.class);
        startActivity(intent);
    }

    public void onInFeedBannerClick(View view) {

        Log.v(TAG, "onInFeedBannerClick");
        // Launch InFeedBanner activity
        Intent intent = new Intent(this, InFeedBannerActivity.class);
        startActivity(intent);
    }

    public void onVideoClick(View view) {

        Log.v(TAG, "onVideoClick");
        // Launch VideoAd activity
        Intent intent = new Intent(this, VideoAdActivity.class);
        startActivity(intent);
    }

    public void onFeedVideoClick(View view) {

        Log.v(TAG, "onFeedVideoClick");
        // Launch FeedVideo activity
        Intent intent = new Intent(this, InFeedVideoActivity.class);
        startActivity(intent);
    }
}
