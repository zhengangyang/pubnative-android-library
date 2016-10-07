package net.pubnative.library.layouts.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.pubnative.library.request.model.PubnativeAdModel;

public abstract class BaseLayoutWidget extends RelativeLayout
        implements PubnativeAdModel.Listener  {

    private static final String TAG = BaseLayoutWidget.class.getSimpleName();

    Listener mListener;

    /**
     * enum for ad view component width.
     */
    public enum Width {
        WIDTH_300,
        WIDTH_320,
        WIDTH_FLEXIBLE
    }

    public BaseLayoutWidget(Context context) {
        super(context);
    }

    public BaseLayoutWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseLayoutWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Interface for callbacks related to the asset group layouts.
     */
    public interface Listener {

        /**
         * called whenever asset group ad load finished.
         */
        void onPubnativeBaseLayoutLoadFinish();

        /**
         * called whenever asset group ad load failed.
         *
         * @param exception exception with the description of the load error.
         */
        void onPubnativeBaseLayoutLoadFail(Exception exception);

        /**
         * called whenvever asset group ad is clicked.
         */
        void onPubnativeBaseLayoutClick();

        /**
         * called whenvever asset group ad is removed from screen.
         */
        void onPubnativeBaseLayoutHide();

        /**
         * called whenever impression is confirmed for this asset group ad.
         */
        void onPubnativeBaseLayoutImpressionConfirmed();
    }

    public abstract void show();
    public abstract void hide();
    public abstract void setModel(PubnativeAdModel adModel);

    /**
     * sets listener for this request.
     *
     * @param listener valid listener.
     */
    public void setListener(Listener listener) {
        mListener = listener;
    }

    /**
     * sets ad view component width.
     * @param width enum for width 300, 320 or variable pixels.
     */
    public void setWidth(Width width) {

        Log.v(TAG, "setWidth");
        final float scale = getContext().getResources().getDisplayMetrics().density;
        int pixels = 0;
        switch (width) {
            case WIDTH_300:
                pixels = (int) (300 * scale + 0.5f);
                break;
            case WIDTH_320:
                pixels = (int) (320 * scale + 0.5f);
                break;
            case WIDTH_FLEXIBLE:
            default:
                pixels = (int) (((View) getParent()).getWidth() * scale + 0.5f);
                break;
        }

        getLayoutParams().width = pixels;
    }

    protected void setText(TextView view, String value) {

        Log.v(TAG, "setText");
        if (!TextUtils.isEmpty(value)) {
            view.setText(value);
        }
    }

    protected void setImage(ImageView view, String url) {

        Log.v(TAG, "setImage");
        if (!TextUtils.isEmpty(url)) {
            Picasso.with(getContext()).load(url).into(view);
        }
    }

    protected void loadWebUrl(WebView webView, String url) {

        Log.v(TAG, "loadWebUrl");
        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);
        }
    }

    protected void setRating(RatingBar ratingBar, int rating) {

        Log.v(TAG, "setRating");
        if (rating > 0) {
            ratingBar.setRating(rating);
        }
    }

    protected void invokeLoadFinish() {

        Log.v(TAG, "invokeLoadFinish");
        if (mListener != null) {
            mListener.onPubnativeBaseLayoutLoadFinish();
        }
    }

    protected void invokeLoadFail(final Exception exception) {

        Log.v(TAG, "invokeLoadFail");
        if (mListener != null) {
            mListener.onPubnativeBaseLayoutLoadFail(exception);
        }
    }

    protected void invokeClick() {

        Log.v(TAG, "invokeClick");
        if (mListener != null) {
            mListener.onPubnativeBaseLayoutClick();
        }
    }

    protected void invokeHide() {

        Log.v(TAG, "invokeHide");
        if (mListener != null) {
            mListener.onPubnativeBaseLayoutHide();
        }
    }

    protected void invokeImpressionConfirmed() {

        Log.v(TAG, "invokeImpressionConfirmed");
        if (mListener != null) {
            mListener.onPubnativeBaseLayoutImpressionConfirmed();
        }
    }
}