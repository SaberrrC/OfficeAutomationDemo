package com.shanlinjinrong.oa.ui.activity.home.workreport.presenter;

import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.model.selectContacts.Child;
import com.shanlinjinrong.oa.model.selectContacts.Group;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.workreport.contract.SelectContactActivityContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by 丁 on 2017/8/21.
 * 选择人员 Presenter
 */
public class SelectContactActivityPresenter extends HttpPresenter<SelectContactActivityContract.View> implements SelectContactActivityContract.Presenter {

    @Inject
    public SelectContactActivityPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void loadData(String departmentId, String searchName, final String selectChildId) {
        HttpParams params = new HttpParams();
        // TODO: 2017/8/28 新给的接口没有分组信息
        mKjHttp.jsonGet(ApiJava.SAME_ORGANIZATION + "?username=" + searchName + "&pagesize=" + Integer.MAX_VALUE, params, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (jo.getString("code")) {
                        case ApiJava.REQUEST_CODE_OK:
                            ArrayList<Group> groups = new ArrayList<>();
                            Child selectChild = null;
                            JSONObject data = Api.getDataToJSONObject(jo);
                            JSONArray usersData = data.getJSONArray("data");
                            Group group = new Group("0", usersData.getJSONObject(0).getString("organization"));
                            for (int j = 0; j < usersData.length(); j++) {
                                JSONObject joChild = usersData.getJSONObject(j);
                                Child child = new Child(joChild.getString("organization"),
                                        "",
                                        joChild.getString("post"),
                                        joChild.getString("id"),
                                        joChild.getString("username"), "", "", "",
                                        false);
                                if (joChild.getString("id").equals(selectChildId)) {
                                    child.setChecked(true);
                                    selectChild = child;
                                }
                                group.addChildrenItem(child);
                            }
                            groups.add(group);
                            mView.loadDataSuccess(groups, selectChild);
                            break;
                        case ApiJava.REQUEST_NO_RESULT:
                            mView.loadDataEmpty();
                            break;
                        default:
                            mView.loadDataFailed(0, jo.getString("message"));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                mView.loadDataFailed(errorNo, strMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mView.loadDataFinish();
            }
        });

        // TODO: 2017/8/28 原来的接口，依然可以用
//        params.put("department_id", departmentId);
//        params.put("search_name", searchName);
//        mKjHttp.post(Api.PUBLIC_COPY_CONTACTS, params, new HttpCallBack() {
//            @Override
//            public void onSuccess(String t) {
//                super.onSuccess(t);
//                try {
//                    JSONObject jo = new JSONObject(t);
//                    switch (Api.getCode(jo)) {
//                        case Api.RESPONSES_CODE_OK:
//                            ArrayList<Group> groups = new ArrayList<>();
//                            JSONObject data = Api.getDataToJSONObject(jo);
//                            JSONArray oname = data.getJSONArray("oname");
//                            JSONArray users = data.getJSONArray("users");
//                            for (int i = 0; i < oname.length(); i++) {
//                                Group group = new Group(String.valueOf(i), oname.getString(i));
//
//                                JSONArray usersArray = users.getJSONArray(i);
//                                for (int j = 0; j < usersArray.length(); j++) {
//                                    JSONObject joChild = usersArray.getJSONObject(j);
//
//                                    Child child = new Child(joChild.getString("oname"),
//                                            joChild.getString("portrait"),
//                                            joChild.getString("post"),
//                                            joChild.getString("uid"),
//                                            joChild.getString("username"), "", "", "",
//                                            false);
//                                    if (joChild.getString("uid").equals(selectChildId)) {
//                                        child.setChecked(true);
//                                    }
//                                    group.addChildrenItem(child);
//                                }
//                                groups.add(group);
//                            }
//                            mView.loadDataSuccess(groups);
//                            break;
//                        case Api.RESPONSES_CODE_DATA_EMPTY:
//                            mView.loadDataEmpty();
//                            break;
//                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
//                        case Api.RESPONSES_CODE_UID_NULL:
//                            mView.uidNull(Api.getCode(jo));
//                            break;
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int errorNo, String strMsg) {
//                super.onFailure(errorNo, strMsg);
//                mView.loadDataFailed(errorNo, strMsg);
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//                mView.loadDataFinish();
//            }
//        });
    }
}
