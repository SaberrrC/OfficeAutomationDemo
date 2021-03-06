package com.shanlinjinrong.oa.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.model.Contacts;
import com.shanlinjinrong.oa.model.User;
import com.shanlinjinrong.oa.ui.activity.contracts.Contact_Details_Activity;
import com.shanlinjinrong.oa.ui.activity.contracts.ContactsActivity;
import com.shanlinjinrong.oa.ui.base.BaseHttpFragment;
import com.shanlinjinrong.oa.ui.fragment.adapter.SearchUserResultAdapter;
import com.shanlinjinrong.oa.ui.fragment.adapter.TabContactsAdapter;
import com.shanlinjinrong.oa.ui.fragment.contract.TabContractsFragmentContract;
import com.shanlinjinrong.oa.ui.fragment.presenter.TabContractsFragmentPresenter;
import com.shanlinjinrong.oa.utils.DateUtils;
import com.shanlinjinrong.oa.utils.SharedPreferenceUtils;
import com.shanlinjinrong.oa.views.ClearEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * <h3>Description: 名片页面</h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/8/26.<br/>
 */
public class TabContactsFragment extends BaseHttpFragment<TabContractsFragmentPresenter> implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, TabContractsFragmentContract.View, View.OnKeyListener {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.recycler_search_result_view)
    RecyclerView recyclerViewSearchResult;
    @BindView(R.id.layout_root)
    RelativeLayout mRootView;

    @BindView(R.id.search_et_input)
    ClearEditText search_et_input;
    @BindView(R.id.search_et_cancle)
    TextView tvCacle;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.tv_error_layout)
    TextView mTvErrorLayout;


    private Dialog dialog;
    private List<Contacts> items = new ArrayList<>();
    private RelativeLayout view;
    private RelativeLayout mRlRecyclerViewContainer;
    private Window window;
    private PopupWindow popupWindow;

    private InputMethodManager inputManager;
    private List<User> userList = null;
    private boolean isPullRefreashing = false;
    private SearchUserResultAdapter mSearchUserResultAdapter;
    private TabContactsAdapter mContactAdapter;
    private List<Contacts> mContacts;
    private long lastClickTime = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = (RelativeLayout) inflater.inflate(R.layout.tab_contacts_fragment, container, false);
        mRlRecyclerViewContainer = (RelativeLayout) view.findViewById(R.id.rl_recycler_view_container);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    //搜索事件
    private void init() {
        inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        recyclerViewSearchResult.setVisibility(View.GONE);
        recyclerViewSearchResult.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewSearchResult.addOnItemTouchListener(new SearchResultItemClick());
        mSearchUserResultAdapter = new SearchUserResultAdapter(userList);
        recyclerViewSearchResult.setAdapter(mSearchUserResultAdapter);

        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#0EA7ED"),
                Color.parseColor("#0EA7ED"), Color.parseColor("#0EA7ED"));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setEnabled(true);
    }

    @OnClick({R.id.search_et_cancle})
    @Override
    public void onClick(View view) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime < 1000) {
            lastClickTime = currentTime;
            return;
        }
        lastClickTime = currentTime;
        switch (view.getId()) {
            case R.id.search_et_cancle:
                recyclerView.setVisibility(View.VISIBLE);
                tvCacle.setVisibility(View.GONE);
                recyclerViewSearchResult.setVisibility(View.GONE);
                reSetSwipRefreash();
                search_et_input.setText("");
                mTvErrorLayout.setVisibility(View.GONE);
                try {
                    inputManager.hideSoftInputFromWindow(
                            getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View viewBack = view.findViewById(R.id.btn_back);
        viewBack.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new ItemClick());
        mContactAdapter = new TabContactsAdapter(items);
        recyclerView.setAdapter(mContactAdapter);
        loadData();

        search_et_input.setOnKeyListener(this);

        //EditText 自动搜索,间隔->输入停止1秒后自动搜索
        RxTextView.textChanges(search_et_input)
                .debounce(10, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(charSequence -> {
                    if (search_et_input == null) {
                        return;
                    }
                    if (!TextUtils.isEmpty(search_et_input.getText().toString().trim())) {
                        String departmentId = AppConfig.getAppConfig(AppManager.mContext)
                                .get(AppConfig.PREF_KEY_DEPARTMENT_ID);
                        String isleader = AppConfig.getAppConfig(AppManager.mContext)
                                .get(AppConfig.PREF_KEY_IS_LEADER);
                        showLoadingView();
                        mPresenter.autoSearch(search_et_input.getText().toString().trim());
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerViewSearchResult.setVisibility(View.GONE);
                        mTvErrorLayout.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected void lazyLoadData() {
        try {
            //TODO 缓存当天的数据
            if (!SharedPreferenceUtils.getStringValue(AppManager.mContext, DateUtils.getCurrentDate("yyyy-MM-dd"), "children", "").equals("")) {
                String childrens = SharedPreferenceUtils.getStringValue(AppManager.mContext, DateUtils.getCurrentDate("yyyy-MM-dd"), "children", "");
                JSONArray children = new JSONArray(childrens);
                mContacts = new ArrayList<>();
                for (int i = 0; i < children.length(); i++) {
                    JSONObject department = children.getJSONObject(i);
                    String number = department.getString("memberCount");
                    if (number.equals("0")) {
                        continue;
                    }
                    Contacts contact = new Contacts(department);
                    mContacts.add(contact);
                }
                String users = SharedPreferenceUtils.getStringValue(AppManager.mContext, DateUtils.getCurrentDate("yyyy-MM-dd"), "users1", "");
                if (!users.equals("")) {
                    JSONArray user = new JSONArray(users);
                    for (int i = 0; i < user.length(); i++) {
                        JSONObject userInfo = user.getJSONObject(i);
                        Contacts person = new Contacts(userInfo);
                        mContacts.add(person);
                    }
                }
                items = mContacts;
                hideEmptyView();
                mContactAdapter.setNewData(mContacts);
                recyclerView.requestLayout();
                mContactAdapter.notifyDataSetChanged();
                reSetSwipRefreash();
                return;
            }
            showLoadingView();
            mPresenter.loadData("1");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 加载通讯录数据
     */
    private void loadData() {
    }

    public void reSetSwipRefreash() {
        if (mSwipeRefreshLayout != null) {
            if (recyclerView.getVisibility() == View.VISIBLE) {
                mSwipeRefreshLayout.setEnabled(true);
            } else {
                mSwipeRefreshLayout.setEnabled(false);
            }
        }
    }

    @Override
    public void onRefresh() {
        isPullRefreashing = true;
        loadData();
        mPresenter.loadData("1");
    }

    @Override
    public void uidNull(String code) {
//        catchWarningByCode(code);
    }

    @Override
    public void autoSearchSuccess(List<User> users) {
        hideLoadingView();
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(false);
        if (userList == null) {
            userList = new ArrayList<>();
        } else {
            userList.clear();
        }
        userList = users;
        if (userList.size() == 0) {
            mTvErrorLayout.setVisibility(View.VISIBLE);
            mTvErrorLayout.setText("暂无该联系人！");
        } else {
            mTvErrorLayout.setVisibility(View.GONE);
        }
        mSearchUserResultAdapter.setNewData(userList);

//        try {
//            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
//                    InputMethodManager.HIDE_NOT_ALWAYS);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        hideEmptyView();
        if (recyclerViewSearchResult != null) {
            recyclerView.setVisibility(View.GONE);
            recyclerViewSearchResult.setVisibility(View.VISIBLE);
        }
        recyclerViewSearchResult.requestLayout();
        mSearchUserResultAdapter.notifyDataSetChanged();
        reSetSwipRefreash();
    }

    @Override
    public void autoSearchFailed(int errCode, String errMsg) {
        hideLoadingView();
        if ("auth error".equals(errMsg)) {
            catchWarningByCode(errMsg);
            return;
        }
    }

    @Override
    public void autoSearchOther(String msg) {
        showToast(msg);
    }

    @Override
    public void autoSearchFinish() {
        hideLoadingView();
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void loadDataStart() {
        try {
            if (!isPullRefreashing && recyclerView != null && recyclerViewSearchResult != null) {
                showLoadingView();
                recyclerView.setVisibility(View.VISIBLE);
                recyclerViewSearchResult.setVisibility(View.GONE);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadDataSuccess(List<Contacts> contacts) {
        hideLoadingView();
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(false);
        try {
            if (items.size() > 0 || items != null) {
                items.clear();
            }
            items = contacts;
            hideEmptyView();
            mContactAdapter.setNewData(items);
            reSetSwipRefreash();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadDataFailed(int code, String msg) {
        hideLoadingView();
        if ("auth error".equals(msg)) {
            catchWarningByCode(msg);
            return;
        }
        reSetSwipRefreash();
        showEmptyView(mRlRecyclerViewContainer, "网络不稳定，请重试！", 0, false);
        reSetSwipRefreash();
    }

    @Override
    public void loadDataEmpty() {
        hideLoadingView();
        showEmptyView(mRlRecyclerViewContainer, " ", 0, false);
    }

    @Override
    public void loadDataFinish() {
        hideLoadingView();
        isPullRefreashing = false;
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void loadDataTokenNoMatch(String code) {
        catchWarningByCode(code);
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            showLoadingView();
            mPresenter.autoSearch(search_et_input.getText().toString().trim());

            // ------------------------------  隐藏键盘  ------------------------------

            try {
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    class ItemClick extends OnItemClickListener {
        @Override
        public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            Intent intent = new Intent();
            switch (items.get(i).getItemType()) {
                case Contacts.DEPARTMENT:
                    intent.setClass(getActivity(), ContactsActivity.class);
                    intent.putExtra(ContactsActivity.PAGE_MAP_DID,
                            items.get(i).getDepartmentId());
                    intent.putExtra(ContactsActivity.PAGE_MAP_TITLE,
                            items.get(i).getDepartmentName());
                    startActivity(intent);
                    break;
                case Contacts.EMPLOYEE:
                    break;
            }
        }
    }

    class SearchResultItemClick extends OnItemClickListener {

        @Override
        public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            showContactsInfo(userList.get(i));
        }
    }

    @SuppressLint("SetTextI18n")
    private void showContactsInfo(final User user) {
        Intent intent = new Intent(getActivity(), Contact_Details_Activity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}