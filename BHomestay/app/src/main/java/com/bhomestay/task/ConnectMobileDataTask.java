package com.bhomestay.task;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

import com.bhomestay.R;
import com.bhomestay.dialog.ProgressDialog;

import java.util.HashMap;
import java.util.Map;



/**
 * Created by suninguyen on 10/18/16.
 */
public class ConnectMobileDataTask extends AsyncTask<String, Void, Boolean> {
    private static final String LOG_TAG = ConnectMobileDataTask.class.getSimpleName();
    private static final int TIME_OUT_WIFI_CONNECTED = 30000;
    private static final int RECEIVED_COUNT_DEFAULT = 1;

    private Map<BroadcastReceiver, Integer> receivedCountMap;
    private ProgressDialog progressDialog;
    private Context ctx;
    private boolean isMobileDataConnected;
    // Save current thread instance to interrupt
    private Thread myThread;
    private Callback callback;

    public ConnectMobileDataTask(Context ctx, Callback callback) {
        this.ctx = ctx;
        this.callback = callback;
        // Init broadcast receiver count map to ignore the first receiver's message after registered
        receivedCountMap = new HashMap<>();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Show progress dialog to force user waiting during connecting wifi
        progressDialog = new ProgressDialog(ctx, R.string.txt_dialog_waiting);
        progressDialog.show();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        // Save current thread to interrupt later
        myThread = Thread.currentThread();
        Log.d(LOG_TAG, "doInBackground() Enable and connect mobile data.....................................");
        // Fix issue trigger onReceive() once automatically when receiver is registered
        receivedCountMap.put(mobileDataConnectedReceiver, RECEIVED_COUNT_DEFAULT);
        // Register receiver for connect wifi
        ctx.registerReceiver(mobileDataConnectedReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        // Enable and connect mobile data
        // Wait for wifi connected event happens or time out
        try {
            isMobileDataConnected = false;
            WifiManager wifi = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
            wifi.setWifiEnabled(false);
            Thread.sleep(TIME_OUT_WIFI_CONNECTED);
        } catch (InterruptedException e) {
            if (isMobileDataConnected) {
                Log.w(LOG_TAG, "doInBackground() Enable and connect mobile data has been success due to " + e);
            } else {
                Log.w(LOG_TAG, "doInBackground() Enable and connect mobile data has been canceled due to " + e);
            }
        }
        // Unregister receiver for enable/connect mobile data
        if (!isMobileDataConnected) {
            unregisterReceiver(mobileDataConnectedReceiver);
            // Enable/connect mobile data failed
            // Exit this task with failed status
            return false;
        }
        // Connected, return true
        return true;
    }

    @Override
    protected void onPostExecute(Boolean connectResult) {
        super.onPostExecute(connectResult);
        progressDialog.dismiss();
        if (callback != null) {
            callback.onConnectResult(connectResult);
        }
    }

    private BroadcastReceiver mobileDataConnectedReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // IMPORTANT NOTE: This method will trigger when the first register receiver, so must ignore this
            Integer receivedCount = receivedCountMap.get(mobileDataConnectedReceiver);
            if (receivedCount > 0) {
                // Decrease the number of limit first message
                receivedCount--;
                receivedCountMap.put(mobileDataConnectedReceiver, receivedCount);
                // Discard this message
                return;
            }
            @SuppressWarnings("deprecation")
            NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (info.getType() == ConnectivityManager.TYPE_MOBILE && info.getState() == NetworkInfo.State.CONNECTED) {
                Log.d(LOG_TAG, "Mobile data connected");
                isMobileDataConnected = true;
                // Unregister receiver for wifi connected
                unregisterReceiver(mobileDataConnectedReceiver);
                // Because this task's thread is being blocked for waiting time out, so we must release it
                // IMPORTANT NOTE: Because all broadcast receivers run on main UI thread, so we must use current task's thread
                myThread.interrupt();
            }
        }
    };

    private void unregisterReceiver(BroadcastReceiver receiver) {
        try {
            ctx.unregisterReceiver(receiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public interface Callback {

        void onConnectResult(boolean isConnected);

    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onCancelled(Boolean aBoolean) {
        super.onCancelled(aBoolean);
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
}
