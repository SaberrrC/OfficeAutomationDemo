package com.shanlinjinrong.oa.ui.fragment.presenter;

import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.model.Contacts;
import com.shanlinjinrong.oa.model.User;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;
import com.shanlinjinrong.oa.ui.fragment.contract.TabContractsFragmentContract;
import com.shanlinjinrong.oa.utils.DateUtils;
import com.shanlinjinrong.oa.utils.LogUtils;
import com.shanlinjinrong.oa.utils.SharedPreferenceUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class TabContractsFragmentPresenter extends HttpPresenter<TabContractsFragmentContract.View> implements TabContractsFragmentContract.Presenter {
    @Inject
    public TabContractsFragmentPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void autoSearch(String name) {
        mKjHttp.get(ApiJava.PHONEBOOK_SEARCHPHONEBOOK + "?name=" + name, new HttpParams(), new HttpCallBack() {

            @Override
            public void onFinish() {
                super.onFinish();
                try {
                    if (mView != null) {
                        mView.autoSearchFinish();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
//                LogUtils.e("联系人返回数据-》" + t);
                List<User> users = new ArrayList<>();
                try {
                    JSONObject jo = new JSONObject(t);
                    String code = jo.getString("code");
                    switch (code) {
                        case ApiJava.REQUEST_CODE_OK:
                            JSONArray jDepartment = jo.getJSONArray("data");
                            for (int i = 0; i < jDepartment.length(); i++) {
                                JSONObject jsonObject = jDepartment.getJSONObject(i);
                                User user = new User(jsonObject.getString("username"), jsonObject.getString("phone"), jsonObject.getString("portraits"), jsonObject.getString("sex"), jsonObject.getString("post_id"), jsonObject.getString("code"), jsonObject.getString("department_id"), jsonObject.getString("post_title"), jsonObject.getString("department_name"), jsonObject.getString("email"));
                                users.add(user);
                            }
                            if (mView != null) {
                                mView.autoSearchSuccess(users);
                            }
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
                            mView.uidNull(code);
                            break;
                        default:
                            if (mView != null) {
                                String msg = jo.getString("message");
                                mView.autoSearchOther(msg);
                            }
                            break;
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                try {
                    mView.uidNull(strMsg);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
    }

    @Override
    public void loadData(String orgId) {
        mKjHttp.get(ApiJava.GET_CONTACTS + "?orgId=" + orgId, new HttpParams(), new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();
                try {
                    mView.loadDataStart();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    String code = jsonObject.getString("code");
                    switch (code) {
                        case ApiJava.REQUEST_CODE_OK:
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            JSONArray children = jsonObject1.getJSONArray("children");
                            JSONArray users = jsonObject1.getJSONArray("users");

                            List<Contacts> contacts = new ArrayList<>();
                            for (int i = 0; i < children.length(); i++) {
                                JSONObject department = children.getJSONObject(i);
                                String number = department.getString("memberCount");
                                if ("0".equals(number)) {
                                    continue;
                                }
                                Contacts contact = new Contacts(department);
                                contacts.add(contact);
                            }

                            for (int i = 0; i < users.length(); i++) {
                                JSONObject user = users.getJSONObject(i);
                                Contacts userInfo = new Contacts(user);
                                contacts.add(userInfo);
                            }
                            mView.loadDataSuccess(contacts);
                            if ("1".equals(orgId)) {
                                SharedPreferenceUtils.clear(AppManager.mContext, SharedPreferenceUtils.getStringValue(AppManager.mContext, "cacheDate", "dateName", ""));
                                SharedPreferenceUtils.putStringValue(AppManager.mContext, "cacheDate", "dateName", DateUtils.getCurrentDate("yyyy-MM-dd"));
                                SharedPreferenceUtils.putStringValue(AppManager.mContext, DateUtils.getCurrentDate("yyyy-MM-dd"), "children", children.toString());
                                SharedPreferenceUtils.putStringValue(AppManager.mContext, DateUtils.getCurrentDate("yyyy-MM-dd"), "users" + orgId, users.toString());
                            } else {
                                SharedPreferenceUtils.putStringValue(AppManager.mContext, DateUtils.getCurrentDate("yyyy-MM-dd"), "children" + orgId, children.toString());
                                SharedPreferenceUtils.putStringValue(AppManager.mContext, DateUtils.getCurrentDate("yyyy-MM-dd"), "users" + orgId, users.toString());
                            }
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
                            mView.uidNull(code);
                        default:
                            break;
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                try {
                    mView.loadDataFailed(errorNo, strMsg);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                try {
                    mView.loadDataFinish();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
