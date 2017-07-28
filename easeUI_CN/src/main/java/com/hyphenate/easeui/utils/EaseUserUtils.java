package com.hyphenate.easeui.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.controller.EaseUI.EaseUserProfileProvider;
import com.hyphenate.easeui.db.FriendsInfoCacheSvc;
import com.hyphenate.easeui.domain.EaseUser;

public class EaseUserUtils {

    static EaseUserProfileProvider userProvider;

    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }

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
//        EaseUser user = getUserInfo(username);
        //setUserAvatar->userApiModel.getHeadImg()->http://null
//        UserApiModel userApiModel = UserInfoCacheSvc.getByChatUserName(context, username);

//        if (userApiModel != null && !StringUtils.isBlank(userApiModel.getHeadImg())) {
//            try {
//                if (userApiModel.getHeadImg().equals("http://null")) {
//                    Glide.with(context).load(R.drawable.ease_default_avatar).into(imageView);
//                }else{
//                Glide.with(context).load(userApiModel.getHeadImg()).into(imageView);
//
//                }
//            } catch (Exception e) {
//                //use default avatar
//                Glide.with(context).load(userApiModel.getHeadImg()).diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .placeholder(R.drawable.ease_default_avatar).into(imageView);
//            }
//        } else {
//            Glide.with(context).load(R.drawable.ease_default_avatar).into(imageView);
//        }
//
//
        String portrait = FriendsInfoCacheSvc.getInstance(context).getPortrait(username);
        Glide.with(context).load(portrait).diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ease_default_avatar).into(imageView);

    }

    /**
     * set user's nickname
     */
    public static void setUserNick(Context context, String username, TextView textView) {
        if (textView != null) {
//            EaseUser user = getUserInfo(username);

//            UserApiModel userApiModel = UserInfoCacheSvc.getByChatUserName(context, username);
//
//            if (userApiModel != null && userApiModel.getUsername() != null) {
//                textView.setText(userApiModel.getUsername());
//            } else {
//                textView.setText(username);
//            }

            String nickName = FriendsInfoCacheSvc.getInstance(context).getNickName(username);
            if (null!=nickName ) {
                textView.setText(nickName);
            } else {
                textView.setText(username);
            }
        }
    }

    /**
     * get user's nickname
     */
    public static String getUserNick(String username) {
        EaseUser user = getUserInfo(username);
        String nikeName = "";
        if (user != null && user.getNick() != null) {
            nikeName = user.getNick();
        } else {
            nikeName = user.getNick();
        }
        return nikeName;
    }

}
