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

package net.pubnative.library.layouts;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import net.pubnative.library.layouts.widget.BaseLayoutWidget;
import net.pubnative.library.layouts.widget.LargeLayoutWidget;
import net.pubnative.library.layouts.widget.MediumLayoutWidget;
import net.pubnative.library.layouts.widget.SmallLayoutWidget;
import net.pubnative.library.request.PubnativeRequest;
import net.pubnative.library.request.model.PubnativeAdModel;

import java.util.List;

public class PubnativeLayouts implements PubnativeRequest.Listener, BaseLayoutWidget.Listener {

    private static final String TAG = PubnativeLayouts.class.getSimpleName();

    protected Context                   mContext;
    protected Listener                  mListener;
    protected boolean                   mIsLoading;
    protected boolean                   mLoadFailed;
    protected Handler                   mHandler;
    protected PubnativeAdModel          mAdModel;
    protected BaseLayoutWidget          mBaseLayout;
    protected LayoutType                mLayoutType;
    protected Boolean                   mIsShown    = false;

    /**
     * ENUM for layout type
     */
    public enum LayoutType {
        SMALL("s"),
        MEDIUM("m"),
        LARGE("l");

        private final String type;

        LayoutType(String layoutType) {
            type = layoutType;
        }

        public String toString() {
            return type;
        }
    }

    /**
     * Interface for callbacks related to the asset layouts.
     */
    public interface Listener {

        /**
         * Called whenever the asset group layout finish loading an ad.
         *
         * @param layout layout that finish the load.
         */
        void onPubnativeLayoutLoadFinish(PubnativeLayouts layout);

        /**
         * Called whenever the asset group layout failed loading an ad.
         *
         * @param layout    layout that failed the load.
         * @param exception exception with the description of the load error.
         */
        void onPubnativeLayoutLoadFailed(PubnativeLayouts layout, Exception exception);

        /**
         * Called whenever the asset group layout was just shown on the screen.
         *
         * @param layout layout that was shown in the screen.
         */
        void onPubnativeLayoutShow(PubnativeLayouts layout);

        /**
         * Called whenever the asset group layout impression was confirmed.
         *
         * @param layout layout that impression was confirmed.
         */
        void onPubnativeLayoutImpressionConfirmed(PubnativeLayouts layout);

        /**
         * Called whenever the asset group layout was clicked by the user.
         *
         * @param layout layout that was clicked.
         */
        void onPubnativeLayoutClick(PubnativeLayouts layout);

        /**
         * Called whenever the asset group layout was removed from the screen.
         *
         * @param layout layout that was hidden.
         */
        void onPubnativeLayoutHide(PubnativeLayouts layout);

    }

    //==============================================================================================
    // Public
    //==============================================================================================
    /**
     * Sets a callback listener for this asset group ad object.
     *
     * @param listener valid PubnativeLayouts.Listener object.
     */
    public void setListener(Listener listener) {

        Log.v(TAG, "setListener");
        mListener = listener;
    }

    /**
     * Load asset group ad.
     *
     * @param context    A valid context.
     * @param appToken   App token.
     * @param zoneId     Zone Id.
     * @param layoutType request layout type - small, medium, large.
     */
    public void load(Context context, String appToken, String zoneId, LayoutType layoutType) {

        Log.v(TAG, "load");
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        if (mListener == null) {
            Log.w(TAG, "load - Listener is not set, there won't be callbacks for this ad");
        }
        if (TextUtils.isEmpty(appToken) || TextUtils.isEmpty(zoneId)) {
            invokeLoadFail(new Exception("PubnativeLayouts - load error: app token or zoneId is null or empty"));
        } else if (context == null) {
            invokeLoadFail(new Exception("PubnativeLayouts - load error: context is null or empty"));
        } else if (mIsLoading) {
            Log.w(TAG, "The ad is being loaded, dropping this call");
        } else if (isReady()) {
            invokeLoadFinish();
        } else {
            mIsLoading = true;
            mContext = context;
            mAdModel = null;
            mIsShown = false;
            mLayoutType = layoutType;

            PubnativeRequest request = new PubnativeRequest();
            request.setParameter(PubnativeRequest.Parameters.APP_TOKEN, appToken);
            request.setParameter(PubnativeRequest.Parameters.ASSET_LAYOUT, layoutType.toString());
            request.setParameter(PubnativeRequest.Parameters.ZONE_ID, zoneId);
            request.start(mContext, this);
        }
    }

    /**
     * Checks whether ad has been retrieved.
     *
     * @return true if retrieved and not shown otherwise false.
     */
    public boolean isReady() {

        Log.v(TAG, "isReady");
        if (mLoadFailed) {
            Log.e(TAG, "ad load has failed, you should try loading it again");
        }
        return mAdModel != null && !mIsLoading && !mLoadFailed;
    }

