package com.shanlinjinrong.oa.ui.activity.home.schedule;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.views.listview.decoration.LineItemDecoration;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MeetingInviteActivity extends AppCompatActivity {

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    @Bind(R.id.meeting_list)
    RecyclerView mMeetingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_invite);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mMeetingList.setLayoutManager(new LinearLayoutManager(this));
        mMeetingList.addItemDecoration(new LineItemDecoration(this, LineItemDecoration.VERTICAL_LIST, com.shanlinjinrong.uilibrary.R.drawable.driver_line));
        mMeetingList.setAdapter(new MeetingInviteAdapter());
    }
}
