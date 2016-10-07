package net.pubnative.library.layouts.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.pubnative.library.layouts.R;
import net.pubnative.library.request.PubnativeAsset;
import net.pubnative.library.request.model.PubnativeAdModel;
import net.pubnative.player.VASTParser;
import net.pubnative.player.VASTPlayer;
import net.pubnative.player.model.VASTModel;

public class LargeLayoutWidget extends BaseLayoutWidget
        implements VASTPlayer.Listener {

    private static final String TAG = LargeLayoutWidget.class.getSimpleName();

    private LayoutInflater              mInflater;
    private TextView                    mTitle;
    private TextView                    mDescription;
    private ImageView                   mIcon;
    private ImageView                   mStandardBanner;
    private ImageView                   mBanner;
    private WebView                     mWebViewBanner;
    private RelativeLayout              mFooterContainer;
    private RelativeLayout              mHeaderContainer;
    private Button                      mCallToAction;
    private RatingBar                   mRating;
    private VASTPlayer                  mPlayer;
    private String                      mVASTString;
    private VASTModel                   mVASTModel;
    private PubnativeAdModel            mAdModel;
    private View                        mContainer;
    private RelativeLayout              mFullScreenLayout;
    private boolean                     mIsShown;
    private Width                       mWidth;
    private WindowManager               mWindowManager;
    private LayoutParams                mLayoutParams;

    public LargeLayoutWidget(Context context) {
        super(context);
        initialise(context);
    }

    public LargeLayoutWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialise(context);
    }

    public LargeLayoutWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialise(context);
    }

    private void initialise(Context context){

        Log.v(TAG, "initialise");
        mInflater = LayoutInflater.from(context);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mContainer = mInflater.inflate(R.layout.pubnative_layout_large, this, true);
        mTitle = (TextView) mContainer.findViewById(R.id.tv_title);
        mDescription = (TextView) mContainer.findViewById(R.id.tv_description);
        mRating = (RatingBar) mContainer.findViewById(R.id.rb_rating);
        mIcon = (ImageView) mContainer.findViewById(R.id.ic_icon);
        mStandardBanner = (ImageView) mContainer.findViewById(R.id.ic_standard_banner);
        mWebViewBanner = (WebView) mContainer.findViewById(R.id.wv_standard_banner);
        mBanner = (ImageView) mContainer.findViewById(R.id.iv_banner);
        mCallToAction = (Button) mContainer.findViewById(R.id.btn_cta);
        mFooterContainer = (RelativeLayout) mContainer.findViewById(R.id.rl_footer_container);
        mHeaderContainer = (RelativeLayout) mContainer.findViewById(R.id.rl_header_container);
        mPlayer = (VASTPlayer) mContainer.findViewById(R.id.player);
        mPlayer.setListener(this);

        //In case of full screen video
        mLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mFullScreenLayout = new RelativeLayout(context) {

            @Override
            public boolean dispatchKeyEvent(KeyEvent event) {

                Log.v(TAG, "dispatchKeyEvent");
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    hide();
                    return true;
                }
                return super.dispatchKeyEvent(event);
            }

            @Override
            protected void onWindowVisibilityChanged(int visibility) {

                Log.v(TAG, "onWindowVisibilityChanged");
                if (visibility != View.VISIBLE) {
                    hide();
                }
            }
        };
        mFullScreenLayout.addView(mContainer, mLayoutParams);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        mWindowManager.addView(mFullScreenLayout, params);
    }

    @Override
    public void show() {

        Log.v(TAG, "show");
        setVisibility(VISIBLE);
        if (mWidth != null) {
            setWidth(mWidth);
        }
        mAdModel.startTracking(mContainer, mCallToAction, this);
        if (mVASTModel != null) {
            mPlayer.setVisibility(VISIBLE);
        }
    }

    @Override
    public void hide() {

        Log.v(TAG, "hide");
        if (mIsShown && mVASTModel != null) {
            mIsShown = false;
            mPlayer.stop();
            mPlayer.destroy();
        }
        if (mFullScreenLayout.getParent() != null) {
            mWindowManager.removeView(mFullScreenLayout);
        }
        if (mAdModel != null) {
            mAdModel.stopTracking();
        }
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

    public void loadVideo() {

        Log.v(TAG, "loadVideo");
        new VASTParser(getContext()).setListener(new VASTParser.Listener() {

            @Override
            public void onVASTParserError(int error) {
                invokeLoadFail(new Exception("PubnativeLayouts - render error: error loading resources"));
            }

            @Override
            public void onVASTParserFinished(VASTModel model) {

                mVASTModel = model;
                mPlayer.load(mVASTModel);
                mIsShown = true;
                invokeLoadFinish();
            }
        }).execute(mVASTString);
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
        mWebViewBanner.setVisibility(GONE);
        mPlayer.setVisibility(GONE);
        mRating.setVisibility(GONE);
    }

    protected void showRelevantViews() {

        Log.v(TAG, "showRelevantViews");
        switch (mAdModel.getAssetGroupId()) {
            case 13:
                mWidth = Width.WIDTH_320;
                mStandardBanner.setVisibility(VISIBLE);
                mFooterContainer.setVisibility(GONE);
                invokeLoadFinish();
                break;
            case 14:
                mWidth = Width.WIDTH_320;
                mBanner.setVisibility(VISIBLE);
                mRating.setVisibility(VISIBLE);
                invokeLoadFinish();
                break;
            case 16:
                mRating.setVisibility(VISIBLE);
                mBanner.setVisibility(VISIBLE);
                invokeLoadFinish();
                break;
            case 17:
                mBanner.setVisibility(VISIBLE);
                invokeLoadFinish();
                break;
            case 15:
                mHeaderContainer.setBackgroundColor(Color.BLACK);
                mFooterContainer.setVisibility(GONE);
                loadVideo();
                break;
            case 19:
                mRating.setVisibility(VISIBLE);
                loadVideo();
                break;
            case 20:
                loadVideo();
                break;
        }
    }

    protected void destroy() {

        Log.v(TAG, "destroy");
        if (mVASTModel != null){
            mIsShown = false;
            mPlayer.destroy();
        }
        if (mFullScreenLayout.getParent() != null) {
            mWindowManager.removeView(mFullScreenLayout);
        }
        mAdModel = null;
        mVASTModel = null;
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
        mPlayer.setVisibility(VISIBLE);
        mPlayer.play();
    }

    @Override
    public void onVASTPlayerFail(Exception exception) {

        Log.v(TAG, "onVASTPlayerFail");
        invokeLoadFail(new Exception("PubnativeLayouts - show error: error loading player"));
        hide();
    }

    @Override
    public void onVASTPlayerPlaybackStart() {

        Log.v(TAG, "onVASTPlayerPlaybackStart");
    }

    @Override
    public void onVASTPlayerPlaybackFinish() {

        Log.v(TAG, "onVASTPlayerPlaybackFinish");
        destroy();
    }

    @Override
    public void onVASTPlayerOpenOffer() {

        Log.v(TAG, "onVASTPlayerOpenOffer");
        invokeClick();
        destroy();
    }
}