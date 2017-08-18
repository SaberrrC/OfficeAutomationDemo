package com.shanlin.oa.api;

import com.shanlin.oa.common.Api;
import com.shanlin.oa.data.DataItemResult;
import com.shanlin.oa.net.MyKjHttp;

import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

/**
 * Created by 丁 on 2017/8/15.
 */

public class PhonebookApi {

    public static DataItemResult getSearchPhonebook(String oid, String isleader, String name) {

        HttpParams params = new HttpParams();
        params.put("oid",oid);
        params.put("isleader",isleader);
        params.put("name",name);
        MyKjHttp.getInstance().post(Api.PHONEBOOK_SEARCHPHONEBOOK, params, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
            }
        });
        return null;
    }
}
