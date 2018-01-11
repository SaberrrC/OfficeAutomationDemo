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
import com.shanlinjinrong.oa.listener.PermissionListener;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.model.User;
import com.shanlinjinrong.oa.ui.activity.contracts.bean.ContactDetailsBean;
import com.shanlinjinrong.oa.ui.activity.contracts.contract.ContactDetailsContract;
import com.shanlinjinrong.oa.ui.activity.contracts.presenter.ContactDetailsPresenter;
import com.shanlinjinrong.oa.ui.activity.message.EaseChatMessageActivity;
import com.shanlinjinrong.oa.ui.activity.message.VoiceCallActivity;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.Utils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.shanlinjinrong.oa.manager.AppManager.mContext;

/**
 * 搜索联系人详情页
 */
public class Contact_Details_Activity extends HttpBaseActivity<ContactDetailsPresenter> implements ContactDetailsContract.View {

    @BindView(R.id.tv_sex)
    TextView tv_sex;
    @BindView(R.id.btn_back)
    ImageView btn_back;
    @BindView(R.id.tv_duties)
    TextView tv_duties;
    @BindView(R.id.tv_mails)
    TextView tv_mails;
    @BindView(R.id.iv_phone)
    ImageView iv_phone;
    @BindView(R.id.iv_img_user)
    CircleImageView ivImgUser;
    @BindView(R.id.send_voice)
    ImageView send_voice;
    @BindView(R.id.send_message)
    ImageView send_message;
    @BindView(R.id.tv_user_name)
    TextView tv_user_name;
    @BindView(R.id.tv_department)
    TextView tv_department;
    @BindView(R.id.tv_phone_number)
    TextView tv_phone_number;
    @BindView(R.id.rel_send_message)
    RelativeLayout rel_send_message;
    @BindView(R.id.rel_voice_call)
    RelativeLayout rel_voice_call;
    @BindView(R.id.rel_phone_call)
    RelativeLayout rel_phone_call;
    @BindView(R.id.tv_error_layout)
    TextView tvErrorLayout;

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
    private final int RESULTGROUP = -2;

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
        mUserCode = getIntent().getStringExtra("user_code");
        mUserId = "sl_" + AppConfig.getAppConfig(AppManager.mContext).getPrivateCode();

