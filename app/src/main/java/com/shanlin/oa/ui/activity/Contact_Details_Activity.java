package com.shanlin.oa.ui.activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyphenate.easeui.db.Friends;
import com.hyphenate.easeui.db.FriendsInfoCacheSvc;
import com.shanlin.oa.R;
import com.shanlin.oa.common.Api;
import com.shanlin.oa.common.Constants;
import com.shanlin.oa.huanxin.EaseChatMessageActivity;
import com.shanlin.oa.huanxin.VoiceCallActivity;
import com.shanlin.oa.manager.AppConfig;
import com.shanlin.oa.manager.AppManager;
import com.shanlin.oa.model.User;
import com.shanlin.oa.ui.PermissionListener;
import com.shanlin.oa.ui.base.BaseActivity;
import com.shanlin.oa.utils.LogUtils;
import com.shanlin.oa.utils.Utils;

import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.shanlin.oa.manager.AppManager.mContext;

//import com.hyphenate.chatuidemo.db.Friends;
//import com.hyphenate.chatuidemo.db.FriendsInfoCacheSvc;

/**
 * 搜索联系人详情页
 */
public class Contact_Details_Activity extends BaseActivity {
    @Bind(R.id.btn_back)
    ImageView btn_back;
    @Bind(R.id.tv_user_name)
    TextView tv_user_name;
    @Bind(R.id.rel_send_message)
    RelativeLayout rel_send_message;
    @Bind(R.id.rel_voice_call)
    RelativeLayout rel_voice_call;
    @Bind(R.id.rel_phone_call)
    RelativeLayout rel_phone_call;
    @Bind(R.id.tv_department)
    TextView tv_department;
    @Bind(R.id.tv_duties)
    TextView tv_duties;
    @Bind(R.id.tv_sex)
    TextView tv_sex;
    @Bind(R.id.tv_phone_number)
    TextView tv_phone_number;
    @Bind(R.id.tv_mails)
    TextView tv_mails;
    @Bind(R.id.iv_phone)
    ImageView iv_phone;

    @Bind(R.id.send_message)
    ImageView send_message;
    @Bind(R.id.send_voice)
    ImageView send_voice;
    @Bind(R.id.iv_img_user)
    ImageView ivImgUser;

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
        ButterKnife.bind(this);
        boolean session = this.getIntent().getBooleanExtra("isSession", false);
        if (session) {
            initSessionInfo();
        } else {
            init();
        }
    }

    String nickName;
    String portrait;

    private void initSessionInfo() {
        final String userName = this.getIntent().getStringExtra("userName");

        nickName = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getNickName(userName);
        portrait = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getPortrait(userName);
        if (!TextUtils.isEmpty(nickName)) {
            tv_user_name.setText(nickName);
        } else {
            tv_user_name.setText("");
        }
        if (!TextUtils.isEmpty(portrait)) {
            Glide.with(AppManager.mContext).load(portrait)
                    .placeholder(R.drawable.ease_default_avatar).into(ivImgUser);
        }

        //判断是否有权限打电话
        if (nickName.equals(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_USERNAME))) {
            Toast.makeText(getApplication(), "不能给自己打电话", Toast.LENGTH_SHORT);
            iv_phone.setImageResource(R.mipmap.ico_phone_disabled);
        } else {
            //TODO 获取不到手机号
            iv_phone.setImageResource(R.mipmap.ico_phone_disabled);
        }

        if (nickName.equals(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_USERNAME))) {
            Toast.makeText(getApplication(), "不能给自己发消息", Toast.LENGTH_SHORT);
            send_message.setImageResource(R.mipmap.ico_message_disabled);
        } else {
            rel_send_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean availabl = Utils.isNetworkAvailabl(mContext);
                    if (!availabl) {
                        showToast("网络不稳定，请重试");
                        return;
                    }

                    startActivity(new Intent(mContext, EaseChatMessageActivity.class)
                            .putExtra("usernike", nickName)
                            .putExtra("user_pic", portrait)
                            .putExtra("u_id", userName));
                }
            });
        }
        if (nickName.equals(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_USERNAME))) {
            Toast.makeText(getApplication(), "不能给自己打电话", Toast.LENGTH_SHORT);
            send_voice.setImageResource(R.mipmap.ico_vedio_disabled);
        } else {
            rel_voice_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean availabl = Utils.isNetworkAvailabl(mContext);
                    if (!availabl) {
                        showToast("网络不稳定，请重试");
                        return;
                    }

                    startActivity(new Intent(mContext, VoiceCallActivity.class)
                            .putExtra("username", userName)
                            .putExtra("nike", nickName)
                            .putExtra("portrait", portrait)
                            .putExtra("isComingCall", false));
                }
            });
        }

        //TODO个人详情
        HttpParams params = new HttpParams();

        final String oid = AppConfig.getAppConfig(AppManager.mContext)
                .get(AppConfig.PREF_KEY_OID);
        String isLeader = AppConfig.getAppConfig(AppManager.mContext)
                .get(AppConfig.PREF_KEY_ISLEADER);
        String code = AppConfig.getAppConfig(AppManager.mContext)
                .get(AppConfig.PREF_KEY_CODE);
        final String uid = AppConfig.getAppConfig(AppManager.mContext)
                .get(AppConfig.PREF_KEY_USER_UID);
        String token = AppConfig.getAppConfig(AppManager.mContext)
                .get(AppConfig.PREF_KEY_TOKEN);

        //TODO 聊天信息
