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

package net.pubnative.library.request;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import net.pubnative.AdvertisingIdClient;
import net.pubnative.library.network.PubnativeHttpRequest;
import net.pubnative.library.request.model.PubnativeAdModel;
import net.pubnative.library.request.model.api.PubnativeAPIV3AdModel;
import net.pubnative.library.request.model.api.PubnativeAPIV3ResponseModel;
import net.pubnative.library.utils.Crypto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PubnativeRequest implements PubnativeHttpRequest.Listener,
                                         AdvertisingIdClient.Listener {

    private static String TAG = PubnativeRequest.class.getSimpleName();

    @Deprecated
    public final static    String               LEGACY_ZONE_ID     = "1";
    protected static final String               BASE_URL           = "http://api.pubnative.net/api/v3/native";
    protected              Context              mContext           = null;
    protected              Map<String, String>  mRequestParameters = new HashMap<String, String>();
    protected              Listener             mListener          = null;
    protected              PubnativeHttpRequest mRequest           = null;
    protected              boolean              mIsRunning         = false;

    /**
     * Enum with all possible endpoints for the request
     *
     * @deprecated Endpoint was deprecated, and no longer used
     */
    @Deprecated
    public enum Endpoint {
        NATIVE,
    }

    //==============================================================================================
    // REQUEST PARAMETERS
    //==============================================================================================

    /**
     * Interface with all possible request parameters
     */
    public interface Parameters {

        String APP_TOKEN                  = "apptoken";
        String ANDROID_ADVERTISER_ID      = "gid";
        String ANDROID_ADVERTISER_ID_SHA1 = "gidsha1";
        String ANDROID_ADVERTISER_ID_MD5  = "gidmd5";
        String OS                         = "os";
        String OS_VERSION                 = "osver";
        String DEVICE_MODEL               = "devicemodel";
        String NO_USER_ID                 = "dnt";
        String LOCALE                     = "locale";
        String AD_COUNT                   = "adcount";
        String ZONE_ID                    = "zoneid";
        String LAT                        = "lat";
        String LONG                       = "long";
        String GENDER                     = "gender";
        String AGE                        = "age";
        String KEYWORDS                   = "keywords";
        String APP_VERSION                = "appver";
        String TEST                       = "test";
        String COPPA                      = "coppa";
        String VIDEO                      = "video";
        String META_FIELDS                = "mf";
        String ASSET_FIELDS               = "af";
        String ASSET_LAYOUT               = "al";
    }

    //==============================================================================================
    // LISTENER
    //==============================================================================================

    /**
     * Listener interface used to start Pubnative request with success and failure callbacks.
     */
    public interface Listener {

        /**
         * Invoked when PubnativeRequest request is success
         *
         * @param request Request object used for making the request
         * @param ads     List of ads received
         */
        void onPubnativeRequestSuccess(PubnativeRequest request, List<PubnativeAdModel> ads);

        /**
         * Invoked when PubnativeRequest request fails
         *
         * @param request Request object used for making the request
         * @param ex      Exception that caused the failure
         */
        void onPubnativeRequestFailed(PubnativeRequest request, Exception ex);
    }

    //==============================================================================================
    // Public
    //==============================================================================================

    /**
     * Sets parameters required to make the pub native request
     *
     * @param key   key name of parameter
     * @param value actual value of parameter
     */
    public void setParameter(String key, String value) {

        Log.v(TAG, "setParameter: " + key + " : " + value);
        if (TextUtils.isEmpty(key)) {
            Log.e(TAG, "Invalid key passed for parameter");
        } else if (TextUtils.isEmpty(value)) {
            mRequestParameters.remove(key);
        } else {
            mRequestParameters.put(key, value);
        }
    }

    /**
     * Sets parameters required to make the pub native request
     *
     * @param key   key name of parameter
     * @param value actual value of parameter
     */
    public void setParameterArray(String key, String[] value) {

        Log.v(TAG, "setParameter: " + key + " : " + value);
        if (TextUtils.isEmpty(key)) {
            Log.e(TAG, "Invalid key passed for parameter");
        } else if (value == null) {
            mRequestParameters.remove(key);
        } else {
            mRequestParameters.put(key, TextUtils.join(",", value));
        }
    }

    /**
     * Starts pubnative request, This function make the ad request to the pubnative server. It makes asynchronous network request in the background.
     *
     * @param context  valid Context object
     * @param endpoint endpoint of ad (ex: NATIVE)
     * @param listener valid nativeRequestListener to track ad request callbacks.
     * @deprecated Start doesn't require an endpoint anymore, this parameter will be ignored
     */
    @Deprecated
    public void start(Context context, Endpoint endpoint, Listener listener) {

        Log.v(TAG, "start");
        start(context, listener);
    }

    /**
     * Starts pubnative request, This function make the ad request to the pubnative server. It makes asynchronous network request in the background.
     *
     * @param context  valid Context object
     * @param listener valid nativeRequestListener to track ad request callbacks.
     */
    public void start(Context context, Listener listener) {

        Log.v(TAG, "start");

        if (listener == null) {
            Log.w(TAG, "start - listener is null and required, dropping call");
        } else if (context == null) {
            Log.w(TAG, "start - context is null and required, dropping call");
        } else if (mIsRunning) {
            Log.w(TAG, "start - this request is already running, dropping the call");
        } else {
            mIsRunning = true;
            mListener = listener;
            mContext = context;

            fillDefaultParameters();

            if (isCoppaModeEnabled()) {

                setAdvertisingID(null);
                doRequest();

            } else {

                // 1. Async Advertising ID request
                // 2. doRequest
                AdvertisingIdClient.getAdvertisingId(mContext, new AdvertisingIdClient.Listener(){

                    @Override
                    public void onAdvertisingIdClientFinish(AdvertisingIdClient.AdInfo adInfo) {

                        setAdvertisingID(adInfo);
                        doRequest();
                    }

                    @Override
                    public void onAdvertisingIdClientFail(Exception exception) {

                        setAdvertisingID(null);
                        doRequest();
                    }
                });
            }
        }
    }

    /**
     * Sets test mode to the status passed in the parameter
     *
     * @param enabled true if you want to enable test mode false if you want to get production ads
     */
    public void setTestMode(boolean enabled) {

        Log.v(TAG, "setTestMode");
        setParameter(Parameters.TEST, enabled ? "1" : "0");
    }

    /**
     * Sets COPPA mode to the status enabled in the parameter
     *
     * @param enabled true if you want to enable COPPA mode
     */
    public void setCoppaMode(boolean enabled) {
        Log.v(TAG, "setCoppaMode");
        setParameter(Parameters.COPPA, enabled ? "1" : "0");
    }

    /**
     * Sets the timeout for the request to the specified timeout
     *
     * @param timeout int value of timeout in milliseconds
     */
    public void setTimeout(int timeout) {

        Log.v(TAG, "setTimeout");
        PubnativeHttpRequest.setConnectionTimeout(timeout);
    }

    //==============================================================================================
    // Private
    //==============================================================================================

    protected void fillDefaultParameters() {

        Log.v(TAG, "startRequest");

        mRequestParameters.put(Parameters.OS, "android");
        mRequestParameters.put(Parameters.DEVICE_MODEL, Build.MODEL);
        mRequestParameters.put(Parameters.OS_VERSION, Build.VERSION.RELEASE);
        mRequestParameters.put(Parameters.LOCALE, Locale.getDefault().getLanguage());

        if (!mRequestParameters.containsKey(Parameters.ASSET_LAYOUT) &&
            !mRequestParameters.containsKey(Parameters.ASSET_FIELDS)) {

            setParameterArray(PubnativeRequest.Parameters.ASSET_FIELDS, new String[]{
                    PubnativeAsset.TITLE,
                    PubnativeAsset.DESCRIPTION,
                    PubnativeAsset.ICON,
                    PubnativeAsset.BANNER,
                    PubnativeAsset.CALL_TO_ACTION,
                    PubnativeAsset.RATING
            });
        }

        String metaString = mRequestParameters.get(Parameters.META_FIELDS);
        List<String> metaList = new ArrayList<String>();
        if (metaString != null) {
            Arrays.asList(TextUtils.split(metaString, ","));
        }
        metaList.add(PubnativeMeta.REVENUE_MODEL);
        metaList.add(PubnativeMeta.CONTENT_INFO);
        setParameterArray(Parameters.META_FIELDS, metaList.toArray(new String[0]));
    }

    protected String getRequestURL() {

        Log.v(TAG, "getRequestURL");
        // Base URL
        Uri.Builder uriBuilder = Uri.parse(BASE_URL).buildUpon();
        // Appending parameters
        for (String key : mRequestParameters.keySet()) {
            String value = mRequestParameters.get(key);
            if (key != null && value != null) {
                uriBuilder.appendQueryParameter(key, value);
            }
        }
        return uriBuilder.build().toString();
    }


    protected void setAdvertisingID(AdvertisingIdClient.AdInfo adInfo) {

        if (adInfo == null || adInfo.isLimitAdTrackingEnabled()) {
            mRequestParameters.put(Parameters.NO_USER_ID, "1");
        } else {
            String advertisingId = adInfo.getId();
            mRequestParameters.put(Parameters.ANDROID_ADVERTISER_ID, advertisingId);
            mRequestParameters.put(Parameters.ANDROID_ADVERTISER_ID_SHA1, Crypto.sha1(advertisingId));
            mRequestParameters.put(Parameters.ANDROID_ADVERTISER_ID_MD5, Crypto.md5(advertisingId));
        }
    }

    protected void doRequest() {

        Log.v(TAG, "doRequest");
        String url = getRequestURL();
        if (url == null) {
            invokeOnFail(new Exception("PubnativeRequest - Error: invalid request URL"));
        } else {
            mRequest = new PubnativeHttpRequest();
            mRequest.start(mContext, url, this);
        }
    }

    protected boolean isCoppaModeEnabled() {
        boolean result = false;
        String coppa = mRequestParameters.get(Parameters.COPPA);
        if (!TextUtils.isEmpty(coppa)) {
            result = coppa.equalsIgnoreCase("1");
        }
        return result;
    }

    protected void processStream(String result) {

        Log.v(TAG, "processStream");
        try {

            PubnativeAPIV3ResponseModel apiResponseModel = new Gson().fromJson(result, PubnativeAPIV3ResponseModel.class);

            if (apiResponseModel == null) {

                // PARSE ERROR
                invokeOnFail(new Exception("PubnativeRequest - Parse error"));

            } else if (PubnativeAPIV3ResponseModel.Status.OK.equals(apiResponseModel.status)) {
                // STATUS 'OK'
                List<PubnativeAdModel> resultModels = null;
                if (apiResponseModel.ads != null) {
                    for (PubnativeAPIV3AdModel adModel : apiResponseModel.ads) {
                        if (resultModels == null) {
                            resultModels = new ArrayList<PubnativeAdModel>();
                        }
                        resultModels.add(PubnativeAdModel.create(mContext, adModel));
                    }
                }
                invokeOnSuccess(resultModels);

            } else {
                // STATUS 'ERROR'
                invokeOnFail(new Exception("PubnativeRequest - Server error: " + apiResponseModel.error_message));
            }

        } catch (Exception exception) {

            invokeOnFail(exception);
        }
    }

    //==============================================================================================
    // Listener Helpers
    //==============================================================================================

    protected void invokeOnSuccess(List<PubnativeAdModel> ads) {

        Log.v(TAG, "invokeOnSuccess");
        mIsRunning = false;
        if (mListener != null) {
            mListener.onPubnativeRequestSuccess(this, ads);
        }
    }

    protected void invokeOnFail(Exception exception) {

        Log.v(TAG, "invokeOnFail: " + exception);
        mIsRunning = false;
        if (mListener != null) {
            mListener.onPubnativeRequestFailed(this, exception);
        }
    }

    //==============================================================================================
    // CALLBACKS
    //==============================================================================================
    // PubnativeHttpRequest.Listener
    //----------------------------------------------------------------------------------------------

    @Override
    public void onPubnativeHttpRequestStart(PubnativeHttpRequest request) {

        Log.v(TAG, "onPubnativeHttpRequestStart");
    }

    @Override
    public void onPubnativeHttpRequestFinish(PubnativeHttpRequest request, String result, int statusCode) {

        Log.v(TAG, "onPubnativeHttpRequestFinish");

        if (PubnativeHttpRequest.HTTP_OK == statusCode
            || PubnativeHttpRequest.HTTP_INVALID_REQUEST == statusCode) {

            // STATUS CODE VALID
            processStream(result);

        } else {

            // STATUS CODE INVALID
            invokeOnFail(new Exception("PubnativeRequest - Response error: " + statusCode));
        }
    }

    @Override
    public void onPubnativeHttpRequestFail(PubnativeHttpRequest request, Exception exception) {

        Log.v(TAG, "onPubnativeHttpRequestFail: " + exception);
        invokeOnFail(exception);
    }

    //----------------------------------------------------------------------------------------------
    // AdvertisingIdClient.Listener
    //----------------------------------------------------------------------------------------------

    @Override
    public void onAdvertisingIdClientFinish(AdvertisingIdClient.AdInfo adInfo) {

        Log.v(TAG, "onAdvertisingIdClientFinish");
        setAdvertisingID(adInfo);
        doRequest();
    }

    @Override
    public void onAdvertisingIdClientFail(Exception exception) {

        Log.v(TAG, "onAdvertisingIdClientFail");
        setAdvertisingID(null);
        doRequest();
    }
}
