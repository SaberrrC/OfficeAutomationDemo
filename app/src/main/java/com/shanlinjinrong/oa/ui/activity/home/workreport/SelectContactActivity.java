package com.shanlinjinrong.oa.ui.activity.home.workreport;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.model.selectContacts.Child;
import com.shanlinjinrong.oa.model.selectContacts.Group;
import com.shanlinjinrong.oa.ui.activity.home.workreport.adapter.ContactAdapter;
import com.shanlinjinrong.oa.ui.activity.home.workreport.adapter.RequestContactAdapter;
import com.shanlinjinrong.oa.ui.activity.home.workreport.contract.SelectContactActivityContract;
import com.shanlinjinrong.oa.ui.activity.home.workreport.presenter.SelectContactActivityPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.views.ClearEditText;
import com.shanlinjinrong.views.common.CommonTopView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * create by lvdinghao 2017/8/23
 * 申请用品：选择审批人界面
 * 发起日报：选择接收人界面
 */
public class SelectContactActivity extends HttpBaseActivity<SelectContactActivityPresenter> implements SwipeRefreshLayout.OnRefreshListener, SelectContactActivityContract.View, OnSelectedContract {

    @BindView(R.id.layout_root)
    LinearLayout mRootView;

    @BindView(R.id.top_view)
    CommonTopView mTopView;

    @BindView(R.id.search_tool)
    RelativeLayout mSearchTool;//搜索栏

    @BindView(R.id.et_search)
    ClearEditText mSearchEdit;//搜索框

    @BindView(R.id.contact_list)
    ExpandableListView mContactList;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.tv_content_empty)
    TextView mContentEmpty;

    @BindView(R.id.contact_recycler_view)
    RecyclerView mContactRecyclerView;

    private ArrayList<Group> groups = new ArrayList<>();//联系人群组
    private List<Child> mChilds;

    private Child mSelectChild;//已选择

    private ContactAdapter mAdapter;
    private RequestContactAdapter mRequestAdapter;
    private String mSelectChildId;
    private List<Child> mChildren;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);
        ButterKnife.bind(this);
        setTranslucentStatus(this);
        initView();
        loadData("");
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }


    private void initView() {
        mTopView.setAppTitle("选择接收人");
        if (!getIntent().getBooleanExtra("isRequest", false)) {
            mTopView.setRightText("确定");
        }
        mTopView.setRightAction(v -> {
            if (mSelectChild == null) {
                Toast.makeText(SelectContactActivity.this, "未选择接收人", Toast.LENGTH_SHORT).show();
                return;
            }
            setFinishResult();
        });


        mContactList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                mSelectChild = groups.get(groupPosition).getChildItem(childPosition);
                handleClick(childPosition, groupPosition);
                return false;
            }
        });


        mSearchEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    autoSearch();
                }
            }
        });

        mSelectChildId = getIntent().getStringExtra("childId");
