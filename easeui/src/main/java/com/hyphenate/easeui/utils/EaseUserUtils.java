package com.hyphenate.easeui.utils;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.EaseUI.EaseUserProfileProvider;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.db.FriendsInfoCacheSvc;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.UserInfoDetailsBean;
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
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ease_default_avatar)
                        .into(imageView);
            } catch (Exception e) {
                //use default avatar
                Glide.with(context).load(portrait)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ease_default_avatar)
                        .into(imageView);
            }
        } else {
            Glide.with(context).load(portrait)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ease_default_avatar)
                    .into(imageView);
        }
    }

    /**
     * set user avatar
     */
    public static void setUserAvatarBean(Context context, UserInfoDetailsBean bean, ImageView imageView) {
        if (bean != null) {
            try {
                Glide.with(context).load(bean.portrait)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ease_default_avatar)
                        .into(imageView);
            } catch (Exception e) {
                //use default avatar
                Glide.with(context).load(bean.portrait)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ease_default_avatar)
                        .into(imageView);
            }
        } else {
            Glide.with(context).load(bean.portrait)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ease_default_avatar)
                    .into(imageView);
        }
        nickName = bean.username;
    }

    /**
     * set user avatar
     */
    public static void setUserAvatarBeanSelf(Context context, UserInfoSelfDetailsBean bean, ImageView imageView) {
        if (bean != null) {
            try {
                String portrait = bean.portrait_self.replace("_self", "");
                Glide.with(context).load(portrait)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ease_default_avatar)
                        .into(imageView);
                if (bean.getUsername_self().equals("sl_sl_admin")) {
                    imageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.meeting_invite_icon, null));
                }
            } catch (Exception e) {
                //use default avatar
                String portrait = bean.portrait_self.replace("_self", "");
                Glide.with(context).load(portrait)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ease_default_avatar)
                        .into(imageView);
            }
        } else {
            String portrait = bean.portrait_self.replace("_self", "");
            Glide.with(context).load(portrait)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ease_default_avatar)
                    .into(imageView);
        }
        nickName = bean.username_self;
    }

    /**
     * set user's nickname
     */
    public static void setUserNick(String username, TextView textView) {
        if (textView != null) {
            EaseUser user = getUserInfo(username);

            if (user != null && user.getNick() != null) {
                if (textView.getText().equals(""))
                    textView.setText(nickName);
            } else {
                if (textView.getText().equals(""))
                    textView.setText(nickName);
            }
        }
    }

    /**
     * set user's nickname
     */
    public static void setUserNickBean(UserInfoDetailsBean bean, TextView textView) {
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