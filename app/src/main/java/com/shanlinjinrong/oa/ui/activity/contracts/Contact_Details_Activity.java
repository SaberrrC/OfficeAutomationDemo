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
import com.hyphenate.easeui.db.Friends;
import com.hyphenate.easeui.db.FriendsInfoCacheSvc;
import com.jakewharton.rxbinding2.view.RxView;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.listener.PermissionListener;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.model.User;
import com.shanlinjinrong.oa.ui.activity.contracts.contract.ContactDetailsContract;
import com.shanlinjinrong.oa.ui.activity.contracts.presenter.ContactDetailsPresenter;
import com.shanlinjinrong.oa.ui.activity.main.bean.UserDetailsBean;
import com.shanlinjinrong.oa.ui.activity.message.EaseChatMessageActivity;
import com.shanlinjinrong.oa.ui.activity.message.VoiceCallActivity;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.GlideRoundTransformUtils;
import com.shanlinjinrong.oa.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.shanlinjinrong.oa.manager.AppManager.mContext;

/**
 * 搜索联系人详情页
 */
public class Contact_Details_Activity extends HttpBaseActivity<ContactDetailsPresenter> implements ContactDetailsContract.View {
    @BindView(R.id.btn_back)
    ImageView btn_back;
    @BindView(R.id.tv_user_name)
    TextView tv_user_name;
    @BindView(R.id.rel_send_message)
    RelativeLayout rel_send_message;
    @BindView(R.id.rel_voice_call)
    RelativeLayout rel_voice_call;
    @BindView(R.id.rel_phone_call)
    RelativeLayout rel_phone_call;
    @BindView(R.id.tv_department)
    TextView tv_department;
    @BindView(R.id.tv_duties)
    TextView tv_duties;
    @BindView(R.id.tv_sex)
    TextView tv_sex;
    @BindView(R.id.tv_phone_number)
    TextView tv_phone_number;
    @BindView(R.id.tv_mails)
    TextView tv_mails;
    @BindView(R.id.iv_phone)
    ImageView iv_phone;

    @BindView(R.id.send_message)
    ImageView send_message;
    @BindView(R.id.send_voice)
    ImageView send_voice;
    @BindView(R.id.iv_img_user)
    ImageView ivImgUser;

    private User user;
    private String mSex;
    private String mPost;
    private String mEmail;
    private String mPhone;
    private String mUserId;
    private String mPortrait;
    private String mNickName;
    private String mUserCode;
    private boolean mSession;
    private String mDepartment;
    private String mDepartmentId;
    private String mUserDepartment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
        setTranslucentStatus(this);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private void initData() {
        mSession = this.getIntent().getBooleanExtra("isSession", false);
        mUserCode = this.getIntent().getStringExtra("user_code");
        mUserId = "sl_" + AppConfig.getAppConfig(AppManager.mContext).getPrivateCode();
        if (mSession) {
            initSessionInfo();
        } else {
            init();
        }
    }

    private void initSessionInfo() {
        if (!mUserCode.equals(mUserId)) {
            mPresenter.searchUserDetails(mUserCode.substring(3,mUserCode.length()));
        } else {
            //TODO 更新DB
            initUserDetails();
            FriendsInfoCacheSvc.getInstance(AppManager.mContext).
                    addOrUpdateFriends(new Friends(mUserId, mNickName, mPortrait, mSex, mPhone, mPost, mDepartment, mEmail, mDepartmentId));
        }
    }

