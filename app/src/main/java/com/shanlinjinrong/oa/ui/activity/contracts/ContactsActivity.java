package com.shanlinjinrong.oa.ui.activity.contracts;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.model.Contacts;
import com.shanlinjinrong.oa.model.User;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.ui.fragment.adapter.TabContactsAdapter;
import com.shanlinjinrong.oa.ui.fragment.contract.TabContractsFragmentContract;
import com.shanlinjinrong.oa.ui.fragment.presenter.TabContractsFragmentPresenter;
import com.shanlinjinrong.oa.utils.DateUtils;
import com.shanlinjinrong.oa.utils.SharedPreferenceUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <h3>Description: 首页名片页面二级一下界面 </h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/9/22.<br />
 */
public class ContactsActivity extends HttpBaseActivity<TabContractsFragmentPresenter> implements TabContractsFragmentContract.View {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.layout_root)
    RelativeLayout mRootView;

    @BindView(R.id.rl_top2)
    RelativeLayout mRLSearchView;

    private List<Contacts> items = new ArrayList<>();
    private List<Map<String, String>> pageMap;
    private TabContactsAdapter adapter;
    private RelativeLayout view;
    @BindView(R.id.rl_recycler_view_container)
    RelativeLayout mRlRecyclerViewContainer;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;


    /**
     * 页面加载数据所需部门ID
     */
    public static final String PAGE_MAP_DID = "pageMapDid";
    /**
     * 页面标题
     */
    public static final String PAGE_MAP_TITLE = "pageMapTitle";
    private String phoneStr;
    private PopupWindow popupWindow;
    private List<Contacts> mContacts;

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = (RelativeLayout) LayoutInflater.from(this).inflate(
                R.layout.tab_contacts_fragment, null);
        setContentView(view);
        ButterKnife.bind(this);
        setTranslucentStatus(this);
        initWidget();
        loadData(getIntent().getStringExtra(PAGE_MAP_DID));
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private void initWidget() {
        mSwipeRefreshLayout.setEnabled(false);
        mRLSearchView.setVisibility(View.GONE);
        btnBack.setVisibility(View.VISIBLE);
        pageMap = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put(PAGE_MAP_DID, getIntent().getStringExtra(PAGE_MAP_DID));
        map.put(PAGE_MAP_TITLE, getIntent().getStringExtra(PAGE_MAP_TITLE));
        pageMap.add(map);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TabContactsAdapter(items);
        recyclerView.setAdapter(adapter);
        //在初始化是为RecyclerView添加点击时间，这样可以防止重复点击问题
        recyclerView.addOnItemTouchListener(new OnItemClick());
    }

    /**
     * 加载通讯录数据
     *
     * @param departmentId 部门ID
     */
    @SuppressWarnings("SpellCheckingInspection")
    private void loadData(String departmentId) {
        title.setText(pageMap.get(pageMap.size() - 1).get(PAGE_MAP_TITLE));
        try {
            if (!SharedPreferenceUtils.getStringValue(AppManager.mContext, DateUtils.getCurrentDate("yyyy-MM-dd"), "users" + departmentId, "").equals("")) {
                String users = SharedPreferenceUtils.getStringValue(AppManager.mContext, DateUtils.getCurrentDate("yyyy-MM-dd"), "users" + departmentId, "");
                JSONArray user = new JSONArray(users);
                mContacts = new ArrayList<>();
                for (int i = 0; i < user.length(); i++) {
                    JSONObject userInfo = user.getJSONObject(i);
                    Contacts person = new Contacts(userInfo);
                    mContacts.add(person);
                }
                String childrens = SharedPreferenceUtils.getStringValue(AppManager.mContext, DateUtils.getCurrentDate("yyyy-MM-dd"), "children" + departmentId, "");
                if (!childrens.equals("")) {
                    JSONArray children = new JSONArray(childrens);
                    for (int i = children.length() - 1; i >= 0; i--) {
                        JSONObject department = children.getJSONObject(i);
                        String number = department.getString("memberCount");
                        if (number.equals("0")) {
                            continue;
                        }
                        Contacts contact = new Contacts(department);
                        mContacts.add(0, contact);
                    }
                }
                if (items.size() > 0 || items != null) {
                    items.clear();
                }
                items = mContacts;
                adapter.setNewData(items);
                adapter.notifyDataSetChanged();
                return;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        showLoadingView();
        mPresenter.loadData(departmentId);
    }

    @Override
    public void uidNull(int code) {
        catchWarningByCode(code);
    }


    class OnItemClick extends OnItemClickListener {
        @Override
        public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            switch (items.get(i).getItemType()) {
                case Contacts.DEPARTMENT:
                    if (!pageMap.get(pageMap.size() - 1).get(PAGE_MAP_DID).equals(
                            items.get(i).getDepartmentId())) {
                        pageMap.add(getPageParam(items.get(i).getDepartmentId(),
                                items.get(i).getDepartmentName()));
                    }
                    loadData(items.get(i).getDepartmentId());
                    break;
                case Contacts.EMPLOYEE:
                    showContactsInfo(items.get(i));
                    break;
            }
        }
    }

    /**
     * 获取页面参数
     *
     * @param id    部门ID
     * @param title 页面标题（使用部门名称）
     * @return param
     */
    private Map<String, String> getPageParam(String id, String title) {
        Map<String, String> param = new HashMap<>();
        param.put(PAGE_MAP_DID, id);
        param.put(PAGE_MAP_TITLE, title);
        return param;
    }

    @SuppressLint("SetTextI18n")
    private void showContactsInfo(final Contacts contacts) {
        Intent intent = new Intent(ContactsActivity.this, Contact_Details_Activity2.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("contacts", contacts);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @OnClick({R.id.btn_back, R.id.title})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
            case R.id.title:
                back();
                break;
        }
    }

    @Override
    public void loadDataSuccess(List<Contacts> contacts) {
        if (items.size() > 0 || items != null) {
            items.clear();
        }
        items = contacts;
        adapter.setNewData(items);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void loadDataFailed(int errorNo, String strMsg) {
        String info = "";
        switch (errorNo) {
            case Api.RESPONSES_CODE_NO_NETWORK:
                info = "请确认是否已连接网络！";
                break;
            case Api.RESPONSES_CODE_NO_RESPONSE:
                info = "网络不稳定，请重试！";
                break;
        }
        showEmptyView(mRlRecyclerViewContainer, info, 0, false);
    }

    @Override
    public void loadDataEmpty() {
        showEmptyView(mRlRecyclerViewContainer, "您还没有联系人", 0, false);
    }

    @Override
    public void loadDataFinish() {
        hideLoadingView();
    }

    @Override
    public void loadDataTokenNoMatch(int code) {
        catchWarningByCode(code);
    }

    @Override
    public void autoSearchSuccess(List<User> users) {

    }

    @Override
    public void autoSearchFailed(int errCode, String errMsg) {

    }

    @Override
    public void autoSearchOther(String msg) {

    }

    @Override
    public void autoSearchFinish() {

    }

    @Override
    public void loadDataStart() {

    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        if (pageMap.size() == 1) {
            finish();
        } else {
            pageMap.remove(pageMap.size() - 1);
            loadData(pageMap.get(pageMap.size() - 1).get(PAGE_MAP_DID));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (popupWindow != null) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }
}
