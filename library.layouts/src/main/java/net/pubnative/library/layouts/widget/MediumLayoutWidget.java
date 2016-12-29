package net.pubnative.library.layouts.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.pubnative.library.layouts.R;
import net.pubnative.library.request.PubnativeAsset;
import net.pubnative.library.request.model.PubnativeAdModel;
import net.pubnative.library.tracking.PubnativeVisibilityTracker;
import net.pubnative.player.VASTParser;
import net.pubnative.player.VASTPlayer;
import net.pubnative.player.model.VASTModel;

import java.util.List;


public class MediumLayoutWidget extends BaseLayoutWidget
        implements PubnativeVisibilityTracker.Listener, VASTPlayer.Listener {

    private static final String TAG = MediumLayoutWidget.class.getSimpleName();

    private LayoutInflater             mInflater;
    private TextView                   mTitle;
    private TextView                   mDescription;
    private ImageView                  mIcon;
    private ImageView                  mStandardBanner;
    private ImageView                  mBanner;
    private Button                     mCallToAction;
    private WebView                    mWebViewBanner;
    private RatingBar                  mRating;
    private VASTPlayer                 mPlayer;
    private RelativeLayout             mHeaderContainer;
    private RelativeLayout             mFooterContainer;
    private RelativeLayout             mWebViewContainer;
    private String                     mVASTString;
    private VASTModel                  mVASTModel;
    private View                       mContainer;
    private PubnativeAdModel           mAdModel;
    private boolean                    mIsVideoLoaded;
    private boolean                    mIsShown;
    private boolean                    mIsVideoPlaying;
    private boolean                    mIsTrackingWaiting;
    private boolean                    mIsAlreadyShown;
    private PubnativeVisibilityTracker mVisibilityTracker;
    private Width                      mWidth;

    public MediumLayoutWidget(Context context) {
        super(context);
        initialise(context);
    }

    public MediumLayoutWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialise(context);
    }

    public MediumLayoutWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialise(context);
    }

    private void initialise(Context context) {

        Log.v(TAG, "initialise");
        mInflater = LayoutInflater.from(context);
        mContainer = mInflater.inflate(R.layout.pubnative_layout_medium, this, true);
        mTitle = (TextView) mContainer.findViewById(R.id.tv_title);
        mDescription = (TextView) mContainer.findViewById(R.id.tv_description);
        mRating = (RatingBar) mContainer.findViewById(R.id.rb_rating);
        mIcon = (ImageView) mContainer.findViewById(R.id.ic_icon);
        mBanner = (ImageView) mContainer.findViewById(R.id.iv_banner);
        mStandardBanner = (ImageView) mContainer.findViewById(R.id.ic_standard_banner);
        mCallToAction = (Button) mContainer.findViewById(R.id.btn_cta);
        mWebViewBanner = (WebView) mContainer.findViewById(R.id.wv_standard_banner);
        mFooterContainer = (RelativeLayout) mContainer.findViewById(R.id.rl_footer_container);
        mHeaderContainer = (RelativeLayout) mContainer.findViewById(R.id.rl_header_container);
        mWebViewContainer = (RelativeLayout) mContainer.findViewById(R.id.rl_webview_container);
        mPlayer = (VASTPlayer) mContainer.findViewById(R.id.player);
        mPlayer.setListener(this);
    }

    @Override
    public void show() {

        Log.v(TAG, "show");
        setVisibility(VISIBLE);
        if (mWidth != null) {
            setWidth(mWidth);
        }

        if (mVASTModel != null) {
            mPlayer.setVisibility(VISIBLE);
        } else {
            mAdModel.startTracking(mContainer, this);
        }
    }

    @Override
    public void hide() {

        Log.v(TAG, "hide");
        if (mVASTModel != null && mIsShown) {

            if (mIsVideoLoaded && mIsVideoPlaying) {
                mPlayer.pause();
                mIsVideoPlaying = false;
            }

            mIsTrackingWaiting = true;
            mIsShown = false;
        }
        mAdModel.stopTracking();
        setVisibility(GONE);
        invokeHide();
    }

    @Override
    public void destroy() {

        Log.v(TAG, "destroy");
        if (mVASTModel != null) {
            if (mIsVideoPlaying) {
                mPlayer.stop();
            }
            mPlayer.destroy();
        }
        if (mAdModel != null) {
            mAdModel.stopTracking();
        }
        mIsShown = false;
        mIsVideoLoaded = false;
        mIsVideoPlaying = false;
        stopVisibilityTracking();
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
        if (mVASTModel != null) {
            mIsAlreadyShown = true;
            mPlayer.stop();
        }
        invokeClick();
    }

    @Override
    public void onPubnativeAdModelOpenOffer(PubnativeAdModel pubnativeAdModel) {

        Log.v(TAG, "onPubnativeAdModelOpenOffer");
    }

    public void loadVideo() {

        Log.v(TAG, "loadVideo");
        new VASTParser(getContext()).setListener(new VASTParser.Listener() {

            @Override
            public void onVASTParserError(int error) {
                invokeLoadFail(new Exception("PubnativeLayouts: MediumLayotWidget - render error: error loading resources"));
            }

            @Override
            public void onVASTParserFinished(VASTModel model) {

                mIsTrackingWaiting = true;
                mVASTModel = model;
                mPlayer.load(mVASTModel);
                invokeLoadFinish();
            }

        }).execute(mVASTString);
    }

    //==============================================================================================
    // Helpers
    //==============================================================================================
    protected void startVisibilityTracking() {

        Log.v(TAG, "startVisibilityTracking");
        stopVisibilityTracking();
        mVisibilityTracker = new PubnativeVisibilityTracker();
        mVisibilityTracker.setListener(this);
        mVisibilityTracker.addView(mPlayer, -1); // We want to be noticed as invisible if visibility is absolute 0
    }

    protected void stopVisibilityTracking() {

        Log.v(TAG, "stopVisibilityTracking");
        if (mVisibilityTracker != null) {
            mVisibilityTracker.clear();
            mVisibilityTracker = null;
        }
    }

    protected void fillData() {

        Log.v(TAG, "fillData");
        setText(mTitle, mAdModel.getTitle());
        setText(mDescription, mAdModel.getDescription());
        setText(mCallToAction, mAdModel.getCtaText());
        setImage(mIcon, mAdModel.getIconUrl());
        setImage(mBanner, mAdModel.getBannerUrl());
        setImage(mStandardBanner, mAdModel.getAssetUrl(PubnativeAsset.STANDARD_BANNER));
        loadWebUrl(mWebViewBanner, mAdModel.getAssetUrl(PubnativeAsset.HTML_BANNER));
        setRating(mRating, mAdModel.getRating());
        if (!TextUtils.isEmpty(mAdModel.getVast())) {
            mVASTString = mAdModel.getVast();
        }
    }

    protected void hideViews() {

        Log.v(TAG, "hideViews");
        mStandardBanner.setVisibility(GONE);
        mBanner.setVisibility(GONE);
        mWebViewContainer.setVisibility(GONE);
        mPlayer.setVisibility(GONE);
        mRating.setVisibility(GONE);
    }

    protected void showRelevantViews() {

        Log.v(TAG, "showRelevantViews");
        switch (mAdModel.getAssetGroupId()) {
            case 5:
                mRating.setVisibility(VISIBLE);
                mBanner.setVisibility(VISIBLE);
                invokeLoadFinish();
                break;
            case 6:
                mBanner.setVisibility(VISIBLE);
                invokeLoadFinish();
                break;
            case 7:
                hideHeaderFooterViews();
                mWidth = Width.WIDTH_300;
                mStandardBanner.setVisibility(VISIBLE);
                invokeLoadFinish();
                break;
            case 8:
                hideHeaderFooterViews();
                mWidth = Width.WIDTH_300;
                mWebViewContainer.setVisibility(VISIBLE);
                invokeLoadFinish();
                break;
            case 3:
                mRating.setVisibility(VISIBLE);
                startVisibilityTracking();
                loadVideo();
                break;
            case 4:
                hideHeaderFooterViews();
                startVisibilityTracking();
                loadVideo();
                break;
            case 18:
                startVisibilityTracking();
                loadVideo();
                break;
        }
    }

    protected void hideHeaderFooterViews() {

        Log.d(TAG, "hideHeaderFooterViews");
        mHeaderContainer.setVisibility(GONE);
        mFooterContainer.setVisibility(GONE);
    }

    protected void startTracking() {

        Log.d(TAG, "startTracking");
        switch (mAdModel.getAssetGroupId()) {
            case 4: // video only
                mAdModel.startTracking(mPlayer, null, null);
                break;
            case 3:
            case 18:
                mAdModel.startTracking(mContainer, mCallToAction, this);
                break;
        }
    }

    //==============================================================================================
    // Callbacks
    //==============================================================================================
    //----------------------------------------------------------------------------------------------
    // VASTPlayer.Listener
    //----------------------------------------------------------------------------------------------
    @Override
    public void onVASTPlayerLoadFinish() {

        Log.v(TAG, "onVASTPlayerLoadFinish");
        mIsVideoLoaded = true;
        mPlayer.setVisibility(VISIBLE);
    }

    @Override
    public void onVASTPlayerFail(Exception exception) {

        Log.v(TAG, "onVASTPlayerFail");
        mIsVideoPlaying = false;
        invokeLoadFail(new Exception("PubnativeLayouts - show error: error loading player"));
        destroy();
    }

    @Override
    public void onVASTPlayerPlaybackStart() {

        Log.v(TAG, "onVASTPlayerPlaybackStart");
    }

    @Override
    public void onVASTPlayerPlaybackFinish() {

        Log.v(TAG, "onVASTPlayerPlaybackFinish");
        mIsAlreadyShown = true;
    }

    @Override
    public void onVASTPlayerOpenOffer() {

        Log.v(TAG, "onVASTPlayerOpenOffer");
        mIsAlreadyShown = true;
        invokeClick();
    }

    //----------------------------------------------------------------------------------------------
    // PubnativeVisibilityTracker.Listener
    //----------------------------------------------------------------------------------------------
    @Override
    public void onVisibilityCheck(List<View> visibleViews, List<View> invisibleViews) {

        Log.v(TAG, "onVisibilityCheck");
        if (visibleViews.contains(mPlayer)) {

            mIsShown = true;
            if (mIsTrackingWaiting) {

                mIsTrackingWaiting = false;
                startTracking();
            }
            if (mIsVideoLoaded && !mIsVideoPlaying && !mIsAlreadyShown) {
                mPlayer.play();
                mIsVideoPlaying = true;
            }
        }

        if (invisibleViews.contains(mPlayer)) {

            if (mIsShown && mIsVideoLoaded && mIsVideoPlaying) {
                mPlayer.pause();
                mIsVideoPlaying = false;
            }
        }
    }
}
