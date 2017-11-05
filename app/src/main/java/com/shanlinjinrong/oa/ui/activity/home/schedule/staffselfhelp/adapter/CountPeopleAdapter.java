package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.adapter;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.retrofit.model.responsebody.CountResponse1;
import com.shanlinjinrong.oa.R;

import java.util.List;

/**
 * Created by Administrator on 2017/11/1 0001.
 */

public class CountPeopleAdapter extends BaseQuickAdapter<CountResponse1> {

    public CountPeopleAdapter( List<CountResponse1> data) {
        super(R.layout.count_people_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CountResponse1 s) {
        baseViewHolder.setText(R.id.tv_name,s.getPsname());
    }

}
