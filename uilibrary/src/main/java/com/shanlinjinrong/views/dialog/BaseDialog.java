package com.shanlinjinrong.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.res.ResourcesCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.shanlinjinrong.uilibrary.R;
import com.shanlinjinrong.utils.DeviceUtil;

/**
 * Created by 丁 on 2017/9/7.
 * BaseDialog
 */
public abstract class BaseDialog<T extends BaseDialog> extends Dialog implements View.OnClickListener {

    private WindowManager.LayoutParams mLayoutParams;
    private Window mWindow;
    OnButtonClickListener mButtonClickListener;

    private TextView mTvTitle;
    private TextView mTvMessage;

    BaseDialog(Context context) {
        super(context);
        initView(context);
    }

    BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    private void initView(Context context) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getContentLayout() != 0) {
            setContentView(getContentLayout());
        }
        mWindow = this.getWindow();
        if (mWindow != null) {
            mLayoutParams = mWindow.getAttributes();
            mWindow.setWindowAnimations(R.style.dialogAnimator);
            mLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            mLayoutParams.width = (int) (DeviceUtil.getScreenWidth(context) * 0.85);
            mLayoutParams.gravity = Gravity.CENTER;
            mWindow.setAttributes(mLayoutParams);
        }


        mTvTitle = (TextView) findViewById(R.id.dialog_title);
        mTvMessage = (TextView) findViewById(R.id.dialog_message);

        hideTitle();

        //设置点击弹框外部不消失
        setCanceledOnTouchOutside(false);
    }


    public T setDialogSize(int width, int height) {
        if (mLayoutParams != null && mWindow != null) {
            mLayoutParams.width = width;
            mLayoutParams.height = height;
            mWindow.setAttributes(mLayoutParams);
        }
        return (T) this;
    }

    public T hideTitle() {
        if (mTvTitle != null) {
            mTvTitle.setVisibility(View.GONE);
        }
        return (T) this;
    }


    public T setTitle(String mTitle) {
        if (mTvTitle != null) {
            mTvTitle.setText(mTitle);
            mTvTitle.setVisibility(View.VISIBLE);
        }
        return (T) this;
    }

    public void setTitle(@StringRes int titleId) {
        setTitle(getContext().getText(titleId).toString());
    }

    public T setMessage(String mMessage) {
        if (mTvMessage != null) {
            mTvMessage.setText(mMessage);
        }
        return (T) this;
    }

    public void setMessage(@StringRes int messageId) {
        setMessage(getContext().getText(messageId).toString());
    }

    public T setIcon(Drawable icon) {
        if (mTvTitle != null && icon != null) {
            mTvTitle.setCompoundDrawables(icon, null, null, null);
            mTvTitle.setCompoundDrawablePadding(DeviceUtil.dip2px(getContext(), 8));
            mTvTitle.setVisibility(View.VISIBLE);
        }
        return (T) this;
    }

    public void setIcon(@DrawableRes int iconId) {
        setIcon(ResourcesCompat.getDrawable(getContext().getResources(), iconId, null));
    }

    protected abstract int getContentLayout();


    public interface OnButtonClickListener {

        void onSureButtonClicked();

        void onCancelButtonClicked();
    }

    public T setOnButtonClickListener(OnButtonClickListener mButtonClickListener) {
        this.mButtonClickListener = mButtonClickListener;
        return (T) this;
    }


}
