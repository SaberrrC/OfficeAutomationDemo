
package com.shanlinjinrong.views.swipeRecycleview.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanlinjinrong.uilibrary.R;
import com.shanlinjinrong.views.swipeRecycleview.SwipeMenuRecyclerView;

public class DefaultEmptyView extends LinearLayout implements View.OnClickListener {

    private ImageView mLoadEmptyImage;
    private TextView mTvMessage;
    private Context mContext;
    private FrameLayout mCustomContent;

    private SwipeMenuRecyclerView.LoadEmptyListener mLoadEmptyListener;

    public DefaultEmptyView(Context context) {
        this(context, null);
        mContext = context;
    }

    public DefaultEmptyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        setGravity(Gravity.CENTER);
        setVisibility(GONE);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        int minHeight = (int) (displayMetrics.density * 60 + 0.5);
        setMinimumHeight(minHeight);

        inflate(getContext(), R.layout.recycler_swipe_view_load_empty, this);
        mLoadEmptyImage = (ImageView) findViewById(R.id.iv_load_empty_image);
        mTvMessage = (TextView) findViewById(R.id.tv_load_more_message);
        mCustomContent = (FrameLayout) findViewById(R.id.custom_content);
        setOnClickListener(this);
    }

    public DefaultEmptyView setLoadEmptyDrawable(Drawable drawable) {
        mLoadEmptyImage.setImageDrawable(drawable);
        mLoadEmptyImage.setVisibility(INVISIBLE);
        return this;
    }

    public void setLoadEmptyDrawable(@DrawableRes int drawableId) {
        Drawable drawable = ResourcesCompat.getDrawable(mContext.getResources(), drawableId, null);
        setLoadEmptyDrawable(drawable);
    }

    public DefaultEmptyView setMessage(String message) {
        mTvMessage.setText(message.trim());
        return this;
    }

    public void setMessage(@StringRes int messageId) {
        String message = mContext.getString(messageId);
        setMessage(message.trim());
    }

    public DefaultEmptyView setLoadEmptyListener(SwipeMenuRecyclerView.LoadEmptyListener mLoadEmptyListener) {
        this.mLoadEmptyListener = mLoadEmptyListener;
        return this;
    }

    @Override
    public void onClick(View v) {
        if (mLoadEmptyListener != null) mLoadEmptyListener.onEmptyClicked();
    }

    public DefaultEmptyView setCustomEmptyView(View view) {
        mCustomContent.addView(view);
        mCustomContent.setVisibility(VISIBLE);
        mLoadEmptyImage.setVisibility(GONE);
        mTvMessage.setVisibility(GONE);
        return this;
    }


}
