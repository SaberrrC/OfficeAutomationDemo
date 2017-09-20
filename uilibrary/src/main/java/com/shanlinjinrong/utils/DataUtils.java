package com.shanlinjinrong.utils;

import com.shanlinjinrong.bean.DataItemDetail;
import com.shanlinjinrong.bean.DataItemResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by 丁 on 2017/9/15.
 */

public class DataUtils {
    public static DataItemDetail jsonToDataItemDetail(String jsonStr) {
        DataItemDetail detail = new DataItemDetail();
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            // TODO: 2017/9/15 单层json的解析过程
            detail.setMessage(jsonObject.optString("message"));
            detail.setCode(jsonObject.optInt("code"));

            // TODO: 2017/9/15 数据
            JSONObject data = jsonObject.getJSONObject("");


            Iterator<String> keys = data.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                Object value = data.opt(key);
                detail.setStringValue(key, value.toString());
            }

        } catch (JSONException e) {
            detail.setHasError(true);
            detail.setMessage(e.getMessage());
            e.printStackTrace();
        }

        return detail;
    }

    public static DataItemResult jsonToDataItemResult(String jsonStr) {
        DataItemResult result = new DataItemResult();

        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            // TODO: 2017/9/15 多层json的解析过程

            JSONObject data = jsonObject.getJSONObject("");


            Iterator<String> keys = data.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                Object value = data.opt(key);
                // TODO: 2017/9/15 看数据结构了
                if ("totalcount".equals(key)) {
                    result.maxCount = data.getInt("totalcount");
                } else if (value instanceof JSONArray) {
                    JSONArray jsonArray = data.getJSONArray(key);

                }
            }

        } catch (JSONException e) {
            result.setHasError(true);
            result.setMessage(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