    /**
     * Shows adview for this request.
     *
     * @param container valid ViewGroup.
     */
    public void show(ViewGroup container) {

        Log.v(TAG, "show");
        if (container == null) {
            Log.w(TAG, "View is null or empty, dropping this call");
        } else if (mIsLoading) {
            Log.w(TAG, "show - the ad is loading, dropping this call");
        } else if (mIsShown) {
            Log.w(TAG, "show - the ad is shown, dropping this call");
        } else if (isReady()) {
            mIsShown = true;
            ViewParent parent = mBaseLayout.getParent();
            if (parent == null) {
                container.addView(mBaseLayout);
            }
            mBaseLayout.show();
            invokeShow();
        } else {
            Log.e(TAG, "show - Error: the ad is not yet loaded");
        }
    }

    protected View getLayout() {

        Log.v(TAG, "getLayout");
        View wigdetLayout = null;
        switch (mLayoutType) {
            case SMALL:
                wigdetLayout = new SmallLayoutWidget(mContext);
                break;
            case MEDIUM:
                wigdetLayout = new MediumLayoutWidget(mContext);
                break;
            case LARGE:
                wigdetLayout = new LargeLayoutWidget(mContext);
                break;
        }
        return wigdetLayout;
    }

    protected void invokeLoadFinish() {

        Log.v(TAG, "invokeLoadFinish");
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                mIsLoading = false;
                mLoadFailed = false;
                if (mListener != null) {
                    mListener.onPubnativeLayoutLoadFinish(PubnativeLayouts.this);
                }
            }
        });
    }

    protected void invokeLoadFail(final Exception exception) {

        Log.v(TAG, "invokeLoadFail", exception);
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                mIsLoading = false;
                mLoadFailed = true;
                if (mListener != null) {
                    mListener.onPubnativeLayoutLoadFailed(PubnativeLayouts.this, exception);
                }
            }
        });
    }

    protected void invokeShow() {

        Log.v(TAG, "invokeShow");
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                if (mListener != null) {
                    mListener.onPubnativeLayoutShow(PubnativeLayouts.this);
                }
            }
        });
    }

    protected void invokeHide() {

        Log.v(TAG, "invokeHide");
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                if (mListener != null) {
                    mListener.onPubnativeLayoutHide(PubnativeLayouts.this);
                }
            }
        });
    }

    protected void invokeImpressionConfirmed() {

        Log.v(TAG, "invokeImpressionConfirmed");
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                if (mListener != null) {
                    mListener.onPubnativeLayoutImpressionConfirmed(PubnativeLayouts.this);
                }
            }
        });
    }

    protected void invokeClick() {

        Log.v(TAG, "invokeClick");
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                if (mListener != null) {
                    mListener.onPubnativeLayoutClick(PubnativeLayouts.this);
                }
            }
        });
    }

    //==============================================================================================
    // Callbacks
    //==============================================================================================
    //----------------------------------------------------------------------------------------------
    // PubnativeRequest.Listener
    //----------------------------------------------------------------------------------------------
    @Override
    public void onPubnativeRequestSuccess(PubnativeRequest request, List<PubnativeAdModel> ads) {

        Log.v(TAG, "onPubnativeRequestSuccess");
        if (ads == null || ads.size() == 0) {
            invokeLoadFail(new Exception("PubnativeLayouts - load error: no-fill"));
            return;
        }

        mAdModel = ads.get(0);
        // Fill with data
        mBaseLayout = (BaseLayoutWidget) getLayout();
        mBaseLayout.setListener(this);
        mBaseLayout.setModel(mAdModel);
    }

    @Override
    public void onPubnativeRequestFailed(PubnativeRequest request, Exception exception) {

        Log.v(TAG, "onPubnativeRequestFailed");
        invokeLoadFail(exception);
    }

    //----------------------------------------------------------------------------------------------
    // BaselayoutWidget.Listener
    //----------------------------------------------------------------------------------------------
    @Override
    public void onPubnativeBaseLayoutLoadFinish() {

        Log.v(TAG, "onPubnativeBaseLayoutLoadFinish");
        invokeLoadFinish();
    }

    @Override
    public void onPubnativeBaseLayoutLoadFail(Exception exception) {

        Log.v(TAG, "onPubnativeBaseLayoutLoadFail");
        invokeLoadFail(exception);
    }

    @Override
    public void onPubnativeBaseLayoutClick() {

        Log.v(TAG, "onPubnativeBaseLayoutClick");
        invokeClick();
    }

    @Override
    public void onPubnativeBaseLayoutHide() {

        Log.v(TAG, "onPubnativeBaseLayoutHide");
        invokeHide();
    }

    @Override
    public void onPubnativeBaseLayoutImpressionConfirmed() {

        Log.v(TAG, "onPubnativeBaseLayoutImpressionConfirmed");
        invokeImpressionConfirmed();
    }
}