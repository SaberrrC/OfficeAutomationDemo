package com.shanlinjinrong.oa.ui.activity.contracts;


import android.Manifest;
import android.annotation.SuppressLint;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.retrofit.net.ApiConstant;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.db.Friends;
import com.hyphenate.easeui.db.FriendsInfoCacheSvc;
import com.hyphenate.easeui.utils.GlideRoundTransformUtils;
import com.jakewharton.rxbinding2.view.RxView;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.listener.PermissionListener;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.model.Contacts;
import com.shanlinjinrong.oa.ui.activity.message.EaseChatMessageActivity;
import com.shanlinjinrong.oa.ui.activity.message.VoiceCallActivity;
import com.shanlinjinrong.oa.ui.base.BaseActivity;
import com.shanlinjinrong.oa.utils.Utils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.shanlinjinrong.oa.manager.AppManager.mContext;

/**
 * 通讯录联系人详情页
 */
public class Contact_Details_Activity2 extends BaseActivity {

    @BindView(R.id.tv_sex)
    TextView        tv_sex;
    @BindView(R.id.btn_back)
    ImageView       btn_back;
    @BindView(R.id.tv_duties)
    TextView        tv_duties;
    @BindView(R.id.tv_mails)
    TextView        tv_mails;
    @BindView(R.id.iv_phone)
    ImageView       iv_phone;
    @BindView(R.id.iv_img_user)
    CircleImageView ivImgUser;
    @BindView(R.id.send_voice)
    ImageView       send_voice;
    @BindView(R.id.tv_user_name)
    TextView        tv_user_name;
    @BindView(R.id.send_message)
    ImageView       send_message;
    @BindView(R.id.tv_department)
    TextView        tv_department;
    @BindView(R.id.tv_phone_number)
    TextView        tv_phone_number;
    @BindView(R.id.rel_voice_call)
    RelativeLayout  rel_voice_call;
    @BindView(R.id.rel_phone_call)
    RelativeLayout  rel_phone_call;
    @BindView(R.id.rel_send_message)
    RelativeLayout  rel_send_message;

