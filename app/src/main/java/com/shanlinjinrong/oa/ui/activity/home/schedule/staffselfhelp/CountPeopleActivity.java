package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.retrofit.model.responsebody.CountResponse1;
import com.example.retrofit.net.HttpMethods;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.event.PeopeNameEvent;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.adapter.CountPeopleAdapter;
import com.shanlinjinrong.oa.ui.base.BaseActivity;
import com.shanlinjinrong.views.common.CommonTopView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;

/**
 * Created by Administrator on 2017/10/27 0027.
 */

public class CountPeopleActivity  extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.top_view)
    CommonTopView mTopView;
    @Bind(R.id.iv_clear_hostory)
    ImageView mIvClearHistory;
    @Bind(R.id.et_content)
    EditText mEtSearchContent;
    @Bind(R.id.tv_confirm)
    TextView mTvConfirm;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    String mSearchContent="";
    List<CountResponse1> mData = new ArrayList<>();
    CountPeopleAdapter mCountPeopleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_people);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        View leftView = mTopView.getLeftView();
        mTopView.setAppTitle("统计人员");
        mIvClearHistory.setOnClickListener(this);
        mTvConfirm.setOnClickListener(this);
        leftView.setOnClickListener(view -> {
            finish();
        });
    }

    private void initData() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(CountPeopleActivity.this));
        mRecyclerView.addOnItemTouchListener(new SearchResultItemClick());
        mCountPeopleAdapter = new CountPeopleAdapter(mData);
        mRecyclerView.setAdapter(mCountPeopleAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //清除搜索记录
            case R.id.iv_clear_hostory:
                mEtSearchContent.setText("");
                mSearchContent="";
                break;
            //确认并搜索
            case R.id.tv_confirm:
                break;
        }
    }

    class SearchResultItemClick extends OnItemClickListener {
        @Override
        public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            PeopeNameEvent peopeNameEvent = new PeopeNameEvent();
            CountResponse1 countResponse1 = mData.get(i);
            EventBus.getDefault().post(countResponse1);
            finish();
        }
    }

    private void doHttp(){

        HttpMethods.getInstance().getAcountData(new Subscriber<ArrayList<CountResponse1>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ArrayList<CountResponse1> countResponse1s) {
                if(countResponse1s!=null&&countResponse1s.size()!=0){
                    mData.clear();
                    mData.addAll(countResponse1s);
                    mCountPeopleAdapter.notifyDataSetChanged();
                }

            }
        });
    }


}