//        params.put("uid", uid);
//        params.put("token", token);
//        params.put("oid",oid);
//        params.put("isleader",isLeader);
        params.put("code", "010110027");
//        params.put("code",code);
        initKjHttp().get(Api.COMMUNICATION_USERINFO, params, new HttpCallBack() {
            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(Map<String, String> headers, byte[] t) {
                super.onSuccess(headers, t);
                System.out.println(t);
                LogUtils.e("聊天个人信息-》" + t);
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                LogUtils.e("聊天个人信息失败-》" + errorNo + strMsg);
            }
        });
    }

    private void addOrUpdateFriendInfo(User user) {
        Friends friend = new Friends();
        friend.setUser_id(Constants.CID + "_" + user.getCode());
        friend.setNickname(user.getUsername());
        friend.setPortrait(user.getPortraits());
        FriendsInfoCacheSvc.getInstance(mContext).addOrUpdateFriends(friend);
    }

    public void init() {
        user = (User) this.getIntent().getSerializableExtra("user");
        tv_user_name.setText(user.getUsername());
        tv_department.setText(user.getDepartmentName());
        tv_duties.setText(user.getPostName());
        tv_sex.setText(user.getSex());

        if (user.getIsshow().equals("1")) {
            tv_phone_number.setText(user.getPhone());
        } else if (user.getPhone() == null || user.getPhone().equals("")) {
            tv_phone_number.setText("-");
        }
//        if (user.getPhone() == null||user.getPhone().equals("")) {
//            tv_phone_number.setText("-");
//        } else {
//            tv_phone_number.setText(user.getPhone());
//        }
//
        if (user.getEmail() == null || user.getEmail().equals("")) {
            tv_mails.setText("-");
        } else {
            tv_mails.setText(user.getEmail());
        }
        //判断是否有权限打电话
        if (user.getUsername().equals(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_USERNAME))) {
            Toast.makeText(getApplication(), "不能给自己打电话", Toast.LENGTH_SHORT);
            iv_phone.setImageResource(R.mipmap.ico_phone_disabled);

        } else {
            if (user.getIsshow().equals("1")) {
                iv_phone.setImageResource(R.mipmap.ico_phone);
                //可以打电话
                rel_phone_call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestRunTimePermission(new String[]{Manifest.permission.CALL_PHONE}, new PermissionListener() {
                            @Override
                            public void onGranted() {
                                Intent intent = new Intent(Intent.ACTION_CALL,
                                        Uri.parse("tel:" + user.getPhone()));

                                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                startActivity(intent);
                            }

                            @Override
                            public void onDenied() {
                                showToast("拨打电话权限被拒绝，请手动设置");
                            }
                        });
                    }
                });
            } else if (user.getPhone().equals("")) {
                iv_phone.setImageResource(R.mipmap.ico_phone_disabled);
            } else {
                iv_phone.setImageResource(R.mipmap.ico_phone_disabled);
            }

        }

        if (user.getUsername().equals(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_USERNAME))) {
            Toast.makeText(getApplication(), "不能给自己发消息", Toast.LENGTH_SHORT);
            send_message.setImageResource(R.mipmap.ico_message_disabled);

        } else {
            rel_send_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean availabl = Utils.isNetworkAvailabl(mContext);
                    if (!availabl) {
                        showToast("网络不稳定，请重试");
                        return;
                    }

                    addOrUpdateFriendInfo(user);


                    startActivity(new Intent(mContext, EaseChatMessageActivity.class)
                            .putExtra("usernike", user.getUsername())
                            .putExtra("user_pic", user.getPortraits())
                            .putExtra("u_id", Constants.CID + "_" + user.getCode()));

                }
            });
        }
        if (user.getUsername().equals(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_USERNAME))) {
            Toast.makeText(getApplication(), "不能给自己打电话", Toast.LENGTH_SHORT);
            send_voice.setImageResource(R.mipmap.ico_vedio_disabled);

        } else {
            rel_voice_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean availabl = Utils.isNetworkAvailabl(mContext);
                    if (!availabl) {
                        showToast("网络不稳定，请重试");
                        return;
                    }

                    addOrUpdateFriendInfo(user);

                    startActivity(new Intent(mContext, VoiceCallActivity.class)
                            .putExtra("username", Constants.CID + "_" + user.getCode())
                            .putExtra("nike", user.getUsername())
                            .putExtra("portrait", user.getPortraits())
                            .putExtra("isComingCall", false));

                }
            });
        }
    }

    @OnClick(R.id.btn_back)
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }

    }
}
