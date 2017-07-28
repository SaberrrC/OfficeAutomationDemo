package com.itcrm.GroupInformationPlatform.ui.draft;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.draft
 * Author:Created by Tsui on Date:2017/2/27 9:40
 * Description:工作汇报草稿类
 */
public class LaunchWorkReportDraft {


    public void setDraft(Context context, ConcurrentHashMap<String, String> map) {
        SharedPreferences.Editor editor = context.getSharedPreferences("work_report_draft",
                Context.MODE_PRIVATE).edit();

        Set<String> keys = map.keySet();
        for (String key : keys) {
            editor.putString(key, map.get(key));
        }

        editor.apply();
    }


    public String getDraft(Context context, String key) {
        return context.getSharedPreferences("work_report_draft",
                Context.MODE_PRIVATE).getString(key, "");

    }

    /**
     * @param context 设置whichDate为 "" ,在发起工作汇报的界面会判断，如果是"",那么，就不设置数据了
     */
    public void setwhichDate(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("work_report_draft",
                Context.MODE_PRIVATE).edit();
        editor.putString("whichDate", "");
        editor.putString("startDate", "");
        editor.putString("endDate", "");
        editor.putString("weeklyContent", "");
        editor.putString("reportContent", "");
        editor.putString("picUrl", "");
        editor.putString("copyId", "");
        editor.putString("copyId", "");
        editor.putString("copyNames", "");
        editor.apply();
    }

}
