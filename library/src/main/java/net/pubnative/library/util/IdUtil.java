/**
 * Copyright 2014 PubNative GmbH
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.pubnative.library.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;

import org.droidparts.util.L;

public class IdUtil {
    /**
     * Checks if an app with the given name is installed in the device.
     * @param context Context object
     * @param pkgName Package name to be checked
     * @return true if app with given package name is installed, else false
     */
    public static boolean isPackageInstalled(Context context, String pkgName) {
        boolean result = false;
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(pkgName, 0);
            result = true;
        } catch (Exception e) {
            // Do nothing
        }
        return result;
    }

    /**
     * Gets you the package name of the app when it's context is passed in.
     * @param context Context object
     * @return a valid package name if found, else an empty string
     */
    public static String getPackageName(Context context) {
        PackageInfo pInfo = getPackageInfo(context);
        return (pInfo != null) ? pInfo.packageName : "";
    }

    private static PackageInfo getPackageInfo(Context ctx) {
        try {
            return ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            L.w("Error getting package info.");
            return null;
        }
    }

    /**
     * Tells if the device running this app is a tablet or not.
     * @param context Context object
     * @return true if the device is a tablet, else false
     */
    public static boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    /**
     * Gives you the android advertising id.
     * @param context  Context object
     * @param listener Listener to get callback when android ad id is fetched.
     */
    public static void getAndroidAdvertisingID(Context context, AndroidAdvertisingIDTask.AndroidAdvertisingIDTaskListener listener) {
        new AndroidAdvertisingIDTask().setListener(listener).execute(context);
    }

    public static class AndroidAdvertisingIDTask extends AsyncTask<Context, Void, String> {
        private AndroidAdvertisingIDTaskListener listener;

        public interface AndroidAdvertisingIDTaskListener {
            void onAndroidAdvertisingIDTaskFinished(String result);
        }

        public AndroidAdvertisingIDTask setListener(AndroidAdvertisingIDTaskListener listener) {
            this.listener = listener;
            return this;
        }

        @Override
        protected String doInBackground(Context... contexts) {
            String result = null;
            Context context = contexts[0];
            if (context != null) {
                Info adInfo = null;
                try {
                    adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
                    if (adInfo != null) {
                        result = adInfo.getId();
                    }
                } catch (Exception e) {
                    Log.e("Pubnative", "Error retrieving androidAdvertisingID: " + e.toString());
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (this.listener != null) {
                this.listener.onAndroidAdvertisingIDTaskFinished(result);
            }
        }
    }

    /**
     * Gets you the last known location of the device.
     * @param context Context object
     * @return Location object if last known location if available, else null
     */
    public static Location getLastLocation(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Location loc = null;
        for (String prov : lm.getProviders(true)) {
            loc = lm.getLastKnownLocation(prov);
            if (loc != null) {
                break;
            }
        }
        return loc;
    }

    private IdUtil() {
    }
}
