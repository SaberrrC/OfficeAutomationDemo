package com.itcrm.GroupInformationPlatform.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.easeui.db.Friends;
import com.hyphenate.easeui.db.FriendsInfoCacheSvc;
import com.itcrm.GroupInformationPlatform.R;
import com.itcrm.GroupInformationPlatform.common.Constants;
import com.itcrm.GroupInformationPlatform.model.Contacts;
import com.itcrm.GroupInformationPlatform.model.User;
import com.itcrm.GroupInformationPlatform.ui.base.BaseActivity;
import com.itcrm.GroupInformationPlatform.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.itcrm.GroupInformationPlatform.manager.AppManager.mContext;


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

    private User user;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
        ButterKnife.bind(this);
        init();
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


        if (user.getPhone() == null||user.getPhone().equals("")) {
            tv_phone_number.setText("-");
        } else {
            tv_phone_number.setText(user.getPhone());
        }
        if (user.getEmail() == null||user.getEmail().equals("")){
            tv_mails.setText("-");
        }else{
            tv_mails.setText(user.getEmail());
        }

        //判断是否有权限打电话
        if (user.getIsshow().equals("1")) {
            //可以打电话
        }else if(user.getPhone().equals("")){
            iv_phone.setImageResource(R.mipmap.ico_phone_disabled);
        }else{
            iv_phone.setImageResource(R.mipmap.ico_phone_disabled);
        }

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

    @OnClick(R.id.btn_back)
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }

    }
}