        if (mSession) {
            tvErrorLayout.setVisibility(View.VISIBLE);
            initSessionInfo();
        } else {
            initUserDetails();
            initView();
        }
    }

    private void initSessionInfo() {
        mPresenter.searchUserDetails(mUserCode.substring(3, mUserCode.length()));
    }

    private void initUserDetails() {
        try {

            //--------------------------------- 初始化个人信息 ---------------------------------

            initSessionData();

            //---------------------------------聊天 语音 拨打电话 逻辑处理---------------------------------

            if (!mDepartment.equals(mUserDepartment))
                iv_phone.setImageResource(R.mipmap.ico_phone_disabled);

            if (mUserCode.equals(mUserId)) {

                iv_phone.setImageResource(R.mipmap.ico_phone_disabled);
                send_voice.setImageResource(R.mipmap.ico_vedio_disabled);
                send_message.setImageResource(R.mipmap.ico_message_disabled);

//                RxView.clicks(rel_send_message).
//                        throttleFirst(1, TimeUnit.SECONDS).
//                        subscribe(o -> showToast("不能给自己发送消息！"), Throwable::printStackTrace);
//
//                RxView.clicks(rel_voice_call).
//                        throttleFirst(1, TimeUnit.SECONDS).
//                        subscribe(o -> showToast("不能跟自己语音通话！"), Throwable::printStackTrace);
//
//                RxView.clicks(rel_phone_call).
//                        throttleFirst(1, TimeUnit.SECONDS).
//                        subscribe(o -> showToast("不能给自己拨打电话！"), Throwable::printStackTrace);

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
                                        .putExtra("department_name", mDepartment)
                                        .putExtra("post_name", mPost)
                                        .putExtra("sex", mSex)
                                        .putExtra("phone", mPhone)
                                        .putExtra("message_to", mUserCode)
                                        .putExtra("message_from", "sl_" + AppConfig.getAppConfig(AppManager.mContext).getPrivateCode())
                                        .putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE)
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
                                    .putExtra("sex", mSex)
                                    .putExtra("u_id", mUserId)
                                    .putExtra("phone", mPhone)
                                    .putExtra("email", mEmail)
                                    .putExtra("nike", mNickName)
                                    .putExtra("username", mUserCode)
                                    .putExtra("post_name", mPost)
                                    .putExtra("portrait", mPortrait)
                                    .putExtra("isComingCall", false)
                                    .putExtra("department_name", mDepartment));
                        }, Throwable::printStackTrace);

                //--------------------------------- 拨打电话 ---------------------------------

                RxView.clicks(rel_phone_call).throttleFirst(1, TimeUnit.SECONDS).
                        subscribe(o -> {

                            String phone = tv_phone_number.getText().toString().trim();
                            if (phone.equals("-") || phone.equals("") || !mDepartment.equals(mUserDepartment)) {
                                iv_phone.setImageResource(R.mipmap.ico_phone_disabled);
//                                showToast("电话为空,无法拨打！");
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
        } catch (
                Throwable e)

        {
            e.printStackTrace();
        }

    }

    private void initSessionData() {
        if (mSession) {
            mSex = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getSex(mUserCode);
            mPost = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getPost(mUserCode);
            mEmail = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getEmail(mUserCode);
            mPhone = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getPhone(mUserCode);
            mNickName = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getNickName(mUserCode);
            mPortrait = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getPortrait(mUserCode);
            mDepartment = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getDepartment(mUserCode);
            mDepartmentId = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getDepartmentId(mUserCode);
            mUserDepartment = AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_DEPARTMENT_NAME);
        } else {
            user = (User) this.getIntent().getSerializableExtra("user");
            mSex = user.getSex();
            String uid = user.getUid();
            mEmail = user.getEmail();
            mPhone = user.getPhone();
            mPost = user.getPostName();
            mUserCode = "sl_" + user.getCode();
            mNickName = user.getUsername();
            mPortrait = ApiConstant.BASE_PIC_URL + user.getPortraits();
            mDepartment = user.getDepartmentName();
            mDepartmentId = user.getDepartmentId();
            mUserDepartment = AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_DEPARTMENT_NAME);

            FriendsInfoCacheSvc.getInstance(AppManager.mContext).
                    addOrUpdateFriends(new Friends(uid, mUserCode,
                            mNickName, mPortrait, mSex, mPhone, mPost, mDepartment, mEmail, mDepartmentId));
        }
    }

    private void initView() {
        try {
            tv_sex.setText(mSex.equals("") ? "-" : mSex);
            tv_duties.setText(mPost.equals("") ? "-" : mPost);
            tv_user_name.setText(mNickName.equals("") ? "-" : mNickName);
            tv_department.setText(mDepartment.equals("") ? "-" : mDepartment);
            if (mDepartment.equals(mUserDepartment)) {
                tv_phone_number.setText(mPhone.equals("") ? "-" : mPhone);
            } else {
                tv_phone_number.setText("-");
            }

            if (!TextUtils.isEmpty(mPortrait) && !ApiConstant.BASE_PIC_URL.equals(mPortrait)) {
                Glide.with(AppManager.mContext)
                        .load(mPortrait)
                        .dontAnimate()
                        .error(R.drawable.ease_user_portraits)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .transform(new CenterCrop(AppManager.mContext), new GlideRoundTransformUtils(AppManager.mContext, 5))
                        .placeholder(R.drawable.ease_user_portraits).into(ivImgUser);
            } else {
                Glide.with(AppManager.mContext).load(R.drawable.ease_user_portraits).asBitmap().into(ivImgUser);
            }

            tv_mails.setText(mEmail.equals("") || mEmail == null || mEmail.equals("null") ? "-" : mEmail);
        } catch (Throwable e) {
            e.printStackTrace();
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
    public void uidNull(String code) {
        catchWarningByCode(code);
    }

    @Override
    public void showLoading() {
        showLoadingView();
    }

    @Override
    public void hideLoading() {
        hideLoadingView();
    }

    @Override
    public void searchUserDetailsSuccess(ContactDetailsBean.DataBean userDetailsBean) {
        tvErrorLayout.setVisibility(View.GONE);
        try {//更新个人详情
            FriendsInfoCacheSvc.getInstance(AppManager.mContext).
                    addOrUpdateFriends(new Friends(userDetailsBean.getUid(), "sl_" + userDetailsBean.getCode(),
                            userDetailsBean.getUsername(), ApiConstant.BASE_PIC_URL + userDetailsBean.getPortrait(),
                            userDetailsBean.getSex(), userDetailsBean.getPhone(), userDetailsBean.getPostname(),
                            userDetailsBean.getOrgan(), userDetailsBean.getEmail(), userDetailsBean.getOid()));
            initSessionData();
            initUserDetails();
            initView();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void searchUserDetailsFailed(int errorCode, String errorMsg) {
        hideLoadingView();
        switch (errorCode) {
            case -1:
                tvErrorLayout.setText(getString(R.string.net_no_connection));
                break;
            default:
                tvErrorLayout.setText(getString(R.string.data_load_error));
                break;
        }

        RxView.clicks(tvErrorLayout).
                throttleFirst(2, TimeUnit.SECONDS).
                subscribe(o -> initSessionInfo(), throwable -> {
                    throwable.printStackTrace();
                    tvErrorLayout.setText("服务器异常,请稍后重试");
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULTGROUP:
                //TODO 暂时不做处理
                break;
        }
    }
}
