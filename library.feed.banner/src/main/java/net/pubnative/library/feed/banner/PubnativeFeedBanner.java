package net.pubnative.library.feed.banner;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.pubnative.library.request.PubnativeAsset;
import net.pubnative.library.request.PubnativeRequest;
import net.pubnative.library.request.model.PubnativeAdModel;
import net.pubnative.library.utils.ImageDownloader;

import java.util.List;

public class PubnativeFeedBanner implements PubnativeRequest.Listener, PubnativeAdModel.Listener {

    private static final String TAG = PubnativeFeedBanner.class.getSimpleName();

    protected Context                      mContext;
    protected PubnativeFeedBanner.Listener mListener;
    protected boolean                      mIsLoading;
    protected boolean                      mLoadFailed;
    protected boolean                      mIsCoppaModeEnabled;
    protected Handler                      mHandler;
    protected PubnativeAdModel             mAdModel;
    // InFeed Banner view
    protected RelativeLayout               mInFeedBannerView;
    protected View                         mContainer;
    protected View                         mLoader;
    protected TextView                     mTitle;
    protected TextView                     mDescription;
    protected ImageView                    mIconImage;
    protected ImageView                    mBannerImage;
    protected View                         mContentInfo;
    protected Button                       mCallToAction;
    protected RatingBar                    mRating;

    /**
     * Interface for callbacks related to the in-feed banner
     */
    public interface Listener {

        /**
         * Called whenever the in-feed banner finished loading ad ad
         *
         * @param feedBanner feedBanner that finished the load
         */
        void onPubnativeFeedBannerLoadFinish(PubnativeFeedBanner feedBanner);

        /**
         * Called whenever the in-feed banner failed loading an ad
         *
         * @param feedBanner feedBanner that failed the load
         * @param exception  exception with the description of the load error
         */
        void onPubnativeFeedBannerLoadFailed(PubnativeFeedBanner feedBanner, Exception exception);

        /**
         * Called when the in-feed banner impression was confirmed
         *
         * @param feedBanner feedBanner which impression was confirmed
         */
        void onPubnativeFeedBannerImpressionConfirmed(PubnativeFeedBanner feedBanner);

        /**
         * Called whenever the in-feed banner was clicked by the user
         *
         * @param feedBanner feedBanner that was clicked
         */
        void onPubnativeFeedBannerClick(PubnativeFeedBanner feedBanner);

    }

    //==============================================================================================
    // Public
    //==============================================================================================

    /**
     * Sets a callback listener for this feed banner object
     *
     * @param listener valid PubnativeFeedBanner.Listener object
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
     * Load Feed Banner
     *
     * @param context  A valid context
     * @param appToken App token
     * @deprecated Use load method with zoneId instead
     */
    public void load(Context context, String appToken) {

        load(context, appToken, PubnativeRequest.LEGACY_ZONE_ID);
    }

    /**
     * Load Feed Banner
     *
     * @param context  valid context
     * @param appToken Valid App token
     * @param zoneId   Valid Zone id
     */
    public void load(Context context, String appToken, String zoneId) {

        Log.v(TAG, "load");
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }

        if (mListener == null) {
            Log.w(TAG, "Listener is not set, try to set listener by setListener(Listener listener) method");
        }

