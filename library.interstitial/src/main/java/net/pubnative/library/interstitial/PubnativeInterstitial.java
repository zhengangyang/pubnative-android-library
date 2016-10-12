package net.pubnative.library.interstitial;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.pubnative.library.request.PubnativeAsset;
import net.pubnative.library.request.PubnativeRequest;
import net.pubnative.library.request.model.PubnativeAdModel;
import net.pubnative.library.utils.ImageDownloader;

import java.util.List;

public class PubnativeInterstitial implements PubnativeRequest.Listener,
                                              PubnativeAdModel.Listener {

    private static final String TAG = PubnativeInterstitial.class.getSimpleName();
    protected Context                        mContext;
    protected PubnativeAdModel               mAdModel;
    protected PubnativeInterstitial.Listener mListener;
    protected boolean                        mIsLoading;
    protected boolean                        mIsShown;
    protected boolean                        mIsCoppaModeEnabled;
    protected WindowManager                  mWindowManager;
    // Interstitial view
    protected RelativeLayout                 mContainer;
    protected TextView                       mTitle;
    protected TextView                       mDescription;
    protected ImageView                      mIcon;
    protected ImageView                      mBanner;
    protected View                           mContentInfo;
    protected RatingBar                      mRating;
    protected TextView                       mCTA;

    /**
     * Interface for callbacks related to the interstitial view behaviour
     */
    public interface Listener {

        /**
         * Called whenever the interstitial finished loading an ad
         *
         * @param interstitial interstitial that finished the load
         */
        void onPubnativeInterstitialLoadFinish(PubnativeInterstitial interstitial);

        /**
         * Called whenever the interstitial failed loading an ad
         *
         * @param interstitial interstitial that failed the load
         * @param exception    exception with the description of the load error
         */
        void onPubnativeInterstitialLoadFail(PubnativeInterstitial interstitial, Exception exception);

        /**
         * Called when the interstitial was just shown on the screen
         *
         * @param interstitial interstitial that was shown in the screen
         */
        void onPubnativeInterstitialShow(PubnativeInterstitial interstitial);

        /**
         * Called when the interstitial impression was confrimed
         *
         * @param interstitial interstitial which impression was confirmed
         */
        void onPubnativeInterstitialImpressionConfirmed(PubnativeInterstitial interstitial);

        /**
         * Called whenever the interstitial was clicked by the user
         *
         * @param interstitial interstitial that was clicked
         */
        void onPubnativeInterstitialClick(PubnativeInterstitial interstitial);

        /**
         * Called whenever the interstitial was removed from the screen
         *
         * @param interstitial interstitial that was hidden
         */
        void onPubnativeInterstitialHide(PubnativeInterstitial interstitial);
    }

    /**
     * Sets a callback listener for this interstitial object
     *
     * @param listener valid PubnativeInterstitial.Listener object
     */
    public void setListener(Listener listener) {

        Log.v(TAG, "setListener");
        mListener = listener;
    }

    /**
     * Sets COPPA mode to the status enabled in the parameter
     *
     * @param enabled true if you want to enable COPPA mode
     */
    public void setCoppaMode(boolean enabled) {
        Log.v(TAG, "setCoppaMode");
        mIsCoppaModeEnabled = enabled;
    }

    /**
     * Starts loading an ad for this interstitial
     * @param context valid Context
     * @param appToken valid App token where to request the ad from
     * @deprecated Use the load method with zone Id parameter instead
     */
    public void load(Context context, String appToken) {

        load(context, appToken, PubnativeRequest.LEGACY_ZONE_ID);
    }

    /**
     * Starts loading an ad for this interstitial
     * @param context valid context
     * @param appToken valid appToken
     * @param zoneId valid zoneId
     */
    public void load(Context context, String appToken, String zoneId) {

        Log.v(TAG, "load");

        if (mListener == null) {
            Log.v(TAG, "load - The ad hasn't a listener");
        }

        if (TextUtils.isEmpty(appToken)) {
            invokeLoadFail(new Exception("PubnativeInterstitial - error: app token is null or empty, dropping this call"));
        } else if (TextUtils.isEmpty(zoneId)) {
            invokeLoadFail(new Exception("PubnativeInterstitial - error: zoneId is null or empty, dropping this call"));
        } else if (context == null) {
            invokeLoadFail(new Exception("PubnativeInterstitial - error: context is null or empty, dropping this call"));
        } else if (mIsLoading) {
            Log.w(TAG, "load - The ad is loaded or being loaded, dropping this call");
        } else if (isReady()) {
            invokeLoadFinish();
        } else {
            mContext = context;
            mIsShown = false;
            mIsLoading = true;
            initialize();
            PubnativeRequest request = new PubnativeRequest();
            request.setCoppaMode(mIsCoppaModeEnabled);
            request.setParameter(PubnativeRequest.Parameters.APP_TOKEN, appToken);
            request.setParameter(PubnativeRequest.Parameters.ZONE_ID, zoneId);
            String[] assets = new String[] {
                    PubnativeAsset.TITLE,
                    PubnativeAsset.DESCRIPTION,
                    PubnativeAsset.ICON,
                    PubnativeAsset.BANNER,
                    PubnativeAsset.CALL_TO_ACTION,
                    PubnativeAsset.RATING
            };
            request.setParameterArray(PubnativeRequest.Parameters.ASSET_FIELDS, assets);
            request.start(mContext, this);
        }
    }

    /**
     * Method that checks if the intersitial is ready to be shown in the screen
     *
     * @return true if the interstitial can be shown false if not
     */
    public boolean isReady() {

        Log.v(TAG, "setListener");
        return mAdModel != null;
    }

    /**
     * Shows, if not shown and ready the cached interstitial over the current activity
     */
    public void show() {

        Log.v(TAG, "show");
        if (mIsShown) {
            Log.w(TAG, "show - the ad is already shown, ");
        } else if (isReady()) {
            render();
        } else {
            Log.e(TAG, "show - Error: the interstitial is not yet loaded");
        }
    }

    /**
     * Destroy the current interstitial resetting all the cached data and removing any possible interstitial in the screen
     */
    public void destroy() {

        Log.v(TAG, "destroy");
        hide();
        mAdModel = null;
        mIsShown = false;
        mIsLoading = false;
    }
    //==============================================================================================
    // Helpers
    //==============================================================================================

    protected void hide() {

        Log.v(TAG, "hide");
        if (mIsShown) {
            mWindowManager.removeView(mContainer);
            mAdModel.stopTracking();
            mIsShown = false;
            invokeHide();
        }
    }

    protected void render() {

        Log.v(TAG, "render");
        mTitle.setText(mAdModel.getTitle());
        mDescription.setText(mAdModel.getDescription());
        mCTA.setText(mAdModel.getCtaText());
        if (mAdModel.getRating() != 0) {
            mRating.setRating(mAdModel.getRating());
        } else {
            mRating.setVisibility(View.GONE);
        }
        // remove content info if already exist
        if(mContentInfo != null) {
            mContainer.removeView(mContentInfo);
        }
        mContentInfo = mAdModel.getContentInfo(mContext);
        if(mContentInfo != null) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            mContainer.addView(mContentInfo, params);
        }
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowManager.addView(mContainer, params);
        mAdModel.startTracking(mContainer, mCTA, this);
        invokeShow();
    }

    protected void initialize() {

        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout interstitial = (RelativeLayout) layoutInflater.inflate(R.layout.pubnative_interstitial, null);
        mTitle = (TextView) interstitial.findViewById(R.id.pubnative_interstitial_title);
        mDescription = (TextView) interstitial.findViewById(R.id.pubnative_interstitial_description);
        mIcon = (ImageView) interstitial.findViewById(R.id.pn_interstitial_icon);
        mBanner = (ImageView) interstitial.findViewById(R.id.pubnative_interstitial_banner);
        mRating = (RatingBar) interstitial.findViewById(R.id.pubnative_interstitial_rating);
        mCTA = (TextView) interstitial.findViewById(R.id.pubnative_interstitial_cta);
        mContainer = new RelativeLayout(mContext) {

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
        mContainer.addView(interstitial);
    }

    //==============================================================================================
    // Callback helpers
    //==============================================================================================

    protected void invokeLoadFinish() {

        Log.v(TAG, "invokeLoadFinish");
        mIsLoading = false;
        if (mListener != null) {
            mListener.onPubnativeInterstitialLoadFinish(this);
        }
    }

    protected void invokeLoadFail(Exception exception) {

        Log.v(TAG, "invokeLoadFail", exception);
        mIsLoading = false;
        if (mListener != null) {
            mListener.onPubnativeInterstitialLoadFail(this, exception);
        }
    }

    protected void invokeShow() {

        mIsShown = true;
        Log.v(TAG, "invokeShow");
        if (mListener != null) {
            mListener.onPubnativeInterstitialShow(this);
        }
    }

    protected void invokeImpressionConfirmed() {

        Log.v(TAG, "invokeImpressionConfirmed");
        if (mListener != null) {
            mListener.onPubnativeInterstitialImpressionConfirmed(this);
        }
    }

    protected void invokeClick() {

        Log.v(TAG, "invokeClick");
        if (mListener != null) {
            mListener.onPubnativeInterstitialClick(this);
        }
    }

    protected void invokeHide() {

        Log.v(TAG, "invokeHide");
        mIsShown = false;
        if (mListener != null) {
            mListener.onPubnativeInterstitialHide(this);
        }
    }
    //==============================================================================================
    // Callbacks
    //==============================================================================================
    // PubnativeRequest.Listener
    //----------------------------------------------------------------------------------------------

    @Override
    public void onPubnativeRequestSuccess(PubnativeRequest request, List<PubnativeAdModel> ads) {

        Log.v(TAG, "onPubnativeRequestSuccess");
        if (ads == null || ads.size() == 0) {
            invokeLoadFail(new Exception("PubnativeInterstitial - load error: error loading resources"));
        } else {
            mAdModel = ads.get(0);
            new ImageDownloader().load(mAdModel.getIconUrl(), new ImageDownloader.Listener() {
                @Override
                public void onImageLoad(String url, Bitmap bitmap) {
                    mIcon.setImageBitmap(bitmap);
                    new ImageDownloader().load(mAdModel.getBannerUrl(), new ImageDownloader.Listener() {
                        @Override
                        public void onImageLoad(String url, Bitmap bitmap) {
                            mBanner.setImageBitmap(bitmap);
                            invokeLoadFinish();
                        }

                        @Override
                        public void onImageFailed(String url, Exception ex) {
                            invokeLoadFail(new Exception("PubnativeInterstitial - preload banner error: can't load banner"));
                        }
                    });
                }

                @Override
                public void onImageFailed(String url, Exception ex) {
                    invokeLoadFail(new Exception("PubnativeInterstitial - preload icon error: can't load icon"));
                }
            });
        }
    }

    @Override
    public void onPubnativeRequestFailed(PubnativeRequest request, Exception ex) {

        Log.v(TAG, "onPubnativeRequestFailed");
        invokeLoadFail(ex);
    }
    //----------------------------------------------------------------------------------------------
    // PubnativeAdModel.Listener
    //----------------------------------------------------------------------------------------------

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
