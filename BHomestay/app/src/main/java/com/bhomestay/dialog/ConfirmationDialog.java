package com.bhomestay.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bhomestay.R;


/**
 *   Created by suninguyen on 10/18/16.
 */
public class ConfirmationDialog extends BaseDialog {
    private int titleResId;
    private int messageResId;
    private int tvOkResId;
    private String message;
    private Callback callback;
    private boolean isOnAttach;


    public ConfirmationDialog(Context context, int titleResId, int messageResId, int tvOkResId, Callback callback) {
        super(context);
        this.titleResId = titleResId;
        this.messageResId = messageResId;
        this.tvOkResId = tvOkResId;
        this.callback = callback;
    }

    public ConfirmationDialog(Context context, int titleResId, String message, int tvOkResId, Callback callback) {
        super(context);
        this.titleResId = titleResId;
        this.tvOkResId = tvOkResId;
        this.message = message;
        this.callback = callback;
    }

    public void setMessage(String message) {
        this.message = message;
        TextView tvMessage = (TextView) findViewById(R.id.tv_message);
        if(tvMessage != null) {
            tvMessage.setText(message);
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        isOnAttach = true;
    }

    public boolean isOnAttach() {
        return isOnAttach;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_confirmation;
    }

    protected void initView() {
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText(titleResId);
        tvTitle.setVisibility(View.GONE);
        TextView tvMessage = (TextView) findViewById(R.id.tv_message);
        if (!TextUtils.isEmpty(message)) {
            tvMessage.setText(message);
        } else {
            tvMessage.setText(messageResId);
        }
        TextView tvOK = (TextView) findViewById(R.id.tv_ok);
        tvOK.setText(tvOkResId);
        tvOK.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            if (callback != null) {
                callback.onBtnOKPressed();
            }
        }
    };

    public interface Callback {

        void onBtnOKPressed();

    }
}
