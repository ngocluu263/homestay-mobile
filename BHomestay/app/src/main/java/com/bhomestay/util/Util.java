package com.bhomestay.util;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

public class Util {
    private static final String LOG_TAG = "Util";

    public static String getDeviceUID(Context context) {
        String androidId = null;
        try {
            androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception ex) {
            Log.w(LOG_TAG, "Can't get android ID" + ", exception: " + ex.getMessage());
        }
        return androidId;
    }
}