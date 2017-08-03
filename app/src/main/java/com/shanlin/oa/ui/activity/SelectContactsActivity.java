
package com.shanlin.oa.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.shanlin.oa.R;
import com.shanlin.oa.common.Api;
import com.shanlin.oa.manager.AppConfig;
import com.shanlin.oa.manager.AppManager;
import com.shanlin.oa.model.GlightContact;
import com.shanlin.oa.model.selectContacts.Child;
import com.shanlin.oa.model.selectContacts.Group;
import com.shanlin.oa.ui.adapter.SelectCopierAdapter;
import com.shanlin.oa.ui.base.BaseActivity;
import com.shanlin.oa.ui.fragment.MyFragment;
import com.shanlin.oa.utils.LogUtils;
import com.shanlin.oa.views.ClearEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ProjectName: GroupInformationPlatform
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by CXP on Date: 2016/9/6 11:21.
 * Description: 选择联系人activity
 */
public class SelectContactsActivity extends BaseActivity {
    @Bind(R.id.layout_content)
    RelativeLayout mRootView;
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.toolbar_text_btn)
    TextView mToolBarText;

    @Bind(R.id.listView)
    ExpandableListView mListView;
    ArrayList<GlightContact> list;
    ArrayList<Group> groups = new ArrayList<>();
    private ArrayList<Child> selectedContacts;//已经选择的联系人
    private SelectCopierAdapter mAdapter;
    //----------自己写的
    @Bind(R.id.search_et_input)
    ClearEditText search_et_input;
    @Bind(R.id.search_et_cancle)
    TextView tvCacle;
    @Bind(R.id.cC_num)
    TextView qty;
    ArrayList<Group> searchGroup = new ArrayList<>();
    @Bind(R.id.listViewsearch)
    ExpandableListView listViewsearch;
    protected BottomSheetLayout bottomSheetLayout;
    //----------------------
    private AppManager appManager = null;
    private MyHandler mHandler = null;
    //----------------------

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contacts);
        ButterKnife.bind(this);
        initToolBar();
        setTranslucentStatus(this);
        initWeiget();
        loadData();
        listViewsearch.setVisibility(View.GONE);
        search();
