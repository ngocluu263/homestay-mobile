package com.bhomestay.dialog;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bhomestay.R;


/**
 *  Created by suninguyen on 10/18/16.
 */
public class RadioChoiceDialog extends BaseDialog {
    public static final String EXTRA_IDX_CHOICE = "index_choice";
    public static final String EXTRA_LST_CHOICE = "list_choice";
    private int titleResId;
    private int messageResId;
    private RadioGroup rgChoiceGroup;
    private Bundle data;
    private int btnOkResId;
    private int btnCancelResId;
    private Callback callback;

    public RadioChoiceDialog(Context context, Bundle data, int titleResId, int messageResId, int btnOkResId, int btnCancelResId, Callback callback) {
        super(context);
        this.titleResId = titleResId;
        this.messageResId = messageResId;
        this.callback = callback;
        this.btnOkResId = btnOkResId;
        this.btnCancelResId = btnCancelResId;
        this.data = data;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_radio_choice;
    }

    protected void initView() {
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText(titleResId);
        tvTitle.setVisibility(View.GONE);
        TextView tvMessage = (TextView) findViewById(R.id.tv_message);
        tvMessage.setText(messageResId);
        rgChoiceGroup = (RadioGroup) findViewById(R.id.rg_choice_group);
        // Get data source from data bundle
        String[] dataChoices = data.getStringArray(EXTRA_LST_CHOICE);
        if (dataChoices != null && dataChoices.length > 0) {
            for (int i = 0; i < dataChoices.length; i++) {
                rgChoiceGroup.addView(generateChoiceView(i, dataChoices[i]));
            }
            // Check default
            rgChoiceGroup.check(0);
        }
        TextView btnOK = (TextView) findViewById(R.id.tv_ok);
        btnOK.setText(btnOkResId);
        btnOK.setOnClickListener(onClickListener);
        TextView btnCancel = (TextView) findViewById(R.id.tv_cancel);
        btnCancel.setText(btnCancelResId);
        btnCancel.setOnClickListener(onClickListener);
    }

    private View generateChoiceView(int id, String choiceName) {
        RadioButton btnRadio = new RadioButton(getContext());
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        btnRadio.setLayoutParams(layoutParams);
        // Set id to check
        btnRadio.setId(id);
        btnRadio.setTextSize(TypedValue.COMPLEX_UNIT_DIP,19);
        btnRadio.setText(choiceName);
        // Set default button icon to checked drawable
        btnRadio.setButtonDrawable(R.drawable.selector_bg_radio_button);
        final float scale = getContext().getResources().getDisplayMetrics().density;
        btnRadio.setPadding(btnRadio.getPaddingLeft() + (int) (10.0f * scale + 0.5f),
                btnRadio.getPaddingTop(),
                btnRadio.getPaddingRight(),
                btnRadio.getPaddingBottom());
        return btnRadio;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            if (callback != null) {
                switch (v.getId()) {
                    case R.id.tv_ok:
                        int radioButtonID = rgChoiceGroup.getCheckedRadioButtonId();
                        Bundle data = new Bundle();
                        data.putInt(EXTRA_IDX_CHOICE, radioButtonID);
                        callback.onBtnOKPressed(data);
                        break;
                    case R.id.tv_cancel:
                        callback.onBtnCancelPressed();
                        break;
                }
            }
        }
    };

    public interface Callback {

        void onBtnOKPressed(Bundle data);

        void onBtnCancelPressed();
    }
}
