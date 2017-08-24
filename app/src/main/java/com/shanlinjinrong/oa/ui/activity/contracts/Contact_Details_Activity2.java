package com.shanlinjinrong.oa.ui.activity.contracts;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.easeui.db.Friends;
import com.hyphenate.easeui.db.FriendsInfoCacheSvc;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.listener.PermissionListener;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.model.Contacts;
import com.shanlinjinrong.oa.ui.activity.message.EaseChatMessageActivity;
import com.shanlinjinrong.oa.ui.activity.message.VoiceCallActivity;
import com.shanlinjinrong.oa.ui.base.BaseActivity;
import com.shanlinjinrong.oa.utils.Utils;
import com.shanlinjinrong.oa.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.shanlinjinrong.oa.manager.AppManager.mContext;

//import com.hyphenate.chatuidemo.db.Friends;
//import com.hyphenate.chatuidemo.db.FriendsInfoCacheSvc;

/**
 * 通讯录联系人详情页
 */
public class Contact_Details_Activity2 extends BaseActivity {
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
    @Bind(R.id.send_message)
    ImageView send_message;
    @Bind(R.id.send_voice)
    ImageView send_voice;

    @Bind(R.id.iv_phone)
    ImageView iv_phone;
    private Contacts constants;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
        ButterKnife.bind(this);
        init2();
    }

    private void addOrUpdateFriendInfo2(Contacts contacts) {
        Friends friend = new Friends();
        friend.setUser_id(Constants.CID + "_" + contacts.getCode());
        friend.setNickname(contacts.getUsername());
        friend.setPortrait(contacts.getPortraits());
        FriendsInfoCacheSvc.getInstance(Contact_Details_Activity2.this).addOrUpdateFriends(friend);
    }

    public void init2() {
        constants = (Contacts) this.getIntent().getSerializableExtra("contacts");
        tv_user_name.setText(constants.getUsername());
        tv_department.setText(constants.getDepartmentName());
        tv_duties.setText(constants.getPostTitle());
        tv_sex.setText(constants.getSex());


        if (constants.getIsshow().equals("1")) {
            tv_phone_number.setText(constants.getPhone());
        }
        else  if (constants.getPhone() == null||constants.getPhone().equals("")){
            tv_phone_number.setText("-");
        }
//        if (constants.getPhone()==null||constants.getPhone().equals("")) {
//            tv_phone_number.setText("-");
//        } else {
//            tv_phone_number.setText(constants.getPhone());
//        }
        if (constants.getEmail() == null || constants.getEmail().equals("")){
            tv_mails.setText("-");
        }else{
            tv_mails.setText(constants.getEmail());
        }

        //判断是否有权限打电话
        if (constants.getUsername().equals(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_USERNAME))) {
            Toast.makeText(getApplication(), "不能给自己打电话", Toast.LENGTH_SHORT);
            iv_phone.setImageResource(R.mipmap.ico_phone_disabled);

        } else {
            if (constants.getIsshow().equals("1")) {
                iv_phone.setImageResource(R.mipmap.ico_phone);
                //可以打电话
                rel_phone_call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestRunTimePermission(new String[]{Manifest.permission.CALL_PHONE}, new PermissionListener() {
                            @Override
                            public void onGranted() {
                                Intent intent = new Intent(Intent.ACTION_CALL,
                                        Uri.parse("tel:" + constants.getPhone()));

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
            } else if (constants.getPhone().equals("")) {
                iv_phone.setImageResource(R.mipmap.ico_phone_disabled);
            } else {
                iv_phone.setImageResource(R.mipmap.ico_phone_disabled);
            }
        }
        if (constants.getUsername().equals(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_USERNAME))) {
            Toast.makeText(getApplication(), "不能呼叫自己", Toast.LENGTH_SHORT);
            send_voice.setImageResource(R.mipmap.ico_vedio_disabled);

        } else {
            rel_voice_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean availabl = Utils.isNetworkAvailabl(Contact_Details_Activity2.this);
                    if (!availabl) {
                        showToast("网络不稳定，请重试");
                        return;
                    }

                    addOrUpdateFriendInfo2(constants);

                    startActivity(new Intent(Contact_Details_Activity2.this, VoiceCallActivity.class)
                            .putExtra("username", Constants.CID + "_" + constants.getCode())
                            .putExtra("nike", constants.getUsername())
                            .putExtra("portrait", constants.getPortraits())
                            .putExtra("isComingCall", false));
                }
            });
        }
        if (constants.getUsername().equals(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_USERNAME))) {
            Toast.makeText(getApplication(), "不能给自己发消息", Toast.LENGTH_SHORT);
            send_message.setImageResource(R.mipmap.ico_message_disabled);
        } else {
            rel_send_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean availabl = Utils.isNetworkAvailabl(Contact_Details_Activity2.this);
                    if (!availabl) {
                        showToast("网络不稳定，请重试");
                        return;
                    }
                    addOrUpdateFriendInfo2(constants);

                    startActivity(new Intent(Contact_Details_Activity2.this, EaseChatMessageActivity.class)
                            .putExtra("usernike", constants.getUsername())
                            .putExtra("user_pic", constants.getPortraits())
                            .putExtra("u_id", Constants.CID + "_" + constants.getCode())
                            .putExtra("department_name", constants.getDepartmentName())
                            .putExtra("post_name", constants.getPostTitle())
                            .putExtra("sex", constants.getSex())
                            .putExtra("phone", constants.getPhone())
                            .putExtra("code", constants.getCode())
                            .putExtra("uid", constants.getUid())
                            .putExtra("email", constants.getEmail()));
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
