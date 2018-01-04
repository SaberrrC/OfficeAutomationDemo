package com.shanlinjinrong.oa.ui.activity.main.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.util.Pair;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.pgyersdk.update.PgyUpdateManager;

import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.main.bean.UserDetailsBean;
import com.shanlinjinrong.oa.ui.activity.main.contract.MainControllerContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;
import com.shanlinjinrong.oa.utils.LogUtils;
import com.shanlinjinrong.oa.utils.SharedPreferenceUtils;

import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by 丁 on 2017/8/19.
 */

public class MainControllerPresenter extends HttpPresenter<MainControllerContract.View> implements MainControllerContract.Presenter {

    @Inject
    public MainControllerPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }


    //存储权限判断
    @Override
    public void applyPermission(Activity context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (SharedPreferenceUtils.getShouldAskPermission(context, "firstshould") && !ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {//第一次已被拒绝
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("权限开启");
                builder.setMessage("更新功能无法正常使用，去权限列表开启该权限");
                builder.setPositiveButton("确定", (dialog, which) -> mView.startAppSetting()).setNegativeButton("取消", (dialog, which) -> {
                }).show();
            } else {//
                SharedPreferenceUtils.setShouldAskPermission(context, "firstshould", ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.WRITE_EXTERNAL_STORAGE));
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.SYSTEM_ALERT_WINDOW}, 100);
            }

        } else {
            //            PgyUpdateManager.setIsForced(true);
            PgyUpdateManager.register(context, "com.shanlinjinrong.oa.fileprovider");
        }
    }
    List<Pair<Long, EMConversation>> sortList = new ArrayList<>();
    List<EMConversation>             list     = new ArrayList<>();


    //TODO 卡顿
    public List<EMConversation> loadConversationList() {
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        sortList.clear();
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        list.clear();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }

        int tempCount = 0;
        for (int i = 0; i < list.size(); i++) {
            int size = list.get(i).getUnreadMsgCount();
            tempCount = tempCount + size;
        }
//        mView.bindBadgeView(tempCount);
        return list;
    }
}
