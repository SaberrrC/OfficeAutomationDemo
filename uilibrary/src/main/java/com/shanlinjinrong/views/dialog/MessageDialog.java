package com.shanlinjinrong.views.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.shanlinjinrong.uilibrary.R;

/**
 * Created by 丁 on 2017/8/23.
 * 确定 取消对话框
 */
public class MessageDialog extends BaseDialog<MessageDialog> {

    private TextView mSureBtn;
    private TextView mCancelBtn;

    private String mSureBtnText = "";
    private String mCancelBtnText = "";

    private View mButtonLine;//两个按钮之间的竖线

    public MessageDialog(Context context) {
        super(context);
        //初始化控件
        initView(context);
    }


    /**
     * 初始化view
     */
    private void initView(Context context) {
        mSureBtn = (TextView) findViewById(R.id.dialog_btn_sure);
        mCancelBtn = (TextView) findViewById(R.id.dialog_btn_cancel);
        mButtonLine = findViewById(R.id.button_line);

        mSureBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.dialog_common;
    }

    private void refreshView() {

        if (!TextUtils.isEmpty(mSureBtnText)) {
            mSureBtn.setText(mSureBtnText.trim());
        }

        if (!TextUtils.isEmpty(mCancelBtnText)) {
            mCancelBtn.setText(mCancelBtnText.trim());
        }
    }


    public MessageDialog setSureBtnText(String mSureBtnText) {
        this.mSureBtnText = mSureBtnText;
        return this;
    }

    public MessageDialog setCancelBtnText(String mCancelBtnText) {
        this.mCancelBtnText = mCancelBtnText;
        return this;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.dialog_btn_sure) {
            if (mButtonClickListener != null) {
                mButtonClickListener.onSureButtonClicked();
            }
        } else if (id == R.id.dialog_btn_cancel) {
            if (mButtonClickListener != null) {
                mButtonClickListener.onCancelButtonClicked();
            }
        }
        dismiss();
    }

    public MessageDialog setOnlySureButton(boolean onlySureButton) {
        if (onlySureButton) {
            mCancelBtn.setVisibility(View.GONE);
            mButtonLine.setVisibility(View.GONE);
        }
        return this;
    }

    @Override
    public void show() {
        refreshView();
        super.show();
    }
}


