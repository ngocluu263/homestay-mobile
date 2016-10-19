package com.bhomestay.application;


import android.text.TextUtils;

import com.bhomestay.manager.PreferenceManager;
import com.bhomestay.util.Util;

/**
 *  Created by suninguyen on 10/18/16.
 */
public class Application extends android.app.Application {
    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        // Init preference by this context
        PreferenceManager.initialize(this);

        instance = this;

        // Init system device id
        if (TextUtils.isEmpty(PreferenceManager.getInstance().getPrefs().getString(PreferenceManager.PREF_DEVICE_ID, null))) {
            PreferenceManager.getInstance().getEditor().putString(PreferenceManager.PREF_DEVICE_ID, Util.getDeviceUID(Application.getInstance())).commit();
        }

    }

    public static Application getInstance() {
        return instance;
    }
}
