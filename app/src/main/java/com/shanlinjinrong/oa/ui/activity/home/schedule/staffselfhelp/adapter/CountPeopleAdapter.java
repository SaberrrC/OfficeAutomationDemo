package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.adapter;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlinjinrong.oa.R;

import java.util.List;

/**
 * Created by Administrator on 2017/11/1 0001.
 */

public class CountPeopleAdapter extends BaseQuickAdapter<String> {

    public CountPeopleAdapter( List<String> data) {
        super(R.layout.count_people_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String s) {
        baseViewHolder.setText(R.id.tv_name,"aaa");
    }

}
