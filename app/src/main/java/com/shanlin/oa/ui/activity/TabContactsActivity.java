package com.shanlin.oa.ui.activity;

import android.os.Bundle;

import com.shanlin.oa.R;
import com.shanlin.oa.ui.base.BaseActivity;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by Tsui on Date:2016/12/6 11:01
 * Description:名片activity
 */
public class TabContactsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_contacts_container);
        setTranslucentStatus(this);
        initWidget();
    }

    private void initWidget() {
    }

}
