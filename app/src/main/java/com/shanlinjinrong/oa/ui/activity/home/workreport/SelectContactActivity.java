
package com.shanlinjinrong.oa.ui.activity.home.workreport;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.shanlinjinrong.views.common.CommonTopView;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.model.selectContacts.Child;
import com.shanlinjinrong.oa.model.selectContacts.Group;
import com.shanlinjinrong.oa.ui.activity.home.workreport.contract.SelectContactActivityContract;
import com.shanlinjinrong.oa.ui.activity.home.workreport.presenter.SelectContactActivityPresenter;
import com.shanlinjinrong.oa.ui.activity.home.workreport.adapter.ContactAdapter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.views.ClearEditText;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * create by lvdinghao 2017/8/23
 * 申请用品：选择审批人界面
 * 发起日报：选择接收人界面
 */
public class SelectContactActivity extends HttpBaseActivity<SelectContactActivityPresenter> implements SwipeRefreshLayout.OnRefreshListener, SelectContactActivityContract.View {

    @Bind(R.id.layout_root)
    LinearLayout mRootView;

    @Bind(R.id.top_view)
    CommonTopView mTopView;

    @Bind(R.id.search_tool)
    RelativeLayout mSearchTool;//搜索栏

    @Bind(R.id.et_search)
    ClearEditText mSearchEdit;//搜索框

    @Bind(R.id.contact_list)
    ExpandableListView mContactList;

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.tv_content_empty)
    TextView mContentEmpty;


    ArrayList<Group> groups = new ArrayList<>();//联系人群组

    private Child mSelectChild;//已选择

    private ContactAdapter mAdapter;
    private String mSelectChildId;


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
        mTopView.setRightText("确定");
        mTopView.setRightAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectChild == null) {
                    Toast.makeText(SelectContactActivity.this, "未选择接收人", Toast.LENGTH_SHORT).show();
                    return;
                }
                setFinishResult();
            }
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
        intent.putExtra("uid", mSelectChild.getUid());
        intent.putExtra("name", mSelectChild.getUsername());
        intent.putExtra("post", mSelectChild.getPost());
        setResult(RESULT_OK, intent);
        finish();
    }


    //搜索事件
    private void autoSearch() {
        //EditText 自动搜索,间隔->输入停止1秒后自动搜索
        RxTextView.textChanges(mSearchEdit)
                .debounce(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        loadData(mSearchEdit.getText().toString().trim());
                    }
                });
    }

    @Override
    public void uidNull(int code) {
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
        mContentEmpty.setVisibility(View.VISIBLE);
        mContentEmpty.setText("没有搜索到该员工，请重新搜索");
//        showEmptyView(mRootView, "数据暂无，请联系管理员进行设置", 0, false);
        catchWarningByCode(errCode);
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
        mContentEmpty.setText("没有搜索到该员工，请重新搜索");
//        showEmptyView(mRootView, "数据暂无，请输入全名搜索", 0, false);
    }

    private void loadData(String name) {
        if (!mSwipeRefreshLayout.isRefreshing()){
            showLoadingView("正在获取联系人列表");
        }
        mContentEmpty.setVisibility(View.GONE);
        String department_id = AppConfig.getAppConfig(AppManager.mContext)
                .get(AppConfig.PREF_KEY_DEPARTMENT_ID);
        mPresenter.loadData(department_id, name, mSelectChildId);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onRefresh() {
        loadData(mSearchEdit.getText().toString().trim());
    }
}