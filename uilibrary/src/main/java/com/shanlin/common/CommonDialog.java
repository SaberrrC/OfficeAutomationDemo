package com.shanlin.common;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.shanlin.uilibrary.R;
import com.shanlin.utils.DeviceUtil;

/**
 * Created by 丁 on 2017/8/23.
 * 通用对话框
 */
public class CommonDialog extends Dialog {
    private Context mContext;
    private boolean mIsConfirmDialog = false;//是否是确定框
    private String mLeftButtonText = "";//左边按钮文案,默认取消按钮
    private String mRightButtonText = "";//右边按钮文案，默认确定
    private String mContent = "";//正文内容
    private OnBtnClickListener mBtnClickListener;//按钮点击事件


    public CommonDialog(Context context, String content, String right_bt_text, OnBtnClickListener clickListener,
                        boolean is_back_cancle) {
        super(context, R.style.dialog);
        //初始化参数
//        initParams(context, true, content, "", "", right_bt_text, clickListener, null, is_back_cancle);
        //去除弹框原有title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置布局
        setContentView(R.layout.dialog_custom);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        //设置点击弹框外部不消失
        setCanceledOnTouchOutside(false);
        //初始化控件
        initView(context);

    }


//    public CommonDialog(Context context, String content, String left_bt_text, String middle_bt_text, String right_bt_text, DialogOnClickListenterAble click_listener,
//                        boolean is_back_cancle) {
//        super(context, R.style.dialog);
//        //初始化参数
//        initParams(context, false, content, left_bt_text, middle_bt_text, right_bt_text, click_listener, null, is_back_cancle);
//        //去除弹框原有title
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        //设置布局
//        setContentView(R.layout.dialog_custom);
//        //设置点击弹框外部不消失
//        setCanceledOnTouchOutside(false);
//        //初始化控件
//        initView();
//
//    }
//
//
//    public CommonDialog(Context context, boolean is_confirm_dialog, String content, String left_bt_text, String middle_bt_text, String right_bt_text, DialogOnClickListenterAble click_listener,
//                        boolean is_back_cancle) {
//        super(context, R.style.dialog);
//        //初始化参数
//        initParams(context, is_confirm_dialog, content, left_bt_text, middle_bt_text, right_bt_text, click_listener, null, is_back_cancle);
//        //去除弹框原有title
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        //设置布局
//        setContentView(R.layout.dialog_custom);
//        //设置点击弹框外部不消失
//        setCanceledOnTouchOutside(false);
//        //初始化控件
//        initView();
//
//    }
//
//    /
//
//    public CommonDialog(Context context, boolean is_confirm_dialog, String content, String left_bt_text, String middle_bt_text, String right_bt_text, DialogOnClickListenterAble click_listener,
//                        DialogOnKeyListenterAble key_listener, boolean is_back_cancle) {
//        super(context, R.style.dialog);
//        //初始化参数
//        initParams(context, is_confirm_dialog, content, left_bt_text, middle_bt_text, right_bt_text, click_listener, key_listener, is_back_cancle);
//        //去除弹框原有title
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        //设置布局
//        setContentView(R.layout.dialog_custom);
//        //设置点击弹框外部不消失
//        setCanceledOnTouchOutside(false);
//        //初始化控件
//        initView();
//
//    }
//
//    /**
//     * 初始化参数
//     */
//    public void initParams(Context context, boolean is_confirm_dialog, String content, String left_bt_text, String middle_bt_text, String right_bt_text, DialogOnClickListenterAble click_listener,
//                           DialogOnKeyListenterAble key_listener, boolean is_back_cancel) {
//        mContext = context;
//        mIsConfirmDialog = is_confirm_dialog;
//        mLeftButtonText = left_bt_text;
//        mRightButtonText = right_bt_text;
//        mMiddleButtonText = middle_bt_text;
//        mContent = content;
//        mClickListenerInter = click_listener;
//        mKeyListener = key_listener;
//        mIsBackCancel = is_back_cancel;
//    }

    /**
     * 初始化view
     */
    public void initView(Context context) {
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.width = DeviceUtil.dip2px(context, 280); // 弹框宽度
        dialogWindow.setAttributes(p);

        TextView dialogContentTv = (TextView) findViewById(R.id.dialog_content_tv);//弹框正文
        dialogContentTv.setMovementMethod(ScrollingMovementMethod.getInstance());//设置textview可滑动
        TextView dialogLeftTv = (TextView) findViewById(R.id.dialog_left_tv);//左边按钮
        TextView dialogRightTv = (TextView) findViewById(R.id.dialog_right_tv);//右边按钮

        if (null == mRightButtonText || mRightButtonText.length() < 1) {//右边按钮文字未传入，默认为'确定'
            mRightButtonText = mContext.getString(R.string.common_text_yes);
        }

        dialogContentTv.setText(mContent);

        dialogRightTv.setText(mRightButtonText);
        //设置右边按钮点击事件
        dialogRightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // 如果不会关闭启动对话框的界面那么正常执行
                dismiss();
                if (null != mBtnClickListener) {
                    mBtnClickListener.onClick(DialogInterface.BUTTON_POSITIVE);
                }
            }
        });


        //取消按钮属性设置
        dialogLeftTv.setText(mLeftButtonText);
        dialogLeftTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (null != mBtnClickListener) {
                    mBtnClickListener.onClick(DialogInterface.BUTTON_NEGATIVE);
                }
            }
        });


    }




    /**
     * 点击事件
     */
    public interface OnBtnClickListener {
        void onClick(int key_value);
    }

}


