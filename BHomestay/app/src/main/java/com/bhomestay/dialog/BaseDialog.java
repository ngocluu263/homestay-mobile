package com.bhomestay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;


/**
 * Created by suninguyen on 10/18/16.
 */
public abstract class BaseDialog extends Dialog implements DialogInterface {
    private static final String LOG_TAG = BaseDialog.class.getSimpleName();

    protected boolean isHasTitle;

    public BaseDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(getLayoutId());
        setCancelable(false);
        initView();
    }

    public void setValidated(boolean isValidated) {
        // Sub-class of this base dialog can override this method to perform validation if any
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

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
            try {
                super.dismiss();
            } catch (Exception e) {
                Log.d(LOG_TAG, e.getMessage());
            }
        }
    }


    public void setHasTitle(boolean isHasTitle) {
        this.isHasTitle = isHasTitle;
    }

}