        if (context == null) {
            invokeLoadFail(new Exception("PubnativeFeedBanner - load error: context is null or empty and required, dropping this call"));
        } else if (TextUtils.isEmpty(appToken)) {
            invokeLoadFail(new Exception("PubnativeFeedBanner - load error: app token is null or empty and required, dropping this call"));
        } else if (TextUtils.isEmpty(zoneId)) {
            invokeLoadFail(new Exception("PubnativeFeedBanner - load error: zoneId is null or empty and required, dropping this call"));
        } else if (mIsLoading) {
            Log.w(TAG, "The ad is being loaded, dropping this call");
        } else if (isReady()) {
            invokeLoadFinish();
        } else {
            mIsLoading = true;
            mContext = context;
            mAdModel = null;
            initialize(); // to prepare the view

            mContainer.setVisibility(View.INVISIBLE);
            mLoader.setVisibility(View.VISIBLE);

            PubnativeRequest request = new PubnativeRequest();
            request.setCoppaMode(mIsCoppaModeEnabled);
            request.setParameter(PubnativeRequest.Parameters.APP_TOKEN, appToken);
            request.setParameter(PubnativeRequest.Parameters.ZONE_ID, zoneId);
            String[] assets = new String[]{
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
     * Checks whether ad has been retrieved
     *
     * @return true if retrieved and not shown otherwise false
     */
    public boolean isReady() {

        Log.v(TAG, "isReady");
        if (mLoadFailed) {
            Log.e(TAG, "banner load has failed, you should try loading it again");
        }
        return mAdModel != null && !mIsLoading && !mLoadFailed;
    }

    /**
     * Returns the configured ad view
     *
     * @return Valid View with configured ad view format
     */
    public View getView() {

        return mInFeedBannerView;
    }

    //==============================================================================================
    // Private
    //==============================================================================================

    protected void initialize() {

        Log.v(TAG, "initialize");
        if (mInFeedBannerView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mInFeedBannerView = (RelativeLayout) inflater.inflate(R.layout.pubnative_feed_banner, null);
            mContainer = mInFeedBannerView.findViewById(R.id.pubnative_feed_banner_container);
            mLoader = mInFeedBannerView.findViewById(R.id.pubnative_Feed_banner_loader);
            mTitle = (TextView) mInFeedBannerView.findViewById(R.id.pubnative_feed_banner_title);
            mRating = (RatingBar) mInFeedBannerView.findViewById(R.id.pubnative_infeed_rating);
            mDescription = (TextView) mInFeedBannerView.findViewById(R.id.pubnative_feed_banner_description);
            mIconImage = (ImageView) mInFeedBannerView.findViewById(R.id.pubnative_feed_banner_iconImage);
            mBannerImage = (ImageView) mInFeedBannerView.findViewById(R.id.pubnative_feed_banner_bannerImage);
            mCallToAction = (Button) mInFeedBannerView.findViewById(R.id.pubnative_feed_banner_button);
        }
    }

    protected void invokeLoadFinish() {

        Log.v(TAG, "invokeLoadFinish");
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                mIsLoading = false;
                mContainer.setVisibility(View.VISIBLE);
                mLoader.setVisibility(View.INVISIBLE);
                if (mListener != null) {
                    mListener.onPubnativeFeedBannerLoadFinish(PubnativeFeedBanner.this);
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
                    mListener.onPubnativeFeedBannerLoadFailed(PubnativeFeedBanner.this, exception);
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
                    mListener.onPubnativeFeedBannerImpressionConfirmed(PubnativeFeedBanner.this);
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
                    mListener.onPubnativeFeedBannerClick(PubnativeFeedBanner.this);
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
            invokeLoadFail(new Exception("PubnativeFeedBanner - load error: no-fill"));
            return;
        }

        mAdModel = ads.get(0);
        mAdModel.startTracking(mInFeedBannerView, this);

        // Fill with data
        mTitle.setText(mAdModel.getTitle());
        mDescription.setText(mAdModel.getDescription());
        mCallToAction.setText(mAdModel.getCtaText());

        if (mAdModel.getRating() > 0) {
            mRating.setRating(mAdModel.getRating());
            mRating.setVisibility(View.VISIBLE);
        } else {
            mRating.setVisibility(View.GONE);
        }

        new ImageDownloader().load(mAdModel.getIconUrl(), new ImageDownloader.Listener() {
            @Override
            public void onImageLoad(String url, Bitmap bitmap) {
                mIconImage.setImageBitmap(bitmap);
                new ImageDownloader().load(mAdModel.getBannerUrl(), new ImageDownloader.Listener() {
                    @Override
                    public void onImageLoad(String url, Bitmap bitmap) {
                        mBannerImage.setImageBitmap(bitmap);
                        invokeLoadFinish();
                    }

                    @Override
                    public void onImageFailed(String url, Exception ex) {
                        invokeLoadFail(new Exception("Banner loading error"));
                    }
                });
            }

            @Override
            public void onImageFailed(String url, Exception ex) {
                invokeLoadFail(new Exception("Icon loading error"));
            }
        });

        // remove content info if already exist
        if(mContentInfo != null) {
            mInFeedBannerView.removeView(mContentInfo);
        }
        mContentInfo = mAdModel.getContentInfo(mContext);
        if(mContentInfo != null) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            mInFeedBannerView.addView(mContentInfo, params);
        }
    }

    @Override
    public void onPubnativeRequestFailed(PubnativeRequest request, Exception ex) {

        Log.v(TAG, "onPubnativeRequestFailed");
        invokeLoadFail(ex);
    }

    //----------------------------------------------------------------------------------------------
    // PubnativeRequest.Listener
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
        // Do nothing
    }
}
