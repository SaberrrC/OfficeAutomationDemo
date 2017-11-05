package com.shanlinjinrong.views.common;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanlinjinrong.uilibrary.R;
import com.shanlinjinrong.utils.DeviceUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by 丁 on 2017/8/18.
 * 通用的顶部标题栏
 * 1. 左侧文字或图片显示按钮
 * 2. 中部标题
 * 3. 右侧图片或者文字按钮
 */
public class CommonTopView extends RelativeLayout {

    private String mTitle;
    private int mTitleColor;
    private int mTitleSize = 18;

    private String mRightText;
    private int mRightTextColor;
    private int mRightTextSize = 16;
    private int mRightMargin; //右View与右边界的间距
    private Drawable mRightDrawable;

    private String mLeftText;
    private int mLeftTextColor;
    private int mLeftTextSize = 16;
    private Drawable mLeftDrawable;
    private int mLeftMargin;//左View与左侧边界的距离

    private Context mContext;


    private OnClickListener mLeftAction = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mContext == null) {
                return;
            }
            ((Activity) mContext).onBackPressed();
        }
    };
    private OnClickListener mRightAction;

    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.topview_left_view && mLeftAction != null) {
                mLeftAction.onClick(v);
                return;
            }
            if (v.getId() == R.id.topview_right_view && mRightAction != null) {
                mRightAction.onClick(v);
            }
        }
    };


    public CommonTopView(Context context) {
        this(context, null);
    }

    public CommonTopView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonTopView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        initAttr(context, attrs);
        initTopView();
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.commonTopView);
        //左侧样式
        mLeftText = a.getString(R.styleable.commonTopView_leftText);
        mLeftTextColor = a.getColor(R.styleable.commonTopView_leftTextColor, ResourcesCompat.getColor(getResources(), R.color.black, null));
        mLeftTextSize = (int) a.getDimension(R.styleable.commonTopView_leftTextSize, mLeftTextSize);
        mLeftMargin = (int) a.getDimension(R.styleable.commonTopView_leftTextMargin, DeviceUtil.dip2px(mContext, 12));
        mLeftDrawable = a.getDrawable(R.styleable.commonTopView_leftDrawable);

        //右侧样式
        mRightText = a.getString(R.styleable.commonTopView_rightText);
        mRightTextColor = a.getColor(R.styleable.commonTopView_rightTextColor, ResourcesCompat.getColor(getResources(), R.color.black, null));
        mRightTextSize = (int) a.getDimension(R.styleable.commonTopView_rightTextSize, mRightTextSize);
        mRightMargin = (int) a.getDimension(R.styleable.commonTopView_rightTextMargin, DeviceUtil.dip2px(mContext, 12));
        mRightDrawable = a.getDrawable(R.styleable.commonTopView_rightDrawable);


        //Title默认样式
        mTitle = a.getString(R.styleable.commonTopView_topTitle);
        mTitleColor = a.getColor(R.styleable.commonTopView_titleColor, ResourcesCompat.getColor(getResources(), R.color.black, null));
        mTitleSize = (int) a.getDimension(R.styleable.commonTopView_titleTextSize, mTitleSize);

        a.recycle();
    }

    /** @hide */
    @IntDef({VISIBLE, INVISIBLE, GONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Visibility {}

    private void initTopView() {
        Drawable background = getBackground();
        if (background == null) {//设置background
            setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
        }
        TextView leftView = null;
        if (mLeftText != null) {
            leftView = (TextView) getOrCreateLeftView();//初始化这个位置肯定是TextView
            leftView.setText(mLeftText);
        }
        if (mLeftDrawable != null) {
            leftView = (TextView) getOrCreateLeftView();
            leftView.setCompoundDrawablesWithIntrinsicBounds(mLeftDrawable, null, null, null);
        }

        TextView rightView;
        if (mRightText != null) {
            rightView = (TextView) getOrCreateRightView();//初始化这个位置肯定是TextView
            rightView.setText(mRightText);
        }
        if (mRightDrawable != null) {
            rightView = (TextView) getOrCreateRightView();
            rightView.setCompoundDrawablesWithIntrinsicBounds(null, null, mRightDrawable, null);
        }

        if (mTitle != null) {
            setAppTitle(mTitle);
        }

    }

    /**
     * 自定义TopView左侧视图View
     *
     * @param view 左侧视图View
     */
    public void setLeftView(View view) {
        View leftView = findViewById(R.id.topview_left_view);

        if (null == view) {
            return;
        }

        if (null != leftView) {
            removeView(leftView);
        }

        LayoutParams lp = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        view.setOnClickListener(mClickListener);
        view.setId(R.id.topview_left_view);
        addView(view, lp);
    }

    /**
     * 设置TopView左侧文本
     *
     * @param resId String ID
     */
    public void setLeftText(int resId) {
        String content = getResources().getString(resId);
        setLeftText(content);
    }

    /**
     * 设置TopView左侧文本
     *
     * @param content String文本
     */
    public void setLeftText(String content) {
        View view = getOrCreateLeftView();
        if (view instanceof TextView) {
            ((TextView) view).setText(content);
        } else {
            throw new IllegalStateException(view.getClass() + "cannot set a text");
        }

    }

    /**
     * 设置TopView左侧图标
     */
    public void setLeftDrawable(Drawable drawable) {
        View view = getOrCreateLeftView();
        if (view instanceof TextView) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            ((TextView) view).setCompoundDrawables(drawable, null, null, null);
        } else if (view instanceof ImageView) {
            ((ImageView) view).setImageDrawable(drawable);
        } else {
            throw new IllegalStateException(view.getClass() + "cannot set a image");
        }

    }

    /**
     * 设置TopView左侧图标
     */
    public void setLeftDrawable(int resId) {
        setLeftDrawable(getResources().getDrawable(resId));
    }

    private View getOrCreateLeftView() {
        View view = findViewById(R.id.topview_left_view);
        if (view == null) {
            TextView textView = new TextView(getContext());
            //默认左侧样式
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.setMargins(mLeftMargin, 0, 0, 0);
            textView.setId(R.id.topview_left_view);
            textView.setTextColor(mLeftTextColor);
            textView.setTextSize(mLeftTextSize);
            textView.setGravity(Gravity.CENTER);
            textView.setOnClickListener(mClickListener);
            view = textView;
            addView(view, params);
        }
        return view;
    }


    /**
     * 自定义右侧视图View
     */
    public void setRightView(View view) {
        if (null == view) {
            return;
        }

        View rightView = findViewById(R.id.topview_right_view);
        if (null != rightView) {
            removeView(rightView);
        }

        LayoutParams lp = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        view.setLayoutParams(lp);
        view.setId(R.id.topview_right_view);
        view.setOnClickListener(mClickListener);
        addView(view);
    }

    /**
     * 设置TopView右侧文本
     */
    public void setRightText(int resId) {
        String content = getResources().getString(resId);
        setRightText(content);
    }

    /**
     * 设置TopView右侧文本
     */
    public void setRightText(String content) {
        View view = getOrCreateRightView();
        if (view instanceof TextView) {
            ((TextView) view).setText(content);
        } else {
            throw new IllegalStateException(view.getClass() + "cannot set a text");
        }

    }

    /**
     * 设置TopView右侧图标
     */
    public void setRightDrawable(Drawable drawable) {
        View view = getOrCreateRightView();
        if (view instanceof TextView) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            ((TextView) view).setCompoundDrawables(drawable, null, null, null);
        } else if (view instanceof ImageView) {
            ((ImageView) view).setImageDrawable(drawable);
        } else {
            throw new IllegalStateException(view.getClass() + "cannot set a image");
        }

    }

    /**
     * 设置TopView右侧图标
     */
    public void setRightDrawable(int resId) {
        setRightDrawable(getResources().getDrawable(resId));
    }

    private View getOrCreateRightView() {
        View view = findViewById(R.id.topview_right_view);
        if (view == null) {
            TextView textView = new TextView(getContext());
            //右侧默认样式
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.setMargins(0, 0, mRightMargin, 0);
            textView.setId(R.id.topview_right_view);
            textView.setTextColor(mRightTextColor);
            textView.setTextSize(mRightTextSize);
            textView.setGravity(Gravity.CENTER);
            textView.setOnClickListener(mClickListener);
            view = textView;
            addView(view, params);
        }
        return view;
    }

    /**
     * 设置title
     */
    public void setAppTitle(int resID) {
        String content = getResources().getString(resID);
        setAppTitle(content);
    }

    public TextView getTitleView(){
        View view = findViewById(R.id.topview_app_title);
        TextView titleView;
        if (view != null && !(view instanceof TextView)) {
            throw new RuntimeException(view.getClass().getName() + " cannot set a text.");
        }

        if (view == null) {
            titleView = new TextView(mContext);
            titleView.setTextColor(mTitleColor);
            titleView.setTextSize(mTitleSize);
            titleView.setGravity(Gravity.CENTER);
            titleView.setId(R.id.topview_app_title);
            titleView.setSingleLine();
            titleView.setEllipsize(TextUtils.TruncateAt.END);
            LayoutParams lp = new LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            lp.setMargins(DeviceUtil.dip2px(mContext, 70), 0, DeviceUtil.dip2px(mContext, 70), 0);
            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
            addView(titleView, lp);
        } else {
            titleView = (TextView) view;
        }
        return titleView;
    }

    /**
     * 设置title
     */
    public void setAppTitle(String content) {
        View view = findViewById(R.id.topview_app_title);
        TextView titleView;
        if (view != null && !(view instanceof TextView)) {
            throw new RuntimeException(view.getClass().getName() + " cannot set a text.");
        }

        if (view == null) {
            titleView = new TextView(mContext);
            titleView.setTextColor(mTitleColor);
            titleView.setTextSize(mTitleSize);
            titleView.setGravity(Gravity.CENTER);
            titleView.setId(R.id.topview_app_title);
            titleView.setSingleLine();
            titleView.setEllipsize(TextUtils.TruncateAt.END);
            LayoutParams lp = new LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            lp.setMargins(DeviceUtil.dip2px(mContext, 70), 0, DeviceUtil.dip2px(mContext, 70), 0);
            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
            addView(titleView, lp);
        } else {
            titleView = (TextView) view;
        }
        titleView.setText(content);
    }

    /**
     * 为布局设置焦点事件
     */
    public void setTopViewFocus() {
        setFocusable(true);
        setFocusableInTouchMode(true);
    }


    /**
     * @return 返回左侧View
     */
    public View getLeftView() {
        return findViewById(R.id.topview_left_view);
    }

    /**
     * @return 返回右侧View
     */
    public View getRightView() {
        return findViewById(R.id.topview_right_view);
    }


    /**
     * 设置右边显示隐藏
     */
    public void setRightVisible(@Visibility int visible) {
        View view = findViewById(R.id.topview_right_view);
        if (null == view) {
            return;
        }
        view.setVisibility(visible);
    }

    /**
     * 设置右侧view不可点击
     *
     * @param enabled
     */
    public void setRightEnabled(boolean enabled) {
        View view = findViewById(R.id.topview_right_view);
        if (null == view) {
            return;
        }
        view.setEnabled(enabled);
    }

    /**
     * 设置左边显示隐藏
     */
    public void setLeftVisible(int visible) {
        View view = findViewById(R.id.topview_left_view);
        if (null == view) {
            return;
        }
        view.setVisibility(visible);
    }

    /**
     * 设置左边的点击事件
     */
    public void setLeftAction(OnClickListener listener) {
        mLeftAction = listener;
    }

    /**
     * 设置右边的点击事件
     */
    public void setRightAction(OnClickListener listener) {
        mRightAction = listener;
    }

}
