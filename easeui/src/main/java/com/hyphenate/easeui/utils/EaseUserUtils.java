package com.hyphenate.easeui.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.EaseUI.EaseUserProfileProvider;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.db.FriendsInfoCacheSvc;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.UserInfoSelfDetailsBean;

public class EaseUserUtils {

    static EaseUserProfileProvider userProvider;

    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }

    private static String nickName;

    /**
     * get EaseUser according username
     *
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username) {
        if (userProvider != null)
            return userProvider.getUser(username);

        return null;
    }

    /**
     * set user avatar
     *
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView) {
        EaseUser user = getUserInfo(username);
        String portrait = FriendsInfoCacheSvc.getInstance(context).getPortrait(user.getUsername());
        if (user != null && user.getAvatar() != null) {
            try {
                int avatarResId = Integer.parseInt(user.getAvatar());
                Glide.with(context).load(portrait)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ease_default_avatar)
                        .into(imageView);
            } catch (Exception e) {
                //use default avatar
                Glide.with(context).load(portrait)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ease_default_avatar)
                        .into(imageView);
            }
        } else {
            Glide.with(context).load(portrait)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ease_default_avatar)
                    .into(imageView);
        }
    }

    /**
     * set user avatar
     */
    public static void setUserAvatarBean(Context context, String portrait, ImageView imageView) {
        if (!portrait.equals("")) {
            try {
                Glide.with(context).load(portrait)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ease_default_avatar)
                        .into(imageView);
            } catch (Exception e) {
                //use default avatar
                Glide.with(context).load(portrait)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ease_default_avatar)
                        .into(imageView);
            }
        } else {
            Glide.with(context).load(portrait)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ease_default_avatar)
                    .into(imageView);
        }
    }

    /**
     * set user avatar
     */
    public static void setUserAvatarBeanSelf(Context context, String portrait, ImageView imageView) {
        if (!portrait.equals("")) {
            try {
                Glide.with(context).load(portrait)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ease_default_avatar)
                        .into(imageView);

            } catch (Exception e) {
                Glide.with(context).load(portrait)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ease_default_avatar)
                        .into(imageView);
            }
        } else {
            Glide.with(context).load(portrait)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ease_default_avatar)
                    .into(imageView);
        }
    }

    /**
     * set user's nickname
     */
    public static void setUserNick(String username, TextView textView) {
        if (textView != null) {
            EaseUser user = getUserInfo(username);

            if (user != null && user.getNick() != null) {
                if (textView.getText().equals(""))
                    textView.setText(username);
            } else {
                if (textView.getText().equals(""))
                    textView.setText(username);
            }
        }
    }

    /**
     * set user's nickname
     */
    public static void setUserNickBean(String nickName, TextView textView) {
        if (textView != null) {
            if (!nickName.equals("")) {
                if (textView.getText().equals(""))
                    textView.setText(nickName);
            } else {
                if (textView.getText().equals(""))
                    textView.setText("");
            }
        }
    }

    /**
     * set user's nickname
     */
    public static void setUserNickSelfBean(UserInfoSelfDetailsBean bean, TextView textView) {
        if (textView != null) {
            if (bean != null) {
                if (textView.getText().equals(""))
                    textView.setText(nickName);
            } else {
                if (textView.getText().equals(""))
                    textView.setText("");
            }
        }
    }
}