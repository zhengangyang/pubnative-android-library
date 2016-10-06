package net.pubnative.library.feed.banner;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import net.pubnative.library.request.PubnativeAsset;
import net.pubnative.library.request.PubnativeRequest;
import net.pubnative.library.request.model.PubnativeAdModel;

import java.util.List;

public class PubnativeFeedBanner implements PubnativeRequest.Listener,
                                            PubnativeAdModel.Listener {

    private static final String  TAG = PubnativeFeedBanner.class.getSimpleName();

    protected Context                       mContext;
    protected String                        mAppToken;
    protected PubnativeFeedBanner.Listener  mListener;
    protected boolean                       mIsLoading = false;
    protected boolean                       mIsShown = false;
    protected Handler                       mHandler;
    protected PubnativeAdModel              mAdModel;

    // InFeed Banner view
    protected RelativeLayout                mInFeedBannerView;
    protected TextView                      mTitle;
    protected TextView                      mDescription;
    protected ImageView                     mIconImage;
    protected ImageView                     mBannerImage;
    protected Button                        mCallToAction;
    protected RatingBar                     mRating;

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
         * Called when the in-feed banner is shown on the screen
         *
         * @param feedBanner feedBanner that is shown on the screen
         */
        void onPubnativeFeedBannerShow(PubnativeFeedBanner feedBanner);

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
     * Load Feed Banner
     * @param context  A valid context
     * @param appToken App token
     */
    public void load(Context context, String appToken) {

        Log.v(TAG, "load");
        if(mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        if (mListener == null) {
            Log.w(TAG, "Listener is not set, try to set listener by setListener(Listener listener) method");
        }
        if (TextUtils.isEmpty(appToken)) {
            invokeLoadFail(new Exception("PubnativeFeedBanner - load error: app token is null or empty"));
        } else if (context == null) {
            invokeLoadFail(new Exception("PubnativeFeedBanner - load error: context is null or empty"));
        } else if (mIsLoading) {
            Log.w(TAG, "The ad is being loaded, dropping this call");
        }  else if (mIsShown) {
            Log.w(TAG, "The ad has been shown already, dropping this call");
        } else if (isReady()) {
            invokeLoadFinish();
        } else {
            mIsLoading = true;
            mContext = context;
            mAppToken = appToken;
            mAdModel = null;
            initialize(); // to prepare the view
            PubnativeRequest request = new PubnativeRequest();
            request.setParameter(PubnativeRequest.Parameters.APP_TOKEN, mAppToken);
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
     * Checks whether ad has been retrieved
     * @return true if retrieved and not shown otherwise false
     */
    public boolean isReady() {

        Log.v(TAG, "isReady");
        return mAdModel != null;
    }

    /**
     * Show Feed banner
     * @param container Valid container that will contain Feed Banner
     */
    public void show(ViewGroup container) {

        Log.v(TAG, "show");
        if(container == null) {
            Log.e(TAG, "passed container argument cannot be null");
        } else if(mIsLoading) {
            Log.w(TAG, "The ad is loading, dropping this call");
        } else if(mIsShown) {
            Log.w(TAG, "The ad has been shown already, dropping this call");
        } else if(isReady()) {
            mIsShown = true;
            container.removeAllViews();
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            container.addView(mInFeedBannerView, params);
            render();
            invokeShow();
            mAdModel.startTracking(mInFeedBannerView, mCallToAction, this);
        } else {
            Log.w(TAG, "The ad is not loaded, please ensure to call load() before calling show()");
        }
    }

    /**
     * Destroy the current InFeed banner
     */
    public void destroy() {

        Log.v(TAG, "destroy");
        hide();
        mAdModel = null;
        mIsLoading = false;
        mIsShown = false;
    }

    /**
     * Hides the current InFeed banner
     */
    public void hide() {

        Log.v(TAG, "hide");
        if(mIsShown && mInFeedBannerView.getParent() != null) {
            mAdModel.stopTracking();
            ((ViewGroup)mInFeedBannerView.getParent()).removeAllViews();
        }
    }

    private void initialize() {

        Log.v(TAG, "initialize");
        if(mInFeedBannerView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mInFeedBannerView = (RelativeLayout) inflater.inflate(R.layout.pubnative_feed_banner, null);
            mTitle = (TextView) mInFeedBannerView.findViewById(R.id.pubnative_feed_banner_title);
            mRating = (RatingBar) mInFeedBannerView.findViewById(R.id.pubnative_infeed_rating);
            mDescription = (TextView) mInFeedBannerView.findViewById(R.id.pubnative_feed_banner_description);
            mIconImage = (ImageView) mInFeedBannerView.findViewById(R.id.pubnative_feed_banner_iconImage);
            mBannerImage = (ImageView) mInFeedBannerView.findViewById(R.id.pubnative_feed_banner_bannerImage);
            mCallToAction = (Button) mInFeedBannerView.findViewById(R.id.pubnative_feed_banner_button);
        }
    }

    private void render() {

        Log.v(TAG, "render");
        mTitle.setText(mAdModel.getTitle());
        mDescription.setText(mAdModel.getDescription());
        mCallToAction.setText(mAdModel.getCtaText());
        Picasso.with(mContext).load(mAdModel.getIconUrl()).into(mIconImage);
        Picasso.with(mContext).load(mAdModel.getBannerUrl()).into(mBannerImage);
        if(mAdModel.getRating() > 0) {
            mRating.setRating(mAdModel.getRating());
            mRating.setVisibility(View.VISIBLE);
        } else {
            mRating.setVisibility(View.GONE);
        }
    }

    protected void invokeShow() {

        Log.v(TAG, "invokeShow");
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                if (mListener != null) {
                    mListener.onPubnativeFeedBannerShow(PubnativeFeedBanner.this);
                }
            }
        });
    }

    protected void invokeLoadFail(final Exception exception) {

        Log.v(TAG, "invokeLoadFail", exception);
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                if (mListener != null) {
                    mListener.onPubnativeFeedBannerLoadFailed(PubnativeFeedBanner.this, exception);
                }
            }
        });
    }

    protected void invokeLoadFinish() {

        Log.v(TAG, "invokeLoadFinish");
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                if (mListener != null) {
                    mListener.onPubnativeFeedBannerLoadFinish(PubnativeFeedBanner.this);
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
    // PubnativeRequest.Listener
    //----------------------------------------------------------------------------------------------
    @Override
    public void onPubnativeRequestSuccess(PubnativeRequest request, List<PubnativeAdModel> ads) {

        Log.v(TAG, "onPubnativeRequestSuccess");
        mIsLoading = false;
        if (ads == null || ads.size() == 0) {
            invokeLoadFail(new Exception("PubnativeFeedBanner - load error: no-fill"));
        } else {
            mAdModel = ads.get(0);
            Picasso.with(mContext).load(mAdModel.getIconUrl()).fetch(new Callback() {

                @Override
                public void onSuccess() {
                    Picasso.with(mContext).load(mAdModel.getBannerUrl()).fetch(new Callback() {

                        @Override
                        public void onSuccess() {
                            invokeLoadFinish();
                        }

                        @Override
                        public void onError() {
                            invokeLoadFail(new Exception("PubnativeFeedBanner - banner loading error"));
                        }
                    });
                }

                @Override
                public void onError() {
                    invokeLoadFail(new Exception("PubnativeFeedBanner - icon loading error"));
                }
            });
        }
    }

    @Override
    public void onPubnativeRequestFailed(PubnativeRequest request, Exception ex) {

        Log.v(TAG, "onPubnativeRequestFailed");
        mIsLoading = false;
        invokeLoadFail(ex);
    }

    //==============================================================================================
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
