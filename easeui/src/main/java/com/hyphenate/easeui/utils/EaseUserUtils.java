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

public class EaseUserUtils {
    
    static EaseUserProfileProvider userProvider;
    
    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }
    
    /**
     * get EaseUser according username
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username){
        if(userProvider != null)
            return userProvider.getUser(username);
        return null;
    }
    
    /**
     * set user avatar
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView) {
        String portrait = FriendsInfoCacheSvc.getInstance(context).getPortrait(username);
        Glide.with(context).load(portrait).diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ease_default_avatar).into(imageView);

    }
    
    /**
     * set user's nickname
     */
    public static void setUserNick(Context context, String username, TextView textView) {
        if (textView != null) {
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