//        if (!TextUtils.isEmpty(mSelectChildId)) {
//            mSelectChild = new Child()
//        }

        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#0EA7ED"),
                Color.parseColor("#0EA7ED"), Color.parseColor("#0EA7ED"));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setEnabled(true);

        mContactList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    mSwipeRefreshLayout.setEnabled(true);
                } else {
                    mSwipeRefreshLayout.setEnabled(false);
                }
            }
        });
    }

    public void handleClick(int childPosition, int groupPosition) {
        int childrenCount = groups.get(groupPosition).getChildrenCount();
        Child currentChild = groups.get(groupPosition).getChildItem(childPosition);
        for (int i = 0; i < childrenCount; i++) {
            if (i == childPosition) {
                if (currentChild.getChecked())
                    mSelectChild = null;
                currentChild.setChecked(!currentChild.getChecked());
            } else {
                //清除其他的checkbox的状态
                groups.get(groupPosition).getChildItem(i).setChecked(false);
            }

        }
        mAdapter.notifyDataSetChanged();
    }

    private void setFinishResult() {
        Intent intent = new Intent();
        if (getIntent().getBooleanExtra("", false)) {
            intent.putExtra("nextReceiver", getIntent().getIntExtra("nextReceiver", 0));
            if (!TextUtils.isEmpty(mSelectChild.getUid())) {
                intent.putExtra("uid", mSelectChild.getUid());
            }
            intent.putExtra("name", mSelectChild.getUsername());
        } else {
            intent.putExtra("nextReceiver", getIntent().getIntExtra("nextReceiver", 0));
            if (!TextUtils.isEmpty(mSelectChild.getUid())) {
                intent.putExtra("uid", mSelectChild.getUid());
            }
            intent.putExtra("name", mSelectChild.getUsername());
            intent.putExtra("post", mSelectChild.getPost());
        }
        setResult(RESULT_OK, intent);
        finish();
    }


    //搜索事件
    private void autoSearch() {
        //EditText 自动搜索,间隔->输入停止1秒后自动搜索
        try {
            RxTextView.textChanges(mSearchEdit)
                    .debounce(1000, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<CharSequence>() {
                        @Override
                        public void accept(CharSequence charSequence) throws Exception {
                            if(mSearchEdit.getText().toString().trim().equals("")){
                                loadData(mSearchEdit.getText().toString().trim());
                            }else {
                                mPresenter.loadData(mSearchEdit.getText().toString().trim());
                            }
                        }
                    });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void uidNull(String code) {
        catchWarningByCode(code);
    }


    @Override
    public void loadDataSuccess(ArrayList<Group> groups, Child selectChild) {
        hideEmptyView();
        mContactList.setVisibility(View.VISIBLE);
        mContentEmpty.setVisibility(View.GONE);
        this.groups = groups;
        mSelectChild = selectChild;
        mAdapter = new ContactAdapter(this, groups);
        mContactList.setAdapter(mAdapter);
        mContactList.expandGroup(0);
    }

    @Override
    public void loadDataFailed(int errCode, String errMsg) {
        try {
            if (errMsg.equals("auth error")) {
                catchWarningByCode(ApiJava.REQUEST_TOKEN_NOT_EXIST);
                return;
            }
            hideLoadingView();
            mContentEmpty.setVisibility(View.VISIBLE);
            mContentEmpty.setText("没有搜索到该员工，请重新搜索");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadRequestDataSuccess(List<Child> child, Child selectChild) {
        try {
            if (child != null)
                mChilds = child;
            hideEmptyView();
            hideLoadingView();
            mSwipeRefreshLayout.setRefreshing(false);
            mContactList.setVisibility(View.VISIBLE);
            mContentEmpty.setVisibility(View.GONE);
            mContactRecyclerView.setVisibility(View.VISIBLE);
            mContactList.setVisibility(View.GONE);
            mSelectChild = selectChild;
            mRequestAdapter = new RequestContactAdapter(child, this);
            mContactRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            mContactRecyclerView.setAdapter(mRequestAdapter);
            mContactList.deferNotifyDataSetChanged();
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    @Override
    public void loadRequestDataFailed(int errCode, String errMsg) {
        hideLoadingView();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void loadDataFinish() {
        hideLoadingView();
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void loadDataEmpty() {
        mContactList.setVisibility(View.GONE);
        mContentEmpty.setVisibility(View.VISIBLE);
        hideLoadingView();
        mContentEmpty.setText("没有搜索到该员工，请重新搜索");
    }

    private void loadData(String name) {
        if (!mSwipeRefreshLayout.isRefreshing()) {
            showLoadingView("正在获取联系人列表");
        }
        mContentEmpty.setVisibility(View.GONE);
        String department_id = AppConfig.getAppConfig(AppManager.mContext)
                .get(AppConfig.PREF_KEY_DEPARTMENT_ID);
        if (getIntent().getBooleanExtra("isRequest", false)) {
            if (mChilds != null && !name.trim().equals("")) {
                mChildren = new ArrayList<>();
                for (int i = 0; i < mChilds.size(); i++) {
                    if (mChilds.get(i).getUsername().contains(name)) {
                        mChildren.add(mChilds.get(i));
                    }
                }
                hideLoadingView();
                mRequestAdapter.setNewData(mChildren);
                mRequestAdapter.notifyDataSetChanged();
                return;
            }
            mPresenter.loadRequestData();
            return;
        }
        mPresenter.loadData(department_id, name, mSelectChildId);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        if (getIntent().getBooleanExtra("isRequest", false)) {
            mPresenter.loadRequestData();
            return;

        }
        loadData(mSearchEdit.getText().toString().trim());
    }

    @Override
    public void onClick(List<Child> mdata, int position) {
        try {
            if (!mSearchEdit.getText().toString().trim().equals("") && mChildren != null) {
                mSelectChild = mChildren.get(position);
                mRequestAdapter.setNewData(mChildren);
                mRequestAdapter.notifyDataSetChanged();
                setFinishResult();
                return;
            }
            mSelectChild = mdata.get(position);
            mRequestAdapter.setNewData(mdata);
            mRequestAdapter.notifyDataSetChanged();
            setFinishResult();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}