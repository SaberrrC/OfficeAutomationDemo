
package com.shanlin.oa.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
//import com.hyphenate.chatuidemo.db.Friends;
//import com.hyphenate.chatuidemo.db.FriendsInfoCacheSvc;
import com.shanlin.oa.R;
import com.shanlin.oa.common.Api;
import com.shanlin.oa.common.Constants;
import com.shanlin.oa.manager.AppConfig;
import com.shanlin.oa.manager.AppManager;
import com.shanlin.oa.model.Contacts;
import com.shanlin.oa.model.User;
import com.shanlin.oa.ui.activity.Contact_Details_Activity;
import com.shanlin.oa.ui.activity.ContactsActivity;
import com.shanlin.oa.ui.adapter.SearchUserResultAdapter;
import com.shanlin.oa.ui.adapter.TabContactsAdapter;
import com.shanlin.oa.ui.base.BaseFragment;
import com.shanlin.oa.utils.LogUtils;
import com.shanlin.oa.views.ClearEditText;
import com.jakewharton.rxbinding2.widget.RxTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * <h3>Description: 名片页面</h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/8/26.<br/>
 */
public class TabContactsFragment extends BaseFragment implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.recycler_search_result_view)
    RecyclerView recyclerViewSearchResult;
    @Bind(R.id.layout_root)
    RelativeLayout mRootView;

    @Bind(R.id.search_et_input)
    ClearEditText search_et_input;
    @Bind(R.id.search_et_cancle)
    TextView tvCacle;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;


    private Dialog dialog;
    private List<Contacts> items = new ArrayList<>();
    private TabContactsAdapter adapter;
    private RelativeLayout view;
    private RelativeLayout mRlRecyclerViewContainer;
    private Window window;
    private PopupWindow popupWindow;

    private InputMethodManager inputManager;
    private List<User> userList = null;
    private boolean isPullRefreashing =false;

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
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#0EA7ED"),
                Color.parseColor("#0EA7ED"), Color.parseColor("#0EA7ED"));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setEnabled(true);
    }

    @OnClick({R.id.search_et_cancle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_et_cancle:
                recyclerView.setVisibility(View.VISIBLE);
                tvCacle.setVisibility(View.GONE);
                recyclerViewSearchResult.setVisibility(View.GONE);
                reSetSwipRefreash();
                search_et_input.setText("");
                inputManager.hideSoftInputFromWindow(
                        getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                break;
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View viewBack = view.findViewById(R.id.btn_back);
        viewBack.setVisibility(View.GONE);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new TabContactsAdapter(items);
        //在初始化是为RecyclerView添加点击事件，这样可以防止重复点击问题
        recyclerView.addOnItemTouchListener(new ItemClick());


//        title.setText(AppConfig.getAppConfig(getActivity()).get(AppConfig.PREF_KEY_COMPANY_NAME));
        loadData();


        search_et_input.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                recyclerView.setVisibility(View.GONE);
                reSetSwipRefreash();
                tvCacle.setVisibility(View.VISIBLE);
                return false;
            }
        });


        //监听软键盘确认键,触发搜索功能,为匹配IOS 此功能注释掉
