package com.shanlinjinrong.oa.ui.activity.message.presenter;

import android.util.Log;

import com.facebook.stetho.Stetho;
import com.iflytek.cloud.thirdparty.S;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.model.Contacts;
import com.shanlinjinrong.oa.model.User;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.message.CallActivity;
import com.shanlinjinrong.oa.ui.activity.message.contract.SelectedGroupContactContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;
import com.shanlinjinrong.oa.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @Description：
 * @Auther：王凤旭
 * @Email：Tonnywfx@Gmail.com
 */

public class SelectedGroupContactPresenter extends HttpPresenter<SelectedGroupContactContract.View> implements SelectedGroupContactContract.Presenter {

    @Inject
    public SelectedGroupContactPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void QueryGroupContact(String orgId, ArrayList<String> account) {
        mKjHttp.cleanCache();
        HttpParams httpParams = new HttpParams();
        mKjHttp.jsonGet(Api.GET_CONTACTS + "?orgId=" + orgId, httpParams, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();
                try {
                    if (mView != null) {
                        mView.showLoading();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    switch (jsonObject.getString("code")) {
                        case ApiJava.REQUEST_CODE_OK:
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
                                if (user.getString("code").equals(AppConfig.getAppConfig(AppManager.mContext).getPrivateCode())) {
                                    continue;
                                }
                                Contacts userInfo = new Contacts(user);
                                contacts.add(userInfo);
                            }

                            try {
                                for (int i = 0; i < account.size(); i++) {
                                    for (int j = 0; j < contacts.size(); j++) {
                                        String remoteAccount = "sl_" + contacts.get(j).getCode();
                                        if (account.get(i).equals(remoteAccount)) {
                                            contacts.remove(j);
                                            break;
                                        }
                                    }
                                }
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }

                            if (mView != null)
                                mView.QueryGroupContactSuccess(contacts);
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
//                            mView.uidNull(0);
                        default:
                            mView.QueryGroupContactFailed(Integer.parseInt(jsonObject.getString("code")), jsonObject.getString("message"));
                            break;
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    if (mView != null)
                        mView.QueryGroupContactFailed(errorNo, strMsg);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                try {
                    if (mView != null) {
                        mView.hideLoading();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //TODO
    @Override
    public void QueryGroupContact(String orgId, String account) {
        mKjHttp.cleanCache();
        HttpParams httpParams = new HttpParams();
        mKjHttp.jsonGet(Api.GET_CONTACTS + "?orgId=" + orgId, httpParams, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();
                try {
                    if (mView != null) {
                        mView.showLoading();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    switch (jsonObject.getString("code")) {
                        case ApiJava.REQUEST_CODE_OK:
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
                                if (user.getString("code").equals(AppConfig.getAppConfig(AppManager.mContext).getPrivateCode())) {
                                    continue;
                                }
                                Contacts userInfo = new Contacts(user, account);
                                contacts.add(userInfo);
                            }

                            if (mView != null)
                                mView.QueryGroupContactSuccess(contacts);
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
//                            mView.uidNull(0);
                        default:
                            mView.QueryGroupContactFailed(Integer.parseInt(jsonObject.getString("code")), jsonObject.getString("message"));
                            break;
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    if (mView != null)
                        mView.QueryGroupContactFailed(errorNo, strMsg);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                try {
                    if (mView != null) {
                        mView.hideLoading();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void searchContact(String name, ArrayList<String> account) {
        mKjHttp.cleanCache();
        mKjHttp.jsonGet(Api.PHONEBOOK_SEARCHPHONEBOOK + "?name=" + name, new HttpParams(), new HttpCallBack() {
            @Override
            public void onFinish() {
                super.onFinish();
                try {
                    if (mView != null) {
                        mView.hideLoading();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPreStart() {
                super.onPreStart();
                try {
                    if (mView != null) {
                        mView.showLoading();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    switch (jsonObject.getString("code")) {
                        case ApiJava.REQUEST_CODE_OK:
                            JSONArray data = jsonObject.getJSONArray("data");
                            List<Contacts> contacts = new ArrayList<>();
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject user = data.getJSONObject(i);
                                Contacts contact = new Contacts();
                                contact.setUid(user.getString("uid"));
                                contact.setUsername(user.getString("username"));
                                contact.setPhone(user.getString("phone"));
                                contact.setEmail(user.getString("email"));
                                contact.setPortraits(Constants.SLPicBaseUrl + user.getString("portraits"));
                                contact.setSex(user.getString("sex"));
                                contact.setPostTitle(user.getString("post_title"));
                                contact.setPostId(user.getString("post_id"));
                                contact.setDepartmentId(user.getString("department_id"));
                                contact.setDepartmentName(user.getString("department_name"));
                                contact.setCode(user.getString("code"));
                                contact.setChecked(false);
                                contact.setItemType(1);
                                contacts.add(contact);
                            }

                            try {
                                for (int i = 0; i < account.size(); i++) {
                                    for (int j = 0; j < contacts.size(); j++) {
                                        String remoteAccount = "sl_" + contacts.get(j).getCode();
                                        if (account.get(i).equals(remoteAccount)) {
                                            contacts.remove(j);
                                            break;
                                        }
                                    }
                                }
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }

                            if (mView != null)
                                mView.searchContactSuccess(contacts);
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
//                            mView.uidNull(0);
                        default:
                            mView.searchContactFailed(Integer.parseInt(jsonObject.getString("code")), jsonObject.getString("message"));
                            break;
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    if (mView != null) {
                        mView.searchContactFailed(errorNo, strMsg);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void QueryGroupContact(String orgId) {
        mKjHttp.cleanCache();
        HttpParams httpParams = new HttpParams();
        mKjHttp.jsonGet(Api.GET_CONTACTS + "?orgId=" + orgId, httpParams, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();
                try {
                    if (mView != null) {
                        mView.showLoading();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    switch (jsonObject.getString("code")) {
                        case ApiJava.REQUEST_CODE_OK:
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
                                if (user.getString("code").equals(AppConfig.getAppConfig(AppManager.mContext).getPrivateCode())) {
                                    continue;
                                }
                                Contacts userInfo = new Contacts(user);
                                contacts.add(userInfo);
                            }
                            if (mView != null)
                                mView.QueryGroupContactSuccess(contacts);
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
//                            mView.uidNull(0);
                        default:
                            mView.QueryGroupContactFailed(Integer.parseInt(jsonObject.getString("code")), jsonObject.getString("message"));
                            break;
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    if (mView != null)
                        mView.QueryGroupContactFailed(errorNo, strMsg);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                try {
                    if (mView != null) {
                        mView.hideLoading();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void searchContact(String name) {
        mKjHttp.cleanCache();
        mKjHttp.jsonGet(Api.PHONEBOOK_SEARCHPHONEBOOK + "?name=" + name, new HttpParams(), new HttpCallBack() {
            @Override
            public void onFinish() {
                super.onFinish();
                try {
                    if (mView != null) {
                        mView.hideLoading();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPreStart() {
                super.onPreStart();
                try {
                    if (mView != null) {
                        mView.showLoading();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    switch (jsonObject.getString("code")) {
                        case ApiJava.REQUEST_CODE_OK:
                            JSONArray data = jsonObject.getJSONArray("data");
                            List<Contacts> contacts = new ArrayList<>();
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject user = data.getJSONObject(i);
                                if (user.getString("code").equals(AppConfig.getAppConfig(AppManager.mContext).getPrivateCode())) {
                                    continue;
                                }
                                Contacts contact = new Contacts();
                                contact.setUid(user.getString("uid"));
                                contact.setUsername(user.getString("username"));
                                contact.setPhone(user.getString("phone"));
                                contact.setEmail(user.getString("email"));
                                contact.setPortraits(user.getString("portraits"));
                                contact.setSex(user.getString("sex"));
                                contact.setPostTitle(user.getString("post_title"));
                                contact.setPostId(user.getString("post_id"));
                                contact.setDepartmentId(user.getString("department_id"));
                                contact.setDepartmentName(user.getString("department_name"));
                                contact.setCode(user.getString("code"));
                                contact.setChecked(false);
                                contact.setItemType(1);
                                contacts.add(contact);
                            }
                            if (mView != null)
                                mView.searchContactSuccess(contacts);
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
//                            mView.uidNull(0);
                        default:
                            mView.searchContactFailed(Integer.parseInt(jsonObject.getString("code")), jsonObject.getString("message"));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    if (mView != null) {
                        mView.searchContactFailed(errorNo, strMsg);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
