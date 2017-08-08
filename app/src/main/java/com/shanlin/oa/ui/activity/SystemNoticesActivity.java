package com.shanlin.oa.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanlin.oa.R;
import com.shanlin.oa.common.Api;
import com.shanlin.oa.manager.AppConfig;
import com.shanlin.oa.model.SystemNotice;
import com.shanlin.oa.ui.adapter.SystemNoticeDetailAdapter;
import com.shanlin.oa.ui.base.BaseActivity;
import com.shanlin.oa.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ProjectName: dev-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by CXP on Date: 2016/9/19 11:31.
 * Description:系统消息activity
 */
public class SystemNoticesActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar_text_btn)
    TextView toolbarTextBtn;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.notice_detail_recyclerView)
    RecyclerView mRecyclerView;
    private ArrayList<SystemNotice> list;
    private SystemNoticeDetailAdapter mAdapter;
    @Bind(R.id.layout_content)
    RelativeLayout mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_notice_detail);
        ButterKnife.bind(this);
        initToolBar();
        setTranslucentStatus(this);
        initWidget();
        initData();
        loadData();
    }

    private void loadData() {

        HttpParams params = new HttpParams();
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        params.put("department_id", AppConfig.getAppConfig(this).getDepartmentId());
        initKjHttp().post(Api.SYSTEM_NOTICE, params, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();
                showLoadingView();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e(t);
                try {
                    JSONObject jo = new JSONObject(t);
                    if (Api.getCode(jo) ==Api.RESPONSES_CODE_UID_NULL){
                        catchWarningByCode(Api.getCode(jo));
                    }
                    switch (Api.getCode(jo)) {
                        case Api.RESPONSES_CODE_OK:
                            list = new ArrayList<>();
                            JSONArray notices = Api.getDataToJSONArray(jo);
                            for (int i = 0; i < notices.length(); i++) {
                                JSONObject jsonObject = notices.getJSONObject(i);
                                SystemNotice systemNotice =
                                        new SystemNotice(jsonObject);
                                list.add(systemNotice);
                            }
                            mAdapter.notifyDataSetChanged();
                            setDataForRecyclerView();
                            readAllSystemNotices();
                            break;
                        case Api.RESPONSES_CODE_DATA_EMPTY:
                            showEmptyView(mRootView, "抱歉，暂时还未接收到消息",
                                    R.drawable.contacts_empty_icon, false);
                            break;
                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
                            catchWarningByCode(Api.getCode(jo));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoadingView();
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                catchWarningByCode(errorNo);
            }
        });
    }

    private void setDataForRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SystemNoticeDetailAdapter(list);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 系统消息全部已读接口
     */
    private void readAllSystemNotices() {
        showLoadingView();
        HttpParams params = new HttpParams();
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        params.put("department_id", AppConfig.getAppConfig(this).getDepartmentId());
        initKjHttp().post(Api.SYSTEM_NOTICE_ALL_READ, params, new HttpCallBack() {
            @Override
            public void onFinish() {
                super.onFinish();
                hideLoadingView();
            }
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                System.out.println(t);
            }
            @Override
            public void onFailure(int errorNo, String strMsg) {
                hideLoadingView();
                catchWarningByCode(errorNo);
                super.onFailure(errorNo, strMsg);
            }
        });
    }

    private void initData() {
    }

    private void initWidget() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SystemNoticeDetailAdapter(list);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initToolBar() {
        if (toolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);
        tvTitle.setText("系统消息");
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        tvTitle.setLayoutParams(lp);

        toolbarTextBtn.setVisibility(View.VISIBLE);
        toolbar.setNavigationIcon(R.drawable.toolbar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
