package com.shanlinjinrong.views.baserecycler;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.shanlinjinrong.views.baserecycler.base.RVBasicAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by k.huang on 2017/2/20.
 */
public class SampleActivity extends Activity {

    private RecyclerView mRecyclerView;
    private RVBasicAdapter<SampleCell> mSampleCellRVBasicAdapter;
    private List<String> mEntry;
    private Button mAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    protected void init(final Bundle savedInstanceState) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SampleActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mEntry = new ArrayList<>();
        for (int i = 0;i < 10;i ++){
            mEntry.add("小额贷款，1000万内不需要利息" + i);
        }
        mSampleCellRVBasicAdapter = new RVBasicAdapter<>();


        List<SampleCell> list = new ArrayList<>();
        for (int i = 0;i<mEntry.size();i++){
            list.add(new SampleCell(this,mEntry.get(i)));
        }

        mSampleCellRVBasicAdapter.setDataCells(list);
        mRecyclerView.setAdapter(mSampleCellRVBasicAdapter);

    }

}
