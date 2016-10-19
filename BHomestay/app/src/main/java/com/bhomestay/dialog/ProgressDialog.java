package com.bhomestay.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;

import com.bhomestay.R;


/**
 * Created by suninguyen on 10/18/16.
 */
public class ProgressDialog extends android.app.ProgressDialog implements DialogInterface {
    private static final String LOG_TAG = ProgressDialog.class.getSimpleName();
    private String message;

    public ProgressDialog(Context context, int message) {
        this(context, context.getResources().getString(message));
    }

    public ProgressDialog(Context context, String message) {
        super(context);
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.dialog_progress);
    }

    @Override
    public void show() {
        if (isShowing()) {
            Log.w(LOG_TAG, "Badly handle dialog: duplicate dialog");
        } else {
            try {
                super.show();
            } catch (Exception e) {
                Log.d(LOG_TAG, e.getMessage());
            }
        }
    }

    @Override
    public void dismiss() {
        if (!isShowing()) {
            Log.w(LOG_TAG, "Badly handle dialog: dialog is currently not showing to be dismissed");
        } else {
            if(callbackDismiss != null){
                callbackDismiss.onDismiss();
            }
            try {
                super.dismiss();
            } catch (Exception e) {
                Log.d(LOG_TAG, e.getMessage());
            }
        }
    }

    private NotifyDismiss callbackDismiss;

    public void setCallbackDismiss(NotifyDismiss callbackDismiss) {
        this.callbackDismiss = callbackDismiss;
    }

    public interface NotifyDismiss{

        void onDismiss();
    }
}
