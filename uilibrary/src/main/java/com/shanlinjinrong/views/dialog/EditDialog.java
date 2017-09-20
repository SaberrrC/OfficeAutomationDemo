package com.shanlinjinrong.views.dialog;

import android.content.Context;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shanlinjinrong.uilibrary.R;

/**
 * Created by 丁 on 2017/8/23.
 * 确定对话框
 */
public class EditDialog extends BaseDialog<EditDialog> {

    private TextView mSureBtn;
    private TextView mCancelBtn;

    private String mSureBtnText = "";
    private String mCancelBtnText = "";

    private EditText mEditContent;

    private OnEditButtonClickListener mClickListener;

    public EditDialog(Context context) {
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
        mEditContent = (EditText) findViewById(R.id.dialog_edit);

        mSureBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
    }

    @Override
    protected int getContentLayout() {
        //设置布局
        return R.layout.dialog_edit;
    }

    private void refreshView() {
        if (!TextUtils.isEmpty(mSureBtnText)) {
            mSureBtn.setText(mSureBtnText.trim());
        }

        if (!TextUtils.isEmpty(mCancelBtnText)) {
            mCancelBtn.setText(mCancelBtnText.trim());
        }
    }

    public EditDialog setHintText(String hintText) {
        if (mEditContent != null) {
            mEditContent.setHint(hintText);
        }
        return this;
    }

    public EditDialog setHintText(@StringRes int hintTextId) {
        if (mEditContent != null) {
            mEditContent.setHint(hintTextId);
        }
        return this;
    }


    public EditDialog setSureBtnText(String mSureBtnText) {
        this.mSureBtnText = mSureBtnText;
        return this;
    }

    public EditDialog setCancelBtnText(String mCancelBtnText) {
        this.mCancelBtnText = mCancelBtnText;
        return this;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.dialog_btn_sure) {
            if (TextUtils.isEmpty(mEditContent.getText().toString())) {
                Toast.makeText(getContext(), getContext().getString(R.string.dialog_no_write_content), Toast.LENGTH_SHORT).show();
                return;
            }
            if (mClickListener != null) {
                mClickListener.onSureButtonClicked(mEditContent.getText().toString());
            }
        } else if (id == R.id.dialog_btn_cancel) {
            if (mClickListener != null) {
                mClickListener.onCancelButtonClicked();
            }
        }
        dismiss();
    }


    @Override
    public void show() {
        refreshView();
        super.show();
    }

    public interface OnEditButtonClickListener {
        void onSureButtonClicked(String messageContent);

        void onCancelButtonClicked();
    }

    public EditDialog setOnEditButtonClickListener(OnEditButtonClickListener mClickListener) {
        this.mClickListener = mClickListener;
        return this;
    }
}


