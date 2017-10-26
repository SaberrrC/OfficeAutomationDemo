package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.stmt.query.In;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.views.common.CommonTopView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 我的考勤
 */
public class MineWorkRecordActivity extends AppCompatActivity {

    @Bind(R.id.top_view)
    CommonTopView mTopView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_work_record);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {


    }

    private void initView() {
        mTopView.setRightDrawable(getResources().getDrawable(R.drawable.icon_login_user));
        View rightView = mTopView.getRightView();
        rightView.setOnClickListener(view -> {
            Toast.makeText(MineWorkRecordActivity.this, "跳转日历", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent();
//            startActivity(intent);
        });
    }


}