//------------------------------------
        appManager = (AppManager) getApplication();
        // 获得该共享变量实例
        mHandler = appManager.getHandler();
        appManager = (AppManager) getApplication();
        mHandler = new MyHandler();
        //--------------------------
        //底部弹出框
        bottomSheetLayout = (BottomSheetLayout) findViewById(R.id.bottomsheet);
        findViewById(R.id.rel_choose_people_num).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedContacts.size() != 0) {
                    //-------------
                    appManager.setHandler(mHandler);
                    //--------------
                    new MyFragment(selectedContacts).show(getSupportFragmentManager(),
                            R.id.bottomsheet);
                }
            }
        });

    }

    //搜索事件
    public void search() {
        //--------------自己写的
        search_et_input.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mListView.setVisibility(View.GONE);
                tvCacle.setVisibility(View.VISIBLE);

                return false;
            }
        });
        search_et_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                switch (i) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        final String uid = AppConfig.getAppConfig(AppManager.mContext)
                                .get(AppConfig.PREF_KEY_USER_UID);
                        String token = AppConfig.getAppConfig(AppManager.mContext)
                                .get(AppConfig.PREF_KEY_TOKEN);

                        String department_id = AppConfig.getAppConfig(AppManager.mContext)
                                .get(AppConfig.PREF_KEY_DEPARTMENT_ID);
                        HttpParams params = new HttpParams();
                        params.put("uid", uid);
                        params.put("token", token);
                        params.put("department_id", department_id);
                        params.put("search_name", search_et_input.getText().toString());

                        initKjHttp().post(Api.REPORT_CCLIST, params, new HttpCallBack() {
                            @Override
                            public void onFinish() {
                                super.onFinish();
                                hideLoadingView();
                            }

                            @Override
                            public void onSuccess(String t) {
                                super.onSuccess(t);
                                System.out.println(t);
                                LogUtils.e("搜索抄送人返回数据-》" + t);
                                searchGroup.clear();
                                try {
                                    JSONObject jo = new JSONObject(t);
                                    switch (Api.getCode(jo)) {
                                        case Api.RESPONSES_CODE_OK:
                                            list = new ArrayList<>();
                                            JSONObject data = Api.getDataToJSONObject(jo);
                                            JSONArray oname = data.getJSONArray("oname");
                                            JSONArray users = data.getJSONArray("users");
                                            for (int i = 0; i < oname.length(); i++) {
                                                Group group = new Group(String.valueOf(i), oname.getString(i));

                                                JSONArray usersArray = users.getJSONArray(i);
                                                for (int j = 0; j < usersArray.length(); j++) {
                                                    JSONObject joChild = usersArray.getJSONObject(j);
                                                    //在这块做判断处理
                                                    boolean isChecked = false;
                                                    if (selectedContacts.size() > 0) {
                                                        LogUtils.e("selectedContacts.size() > 0->" + selectedContacts.size());
                                                        for (int k = 0; k < selectedContacts.size(); k++) {
                                                            String uid = selectedContacts.get(k).getUid();

                                                            if (joChild.getString("uid").equals(uid)) {
                                                                isChecked = true;
                                                                break;
                                                            }
                                                        }
                                                    }

                                                    Child child = new Child(
                                                            joChild.getString("oname"),
                                                            joChild.getString("portrait"),
                                                            joChild.getString("post"),
                                                            joChild.getString("uid"),
                                                            joChild.getString("username"),
                                                            "", "", "", isChecked);
                                                    LogUtils.e("selectedContacts.size() < 0->" + child.toString());

                                                    group.addChildrenItem(child);
                                                }
                                                searchGroup.add(group);
                                            }
                                            searchForElv(searchGroup);
                                            listViewsearch.setVisibility(View.VISIBLE);
                                            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                                                    .hideSoftInputFromWindow(SelectContactsActivity.this
                                                            .getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                            break;
                                        case Api.RESPONSES_CODE_DATA_EMPTY:
                                            mToolBarText.setVisibility(View.GONE);

                                            showEmptyView(mRootView, "暂无可抄送对象，请联系管理员进行设置", 0, false);
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
                            public void onFailure(int errorNo, String strMsg) {
                                LogUtils.e(errorNo + "--" + strMsg);
                                catchWarningByCode(errorNo);
                                super.onFailure(errorNo, strMsg);
                            }
                        });

                        break;
                }
                return true;
            }
        });
    }

    @OnClick({R.id.search_et_cancle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_et_cancle:
                mListView.setVisibility(View.VISIBLE);
                tvCacle.setVisibility(View.GONE);
                search_et_input.setText("");
                listViewsearch.setVisibility(View.GONE);
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(SelectContactsActivity.this
                                .getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                break;
        }

    }

    private void initWeiget() {
        mAdapter = new SelectCopierAdapter(this, groups);
        mListView.setAdapter(mAdapter);
        //mListView.setOnChildClickListener(mAdapter);
        mListView.setOnChildClickListener(new GetSelectedEmployee());

        //selectedContacts = getIntent().getParcelableArrayListExtra("selectedContacts");
        selectedContacts = new ArrayList<>();


    }

    //搜索配置
    private void searchForElv(ArrayList<Group> searchGroup) {
        mAdapter = new SelectCopierAdapter(this, searchGroup);
        listViewsearch.setAdapter(mAdapter);
        listViewsearch.setOnChildClickListener(new GetSelectedSearchEmployee());

    }

    class GetSelectedSearchEmployee implements ExpandableListView.OnChildClickListener {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                    int childPosition, long id) {
            if (selectedContacts.size() == 0) {
                selectedContacts.add(searchGroup.get(groupPosition).getChildItem(childPosition));
            } else {
                boolean isExist = false;
                for (int i = 0; i < selectedContacts.size(); i++) {
                    if (selectedContacts.get(i).getUid().equals(
                            searchGroup.get(groupPosition).getChildItem(childPosition).getUid())) {
                        isExist = true;
                    }
                }
                if (!isExist) {
                    selectedContacts.add(searchGroup.get(groupPosition).getChildItem(childPosition));
                }
            }

            qty.setText(selectedContacts.size() + "");

            return false;
        }
    }


    class GetSelectedEmployee implements ExpandableListView.OnChildClickListener {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                    int childPosition, long id) {
            if (selectedContacts.size() == 0) {
                selectedContacts.add(groups.get(groupPosition).getChildItem(childPosition));
            } else {
                boolean isExist = false;
                for (int i = 0; i < selectedContacts.size(); i++) {
                    if (selectedContacts.get(i).getUid().equals(
                            groups.get(groupPosition).getChildItem(childPosition).getUid())) {
                        isExist = true;
                    }
                }
                if (!isExist) {
                    selectedContacts.add(groups.get(groupPosition).getChildItem(childPosition));
                }
            }

            qty.setText(selectedContacts.size() + "");
            return false;
        }
    }


    private void loadData() {

        showLoadingView("正在获取联系人列表");
        HttpParams params = new HttpParams();
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        params.put("department_id", AppConfig.getAppConfig(this).getDepartmentId());
        params.put("search_name", "");
        initKjHttp().post(Api.PUBLIC_COPY_CONTACTS, params, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e("loadData-->" + t);
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (Api.getCode(jo)) {
                        case Api.RESPONSES_CODE_OK:
                            list = new ArrayList<>();
                            JSONObject data = Api.getDataToJSONObject(jo);
                            JSONArray oname = data.getJSONArray("oname");
                            JSONArray users = data.getJSONArray("users");
                            for (int i = 0; i < oname.length(); i++) {
                                Group group = new Group(String.valueOf(i), oname.getString(i));

                                JSONArray usersArray = users.getJSONArray(i);
                                for (int j = 0; j < usersArray.length(); j++) {
                                    JSONObject joChild = usersArray.getJSONObject(j);
                                    //在这块做判断处理
                                    boolean isChecked = false;
                                    if (selectedContacts.size() > 0) {
                                        LogUtils.e("selectedContacts.size() > 0->" + selectedContacts.size());
                                        for (int k = 0; k < selectedContacts.size(); k++) {
                                            String uid = selectedContacts.get(k).getUid();

                                            if (joChild.getString("uid").equals(uid)) {
                                                isChecked = true;
                                                break;
                                            }
                                        }
                                    }

                                    Child child = new Child(joChild.getString("oname"),
                                            joChild.getString("portrait"),
                                            joChild.getString("post"),
                                            joChild.getString("uid"),
                                            joChild.getString("username"), "", "", "",
                                            isChecked);
                                    LogUtils.e("selectedContacts.size() < 0->" + child.toString());

                                    group.addChildrenItem(child);
                                }
                                groups.add(group);
                            }
                            mAdapter.notifyDataSetChanged();
                            break;
                        case Api.RESPONSES_CODE_DATA_EMPTY:
                            mToolBarText.setVisibility(View.GONE);

                            showEmptyView(mRootView, "暂无可抄送对象，请联系管理员进行设置", 0, false);
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
                showEmptyView(mRootView, "暂无可抄送对象，请联系管理员进行设置", 0, false);
                catchWarningByCode(errorNo);
            }
        });
    }

    private void initToolBar() {
        if (mToolBar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        mToolBar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(mToolBar);
        mTvTitle.setText("选择抄送人");
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
        mToolBarText.setText("下一步");
        mToolBarText.setVisibility(View.VISIBLE);
        mToolBarText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ArrayList<Child> selectedContacts = getSelectedContacts();

                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("contacts", selectedContacts);
                setResult(WorkReportLaunchActivity.REQUEST_CODE_MULTIPLE,
                        intent);
                finish();
            }
        });
    }

    public ArrayList<Child> getSelectedContacts() {
        ArrayList<Child> childList = new ArrayList<>();
        ArrayList<Group> groups = mAdapter.getGroups();
        for (int i = 0; i < groups.size(); i++) {
            Group group = groups.get(i);
            ArrayList<Child> childs = group.getChildren();
            for (int j = 0; j < childs.size(); j++) {
                Child child = childs.get(j);
                boolean checked = child.getChecked();
                if (checked) {
                    childList.add(child);
                }

            }

        }
        return childList;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
    /**
     * 自己实现 Handler 处理消息更新UI
     *
     * @author mark
     */
    public final class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            if (msg.what == CHANGED) { // 更新UI
//                mHandler.get
            qty.setText(msg.what+"");
//            }
        }
    }
}