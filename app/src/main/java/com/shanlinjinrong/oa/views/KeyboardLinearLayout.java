package com.shanlinjinrong.oa.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * ProjectName: GroupInformationPlatform
 * PackageName: com.itcrm.GroupInformationPlatform.views
 * Author:Created by CXP on Date: 2016/9/4 16:39.
 * Description: 自定义监听软键盘的线性布局
 */
public class KeyboardLinearLayout extends LinearLayout {

    private onSizeChangedListener mChangedListener;
    private static final String TAG = "KeyboardLayoutTAG";
    private boolean mShowKeyboard = false;

    public KeyboardLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public KeyboardLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyboardLinearLayout(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (null != mChangedListener && 0 != oldw && 0 != oldh) {
            if (h < oldh) {
                mShowKeyboard = true;
            } else {
                mShowKeyboard = false;
            }
            mChangedListener.onChanged(mShowKeyboard, h);
        }
    }

    public void setOnSizeChangedListener(onSizeChangedListener listener) {
        mChangedListener = listener;
    }

    public interface onSizeChangedListener {

        void onChanged(boolean showKeyboard, int h);
    }
}
