package com.bhomestay.activity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bhomestay.R;
import com.bhomestay.dialog.BaseDialog;
import com.bhomestay.dialog.RadioChoiceDialog;
import com.bhomestay.manager.NetworkManager;
import com.bhomestay.task.ConnectMobileDataTask;

/**
 * Created by suninguyen on 10/18/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static final String LOG_TAG = BaseActivity.class.getSimpleName();

    private Handler handler = new Handler();
    private BaseDialog dialog;

    /**
     * This method will call before
     */
    protected void prepareCallAPI() {
        // Check network
        boolean isConnected = NetworkManager.isConnected(this);
        if (!isConnected) {

            // if no network, show dialog.
            showChooseNetworkDialog(this);
        } else {
            doFunction();
        }
    }

    /**
     * This method use for calling API;
     */
    protected abstract void doFunction();


    /**
     * User can choose using Wifi or 3G
     */
    private void showChooseNetworkDialog(final Context context) {
        Bundle bundle = new Bundle();
        bundle.putStringArray(RadioChoiceDialog.EXTRA_LST_CHOICE, NetworkManager.NET_SUPPORT);
        dismissAllDialog();
        dialog = new RadioChoiceDialog(
                context, bundle, R.string.tit_dialog_choose_network,
                R.string.txt_dialog_choose_network,
                R.string.txt_dialog_btn_ok,
                R.string.txt_dialog_btn_cancel,
                new RadioChoiceDialog.Callback() {
                    @Override
                    public void onBtnOKPressed(final Bundle data) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                int indexChoice = data.getInt(RadioChoiceDialog.EXTRA_IDX_CHOICE);
                                Log.d(LOG_TAG, "Index in dialog: " + indexChoice);
                                // If user choose wifi network, set flag to auto send when comeback this activity
                                // and go to wifi setting screen.
                                if (NetworkManager.NET_SUPPORT[indexChoice].equals(NetworkManager.NET_SUPPORT_WIFI)) {
                                    // Set flag to auto send request add TCU when comeback this activity
                                    // Go to setting screen
                                    startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
                                }
                                // If user choose 3G network
                                else if (NetworkManager.NET_SUPPORT[indexChoice].equals(NetworkManager.NET_SUPPORT_MOBILE)) {
                                    connectMobileData();
                                }
                            }
                        });
                    }

                    @Override
                    public void onBtnCancelPressed() {

                    }
                });
        dialog.show();
    }

    protected void dismissAllDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void connectMobileData() {
        ConnectMobileDataTask connectMobileDataTask = new ConnectMobileDataTask(BaseActivity.this, new ConnectMobileDataTask.Callback() {
            @Override
            public void onConnectResult(boolean isConnected) {
                if (isConnected) {
                    doFunction();
                } else {
                    //TODO handle no access network

                }
            }
        });
        connectMobileDataTask.execute();
    }

}
