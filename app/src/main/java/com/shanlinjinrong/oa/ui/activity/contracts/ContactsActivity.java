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
import com.shanlinjinrong.oa.model.Contacts;
import com.shanlinjinrong.oa.ui.activity.contracts.contract.ContractActivityContract;
import com.shanlinjinrong.oa.ui.activity.contracts.presenter.ContractActivityPresenter;
import com.shanlinjinrong.oa.ui.fragment.adapter.TabContactsAdapter;
import com.shanlinjinrong.oa.ui.base.MyBaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <h3>Description: 首页名片页面二级一下界面 </h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/9/22.<br />
 */
public class ContactsActivity extends MyBaseActivity<ContractActivityPresenter> implements ContractActivityContract.View {


    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.btn_back)
    ImageView btnBack;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.layout_root)
    RelativeLayout mRootView;

    @Bind(R.id.rl_top2)
    RelativeLayout mRLSearchView;

    private List<Contacts> items = new ArrayList<>();
    private List<Map<String, String>> pageMap;
    private TabContactsAdapter adapter;
    private RelativeLayout view;
    @Bind(R.id.rl_recycler_view_container)
    RelativeLayout mRlRecyclerViewContainer;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;


    /**
     * 页面加载数据所需部门ID
     */
    public static final String PAGE_MAP_DID = "pageMapDid";
    /**
     * 页面标题
     */
    public static final String PAGE_MAP_TITLE = "pageMapTitle";
    private PopupWindow popupWindow;
    private String phoneStr;

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
        loadData(pageMap.get(pageMap.size() - 1).get(PAGE_MAP_DID));
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
        //在初始化是为RecyclerView添加点击时间，这样可以防止重复点击问题
        recyclerView.addOnItemTouchListener(new ItemClick());
    }

    /**
     * 加载通讯录数据
     *
     * @param departmentId 部门ID
     */
    private void loadData(String departmentId) {
        title.setText(pageMap.get(pageMap.size() - 1).get(PAGE_MAP_TITLE));
        showLoadingView();
        mPresenter.loadData(departmentId);
    }

    @Override
    public void loadDataSuccess(List<Contacts> contacts) {
        if (items.size() > 0 || items != null) {
            items.clear();
        }
        items = contacts;
        adapter = new TabContactsAdapter(items);
        recyclerView.setAdapter(adapter);
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
    public void uidNull(int code) {
        catchWarningByCode(code);
    }

    class ItemClick extends OnItemClickListener {
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

//    @SuppressLint("SetTextI18n")
//    private void showContactsInfo(final Contacts contacts) {
//
//        LayoutInflater factory = LayoutInflater.from(this);
//
//        @SuppressLint("InflateParams")
//        View view = factory.inflate(R.layout.custom_dialog, null);
//
//        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams
//                .WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, false);
//        popupWindow.setOutsideTouchable(true);
//        popupWindow.setFocusable(true);
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//        TextView name = (TextView) view.findViewById(R.id.name);
//        name.setText(contacts.getUsername());
//        final TextView phoneNumber = (TextView) view.findViewById(R.id.tv_phone_number);
//        LinearLayout btnCall = (LinearLayout) view.findViewById(R.id.ll_phone_layout);
//        Button btnVoiceCall = (Button) view.findViewById(R.id.btn_voice_call);
//        Button btnSendMsg = (Button) view.findViewById(R.id.btn_send_msg);
//        View partLine =view.findViewById(R.id.partLine);
//        String isshow = contacts.getIsshow();
//
//
//        if (isshow.equals("1")) {
//            //显示电话
//            phoneNumber.setText(contacts.getPhone());
//        } else {
//            phoneNumber.setText("");
//            btnCall.setVisibility(View.GONE);
//            partLine.setVisibility(View.INVISIBLE);
//        }
//
//
//        SimpleDraweeView portrait = (SimpleDraweeView) view.findViewById(R.id.user_portrait);
//        portrait.setImageURI(contacts.getPortraits());
//
//
//        btnVoiceCall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean availabl = Utils.isNetworkAvailabl(ContactsActivity.this);
//                if (!availabl) {
//                    showToast("网络不稳定，请重试");
//                    return;
//                }
//
//                addOrUpdateFriendInfo(contacts);
//
//                    startActivity(new Intent(ContactsActivity.this, VoiceCallActivity.class)
//                            .putExtra("username", Constants.CID + "_" + contacts.getCode())
//                            .putExtra("nike", contacts.getUsername())
//                            .putExtra("portrait", contacts.getPortraits())
//                            .putExtra("isComingCall", false));
//                popupWindow.dismiss();
//                popupWindow = null;
//            }
//        });
//        btnSendMsg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean availabl = Utils.isNetworkAvailabl(ContactsActivity.this);
//                if (!availabl) {
//                    showToast("网络不稳定，请重试");
//                    return;
//                }
//                addOrUpdateFriendInfo(contacts);
//
//                    startActivity(new Intent(ContactsActivity.this, EaseChatMessageActivity.class)
//                            .putExtra("usernike", contacts.getUsername())
//                            .putExtra("user_pic", contacts.getPortraits())
//                            .putExtra("u_id", Constants.CID + "_" + contacts.getCode()));
//                popupWindow.dismiss();
//                popupWindow = null;
//            }
//        });
//
//        btnCall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                requestRunTimePermission(new String[]{Manifest.permission.CALL_PHONE}, new PermissionListener() {
//                    @Override
//                    public void onGranted() {
//                        Intent intent = new Intent(Intent.ACTION_CALL,
//                                Uri.parse("tel:" + contacts.getPhone()));
//
//                        if (ActivityCompat.checkSelfPermission(ContactsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                            // TODO: Consider calling
//                            //    ActivityCompat#requestPermissions
//                            // here to request the missing permissions, and then overriding
//                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                            //                                          int[] grantResults)
//                            // to handle the case where the user grants the permission. See the documentation
//                            // for ActivityCompat#requestPermissions for more details.
//                            return;
//                        }
//                        startActivity(intent);
//                    }
//
//                    @Override
//                    public void onDenied() {
//                        showToast("拨打电话权限被拒绝，请手动设置");
//                    }
//                });
//            }
//        });
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.alpha = 0.5f;
//        getWindow().setAttributes(lp);
//        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//
//            @Override
//            public void onDismiss() {
//                WindowManager.LayoutParams lp = getWindow().getAttributes();
//                lp.alpha = 1f;
//                getWindow().setAttributes(lp);
//            }
//        });
//
//        popupWindow.setAnimationStyle(R.style.dialog_pop_anim_style);
//        popupWindow.showAtLocation(mRootView, Gravity.CENTER, 0, 0);
//    }

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
        ButterKnife.unbind(this);
        if (popupWindow != null) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }
}
