package com.shanlinjinrong.oa.ui.activity.contracts;


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
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.gson.Gson;
import com.hyphenate.easeui.db.Friends;
import com.hyphenate.easeui.db.FriendsInfoCacheSvc;
import com.shanlinjinrong.oa.common.Constants;
import com.hyphenate.easeui.model.UserInfoDetailsBean;
import com.hyphenate.easeui.model.UserInfoSelfDetailsBean;
import com.shanlinjinrong.oa.listener.PermissionListener;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.model.User;
import com.shanlinjinrong.oa.ui.activity.message.EaseChatMessageActivity;
import com.shanlinjinrong.oa.ui.activity.message.VoiceCallActivity;
import com.shanlinjinrong.oa.ui.base.BaseActivity;
import com.shanlinjinrong.oa.utils.GlideRoundTransformUtils;
import com.shanlinjinrong.oa.utils.Utils;
import com.shanlinjinrong.oa.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.shanlinjinrong.oa.manager.AppManager.mContext;

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
        setTranslucentStatus(this);
        ButterKnife.bind(this);
        boolean session = this.getIntent().getBooleanExtra("isSession", false);
        if (session) {
            initSessionInfo();
        } else {
            init();
        }
    }


    private void initSessionInfo() {
        String userCode = this.getIntent().getStringExtra("user_code");
        String userInfo = this.getIntent().getStringExtra("userInfo");
        String userInfo_self = this.getIntent().getStringExtra("userInfo_self");
        final UserInfoDetailsBean userInfoDetailsBean = new Gson().fromJson(userInfo, UserInfoDetailsBean.class);
        final UserInfoSelfDetailsBean userInfoSelfDetailsBean = new Gson().fromJson(userInfo_self, UserInfoSelfDetailsBean.class);
        try {
            if (userCode.equals("sl_" + userInfoDetailsBean.CODE)) {
                if (!TextUtils.isEmpty(userInfoDetailsBean.username)) {
                    tv_user_name.setText(userInfoDetailsBean.username);
                }

                if (!TextUtils.isEmpty(userInfoDetailsBean.portrait)) {
                    Glide.with(AppManager.mContext)
                            .load(userInfoDetailsBean.portrait)
                            .error(R.drawable.ease_default_avatar)
                            .transform(new CenterCrop(AppManager.mContext), new GlideRoundTransformUtils(AppManager.mContext, 5))
                            .placeholder(R.drawable.ease_default_avatar).into(ivImgUser);
                }

                if (!TextUtils.isEmpty(userInfoDetailsBean.department_name)) {
                    tv_department.setText(userInfoDetailsBean.department_name);
                }

                if (!TextUtils.isEmpty(userInfoDetailsBean.post_title)) {
                    tv_duties.setText(userInfoDetailsBean.post_title);
                }

                if (!TextUtils.isEmpty(userInfoDetailsBean.sex)) {
                    tv_sex.setText(userInfoDetailsBean.sex);
                }

                if (!TextUtils.isEmpty(userInfoDetailsBean.phone)) {
                    tv_phone_number.setText(userInfoDetailsBean.phone);
                } else {
                    tv_phone_number.setText("-");
                }

                if (!TextUtils.isEmpty(userInfoDetailsBean.email)) {
                    tv_mails.setText(userInfoDetailsBean.email);
                }
            } else if (userCode.equals("sl_" + userInfoSelfDetailsBean.CODE_self)) {
                if (!TextUtils.isEmpty(userInfoSelfDetailsBean.username_self)) {
                    tv_user_name.setText(userInfoSelfDetailsBean.username_self);
                }

                if (!TextUtils.isEmpty(userInfoSelfDetailsBean.portrait_self)) {
                    Glide.with(AppManager.mContext)
                            .load(userInfoSelfDetailsBean.portrait_self)
                            .error(R.drawable.ease_default_avatar)
                            .transform(new CenterCrop(AppManager.mContext), new GlideRoundTransformUtils(AppManager.mContext, 5))
                            .placeholder(R.drawable.ease_default_avatar).into(ivImgUser);
                }
                if (!TextUtils.isEmpty(userInfoSelfDetailsBean.department_name_self)) {
                    tv_department.setText(userInfoSelfDetailsBean.department_name_self);
                }

                if (!TextUtils.isEmpty(userInfoSelfDetailsBean.post_title_self)) {
                    tv_duties.setText(userInfoSelfDetailsBean.post_title_self);
                }

                if (!TextUtils.isEmpty(userInfoSelfDetailsBean.sex_self)) {
                    tv_sex.setText(userInfoSelfDetailsBean.sex_self);
                }

                if (!TextUtils.isEmpty(userInfoSelfDetailsBean.phone_self)) {
                    tv_phone_number.setText(userInfoSelfDetailsBean.phone_self);
                } else {
                    tv_phone_number.setText("-");
                }
                if (!TextUtils.isEmpty(userInfoSelfDetailsBean.email_self)) {
                    tv_mails.setText(userInfoSelfDetailsBean.email_self);
                }

            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            if (userCode.equals("sl_" + userInfoDetailsBean.CODE)) {
                //判断是否有权限打电话
                if (userCode.equals("sl_" + AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_CODE))) {
                    Toast.makeText(getApplication(), "不能给自己打电话", Toast.LENGTH_SHORT);
                    iv_phone.setImageResource(R.mipmap.ico_phone_disabled);
                } else {
                    if (AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_DEPARTMENT_NAME).equals(userInfoDetailsBean.department_name)) {
                        iv_phone.setImageResource(R.mipmap.ico_phone);
                        //可以打电话
                        rel_phone_call.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestRunTimePermission(new String[]{Manifest.permission.CALL_PHONE}, new PermissionListener() {
                                    @Override
                                    public void onGranted() {
                                        Intent intent = new Intent(Intent.ACTION_CALL,
                                                Uri.parse("tel:" + userInfoDetailsBean.phone));
                                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
                    } else if (TextUtils.isEmpty(userInfoDetailsBean.phone)) {
                        iv_phone.setImageResource(R.mipmap.ico_phone_disabled);
                    } else {
                        iv_phone.setImageResource(R.mipmap.ico_phone_disabled);
                    }
                }

                if (userCode.equals("sl_" + AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_CODE))) {
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
                            try {
                                startActivity(new Intent(mContext, EaseChatMessageActivity.class)
                                        .putExtra("usernike", userInfoDetailsBean.username)
                                        .putExtra("user_pic", userInfoDetailsBean.portrait)
                                        .putExtra("u_id", Constants.CID + "_" + userInfoDetailsBean.CODE)
                                        .putExtra("department_name", userInfoDetailsBean.department_name)
                                        .putExtra("post_name", userInfoDetailsBean.post_title)
                                        .putExtra("sex", userInfoDetailsBean.sex)
//                                      .putExtra("uid", constants.getUid())
                                        .putExtra("phone", userInfoDetailsBean.phone)
                                        .putExtra("email", userInfoDetailsBean.email));
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

                if (userCode.equals("sl_" + AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_CODE))) {
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
                                    .putExtra("username", "sl_" + userInfoDetailsBean.CODE)
                                    .putExtra("CODE", userInfoDetailsBean.CODE)
                                    .putExtra("phone", userInfoDetailsBean.phone)
                                    .putExtra("sex", userInfoDetailsBean.sex)
                                    .putExtra("post_name", userInfoDetailsBean.post_title)
                                    .putExtra("nike", userInfoDetailsBean.username)
                                    .putExtra("portrait", userInfoDetailsBean.portrait)
                                    .putExtra("email", userInfoDetailsBean.email)
                                    .putExtra("department_name", userInfoDetailsBean.department_name)
                                    .putExtra("isComingCall", false));
                        }
                    });
                }
            } else if (userCode.equals("sl_" + userInfoSelfDetailsBean.CODE_self)) {
                //判断是否有权限打电话
                if (userCode.equals("sl_" + AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_CODE))) {
                    Toast.makeText(getApplication(), "不能给自己打电话", Toast.LENGTH_SHORT);
                    iv_phone.setImageResource(R.mipmap.ico_phone_disabled);
                } else {
                    if (AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_DEPARTMENT_NAME).equals(userInfoSelfDetailsBean.department_name_self)) {
                        iv_phone.setImageResource(R.mipmap.ico_phone);
                        //可以打电话
                        rel_phone_call.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestRunTimePermission(new String[]{Manifest.permission.CALL_PHONE}, new PermissionListener() {
                                    @Override
                                    public void onGranted() {
                                        Intent intent = new Intent(Intent.ACTION_CALL,
                                                Uri.parse("tel:" + userInfoSelfDetailsBean.phone_self));
                                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
                    } else if (TextUtils.isEmpty(userInfoSelfDetailsBean.phone_self)) {
                        iv_phone.setImageResource(R.mipmap.ico_phone_disabled);
                    } else {
                        iv_phone.setImageResource(R.mipmap.ico_phone_disabled);
                    }
                }

                if (userCode.equals("sl_" + AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_CODE))) {
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
                            try {
                                startActivity(new Intent(mContext, EaseChatMessageActivity.class)
                                        .putExtra("usernike", userInfoSelfDetailsBean.username_self)
                                        .putExtra("user_pic", userInfoSelfDetailsBean.portrait_self)
                                        .putExtra("u_id", Constants.CID + "_" + userInfoSelfDetailsBean.CODE_self)
                                        .putExtra("department_name", userInfoSelfDetailsBean.department_name_self)
                                        .putExtra("post_name", userInfoSelfDetailsBean.post_title_self)
                                        .putExtra("sex", userInfoSelfDetailsBean.sex_self)
//                                      .putExtra("uid", constants.getUid())
                                        .putExtra("phone", userInfoSelfDetailsBean.phone_self)
                                        .putExtra("email", userInfoSelfDetailsBean.email_self));
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

                if (userCode.equals("sl_" + AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_CODE))) {
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
//                                    .putExtra("username",  "sl_"+userInfoSelfDetailsBean.CODE_self)
//                                    .putExtra("toUsername",  "sl_"+userInfoDetailsBean.CODE)
//                                    .putExtra("nike", userInfoSelfDetailsBean.username_self)
//                                    .putExtra("portrait", userInfoSelfDetailsBean.portrait_self)
//                                    .putExtra("isComingCall", false));
                                    .putExtra("username", "sl_" + userInfoSelfDetailsBean.CODE_self)
                                    .putExtra("CODE", userInfoSelfDetailsBean.CODE_self)
                                    .putExtra("phone", userInfoSelfDetailsBean.phone_self)
                                    .putExtra("sex", userInfoSelfDetailsBean.sex_self)
                                    .putExtra("post_name", userInfoSelfDetailsBean.post_title_self)
                                    .putExtra("nike", userInfoSelfDetailsBean.username_self)
                                    .putExtra("portrait", userInfoSelfDetailsBean.portrait_self)
                                    .putExtra("email", userInfoSelfDetailsBean.email_self)
                                    .putExtra("department_name", userInfoSelfDetailsBean.department_name_self)
                                    .putExtra("isComingCall", false));
                        }
                    });
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

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

        if (!TextUtils.isEmpty(user.getPortraits())) {
            Glide.with(AppManager.mContext)
                    .load(user.getPortraits())
                    .error(R.drawable.ease_default_avatar)
                    .transform(new CenterCrop(AppManager.mContext), new GlideRoundTransformUtils(AppManager.mContext, 5))
                    .placeholder(R.drawable.ease_default_avatar).into(ivImgUser);
        }

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
                            .putExtra("u_id", Constants.CID + "_" + user.getCode())
                            .putExtra("code", user.getCode())


                            .putExtra("department_name", user.getDepartmentName())
                            .putExtra("post_name", user.getPostName())
                            .putExtra("sex", user.getSex())
                            .putExtra("phone", user.getPhone())
                            .putExtra("email", user.getEmail()));

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
                            .putExtra("isComingCall", false)
                            .putExtra("CODE", user.getCode())
                            .putExtra("u_id", user.getUid())
                            .putExtra("department_name", user.getDepartmentName())
                            .putExtra("post_name", user.getPostName())
                            .putExtra("sex", user.getSex())
                            .putExtra("phone", user.getPhone())
                            .putExtra("email", user.getEmail()));

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