    private String   mSex;
    private String   mPost;
    private String   mPhone;
    private String   mEmail;
    private String   mNickName;
    private String   mPortrait;
    private String   mUserCode;
    private String   mDepartment;
    private Contacts constants;
    private String   mDepartmentId;
    private String   mUserDepartment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
        setTranslucentStatus(this);
        ButterKnife.bind(this);
        init();
    }

    @SuppressLint("ShowToast")
    public void init() {
        try {
            initData();
            initView();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void initData() {

        //--------------------------------- 获取数据 ---------------------------------

        constants = (Contacts) this.getIntent().getSerializableExtra("contacts");
        mSex = constants.getSex();
        mPhone = constants.getPhone();
        mEmail = constants.getEmail();
        mPost = constants.getPostTitle();
        mNickName = constants.getUsername();
        String uid = constants.getUid();
        mPortrait = constants.getPortraits();
        mUserCode = "sl_" + constants.getCode();
        mDepartmentId = constants.getDepartmentId();
        mDepartment = constants.getDepartmentName();
        mUserDepartment = AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_DEPARTMENT_NAME);


        FriendsInfoCacheSvc.getInstance(AppManager.mContext)
                .addOrUpdateFriends(new Friends(uid, mUserCode, mNickName, ApiConstant.BASE_PIC_URL + mPortrait, mSex, mPhone, mPost, mDepartment, mEmail, mDepartmentId));

        //---------------------------------聊天 语音 拨打电话 逻辑处理---------------------------------

        if (!mDepartment.equals(mUserDepartment))
            iv_phone.setImageResource(R.mipmap.ico_phone_disabled);

        if (constants.getUsername().equals(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_USERNAME))) {

            iv_phone.setImageResource(R.mipmap.ico_phone_disabled);
            send_voice.setImageResource(R.mipmap.ico_vedio_disabled);
            send_message.setImageResource(R.mipmap.ico_message_disabled);

//            RxView.clicks(rel_send_message).
//                    throttleFirst(1, TimeUnit.SECONDS).
//                    subscribe(o -> showToast("不能给自己发送消息！"), Throwable::printStackTrace);
//
//            RxView.clicks(rel_voice_call).
//                    throttleFirst(1, TimeUnit.SECONDS).
//                    subscribe(o -> showToast("不能跟自己语音通话！"), Throwable::printStackTrace);
//
//            RxView.clicks(rel_phone_call).
//                    throttleFirst(1, TimeUnit.SECONDS).
//                    subscribe(o -> showToast("不能给自己拨打电话！"), Throwable::printStackTrace);
        } else {

            //--------------------------------- 聊天 ---------------------------------

            RxView.clicks(rel_send_message).
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
                                    .putExtra("title", mNickName)
                                    .putExtra("user_pic", mPortrait)
                                    .putExtra("u_id", mUserCode)
                                    .putExtra("code", mUserCode)
                                    .putExtra("department_name", mDepartment)
                                    .putExtra("post_name", mPost)
                                    .putExtra("sex", mSex)
                                    .putExtra("message_to", mUserCode)
                                    .putExtra("message_from", "sl_" + AppConfig.getAppConfig(AppManager.mContext).getPrivateCode())
                                    .putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE)
                                    .putExtra("phone", mPhone)
                                    .putExtra("email", mEmail));
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }, Throwable::printStackTrace);

            //--------------------------------- 语音 ---------------------------------

            RxView.clicks(rel_voice_call).throttleFirst(1, TimeUnit.SECONDS).
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
                                .putExtra("username", mUserCode)
                                .putExtra("portrait", mPortrait)
                                .putExtra("email", mEmail)
                                .putExtra("department_name", mDepartment)
                                .putExtra("isComingCall", false));
                    }, Throwable::printStackTrace);

            //--------------------------------- 拨打电话 ---------------------------------

            RxView.clicks(rel_phone_call).throttleFirst(1, TimeUnit.SECONDS).
                    subscribe(o -> {
                        String phone = tv_phone_number.getText().toString().trim();
                        if (phone.equals("-") || phone.equals("") || !constants.getIsshow().equals("1") || !mDepartment.equals(mUserDepartment)) {
                            iv_phone.setImageResource(R.mipmap.ico_phone_disabled);
//                            showToast("电话为空,无法拨打！");
                        } else {
                            iv_phone.setImageResource(R.mipmap.ico_phone);
                            requestRunTimePermission(new String[]{Manifest.permission.CALL_PHONE}, new PermissionListener() {
                                @Override
                                public void onGranted() {
                                    Intent intent = new Intent(Intent.ACTION_CALL,
                                            Uri.parse("tel:" + constants.getPhone()));
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
    }

    private void initView() {

        tv_sex.setText(mSex);
        tv_duties.setText(mPost);
        tv_user_name.setText(mNickName);
        tv_department.setText(mDepartment);

        if (constants.getIsshow().equals("1")) {
            tv_phone_number.setText(mPhone);
        } else if (constants.getPhone() == null || constants.getPhone().equals("")) {
            tv_phone_number.setText("-");
        }

        if (constants.getEmail() == null || constants.getEmail().equals("") || constants.getEmail().equals("null")) {
            tv_mails.setText("-");
        } else {
            tv_mails.setText(mEmail);
        }
        if (!TextUtils.isEmpty(mPortrait)) {
            Glide.with(AppManager.mContext)
                    .load(ApiConstant.BASE_PIC_URL + mPortrait)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.ease_user_portraits)
                    .transform(new CenterCrop(AppManager.mContext), new GlideRoundTransformUtils(AppManager.mContext, 5))
                    .placeholder(R.drawable.ease_user_portraits).into(ivImgUser);
        } else {
            Glide.with(AppManager.mContext).load(R.drawable.ease_user_portraits).asBitmap().into(ivImgUser);
        }
    }

    @OnClick(R.id.btn_back)
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
