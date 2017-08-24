package com.shanlinjinrong.oa.ui.activity.home.schedule;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.model.GlightContact;
import com.shanlinjinrong.oa.ui.adapter.SelectContactsAdapter;
import com.shanlinjinrong.oa.ui.base.BaseActivity;
import com.shanlinjinrong.oa.ui.activity.home.workreport.WorkReportLaunchActivity;
import com.shanlinjinrong.oa.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ProjectName: GroupInformationPlatform
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by CXP on Date: 2016/9/6 11:21.
 * Description: 选择联系人activity
 */
public class SelectContactsActivityTempTest extends BaseActivity {
    @Bind(R.id.layout_content)
    RelativeLayout mRootView;
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.select_recyclerView)
    RecyclerView mRecyclerVeiw;
    @Bind(R.id.toolbar_text_btn)
    TextView mToolBarText;
    @Bind(R.id.tv_department)
    TextView mTvDepartment;
    ArrayList<GlightContact> list;
    private SelectContactsAdapter mAdapter;
    private ArrayList<GlightContact> selectedContacts;//已经选择的联系人

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contacts);
        ButterKnife.bind(this);
        getContactType();
        initToolBar();
        setTranslucentStatus(this);
        initWeiget();
        loadData();
    }

    private void initWeiget() {
        mTvDepartment.setText(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_DEPARTMENT_NAME));
    }

    /**
     * 获得显示联系人类型的参数
     */
    private void getContactType() {
        Intent intent = getIntent();
        selectedContacts = new ArrayList<>();
        ArrayList<GlightContact> selectedContact = intent.getParcelableArrayListExtra("selectedContacts");
        this.selectedContacts.addAll(selectedContact);
    }

    private void loadData() {
        showLoadingView("正在获取联系人列表");
        HttpParams params = new HttpParams();
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        params.put("department_id", AppConfig.getAppConfig(this).getDepartmentId());
        initKjHttp().post(Api.PUBLIC_COPY_CONTACTS, params, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e(t);
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (Api.getCode(jo)) {
                        case Api.RESPONSES_CODE_OK:
                            list = new ArrayList<>();
                            JSONArray contacts = Api.getDataToJSONArray(jo);
                            for (int i = 0; i < contacts.length(); i++) {
                                JSONObject jsonObject = contacts.getJSONObject(i);

                                GlightContact contact =
                                        new GlightContact(jsonObject.getString("portrait"),
                                                jsonObject.getString("username"),
                                                jsonObject.getString("uid"), true,  jsonObject
                                                .getString("post"));
                                list.add(contact);
                            }

                            setDataForRecyclerView();
                            break;
                        case Api.RESPONSES_CODE_DATA_EMPTY:
                            mToolBarText.setVisibility(View.GONE);

                                showEmptyView(mRootView, "暂无可抄送对象，请联系管理员进行设置", 0, false);
                            break;
                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
                            catchWarningByCode(Api.getCode(jo));
                            break;
                        case Api.RESPONSES_CODE_UID_NULL:
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
        mRecyclerVeiw.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SelectContactsAdapter(list, selectedContacts);
        mAdapter.setSelectedPeopleInter(new SelectContactsAdapter.InterSelectedPeople() {
            @Override
            public void addPeople(String uId, String userName) {
                GlightContact contact = new GlightContact(userName, uId);
                boolean isHad = false;
                for (int i = 0; i < selectedContacts.size(); i++) {
                    if (uId.equals(selectedContacts.get(i).uid)) {

                        isHad = true;
                        break;
                    }
                }
                if (!isHad) {
                    //当前集合没有此联系人，不做处理
                    selectedContacts.add(contact);
                }
            }

            @Override
            public void removePeople(String uId, String userName) {
                for (int i = 0; i < selectedContacts.size(); i++) {
                    GlightContact contact = selectedContacts.get(i);
                    if (uId.equals(contact.uid)) {
                        selectedContacts.remove(i);
                    }
                }

            }
        });
        mRecyclerVeiw.setAdapter(mAdapter);


        mRecyclerVeiw.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view,
                                          int position) {
                CheckBox cb = (CheckBox) view.findViewById(R.id.select_contact_icon);
                cb.setChecked(!cb.isChecked());
            }
        });
    }

    /**
     * 获取用户选择的联系人
     */
    private void executeMultiOperate() {

        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("contacts", selectedContacts);
        SelectContactsActivityTempTest.this.setResult(WorkReportLaunchActivity.REQUEST_CODE_MULTIPLE,
                intent);
        SelectContactsActivityTempTest.this.finish();

    }

    private void initToolBar() {
        if (mToolBar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        mToolBar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(mToolBar);
        mTvTitle.setText(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_COMPANY_NAME));
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        mTvTitle.setLayoutParams(lp);

        mToolBar.setNavigationIcon(R.drawable.toolbar_back);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
            mToolBarText.setVisibility(View.VISIBLE);
            mToolBarText.setText("确定");
        mToolBarText.setVisibility(View.VISIBLE);
        mToolBarText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeMultiOperate();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}