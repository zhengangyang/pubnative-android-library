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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class ScreenUtil {
    /**
     * Gets the screen density of the device's display
     * @param context Context object
     * @return Screen density in Dpi
     */
    public static int getScreenDensityDpi(Context context) {
        return context.getResources().getDisplayMetrics().densityDpi;
    }

    /**
     * Gets the width of the device's display
     * @param context Context object
     * @return Width of the screen in pixels
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * Gets the height of the device's display
     * @param context Context object
     * @return Height of the screen in pixels
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    @SuppressLint("NewApi")
    public static Point getRealScreenSize(Context ctx) {
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();
        Point p = new Point();
        p.x = getScreenWidth(ctx);
        p.y = getScreenHeight(ctx);
        return p;
    }

    /**
     * Checks if the orientation of the screen is portrait
     * @param context Context object
     * @return true if orientation is portrait, else false
     */
    public static boolean isPortrait(Context context) {
        int orientation = context.getResources().getConfiguration().orientation;
        return (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * Checks if the activity is shown in fullscreen
     * @param activity Activity object
     * @return true if activity is fullscreen, else false
     */
    public static boolean isFullScreen(Activity activity) {
        int windowFlags = activity.getWindow().getAttributes().flags;
        return (windowFlags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0;
    }

    private ScreenUtil() {
    }
}
