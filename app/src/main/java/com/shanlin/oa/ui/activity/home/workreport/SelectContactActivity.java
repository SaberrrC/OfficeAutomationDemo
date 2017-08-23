
package com.shanlin.oa.ui.activity.home.workreport;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.shanlin.common.CommonTopView;
import com.shanlin.oa.R;
import com.shanlin.oa.manager.AppConfig;
import com.shanlin.oa.manager.AppManager;
import com.shanlin.oa.model.selectContacts.Child;
import com.shanlin.oa.model.selectContacts.Group;
import com.shanlin.oa.ui.activity.home.workreport.contract.SelectContactActivityContract;
import com.shanlin.oa.ui.activity.home.workreport.presenter.SelectContactActivityPresenter;
import com.shanlin.oa.ui.adapter.SelectCopierAdapter;
import com.shanlin.oa.ui.base.MyBaseActivity;
import com.shanlin.oa.views.ClearEditText;

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
public class SelectContactActivity extends MyBaseActivity<SelectContactActivityPresenter> implements SelectContactActivityContract.View {

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

    ArrayList<Group> groups = new ArrayList<>();//联系人群组

    private Child mSelectChild;//已选择

    private SelectCopierAdapter mAdapter;
    private boolean isFirstLoad = true;


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

        mContactList.setOnChildClickListener(new GetSelectedContact());

        mSearchEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    autoSearch();
                }
            }
        });
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
    public void loadDataSuccess(ArrayList<Group> groups) {
        hideEmptyView();
        this.groups = groups;
        mAdapter = new SelectCopierAdapter(this, groups);
        mContactList.setAdapter(mAdapter);
    }

    @Override
    public void loadDataFailed(int errCode, String errMsg) {
        showEmptyView(mRootView, "数据暂无，请联系管理员进行设置", 0, false);
        catchWarningByCode(errCode);
    }

    @Override
    public void loadDataFinish() {
        hideLoadingView();
    }

    @Override
    public void loadDataEmpty() {
        showEmptyView(mRootView, "数据暂无，请联系管理员进行设置", 0, false);
    }

    class GetSelectedContact implements ExpandableListView.OnChildClickListener {

        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                    int childPosition, long id) {
            mSelectChild = groups.get(groupPosition).getChildItem(childPosition);
            return false;
        }
    }


    private void loadData(String name) {
        showLoadingView("正在获取联系人列表");
        String department_id = AppConfig.getAppConfig(AppManager.mContext)
                .get(AppConfig.PREF_KEY_DEPARTMENT_ID);
        mPresenter.loadData(department_id, name);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}