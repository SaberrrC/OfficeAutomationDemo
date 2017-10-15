//package com.itcrm.GroupInformationPlatform.ui.activity;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.view.Gravity;
//import android.view.View;
//import android.widget.CheckBox;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.chad.library.adapter.base.listener.OnItemClickListener;
//import com.itcrm.GroupInformationPlatform.R;
//import com.itcrm.GroupInformationPlatform.common.Api;
//import com.itcrm.GroupInformationPlatform.manager.AppConfig;
//import com.itcrm.GroupInformationPlatform.model.JoinPeople;
//import com.itcrm.GroupInformationPlatform.ui.adapter.SelectJoinPeopleAdapter;
//import com.itcrm.GroupInformationPlatform.ui.base.BaseActivity;
//import com.itcrm.GroupInformationPlatform.utils.LogUtils;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.kymjs.kjframe.http.HttpCallBack;
//import org.kymjs.kjframe.http.HttpParams;
//
//import java.util.ArrayList;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//
///**
// * ProjectName: GroupInformationPlatform
// * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
// * Author:Created by CXP on Date: 2016/9/6 11:21.
// * Description: 选择参会人activity
// */
//public class SelectJoinPeopleActivity extends BaseActivity {
//    @Bind(R.id.layout_content)
//    RelativeLayout mRootView;
//    @Bind(R.id.toolbar)
//    Toolbar mToolBar;
//    @Bind(R.id.tv_title)
//    TextView mTvTitle;
//    @Bind(R.id.select_recyclerView)
//    RecyclerView mRecyclerVeiw;
//    @Bind(R.id.toolbar_text_btn)
//    TextView mToolBarText;
//    @Bind(R.id.tv_department)
//    TextView mTvDepartment;
//    ArrayList<JoinPeople> list;
//    private SelectJoinPeopleAdapter mAdapter;
//    private ArrayList<JoinPeople> selectedContacts;//已经选择的联系人
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_select_join_people);
//        ButterKnife.bind(this);
//        getContactType();
//        initToolBar();
//        setTranslucentStatus(this);
//        initWeiget();
//        loadData();
//    }
//
//    private void initWeiget() {
//        mTvDepartment.setText(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_COMPANY_NAME));
//    }
//
//    /**
//     * 获得显示联系人类型的参数
//     */
//    private void getContactType() {
//        Intent intent = getIntent();
//        selectedContacts = new ArrayList<>();
//        ArrayList<JoinPeople> selectedContact = intent.getParcelableArrayListExtra("selectedContacts");
//        this.selectedContacts.addAll(selectedContact);
//        LogUtils.e("从创建会议过来的联系人-》" + selectedContact.toString());
//    }
//
//    private void loadData() {
//        showLoadingView("正在获取联系人列表");
//        HttpParams params = new HttpParams();
//        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
//        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
//        initKjHttp().post(Api.CONFERENCE_CCLIST, params, new HttpCallBack() {
//
//            @Override
//            public void onPreStart() {
//                super.onPreStart();
//            }
//
//            @Override
//            public void onSuccess(String t) {
//                super.onSuccess(t);
//                LogUtils.e(t);
//                //{"department_id":"1","department_name":"智汇时代",
//                // "portraits":"http://public.zhihui.s1.zhitongoa.com/1/Cut_image_14798821967691479882196.jpg",
//                // "uid":"3","username":"皇上","sex":"男","post":"权限都有"}
//                try {
//                    JSONObject jo = new JSONObject(t);
//                    switch (Api.getCode(jo)) {
//                        case Api.RESPONSES_CODE_OK:
//                            list = new ArrayList<>();
//                            JSONArray contacts = Api.getDataToJSONArray(jo);
//                            for (int i = 0; i < contacts.length(); i++) {
//                                JSONObject jsonObject = contacts.getJSONObject(i);
//                                //String portraits, String username, String uid,
//                                // String department_id, String department_name
//                                //     ,String sex
//                                JoinPeople contact =
//                                        new JoinPeople(jsonObject.getString("portraits"),
//                                                jsonObject.getString("username"),
//                                                jsonObject.getString("uid"), jsonObject
//                                                .getString("department_id"), jsonObject
//                                                .getString("department_name"),
//                                                jsonObject.getString("sex"),
//                                                jsonObject.getString("post")
//                                        );
//                                list.add(contact);
//                            }
//
//                            setDataForRecyclerView();
//                            break;
//                        case Api.RESPONSES_CODE_DATA_EMPTY:
//                            mToolBarText.setVisibility(View.GONE);
//
//                            break;
//                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
//                            catchWarningByCode(Api.getCode(jo));
//                            break;
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//                hideLoadingView();
//            }
//
//            @Override
//            public void onFailure(int errorNo, String strMsg) {
//                super.onFailure(errorNo, strMsg);
//                catchWarningByCode(errorNo);
//            }
//        });
//    }
//
//    private void setDataForRecyclerView() {
//        mRecyclerVeiw.setLayoutManager(new LinearLayoutManager(this));
//        mAdapter = new SelectJoinPeopleAdapter(list, selectedContacts);
//        mAdapter.setSelectedJoinPeopleInter(new SelectJoinPeopleAdapter.InterSelectedJoinPeople() {
//            @Override
//            public void addJoinPeople(String uId, String userName) {
//                JoinPeople joinPeople = new JoinPeople(userName, uId, true);
//                boolean isHad = false;
//                for (int i = 0; i < selectedContacts.size(); i++) {
//                    if (uId.equals(selectedContacts.get(i).uid)) {
//
//                        isHad = true;
//                        break;
//                    }
//                }
//                if (!isHad) {
//                    //当前集合没有此联系人，不做处理
//                    selectedContacts.add(joinPeople);
//                }
//            }
//
//            @Override
//            public void removeJoinPeople(String uId, String userName) {
//                for (int i = 0; i < selectedContacts.size(); i++) {
//                    JoinPeople joinPeople = selectedContacts.get(i);
//                    if (uId.equals(joinPeople.uid)) {
//                        selectedContacts.remove(i);
//                    }
//                }
//
//            }
//        });
//        mRecyclerVeiw.setAdapter(mAdapter);
//
//        mRecyclerVeiw.addOnItemTouchListener(new OnItemClickListener() {
//            @Override
//            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view,
//                                          int position) {
//                CheckBox cb = (CheckBox) view.findViewById(R.id.select_contact_icon);
//                cb.setChecked(!cb.isChecked());
//            }
//        });
//    }
//
//    /**
//     * 获取用户选择的联系人
//     */
//    private void executeMultiOperate() {
//       /* ArrayList<JoinPeople> glightContacts = new ArrayList<>();
//        for (int i = 0; i < mRecyclerVeiw.getChildCount(); i++) {
//            RelativeLayout itemView = (RelativeLayout) mRecyclerVeiw.getChildAt(i);
////            AppCompatCheckBox cb = (AppCompatCheckBox) itemView.getChildAt(3);
//            CheckBox cb= (CheckBox) itemView.findViewById(R.id.select_contact_icon);
//            if (cb.isChecked()) {
//               LogUtils.e ("选择参会人界面：被选中的"+((JoinPeople)list.get(i)).uid);
//                JoinPeople contact = new JoinPeople(list.get(i).username, list.get(i).uid,true);
//                glightContacts.add(contact);
//            }
//        }
//        LogUtils.e("获取用户选择的联系人->"+glightContacts.toString());*/
//        Intent intent = new Intent();
//        intent.putParcelableArrayListExtra("contacts", selectedContacts);
//        SelectJoinPeopleActivity.this.setResult(WorkReportLaunchActivity.REQUEST_CODE_MULTIPLE,
//                intent);
//        SelectJoinPeopleActivity.this.finish();
//
//    }
//
//
//    private void initToolBar() {
//        if (mToolBar == null) {
//            return;
//        }
//        setTitle("");//必须在setSupportActionBar之前调用
//        mToolBar.setTitleTextColor(Color.parseColor("#000000"));
//        setSupportActionBar(mToolBar);
//        mTvTitle.setText(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_COMPANY_NAME));
//        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
//                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
//        lp.gravity = Gravity.CENTER_HORIZONTAL;
//        mTvTitle.setLayoutParams(lp);
//
//        mToolBar.setNavigationIcon(R.drawable.toolbar_back);
//        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//        mToolBarText.setVisibility(View.VISIBLE);
//        mToolBarText.setText("确定");
//        mToolBarText.setVisibility(View.VISIBLE);
//        mToolBarText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                executeMultiOperate();
//            }
//        });
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        ButterKnife.unbind(this);
//    }
//}
package com.shanlinjinrong.oa.ui.activity.home.schedule;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.model.GlightContact;
import com.shanlinjinrong.oa.model.selectContacts.Child;
import com.shanlinjinrong.oa.model.selectContacts.Group;
import com.shanlinjinrong.oa.ui.activity.home.schedule.adapter.SelectJoinPeopleAdapter;
import com.shanlinjinrong.oa.ui.base.BaseActivity;
import com.shanlinjinrong.oa.ui.fragment.MyJoinPeopleFragment;
import com.shanlinjinrong.oa.utils.LogUtils;
import com.shanlinjinrong.oa.views.ClearEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * ProjectName: GroupInformationPlatform
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by CXP on Date: 2016/9/6 11:21.
 * Description: 选择参会人activity
 */