    private void initUserDetails() {
        //--------------------------------- 初始化个人信息 ---------------------------------
        try {//对方信息
            if (!mUserCode.equals(mUserId)) {
                mNickName = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getNickName(mUserCode);
                mPortrait = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getPortrait(mUserCode);
                mDepartment = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getDepartment(mUserCode);
                mEmail = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getEmail(mUserCode);
                mPost = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getPost(mUserCode);
                mPhone = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getPhone(mUserCode);
                mSex = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getSex(mUserCode);
                mUserDepartment = AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_DEPARTMENT_NAME);
            } else {//自己信息
                mPortrait = AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_PORTRAITS);
                mSex = AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_SEX);
                mPost = AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_POST_NAME);
                mPhone = AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_PHONE);
                mNickName = AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_USERNAME);
                mDepartment = AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_DEPARTMENT_NAME);
                mEmail = AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_USER_EMAIL);
                mDepartmentId = AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_DEPARTMENT_ID);
            }
            tv_sex.setText(mSex.equals("") ? "-" : mSex);
            tv_duties.setText(mPost.equals("") ? "-" : mPost);
            tv_user_name.setText(mNickName.equals("") ? "-" : mNickName);
            tv_department.setText(mDepartment.equals("") ? "-" : mDepartment);
            tv_mails.setText(mEmail.equals("") ? "-" : mEmail);
            if (mDepartment.equals(mUserDepartment)) {
                tv_phone_number.setText(mPhone.equals("") ? "-" : mPhone);
            } else {
                tv_phone_number.setText("-");
            }
            Glide.with(AppManager.mContext)
                    .load(mPortrait)
                    .error(R.drawable.ease_default_avatar)
                    .transform(new CenterCrop(AppManager.mContext), new GlideRoundTransformUtils(AppManager.mContext, 5))
                    .placeholder(R.drawable.ease_default_avatar).into(ivImgUser);

            //---------------------------------聊天 语音 拨打电话 逻辑处理---------------------------------

            if (mUserCode.equals(mUserId)) {
                iv_phone.setImageResource(R.mipmap.ico_phone_disabled);
                send_voice.setImageResource(R.mipmap.ico_vedio_disabled);
                send_message.setImageResource(R.mipmap.ico_message_disabled);
                RxView.clicks(send_message).
                        throttleFirst(1, TimeUnit.SECONDS).
                        subscribe(o -> showToast("不能给自己发送消息！"), Throwable::printStackTrace);
                RxView.clicks(send_voice).
                        throttleFirst(1, TimeUnit.SECONDS).
                        subscribe(o -> showToast("不能跟自己语音通话！"), Throwable::printStackTrace);
                RxView.clicks(iv_phone).
                        throttleFirst(1, TimeUnit.SECONDS).
                        subscribe(o -> showToast("不能给自己拨打电话！"), Throwable::printStackTrace);
            } else {

                //--------------------------------- 聊天 ---------------------------------

                RxView.clicks(send_message).
                        throttleFirst(1, TimeUnit.SECONDS).
                        subscribe(o -> {
                            boolean isNetwork = Utils.isNetworkAvailabl(mContext);
                            if (!isNetwork) {
                                showToast("网络不稳定，请重试！");
                                return;
                            }
                            try {
                                startActivity(new Intent(mContext, EaseChatMessageActivity.class)
                                        .putExtra("usernike", mNickName)
                                        .putExtra("user_pic", mPortrait)
                                        .putExtra("u_id", mUserCode)
                                        .putExtra("department_name", mDepartment)
                                        .putExtra("post_name", mPost)
                                        .putExtra("sex", mSex)
                                        .putExtra("phone", mPhone)
                                        .putExtra("email", mEmail));
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }, Throwable::printStackTrace);

                //--------------------------------- 语音 ---------------------------------

                RxView.clicks(send_voice).throttleFirst(1, TimeUnit.SECONDS).
                        subscribe(o -> {
                            boolean isNetwork = Utils.isNetworkAvailabl(mContext);
                            if (!isNetwork) {
                                showToast("网络不稳定，请重试！");
                                return;
                            }
                            startActivity(new Intent(mContext, VoiceCallActivity.class)
                                    .putExtra("username", mUserCode)
                                    .putExtra("phone", mPhone)
                                    .putExtra("sex", mSex)
                                    .putExtra("post_name", mPost)
                                    .putExtra("nike", mNickName)
                                    .putExtra("portrait", mPortrait)
                                    .putExtra("email", mEmail)
                                    .putExtra("department_name", mDepartment)
                                    .putExtra("isComingCall", false));
                        }, Throwable::printStackTrace);

                //--------------------------------- 拨打电话 ---------------------------------

                RxView.clicks(iv_phone).throttleFirst(1, TimeUnit.SECONDS).
                        subscribe(o -> {
                            if (mPhone.equals("-") || mPhone.equals("") || !mDepartment.equals(mUserDepartment)) {
                                iv_phone.setImageResource(R.mipmap.ico_phone_disabled);
                                showToast("电话为空,无法拨打！");
                                return;
                            } else {
                                requestRunTimePermission(new String[]{Manifest.permission.CALL_PHONE}, new PermissionListener() {
                                    @Override
                                    public void onGranted() {
                                        Intent intent = new Intent(Intent.ACTION_CALL,
                                                Uri.parse("tel:" + mPhone));
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
                        }, Throwable::printStackTrace);
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
                    Intent intent = new Intent(Contact_Details_Activity.this, EaseChatMessageActivity.class);
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("CODE", user.getCode());
                        jsonObject.put("department_name", user.getDepartmentName());
                        jsonObject.put("email", user.getEmail());
                        jsonObject.put("phone", user.getPhone());
                        jsonObject.put("portrait", user.getPortraits());
                        jsonObject.put("post_title", user.getPostName());
                        jsonObject.put("sex", user.getSex());
                        jsonObject.put("username", user.getUsername());
                        intent.putExtra("u_id", Constants.CID + "_" + user.getCode());
                        intent.putExtra("userInfo", jsonObject.toString());
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

    @Override
    public void uidNull(int code) {

    }

    @Override
    public void searchUserDetailsSuccess(UserDetailsBean.DataBean userDetailsBean) {
        try {//更新个人详情
            FriendsInfoCacheSvc.getInstance(AppManager.mContext).
                    addOrUpdateFriends(new Friends("sl_" + userDetailsBean.getCode(), userDetailsBean.getUsername(),
                            "http://" + userDetailsBean.getImg(), userDetailsBean.getSex(), userDetailsBean.getPhone(),
                            userDetailsBean.getPostname(), userDetailsBean.getOrgan(), userDetailsBean.getEmail(), userDetailsBean.getOid()));
            initUserDetails();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void searchUserDetailsFailed() {
    }
}
