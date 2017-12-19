package com.shanlinjinrong.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.shanlinjinrong.uilibrary.R;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 丁 on 2017/9/27.
 * 蒙版
 */
public class MaskDialog extends Dialog {

    Drawable mDrawable;
    private ImageView image;
    private long lastClickTime = 0;

    public MaskDialog(Context context, @DrawableRes int drawableId) {
        this(context, drawableId, R.style.TipDialog);
    }


    public MaskDialog(Context context, @DrawableRes int drawableId, int themeResId) {
        super(context, themeResId);
        mDrawable = ResourcesCompat.getDrawable(getContext().getResources(), drawableId, null);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDialogWidth();
        setContentView(R.layout.dialog_mask);
        image = (ImageView) findViewById(R.id.mask_image);
        if (mDrawable != null)
            image.setImageDrawable(mDrawable);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime < 1000) {
                    lastClickTime = currentTime;
                    return;
                }
                dismiss();
            }
        });

    }

    private void initDialogWidth() {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams wmLp = window.getAttributes();
            wmLp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            wmLp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            window.setAttributes(wmLp);
        }
    }


    @Override
    public void show() {
        super.show();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                dismiss();
            }
        }, 3000);
    }
}


