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

package net.pubnative.library.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import java.net.URL;

public class ImageDownloader {

    private static final String TAG = ImageDownloader.class.getSimpleName();

    private Listener mListener;
    private Handler mHandler;

    /**
     * Interface for callbacks related to image downloader
     */
    public interface Listener {
        /**
         * Called whenever image is loaded either from cache or from network
         * @param bitmap Image
         * @param url Url of image
         */
        void onImageLoad(String url, Bitmap bitmap);

        /**
         * Called whenever image loading failed
         * @param url Url of image
         */
        void onImageFailed(String url, Exception exception);
    }

    /**
     * Load image
     * @param url valid url
     * @param listener valid listener
     */
    public void load(String url, Listener listener) {

        Log.v(TAG, "load");
        mHandler = new Handler(Looper.getMainLooper());
        if(listener == null) {
            Log.e(TAG, "Listener is not set, dropping call");
        } else if(TextUtils.isEmpty(url)) {
            invokeFail(url, new Exception("URL is not valid"));
        } else {
            mListener = listener;
            downloadImage(url);
        }
    }

    //==============================================================================================
    // Private methods
    //==============================================================================================
    private void downloadImage(final String urlString) {

        Log.v(TAG, "downloadImage");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap result = null;
                try {
                    URL url = new URL(urlString);
                    final BitmapFactory.Options options = new BitmapFactory.Options();

                    // Fill options with data about bitmap only
                    // without allocating memory.
                    // decodeStream() will return null.
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(url.openConnection().getInputStream(), new Rect(), options);

                    // Calculate size of the image depends of free memory of device
                    options.inSampleSize = calculateInSampleSize(options);

                    // Get image and allocate memory for it.
                    options.inJustDecodeBounds = false;
                    result = BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, options);
                } catch (Exception e) {
                    invokeFail(urlString, e);
                } finally {
                    invokeLoad(urlString, result);
                }
            }
        }).start();
    }

    //==============================================================================================
    // Callback helpers
    //==============================================================================================
    protected void invokeLoad(final String url, final Bitmap bitmap) {

        Log.v(TAG, "invokeLoad");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mListener != null) {
                    mListener.onImageLoad(url, bitmap);
                }
            }
        });
    }

    protected void invokeFail(final String url, final Exception exception) {

        Log.v(TAG, "invokeFail");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mListener != null) {
                    mListener.onImageFailed(url, exception);
                }
            }
        });
    }

    /**
     * This method calculates the inSampleSize which create a smaller image than original.
     * It's needed, because when we try to use decodeStream for the big image BitmapFactory
     * allocate memory not only for himself but also and for downloaded image and some processed data.
     * @param options options for {@link android.graphics.BitmapFactory.Options BitmapFactory.Options}
     * @return int {@link android.graphics.BitmapFactory.Options#inSampleSize inSampleSize} which decoder use to subsample the original image, returning a smaller image to save memory
     */
    protected int calculateInSampleSize(BitmapFactory.Options options) {

        Log.v(TAG, "calculateInSampleSize");
        int bytesPerPixel = 4;
        int inSampleSize = 1;
        final int height = options.outHeight;
        final int width = options.outWidth;
        final int freeMemory = (int) (Runtime.getRuntime().freeMemory());
        final int pictureSize = width * height * bytesPerPixel;

        if (pictureSize > freeMemory) {
            inSampleSize = pictureSize / freeMemory;
        }

        return inSampleSize;
    }
}
