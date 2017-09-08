package com.shanlinjinrong.oa.ui.fragment.presenter;

import android.util.Log;

import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.model.Contacts;
import com.shanlinjinrong.oa.model.User;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;
import com.shanlinjinrong.oa.ui.fragment.contract.TabContractsFragmentContract;
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
 * Created by 丁 on 2017/8/21.
 * TabContractsFragmentPresenter
 */
public class TabContractsFragmentPresenter extends HttpPresenter<TabContractsFragmentContract.View> implements TabContractsFragmentContract.Presenter {
    @Inject
    public TabContractsFragmentPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void autoSearch(String departmentId, String isleader, String name) {
        HttpParams params = new HttpParams();
        params.put("oid", departmentId);
        params.put("isleader", isleader);
        params.put("name", name);

        mKjHttp.post(Api.PHONEBOOK_SEARCHPHONEBOOK, params, new HttpCallBack() {
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
    public void loadData() {
        HttpParams params = new HttpParams();
        params.put("department_id", "");
        mKjHttp.jsonGet(Api.GET_CONTACTS, params, new HttpCallBack() {
            @Override
            public void onPreStart() {
                super.onPreStart();
                mView.loadDataStart();

            }

            @Override
            public void onFinish() {
                super.onFinish();
                mView.loadDataFinish();

            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                //System.out.println(t);
                LogUtils.e(t);
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (Api.getCode(jo)) {
                        case 000000:

                            List<Contacts> contacts = new ArrayList<>();

                            JSONArray jDepartment = Api.getDataToJSONObject(jo)
                                    .getJSONArray("children");
                            for (int i = 0; i < jDepartment.length(); i++) {
                                JSONObject d = jDepartment.getJSONObject(i);
                                if (!d.getString("memberCount").equals("0")) {
                                    Contacts c = new Contacts(d);
                                    contacts.add(c);
                                }
                            }
                            JSONArray jEmployee = Api.getDataToJSONObject(jo)
                                    .getJSONArray("users");
                            for (int i = 0; i < jEmployee.length(); i++) {
                                JSONObject jsonObject = jEmployee.getJSONObject(i);
                                Contacts c = new Contacts();

                                //TODO 修改部分
                                c.code = jsonObject.getString("code");
                                c.sex = jsonObject.getString("sex");
                                c.username = jsonObject.getString("username");
                                c.uid = jsonObject.getString("uid");
                                c.portraits = "http://" + jsonObject.getString("portraits");
                                c.postId = jsonObject.getString("post_id");
                                c.postTitle = jsonObject.getString("post_title");
                                c.phone = jsonObject.getString("phone");
                                c.email = jsonObject.getString("email");
                                c.departmentName = jsonObject.getString("department_name");
                                c.itemType = 1;
                                if (AppConfig.getAppConfig(AppManager.sharedInstance()).getDepartmentId().equals(jsonObject.getString("department_id"))) {
                                    c.isshow = "1";
                                } else {
                                    c.isshow = "0";
                                }

                                contacts.add(c);
                            }
                            mView.loadDataSuccess(contacts);
                            break;
                        case Api.RESPONSES_CODE_DATA_EMPTY:
                            mView.loadDataEmpty();
                            break;
                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
                            mView.loadDataTokenNoMatch(Api.getCode(jo));
                            break;
                    }
                } catch (JSONException e) {
                    System.out.println(e.toString());
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                mView.loadDataFailed(errorNo, strMsg);
                super.onFailure(errorNo, strMsg);
            }
        });
    }
}
