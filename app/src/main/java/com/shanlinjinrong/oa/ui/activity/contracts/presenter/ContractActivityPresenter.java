package com.shanlinjinrong.oa.ui.activity.contracts.presenter;

import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.model.Contacts;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.contracts.contract.ContractActivityContract;
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
 * Created by 丁 on 2017/8/21.
 */

//todo 数据 空 token
public class ContractActivityPresenter extends HttpPresenter<ContractActivityContract.View> implements ContractActivityContract.Presenter {

    @Inject
    public ContractActivityPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void loadData(String departmentId) {
        HttpParams params = new HttpParams();
        mKjHttp.get(ApiJava.GET_CONTACTS + "?id=" + departmentId, params, new HttpCallBack() {
            @Override
            public void onFinish() {
                super.onFinish();
                try {
                    if (mView != null){
                        mView.loadDataFinish();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e(t.toString());
                List<Contacts> items = new ArrayList<>();
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (ApiJava.getCode(jo)) {
                        case 000000:
                            JSONArray jDepartment = ApiJava.getDataToJSONObject(jo)
                                    .getJSONArray("children");
                            for (int i = 0; i < jDepartment.length(); i++) {
                                JSONObject d = jDepartment.getJSONObject(i);
                                if (!d.getString("memberCount").equals("0")) {
//                                    Contacts c = new Contacts(d);
//                                    items.add(c);
                                }
                            }
                            JSONArray jEmployee = ApiJava.getDataToJSONObject(jo)
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
                                //c.setIsshow(jsonObject.getString("isshow"));

                                items.add(c);
                            }
                            if (mView != null)
                                mView.loadDataSuccess(items);
                            break;
                    }
                } catch (JSONException e) {
                    System.out.println(e.toString());
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                try {
                    if (mView != null)
                        mView.loadDataFailed(errorNo, strMsg);
                    super.onFailure(errorNo, strMsg);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
