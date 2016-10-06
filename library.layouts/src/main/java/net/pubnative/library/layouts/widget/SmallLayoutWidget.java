package net.pubnative.library.layouts.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import net.pubnative.library.layouts.R;
import net.pubnative.library.request.PubnativeAsset;
import net.pubnative.library.request.model.PubnativeAdModel;

public class SmallLayoutWidget extends BaseLayoutWidget {

    private static final String TAG = SmallLayoutWidget.class.getSimpleName();

    private LayoutInflater   mInflater;
    private TextView         mTitle;
    private TextView         mDescription;
    private ImageView        mIcon;
    private ImageView        mStandardBanner;
    private Button           mCallToAction;
    private WebView          mWebViewBanner;
    private RatingBar        mRating;
    private View             mContainer;
    private PubnativeAdModel mAdModel;
    private Width            mWidth;

    public SmallLayoutWidget(Context context) {
        super(context);
        initialise(context);
    }

    public SmallLayoutWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialise(context);
    }

    public SmallLayoutWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialise(context);
    }

    private void initialise(Context context) {

        Log.v(TAG, "initialise");
        mInflater = LayoutInflater.from(context);
        mContainer = mInflater.inflate(R.layout.pubnative_layout_small, this, true);
        mTitle = (TextView) mContainer.findViewById(R.id.tv_title);
        mDescription = (TextView) mContainer.findViewById(R.id.tv_description);
        mRating = (RatingBar) mContainer.findViewById(R.id.rb_rating);
        mIcon = (ImageView) mContainer.findViewById(R.id.ic_icon);
        mStandardBanner = (ImageView) mContainer.findViewById(R.id.ic_standard_banner);
        mCallToAction = (Button) mContainer.findViewById(R.id.btn_cta);
        mWebViewBanner = (WebView) mContainer.findViewById(R.id.wv_standard_banner);
    }

    @Override
    public void show() {

        Log.v(TAG, "show");
        setVisibility(VISIBLE);
        if (mWidth != null) {
            setWidth(mWidth);
        }
        mAdModel.startTracking(mContainer, this);
    }

    @Override
    public void hide() {

        Log.v(TAG, "hide");
        setVisibility(INVISIBLE);
        mAdModel.stopTracking();
        invokeHide();
    }

    @Override
    public void setModel(PubnativeAdModel adModel) {

        Log.v(TAG, "setModel");
        mAdModel = adModel;

        if (mAdModel == null) {
            invokeLoadFail(new Exception("PubnativeLayouts - load error: no-fill"));
        } else {
            hideViews();
            fillData();
            showRelevantViews();
            invokeLoadFinish();
        }
    }

    protected void fillData() {

        Log.v(TAG, "fillData");
        setText(mTitle, mAdModel.getTitle());
        setText(mDescription, mAdModel.getDescription());
        setText(mCallToAction, mAdModel.getCtaText());
        setImage(mIcon, mAdModel.getIconUrl());
        setImage(mStandardBanner, mAdModel.getAssetUrl(PubnativeAsset.STANDARD_BANNER));
        loadWebUrl(mWebViewBanner, mAdModel.getAssetUrl(PubnativeAsset.HTML_BANNER));
        setRating(mRating, mAdModel.getRating());
    }

    protected void hideViews() {

        Log.v(TAG, "hideViews");
        mStandardBanner.setVisibility(GONE);
        mWebViewBanner.setVisibility(GONE);
        mRating.setVisibility(GONE);
        mDescription.setVisibility(GONE);
    }

    protected void showRelevantViews() {

        Log.v(TAG, "showRelevantViews");
        switch (mAdModel.getAssetGroupId()) {
            case 1:
                mRating.setVisibility(VISIBLE);
                break;
            case 2:
                mDescription.setVisibility(VISIBLE);
                break;
            case 9:
                mWidth = Width.WIDTH_320;
                mStandardBanner.setVisibility(VISIBLE);
                break;
            case 10:
                mWidth = Width.WIDTH_320;
                mWebViewBanner.setVisibility(VISIBLE);
                break;
            case 11:
                mWidth = Width.WIDTH_300;
                mStandardBanner.setVisibility(VISIBLE);
                break;
            case 12:
                mWidth = Width.WIDTH_300;
                mWebViewBanner.setVisibility(VISIBLE);
                break;
        }
    }

    @Override
    public void onPubnativeAdModelImpression(PubnativeAdModel pubnativeAdModel, View view) {

        Log.v(TAG, "onPubnativeAdModelImpression");
        invokeImpressionConfirmed();
    }

    @Override
    public void onPubnativeAdModelClick(PubnativeAdModel pubnativeAdModel, View view) {

        Log.v(TAG, "onPubnativeAdModelClick");
        invokeClick();
    }

    @Override
    public void onPubnativeAdModelOpenOffer(PubnativeAdModel pubnativeAdModel) {

        Log.v(TAG, "onPubnativeAdModelOpenOffer");
    }
}
