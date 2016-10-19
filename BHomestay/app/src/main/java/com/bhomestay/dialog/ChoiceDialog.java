package com.bhomestay.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bhomestay.R;


/**
 *  Created by suninguyen on 10/18/16.
 */
public class ChoiceDialog extends BaseDialog {

    private int titleResId;
    private int messageResId;
    private int tvOkResId;
    private int tvCancelResId;
    private String message;
    private String title;
    private String leftBtn;
    private String rightBtn;
    private Callback callback;

    public ChoiceDialog(Context context, int titleResId, int messageResId, int tvOkResId, int tvCancelResId, Callback callback) {
        super(context);
        this.titleResId = titleResId;
        this.messageResId = messageResId;
        this.tvOkResId = tvOkResId;
        this.tvCancelResId = tvCancelResId;
        this.callback = callback;
    }

    public ChoiceDialog(Context context, int titleResId, String message, int tvOkResId, int tvCancelResId, Callback callback) {
        super(context);
        this.titleResId = titleResId;
        this.message = message;
        this.tvOkResId = tvOkResId;
        this.tvCancelResId = tvCancelResId;
        this.callback = callback;
    }

    public void setTitle(String title) {
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        this.title = title;
        if(tvTitle != null) {
            tvTitle.setText(title);
        }
    }

    public void setMessage(String message) {
        TextView tvMessage = (TextView) findViewById(R.id.tv_message);
        this.message = message;
        if(tvMessage != null) {
            tvMessage.setText(message);
        }
    }

    public void setBtnOk(String text) {
        TextView tvOK = (TextView) findViewById(R.id.tv_ok);
        this.leftBtn = text;
        if(tvOK != null) {
            tvOK.setText(text);
        }
    }

    public void setBtnCancel(String text) {
        TextView tvCancel = (TextView) findViewById(R.id.tv_cancel);
        this.rightBtn = text;
        if(tvCancel != null) {
            tvCancel.setText(text);
        }
    }


    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_choice;
    }

    protected void initView() {
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        if (isHasTitle && titleResId != 0) {
            tvTitle.setText(titleResId);
            tvTitle.setVisibility(View.VISIBLE);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
        TextView tvMessage = (TextView) findViewById(R.id.tv_message);
        if(!TextUtils.isEmpty(message)) {
            tvMessage.setText(message);
        } else {
            tvMessage.setText(messageResId);
        }
        TextView tvOK = (TextView) findViewById(R.id.tv_ok);
        if(!TextUtils.isEmpty(leftBtn)) {
            tvOK.setText(leftBtn);
        } else {
            tvOK.setText(tvOkResId);
        }
        tvOK.setOnClickListener(onClickListener);
        TextView tvCancel = (TextView) findViewById(R.id.tv_cancel);
        if(!TextUtils.isEmpty(rightBtn)) {
            tvCancel.setText(rightBtn);
        } else {
            tvCancel.setText(tvCancelResId);
        }
        tvCancel.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            if (callback != null) {
                switch (v.getId()) {
                    case R.id.tv_ok:
                        callback.onBtnOKPressed();
                        break;
                    case R.id.tv_cancel:
                        callback.onBtnCancelPressed();
                        break;
                }
            }
        }
    };

    public interface Callback {

        void onBtnOKPressed();

        void onBtnCancelPressed();
    }

}
