package com.shanlinjinrong.views.tips;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanlinjinrong.uilibrary.R;
import com.shanlinjinrong.utils.DeviceUtil;
import com.shanlinjinrong.views.loadingview.LoadingView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 丁 on 2017/9/13.
 * <p>
 * 提供一个浮层展示在屏幕中间, 一般使用 {@link TipDialog.Builder} 或 {@link TipDialog.CustomBuilder} 生成。
 * <ul>
 * <li>{@link TipDialog.Builder} 提供了一个图标和一行文字的样式, 其中图标有几种类型可选, 见 {@link TipDialog.Builder.IconType}</li>
 * <li>{@link TipDialog.CustomBuilder} 支持传入自定义的 layoutResId, 达到自定义 TipDialog 的效果。</li>
 * </ul>
 */

public class TipDialog extends Dialog {

    public TipDialog(Context context) {
        this(context, R.style.TipDialog);
    }

    public TipDialog(Context context, int themeResId) {
        super(context, themeResId);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDialogWidth();
    }

    private void initDialogWidth() {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams wmLp = window.getAttributes();
            wmLp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            window.setAttributes(wmLp);
            window.setWindowAnimations(R.style.tipAnimator);
        }
    }

    /**
     * 生成默认的 {@link TipDialog}
     * <p>
     * 提供了一个图标和一行文字的样式, 其中图标有几种类型可选。见 {@link IconType}
     * </p>
     *
     * @see CustomBuilder
     */
    public static class Builder {
        /**
         * 不显示任何icon
         */
        public static final int ICON_TYPE_NOTHING = 0;
        /**
         * 显示 Loading 图标
         */
        public static final int ICON_TYPE_LOADING = 1;
        /**
         * 显示成功图标
         */
        public static final int ICON_TYPE_SUCCESS = 2;
        /**
         * 显示失败图标
         */
        public static final int ICON_TYPE_FAIL = 3;
        /**
         * 显示信息图标
         */
        public static final int ICON_TYPE_INFO = 4;

        private int mDuration = 2000;

        @IntDef({ICON_TYPE_NOTHING, ICON_TYPE_LOADING, ICON_TYPE_SUCCESS, ICON_TYPE_FAIL, ICON_TYPE_INFO})
        @Retention(RetentionPolicy.SOURCE)
        public @interface IconType {
        }


        @IconType
        private int mCurrentIconType = ICON_TYPE_NOTHING;

        private Context mContext;

        private CharSequence mTipMessage;

        public Builder(Context context) {
            mContext = context;
        }

        /**
         * 设置 icon 显示的内容
         *
         * @see IconType
         */
        public Builder setIconType(@IconType int iconType) {
            mCurrentIconType = iconType;
            return this;
        }

        /**
         * 设置显示的文案
         */
        public Builder setTipMessage(CharSequence tipMessage) {
            mTipMessage = tipMessage;
            return this;
        }

        /**
         * 设置显示的时间
         */
        public Builder setDuration(int mDuration) {
            this.mDuration = mDuration;
            return this;
        }

        /**
         * 创建 Dialog, 但没有弹出来, 如果要弹出来, 请调用返回值的 {@link Dialog#show()} 方法
         *
         * @return 创建的 Dialog
         */
        private TipDialog create() {
            TipDialog dialog = new TipDialog(mContext);
            dialog.setContentView(R.layout.tip_dialog_layout);
            ViewGroup contentView = (ViewGroup) dialog.findViewById(R.id.contentView);

            switch (mCurrentIconType) {
                case ICON_TYPE_LOADING:
                    LoadingView loadingView = new LoadingView(mContext);
                    loadingView.setColor(Color.WHITE);
                    loadingView.setSize(DeviceUtil.dip2px(mContext, 32));
                    LinearLayout.LayoutParams loadingViewLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    loadingView.setLayoutParams(loadingViewLP);
                    contentView.addView(loadingView);
                    break;
                case ICON_TYPE_SUCCESS:
                case ICON_TYPE_FAIL:
                case ICON_TYPE_INFO:
                    ImageView imageView = new ImageView(mContext);
                    LinearLayout.LayoutParams imageViewLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    imageView.setLayoutParams(imageViewLP);

                    if (mCurrentIconType == ICON_TYPE_SUCCESS) {
                        imageView.setImageDrawable(getDrawable(R.drawable.tip_icon_notify_done));
                    } else if (mCurrentIconType == ICON_TYPE_FAIL) {
                        imageView.setImageDrawable(getDrawable(R.drawable.tip_icon_notify_error));
                    } else {
                        imageView.setImageDrawable(getDrawable(R.drawable.tip_icon_notify_info));
                    }

                    contentView.addView(imageView);
                    break;
                case ICON_TYPE_NOTHING:
                    break;
            }

            if (mTipMessage != null && mTipMessage.length() > 0) {
                TextView tipView = new TextView(mContext);
                LinearLayout.LayoutParams tipViewLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                if (mCurrentIconType != ICON_TYPE_NOTHING) {
                    tipViewLP.topMargin = DeviceUtil.dip2px(mContext, 12);
                }
                tipView.setLayoutParams(tipViewLP);

                tipView.setEllipsize(TextUtils.TruncateAt.END);
                tipView.setGravity(Gravity.CENTER);
                tipView.setMaxLines(2);
                tipView.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                tipView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tipView.setText(mTipMessage);

                contentView.addView(tipView);
            }
            return dialog;
        }

        private Drawable getDrawable(@DrawableRes int drawableId) {
            return ResourcesCompat.getDrawable(mContext.getResources(), drawableId, null);
        }

        public TipDialog show() {
            final TipDialog dialog = create();
            dialog.show();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            }, mDuration);
            return dialog;
        }


    }


    /**
     * 传入自定义的布局并使用这个布局生成 TipDialog
     */
    public static class CustomBuilder {
        private Context mContext;
        private int mContentLayoutId;
        private long mDuration = 2000;

        public CustomBuilder(Context context) {
            mContext = context;
        }

        public CustomBuilder setContentView(@LayoutRes int layoutId) {
            mContentLayoutId = layoutId;
            return this;
        }

        /**
         * 创建 Dialog, 但没有弹出来, 如果要弹出来, 请调用返回值的 {@link Dialog#show()} 方法
         *
         * @return 创建的 Dialog
         */
        private TipDialog create() {
            TipDialog dialog = new TipDialog(mContext);
            dialog.setContentView(R.layout.tip_dialog_layout);
            ViewGroup contentWrap = (ViewGroup) dialog.findViewById(R.id.contentView);
            View customView = LayoutInflater.from(mContext).inflate(mContentLayoutId, contentWrap, true);
            return dialog;
        }

        public TipDialog show() {
            final TipDialog dialog = create();
            dialog.show();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            }, mDuration);
            return dialog;
        }

        public CustomBuilder setDuration(long mDuration) {
            this.mDuration = mDuration;
            return this;
        }
    }
}
