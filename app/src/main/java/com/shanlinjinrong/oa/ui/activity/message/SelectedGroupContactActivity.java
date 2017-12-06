package com.shanlinjinrong.oa.ui.activity.message;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.model.Contacts;
import com.shanlinjinrong.oa.ui.activity.message.Fragment.GroupContactListFragment;
import com.shanlinjinrong.oa.ui.activity.message.Fragment.SelectedGroupContactFragment;
import com.shanlinjinrong.oa.ui.activity.message.adapter.SelectedContactAdapter;
import com.shanlinjinrong.oa.ui.activity.message.bean.DeleteContactEvent;
import com.shanlinjinrong.oa.ui.activity.message.bean.GroupUsers;
import com.shanlinjinrong.oa.ui.activity.message.contract.SelectedGroupContactContract;
import com.shanlinjinrong.oa.ui.activity.message.presenter.SelectedGroupContactPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.views.common.CommonTopView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SelectedGroupContactActivity extends HttpBaseActivity<SelectedGroupContactPresenter> implements SelectedGroupContactContract.View, SelectedGroupContactFragment.onLoadUsersListener, SelectedGroupContactFragment.onSelectedUsersListener {

    @BindView(R.id.top_view)
    CommonTopView mTopView;
    @BindView(R.id.search_et_input)
    EditText mSearchContact;
    @BindView(R.id.tv_selected_contact)
    TextView mTvSelectedContact;
    @BindView(R.id.rv_search_contact)
    RecyclerView mRvSearchContact;
    @BindView(R.id.ll_selected_contact)
    LinearLayout mLlSelectedContact;
    @BindView(R.id.bottom_container_layout)
    BottomSheetLayout bottomContainerLayout;

    private List<Contacts> mGroupUsers;
    private InputMethodManager inputManager;
    private SparseArray<Contacts> mCacheContact;
    private List<Contacts> mSearchData;
    private SelectedContactAdapter mUserAdapter;
    private GroupContactListFragment mBottomFragment;
    private List<SelectedGroupContactFragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_group_contact);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initData();
        initView();
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private void initData() {
        mFragments = new ArrayList<>();
        mGroupUsers = new ArrayList<>();
        mCacheContact = new SparseArray<>();
        mSearchData = new ArrayList<>();
    }

    private void initView() {

        //搜索联系人
        RxTextView.textChanges(mSearchContact)
                .debounce(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(charSequence -> {
                    if (mSearchContact.getText().toString().trim().equals("")) {
                        mTopView.setAppTitle(mFragments.get(0).getArguments().getString("title", "选择成员"));
                        mRvSearchContact.setVisibility(View.GONE);
                        return;
                    }
                    mTopView.setAppTitle("搜索");
                    mPresenter.searchContact(mSearchContact.getText().toString().trim());
                });

        //初始化视图
        SelectedGroupContactFragment fragment = new SelectedGroupContactFragment(mGroupUsers, mCacheContact, this, this);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isBack", false);
        fragment.setArguments(bundle);
        mFragments.add(fragment);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container_layout, fragment).commit();

        mUserAdapter = new SelectedContactAdapter(mSearchData);
        mRvSearchContact.setAdapter(mUserAdapter);
        mRvSearchContact.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRvSearchContact.addOnItemTouchListener(new OnItemClick());
        mUserAdapter.notifyDataSetChanged();

        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @OnClick(R.id.ll_selected_contact)
    public void onSelectedContact() {
        if (mGroupUsers.size() > 0) {
            if (mBottomFragment == null) {
                mBottomFragment = new GroupContactListFragment(mGroupUsers);
            }
            if (bottomContainerLayout.isSheetShowing()) {
                mBottomFragment.dismiss();
                bottomContainerLayout.dismissSheet();
            } else {
                mBottomFragment.show(getSupportFragmentManager(), R.id.bottom_container_layout);
            }
        }
    }


    @Override
    public void loadUsers(String title, String orgId) {
        mTopView.setAppTitle(title);
        SelectedGroupContactFragment fragment = new SelectedGroupContactFragment(mGroupUsers, mCacheContact, this, this);
        Bundle bundle = new Bundle();
        bundle.putString("orgId", orgId);
        bundle.putString("title", title);
        bundle.putBoolean("isBack", true);
        fragment.setArguments(bundle);
        mFragments.add(0, fragment);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fragment_animation_enter, R.anim.fragmemt_animation_exit)
                .add(R.id.fl_container_layout, fragment).commit();
    }

    @Override
    public void selectedUsers(List<Contacts> groupUsers) {
        mTvSelectedContact.setText(String.valueOf(groupUsers.size()));
        mTopView.setRightText(groupUsers.size() != 0 ? "确认" + "(" + groupUsers.size() + ")" : "确认");
    }


    //TODO 选择逻辑问题
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteContact(DeleteContactEvent event) {

//        for (int i = 0; i <= mGroupUsers.size(); i++) {
//            Contacts contacts = mCacheContact.get(Integer.parseInt(mGroupUsers.get(i).getOrgId() + i));
//            if (contacts != null) {
//                if (event.getCode().equals(contacts.getCode())) {
//                    contacts.setChecked(!contacts.isChecked());
//                    mCacheContact.put(Integer.parseInt(mGroupUsers.get(i).getOrgId() + i), contacts);
//
//                    SelectedGroupContactFragment fragment = mFragments.get(0);
//                    if (fragment != null)
//                        mFragments.get(0).updateSelected();
//                }
//            }
//        }


        //搜索人员
        for (int i = 0; i < mSearchData.size(); i++) {
            if (mSearchData.get(i).getCode().equals(event.getCode())) {
                mSearchData.get(i).setChecked(!mSearchData.get(i).isChecked());
            }
        }

        mUserAdapter.setNewData(mSearchData);
        mUserAdapter.notifyDataSetChanged();

        if (event.getSize() == 0) {
            if (mBottomFragment != null) {
                if (bottomContainerLayout.isSheetShowing()) {
                    mBottomFragment.dismiss();
                    bottomContainerLayout.dismissSheet();
                    return;
                }
            }
        }

        mTvSelectedContact.setText(mGroupUsers.size() + "");
        mTopView.setRightText(mGroupUsers.size() != 0 ? "确认" + "(" + mGroupUsers.size() + ")" : "确认");
    }

    @Override
    public void onBackPressed() {
        if (mBottomFragment != null) {
            if (bottomContainerLayout.isSheetShowing()) {
                mBottomFragment.dismiss();
                bottomContainerLayout.dismissSheet();
                return;
            }
        }

        if (mRvSearchContact.getVisibility() == View.VISIBLE) {
            mRvSearchContact.setVisibility(View.GONE);
            mSearchContact.setText("");
            return;
        }

        for (SelectedGroupContactFragment fragment : mFragments) {
            if (fragment.onBackPressed()) {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fragment_animation_pop_enter, R.anim.fragment_animation_pop_exit)
                        .remove(fragment).commit();
                mFragments.remove(0);
                mTopView.setAppTitle(mFragments.get(0).getArguments().getString("title", "选择成员"));
                return;
            }
        }
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void uidNull(int code) {
        catchWarningByCode(code);
    }

    @Override
    public void QueryGroupContactSuccess(List<Contacts> bean) {
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void searchContactSuccess(List<Contacts> bean) {
        try {
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bean != null) {
            mSearchData.clear();
            mRvSearchContact.setVisibility(View.VISIBLE);
            //遍历搜索数据 更新选择人员
            for (int i = 0; i < mGroupUsers.size(); i++) {
                for (Contacts contacts : bean) {
                    if (contacts.getCode().equals(mGroupUsers.get(i).getCode())) {
                        contacts.setChecked(true);
                    }
                }
            }
            mSearchData.addAll(bean);
            mUserAdapter.setNewData(mSearchData);
            mUserAdapter.notifyDataSetChanged();
        }
    }

    class OnItemClick extends OnItemClickListener {
        @Override
        public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            mSearchData.get(i).setChecked(!mSearchData.get(i).isChecked());
            if (mSearchData.get(i).isChecked()) {
                mGroupUsers.add(mSearchData.get(i));
            } else {
                mGroupUsers.remove(mSearchData.get(i));
            }

            //BottomSheet
            mTvSelectedContact.setText(String.valueOf(mGroupUsers.size()));
            mTopView.setRightText(mGroupUsers.size() != 0 ? "确认" + "(" + mGroupUsers.size() + ")" : "确认");

            //防止Item点击效果错位问题
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                mUserAdapter.setNewData(mSearchData);
                mUserAdapter.notifyDataSetChanged();
            }, 100);
        }
    }
}
