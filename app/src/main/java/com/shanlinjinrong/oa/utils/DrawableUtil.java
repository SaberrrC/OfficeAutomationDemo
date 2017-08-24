package com.shanlinjinrong.oa.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.utils
 * Author:Created by Tsui on Date:2017/2/14 14:33
 * Description:Drawable工具类
 */
public class DrawableUtil {

    /**
     * http://www.jianshu.com/p/8c479ed24ca8
     *
     * @param Rid 要着色的Drawable的id
     * @param colors
     * @return Drawable
     */
    public static Drawable tintDrawable(Context context, int Rid, int colors) {

        Drawable drawable = context.getResources().getDrawable(Rid);
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, ColorStateList.valueOf(colors));
        return wrappedDrawable;
    }
}