public class SelectJoinPeopleActivity extends BaseActivity {
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
    private SelectJoinPeopleAdapter mAdapter;
    private boolean isFirstLoad = true;//刚进入页面，第一次加载数据
    //----------自己写的
    @Bind(R.id.search_et_input)
    ClearEditText search_et_input;
    @Bind(R.id.search_et_cancle)
    TextView tvCacle;
    @Bind(R.id.cC_num)
    TextView qty;
    protected BottomSheetLayout bottomSheetLayout;
    //    //----------------------
    private AppManager appManager = null;
    private MyJoinHandler mJoinHandler = null;
    //    //----------------------

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contacts);
        ButterKnife.bind(this);
        initToolBar();
        setTranslucentStatus(this);
        initWeiget();
        loadData();
        search();
        //        //------------------------------------
        appManager = (AppManager) getApplication();
        // 获得该共享变量实例
        mJoinHandler = appManager.getJoinhandler();
        appManager = (AppManager) getApplication();
        mJoinHandler = new MyJoinHandler();
        //        //--------------------------
        //底部弹出框
        bottomSheetLayout = (BottomSheetLayout) findViewById(R.id.bottomsheet);
        findViewById(R.id.rel_choose_people_num).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedContacts.size() > 0) {
                    //-------------
                    appManager.setJoinhandler(mJoinHandler);
                    //--------------
                    new MyJoinPeopleFragment(selectedContacts).show(getSupportFragmentManager(), R.id.bottomsheet);
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
        //自动检索参会人
        autoSearch();
    }

    private void autoSearch() {
        //EditText 自动搜索,间隔->输入停止1秒后自动搜索
        RxTextView.textChanges(search_et_input)
                .debounce(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        if (isFirstLoad) {
                            isFirstLoad = false;
                            return;
                        }
                        loadData();
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
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(SelectJoinPeopleActivity.this
                                .getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                break;
        }

    }

    private void initWeiget() {
        mAdapter = new SelectJoinPeopleAdapter(this, groups);
        mListView.setAdapter(mAdapter);
        //mListView.setOnChildClickListener(mAdapter);
        mListView.setOnChildClickListener(new GetSelectedEmployee());

        //selectedContacts = getIntent().getParcelableArrayListExtra("selectedContacts");
        selectedContacts = new ArrayList<>();


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
                        Toast.makeText(SelectJoinPeopleActivity.this, R.string.selectJoinPeopleHint, Toast.LENGTH_SHORT).show();
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

    //底部弹出框的点击事件


    private void loadData() {
        if (isFirstLoad) {
            showLoadingView("正在获取联系人列表");
        }
        HttpParams params = new HttpParams();
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        params.put("search_name", search_et_input.getText().toString().trim());
        initKjHttp().post(Api.CONFERENCE_CCLIST, params, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e("loadData-->" + t);
                groups.clear();
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
                                            joChild.getString("portraits"),
                                            joChild.getString("post"),
                                            joChild.getString("uid"),
                                            joChild.getString("username"),
                                            joChild.getString("sex"),
                                            joChild.getString("department_id"),
                                            joChild.getString("CODE"), isChecked);

                                    LogUtils.e("selectedContacts.size() < 0->" + child.toString());

                                    group.addChildrenItem(child);
                                }
                                groups.add(group);
                            }
                            hideEmptyView();
                            mListView.setVisibility(View.VISIBLE);
                            mAdapter.notifyDataSetChanged();
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
                    LogUtils.e(e.toString());
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
        mTvTitle.setText("选择参会人");
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
                setResult(RESULT_OK, intent);
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
    public final class MyJoinHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            qty.setText(msg.what + "");
        }
    }
}