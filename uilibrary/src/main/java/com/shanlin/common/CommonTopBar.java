package com.shanlin.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanlin.uilibrary.R;

/**
 * Created by 丁 on 2017/8/18.
 * 通用的顶部标题栏
 * 1. 左侧文字或图片显示按钮
 * 2. 中部标题
 * 3. 右侧图片或者文字按钮
 */
public class CommonTopBar extends LinearLayout {
    private TextView mLeftTextBtn; // 左侧文字按钮
    private TextView mRightTextBtn; // 右侧文字按钮
    private TextView mTitle; // 标题
    private ImageButton mLeftImageBtn; // 左侧图片按钮
    private ImageButton mRightImageBtn; // 右侧图片按钮


    private int mTitleSize; //标题文字大小
    private int mLeftTextSize; // 左侧文字大小
    private int mRightTextSize; // 右侧文字大小
    private String mLeftText; // 左侧文字Btn
    private String mRightText; // 右侧文字Btn
    private Drawable mLeftIcon; // 左侧图标Btn
    private Drawable mRightIcon; // 右侧图标Btn
    private int mTitleBarColor; // 标题栏背景颜色
    private int mTitleBarHeight; // 标题栏的高度

    public CommonTopBar(Context context) {
        super(context);
        initView(context);
    }

    public CommonTopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        initView(context);
    }


    public CommonTopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.common_title_bar, this, false);
        mLeftTextBtn = (TextView) rootView.findViewById(R.id.left_text_btn);
        mRightTextBtn = (TextView) rootView.findViewById(R.id.right_text_btn);
        mLeftImageBtn = (ImageButton) rootView.findViewById(R.id.left_image_btn);
        mRightImageBtn = (ImageButton) rootView.findViewById(R.id.right_image_btn);
        mTitle = (TextView) rootView.findViewById(R.id.title);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CommonTopBar);
        mTitleBarColor = array.getColor(R.styleable.CommonTopBar_bar_background, mTitleBarColor);
        array.recycle();
    }


    public int getTitleSize() {
        return mTitleSize;
    }

    public CommonTopBar setTitleSize(int mTitleSize) {
        this.mTitleSize = mTitleSize;
        return this;
    }

    public int getLeftTextSize() {
        return mLeftTextSize;
    }

    public CommonTopBar setLeftTextSize(int mLeftTextSize) {
        this.mLeftTextSize = mLeftTextSize;
        return this;
    }

    public int getRightTextSize() {
        return mRightTextSize;
    }

    public CommonTopBar setRightTextSize(int mRightTextSize) {
        this.mRightTextSize = mRightTextSize;
        return this;
    }

    public String getLeftText() {
        return mLeftText;
    }

    public CommonTopBar setLeftText(String mLeftText) {
        this.mLeftText = mLeftText;
        return this;
    }

    public Drawable getLeftIcon() {
        return mLeftIcon;
    }

    public CommonTopBar setLeftIcon(Drawable mLeftIcon) {
        this.mLeftIcon = mLeftIcon;
        return this;
    }

    public String getRightText() {
        return mRightText;
    }

    public CommonTopBar setRightText(String mRightText) {
        this.mRightText = mRightText;
        return this;
    }

    public Drawable getRightIcon() {
        return mRightIcon;
    }

    public CommonTopBar setRightIcon(Drawable mRightIcon) {
        this.mRightIcon = mRightIcon;
        return this;
    }

    public CommonTopBar setTitleBarColor(int mTitleBarColor) {
        this.mTitleBarColor = mTitleBarColor;
        return this;
    }

    public CommonTopBar setTitleBarHeight(int mTitleBarHeight) {
        this.mTitleBarHeight = mTitleBarHeight;
        return this;
    }
}