//        search_et_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                switch (i) {
//                    case EditorInfo.IME_ACTION_SEARCH:
//                        final String uid = AppConfig.getAppConfig(AppManager.mContext)
//                                .get(AppConfig.PREF_KEY_USER_UID);
//                        String token = AppConfig.getAppConfig(AppManager.mContext)
//                                .get(AppConfig.PREF_KEY_TOKEN);
//                        String departmentId = AppConfig.getAppConfig(AppManager.mContext)
//                                .get(AppConfig.PREF_KEY_DEPARTMENT_ID);
//                        String isleader = AppConfig.getAppConfig(AppManager.mContext)
//                                .get(AppConfig.PREF_KEY_IS_LEADER);
//
//                        HttpParams params = new HttpParams();
//                        params.put("uid", uid);
//                        params.put("token", token);
//                        params.put("oid", departmentId);
//                        params.put("isleader", isleader);
//                        params.put("name", search_et_input.getText().toString());
//
//                        initKjHttp().post(Api.PHONEBOOK_SEARCHPHONEBOOK, params, new HttpCallBack() {
//                            @Override
//                            public void onFinish() {
//                                super.onFinish();
//                                hideLoadingView();
//                            }
//
//                            @Override
//                            public void onSuccess(String t) {
//                                super.onSuccess(t);
//                                System.out.println(t);
//                                LogUtils.e("联系人返回数据-》" + t);
//
//                                if (userList == null) {
//                                    userList = new ArrayList<User>();
//                                } else {
//                                    userList.clear();
//                                }
//                                try {
//                                    JSONObject jo = new JSONObject(t);
//                                    if (Api.getCode(jo) == Api.RESPONSES_CODE_OK) {
//                                        JSONArray jDepartment = jo.getJSONArray("data");
//                                        for (int i = 0; i < jDepartment.length(); i++) {
//                                            JSONObject jsonObject = jDepartment.getJSONObject(i);
//                                            Log.e("", "-------" + jsonObject.getString("username") + "----" + jsonObject.getString("department_name"));
//                                            User user = new User(jsonObject.getString("username"),
//                                                    jsonObject.getString("phone"),
//                                                    jsonObject.getString("portrait"),
//                                                    jsonObject.getString("sex"),
//                                                    jsonObject.getString("post_id"),
//                                                    jsonObject.getString("CODE"),
//                                                    jsonObject.getString("department_id"),
//                                                    jsonObject.getString("post_title"),
//                                                    jsonObject.getString("department_name"),
//                                                    jsonObject.getString("email"),
//                                                    jsonObject.getString("isshow"));
//                                            userList.add(user);
//                                        }
//                                        inputManager.hideSoftInputFromWindow(
//                                                getActivity().getCurrentFocus().getWindowToken(),
//                                                InputMethodManager.HIDE_NOT_ALWAYS);
//                                        recyclerViewSearchResult.setVisibility(View.VISIBLE);
//                                        SearchUserResultAdapter adapter = new SearchUserResultAdapter(userList);
//                                        recyclerViewSearchResult.setAdapter(adapter);
//                                    } else {
//                                        showToast(Api.getInfo(jo));
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(int errorNo, String strMsg) {
//                                LogUtils.e(errorNo + "--" + strMsg);
//                                catchWarningByCode(errorNo);
//                                super.onFailure(errorNo, strMsg);
//                            }
//                        });
//
//                        break;
//                }
//                return true;
//            }
//        });

        //EditText 自动搜索,间隔->输入停止1秒后自动搜索
        RxTextView.textChanges(search_et_input)
                .debounce(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        if (search_et_input == null) {
                            return;
                        }
                        if (!TextUtils.isEmpty(search_et_input.getText().toString().trim())) {
                            final String uid = AppConfig.getAppConfig(AppManager.mContext)
                                    .get(AppConfig.PREF_KEY_USER_UID);
                            String token = AppConfig.getAppConfig(AppManager.mContext)
                                    .get(AppConfig.PREF_KEY_TOKEN);
                            String departmentId = AppConfig.getAppConfig(AppManager.mContext)
                                    .get(AppConfig.PREF_KEY_DEPARTMENT_ID);
                            String isleader = AppConfig.getAppConfig(AppManager.mContext)
                                    .get(AppConfig.PREF_KEY_IS_LEADER);

                            HttpParams params = new HttpParams();
                            params.put("uid", uid);
                            params.put("token", token);
                            params.put("oid", departmentId);
                            params.put("isleader", isleader);
                            params.put("name", search_et_input.getText().toString());

                            initKjHttp().post(Api.PHONEBOOK_SEARCHPHONEBOOK, params, new HttpCallBack() {
                                @Override
                                public void onFinish() {
                                    super.onFinish();
                                    hideLoadingView();
                                    mSwipeRefreshLayout.setRefreshing(false);
                                }

                                @Override
                                public void onSuccess(String t) {
                                    super.onSuccess(t);
                                    System.out.println(t);
                                    LogUtils.e("联系人返回数据-》" + t);

                                    if (userList == null) {
                                        userList = new ArrayList<User>();
                                    } else {
                                        userList.clear();
                                    }
                                    try {
                                        JSONObject jo = new JSONObject(t);
                                        if (Api.getCode(jo) == Api.RESPONSES_CODE_OK) {
                                            JSONArray jDepartment = jo.getJSONArray("data");
                                            for (int i = 0; i < jDepartment.length(); i++) {
                                                JSONObject jsonObject = jDepartment.getJSONObject(i);
                                                Log.e("", "-------" + jsonObject.getString("username") + "----" + jsonObject.getString("department_name"));
                                                User user = new User(jsonObject.getString("username"),
                                                        jsonObject.getString("phone"),
                                                        jsonObject.getString("portrait"),
                                                        jsonObject.getString("sex"),
                                                        jsonObject.getString("post_id"),
                                                        jsonObject.getString("CODE"),
                                                        jsonObject.getString("department_id"),
                                                        jsonObject.getString("post_title"),
                                                        jsonObject.getString("department_name"),
                                                        jsonObject.getString("email"),
                                                        jsonObject.getString("isshow"));
                                                userList.add(user);
                                            }
                                            try {
                                                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                                        InputMethodManager.HIDE_NOT_ALWAYS);
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }

                                            hideEmptyView();
                                            recyclerViewSearchResult.setVisibility(View.VISIBLE);
                                            reSetSwipRefreash();
                                            SearchUserResultAdapter adapter = new SearchUserResultAdapter(userList);
                                            recyclerViewSearchResult.setAdapter(adapter);
                                        } else {
                                            showToast(Api.getInfo(jo));
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
                        }
                    }
                });
    }


    @Override
    public void onResume() {
        super.onResume();
//        loadData();
    }

    /**
     * 加载通讯录数据
     */
    private void loadData() {

        HttpParams params = new HttpParams();
        params.put("uid", AppConfig.getAppConfig(getActivity()).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(getActivity()).getPrivateToken());
        params.put("department_id", "");
        initKjHttp().post(Api.GET_CONTACTS, params, new HttpCallBack() {
            @Override
            public void onPreStart() {
                super.onPreStart();
                if(!isPullRefreashing){
                    showLoadingView();
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerViewSearchResult.setVisibility(View.GONE);

                }

            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoadingView();
                isPullRefreashing=false;
                mSwipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                //System.out.println(t);
                LogUtils.e(t);
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (Api.getCode(jo)) {
                        case Api.RESPONSES_CODE_OK:
                            if (items.size() > 0 || items != null) {
                                items.clear();
                            }
                            JSONArray jDepartment = Api.getDataToJSONObject(jo)
                                    .getJSONArray("department");
                            for (int i = 0; i < jDepartment.length(); i++) {
                                JSONObject d = jDepartment.getJSONObject(i);
                                Contacts c = new Contacts(d);
                                items.add(c);
                            }
                            JSONArray jEmployee = Api.getDataToJSONObject(jo)
                                    .getJSONArray("employee");
                            for (int i = 0; i < jEmployee.length(); i++) {
                                JSONObject e = jEmployee.getJSONObject(i);
                                Contacts c = new Contacts(e);
                                items.add(c);
                            }

                            hideEmptyView();
                            recyclerView.setAdapter(adapter);
                            reSetSwipRefreash();
                            break;
                        case Api.RESPONSES_CODE_DATA_EMPTY:
                            showEmptyView(mRlRecyclerViewContainer, " ", 0, false);
                            break;
                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
                            catchWarningByCode(Api.getCode(jo));
                            break;
                    }
                } catch (JSONException e) {
                    System.out.println(e.toString());
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                hideLoadingView();
                reSetSwipRefreash();
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
                reSetSwipRefreash();
                super.onFailure(errorNo, strMsg);
            }
        });
    }

    public void reSetSwipRefreash(){
        if(mSwipeRefreshLayout!=null){
            if(recyclerView.getVisibility()==View.VISIBLE){
                mSwipeRefreshLayout.setEnabled(true);
            }else {
                mSwipeRefreshLayout.setEnabled(false);
            }
        }

    }

    @Override
    public void onRefresh() {
        isPullRefreashing=true;
        loadData();
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
//                    showContactsInfo(items.get(i));


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

//    @SuppressLint("SetTextI18n")
//    private void showContactsInfo(final User user) {
//
//        LayoutInflater factory = LayoutInflater.from(getActivity());
//
//        @SuppressLint("InflateParams")
//        View view = factory.inflate(R.layout.custom_dialog, null);
//
//
//        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams
//                .WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, false);
//        popupWindow.setOutsideTouchable(true);
//        popupWindow.setFocusable(true);
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//        TextView name = (TextView) view.findViewById(R.id.name);
//        name.setText(user.getUsername());
//        final TextView phoneNumber = (TextView) view.findViewById(R.id.tv_phone_number);
//        LinearLayout btnCall = (LinearLayout) view.findViewById(R.id.ll_phone_layout);
//        Button btnVoiceCall = (Button) view.findViewById(R.id.btn_voice_call);
//        Button btnSendMsg = (Button) view.findViewById(R.id.btn_send_msg);
//        View partLine = view.findViewById(R.id.partLine);
//
//        String isshow = user.getIsshow();
//
//
//        if (isshow.equals("1")) {
//            //显示电话
//            phoneNumber.setText(user.getPhone());
//        } else {
//            btnCall.setVisibility(View.GONE);
//            partLine.setVisibility(View.INVISIBLE);
//        }
//
//
//        SimpleDraweeView portrait = (SimpleDraweeView) view.findViewById(R.id.user_portrait);
//        portrait.setImageURI(user.getPortraits());
//
//
//        btnVoiceCall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean availabl = Utils.isNetworkAvailabl(mContext);
//                if (!availabl) {
//                    showToast("网络不稳定，请重试");
//                    return;
//                }
//
//                addOrUpdateFriendInfo(user);
//
//                startActivity(new Intent(mContext, VoiceCallActivity.class)
//                        .putExtra("username", Constants.CID + "_" + user.getCode())
//                        .putExtra("nike", user.getUsername())
//                        .putExtra("portrait", user.getPortraits())
//                        .putExtra("isComingCall", false));
//
//                popupWindow.dismiss();
//                popupWindow = null;
//            }
//        });
//        btnSendMsg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean availabl = Utils.isNetworkAvailabl(mContext);
//                if (!availabl) {
//                    showToast("网络不稳定，请重试");
//                    return;
//                }
//
//                addOrUpdateFriendInfo(user);
//
//
//                startActivity(new Intent(mContext, EaseChatMessageActivity.class)
//                        .putExtra("usernike", user.getUsername())
//                        .putExtra("user_pic", user.getPortraits())
//                        .putExtra("u_id", Constants.CID + "_" + user.getCode()));
//
//                popupWindow.dismiss();
//                popupWindow = null;
//            }
//        });
//        btnCall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                requestRunTimePermission(new String[]{Manifest.permission.CALL_PHONE}, new PermissionListener() {
//                    @Override
//                    public void onGranted() {
//                        Intent intent = new Intent(Intent.ACTION_CALL,
//                                Uri.parse("tel:" + user.getPhone()));
//
//                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
//
//        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
//        lp.alpha = 0.5f;
//        getActivity().getWindow().setAttributes(lp);
//        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//
//            @Override
//            public void onDismiss() {
//                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
//                lp.alpha = 1f;
//                getActivity().getWindow().setAttributes(lp);
//            }
//        });
//
//        popupWindow.setAnimationStyle(R.style.dialog_pop_anim_style);
//        popupWindow.showAtLocation(mRootView, Gravity.CENTER, 0, 0);
//    }

    private void addOrUpdateFriendInfo(User user) {
//        Friends friend = new Friends();
//        friend.setUser_id(Constants.CID + "_" + user.getCode());
//        friend.setNickname(user.getUsername());
//        friend.setPortrait(user.getPortraits());
//        FriendsInfoCacheSvc.getInstance(mContext).addOrUpdateFriends(friend);
    }

    public void callPhone(String phone) {
        //用intent启动拨打电话
        Intent intent = new Intent(Intent.ACTION_CALL,
                Uri.parse("tel:" + phone));
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}