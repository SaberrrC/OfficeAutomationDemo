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

    private User user;
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

        if (constants.getPhone()==null||constants.getPhone().equals("")) {
            tv_phone_number.setText("-");
        } else {
            tv_phone_number.setText(constants.getPhone());
        }
        if (constants.getEmail() == null || constants.getEmail().equals("")){
            tv_mails.setText("-");
        }else{
            tv_mails.setText(constants.getEmail());
        }
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
                        .putExtra("u_id", Constants.CID + "_" + constants.getCode()));
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
