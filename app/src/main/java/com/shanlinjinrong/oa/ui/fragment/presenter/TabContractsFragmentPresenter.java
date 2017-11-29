package com.shanlinjinrong.oa.ui.fragment.presenter;

import android.util.Log;

import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.model.Contacts;
import com.shanlinjinrong.oa.model.User;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;
import com.shanlinjinrong.oa.ui.fragment.contract.TabContractsFragmentContract;
import com.shanlinjinrong.oa.utils.DateUtils;
import com.shanlinjinrong.oa.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
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
        mKjHttp.jsonGet(Api.PHONEBOOK_SEARCHPHONEBOOK + "?name=" + name, new HttpParams(), new HttpCallBack() {
            @Override
            public void onFinish() {
                super.onFinish();
                mView.autoSearchFinish();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e("联系人返回数据-》" + t);
                List<User> users = new ArrayList<>();
                try {
                    JSONObject jo = new JSONObject(t);
                    if (jo.getString("code").equals("000000")) {
                        JSONArray jDepartment = jo.getJSONArray("data");
                        for (int i = 0; i < jDepartment.length(); i++) {
                            JSONObject jsonObject = jDepartment.getJSONObject(i);
                            Log.e("", "-------" + jsonObject.getString("username") + "----" + jsonObject.getString("department_name"));
                            User user = new User(jsonObject.getString("username"),
                                    jsonObject.getString("phone"),
                                    jsonObject.getString("portraits"),
                                    jsonObject.getString("sex"),
                                    jsonObject.getString("post_id"),
                                    jsonObject.getString("code"),
                                    jsonObject.getString("department_id"),
                                    jsonObject.getString("post_title"),
                                    jsonObject.getString("department_name"),
                                    jsonObject.getString("email"));
                            users.add(user);
                        }
                        mView.autoSearchSuccess(users);
                    } else if (Api.getCode(jo) == Api.RESPONSES_CODE_UID_NULL) {
                        mView.uidNull(Api.getCode(jo));
                    } else {
                        mView.autoSearchOther(Api.getInfo(jo));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                LogUtils.e(errorNo + "--" + strMsg);
                super.onFailure(errorNo, strMsg);
            }
        });
    }

    @Override
    public void loadData(String orgId) {
        mKjHttp.jsonGet(Api.GET_CONTACTS + "?orgId=" + orgId, new HttpParams(), new HttpCallBack() {

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
                    if (jsonObject.getString("code").equals(ApiJava.REQUEST_CODE_OK)) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONArray children = jsonObject1.getJSONArray("children");
                        JSONArray users = jsonObject1.getJSONArray("users");

                        List<Contacts> contacts = new ArrayList<>();
                        for (int i = 0; i < children.length(); i++) {
                            JSONObject department = children.getJSONObject(i);
                            String number = department.getString("memberCount");
                            if (number.equals("0")) {
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
                        if (orgId.equals("1")) {
                            SharedPreferenceUtils.clear(AppManager.mContext, SharedPreferenceUtils.getStringValue(AppManager.mContext, "cacheDate", "dateName", ""));
                            SharedPreferenceUtils.putStringValue(AppManager.mContext, "cacheDate", "dateName", DateUtils.getCurrentDate("yyyy-MM-dd"));
                            SharedPreferenceUtils.putStringValue(AppManager.mContext, DateUtils.getCurrentDate("yyyy-MM-dd"), "children", children.toString());
                            SharedPreferenceUtils.putStringValue(AppManager.mContext, DateUtils.getCurrentDate("yyyy-MM-dd"), "users" + orgId, users.toString());
                        } else {
                            SharedPreferenceUtils.putStringValue(AppManager.mContext, DateUtils.getCurrentDate("yyyy-MM-dd"), "children" + orgId, children.toString());
                            SharedPreferenceUtils.putStringValue(AppManager.mContext, DateUtils.getCurrentDate("yyyy-MM-dd"), "users" + orgId, users.toString());
                        }
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
