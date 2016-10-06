package net.pubnative.library.demo.utils;

/**
 * Created by davidmartin on 25/02/16.
 */
public class Settings
{
    private static final String DEFAULT_APP_TOKEN = "1bc3b0a3ee6cfa1f35ac2f3e9630cc7320e999118cb49a9591dfc957b58d98a0";
    private static final String DEFAULT_ZONE_ID = "1";

    private static String mAppToken = null;
    private static String mZoneId = null;

    public static String getAppToken() {

        if(mAppToken  == null) {
            mAppToken  = DEFAULT_APP_TOKEN;
        }
        return mAppToken ;
    }

    public static void setAppToken(String appToken) {
        mAppToken = appToken;
    }

    public static String getZoneId() {

        if(mZoneId  == null) {
            mZoneId  = DEFAULT_ZONE_ID;
        }
        return mZoneId ;
    }

    public static void setZoneId(String zoneId) {
        mZoneId = zoneId;
    }
}
