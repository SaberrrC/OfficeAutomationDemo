package com.shanlinjinrong.oa.ui.activity.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.views.common.CommonTopView;

import butterknife.BindView;
import butterknife.ButterKnife;

//查看消息记录界面
public class LookMessageRecordActivity extends AppCompatActivity {

    @BindView(R.id.top_view)
    CommonTopView topView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_message_record);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        topView.setOnClickListener(view -> {
            Intent intent = new Intent(this, SearchMessageRecordActivity.class);
            startActivity(intent);
        });
    }
}
