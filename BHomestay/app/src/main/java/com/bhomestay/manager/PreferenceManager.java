package com.bhomestay.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Common
 * Created by suninguyen on 10/18/16.
 .
 */
public class PreferenceManager {

    public static final String PREF_DEVICE_ID = "device_id";


    private SharedPreferences prefs;
    private static PreferenceManager instance;

    private PreferenceManager(Context ctx) {
        prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void initialize(Context ctx) {
        instance = new PreferenceManager(ctx);
    }

    public static PreferenceManager getInstance() {
        return instance;
    }

    public SharedPreferences getPrefs() {
        return prefs;
    }

    public SharedPreferences.Editor getEditor() {
        return prefs.edit();
    }

}
