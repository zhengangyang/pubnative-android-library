package net.pubnative.library.demo.utils;

/**
 * Created by davidmartin on 25/02/16.
 */
public class Settings
{
    private static final String DEFAULT_APP_TOKEN = "1bc3b0a3ee6cfa1f35ac2f3e9630cc7320e999118cb49a9591dfc957b58d98a0";

    private static String mAppToken = null;

    public static String getAppToken() {

        if(mAppToken  == null) {
            mAppToken  = DEFAULT_APP_TOKEN;
        }
        return mAppToken ;
    }

    public static void setAppToken(String appToken) {
        mAppToken = appToken;
    }
}
