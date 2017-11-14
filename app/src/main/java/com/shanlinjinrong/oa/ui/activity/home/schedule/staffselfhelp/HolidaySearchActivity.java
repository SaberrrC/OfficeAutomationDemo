package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retrofit.model.responsebody.HolidaySearchResponse;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.widget.ApproveDecorationLine;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.adapter.HolidayAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.contract.HolidaySearchContract;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.event.HolidayEvent;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.presenter.HolidaySearchPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.CustomDialogUtils;
import com.shanlinjinrong.oa.utils.YearDateSelected;
import com.shanlinjinrong.oa.utils.YearTimeSelectedListener;
import com.shanlinjinrong.oa.views.HolidaySearchItem;
import com.shanlinjinrong.views.common.CommonTopView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//假期查询
public class HolidaySearchActivity extends HttpBaseActivity<HolidaySearchPresenter> implements HolidaySearchContract.View, YearTimeSelectedListener {

    @BindView(R.id.top_view)
    CommonTopView mTopView;
    @BindView(R.id.tv_center_title)
    TextView tv_center_title;
    @BindView(R.id.ll_content)
    LinearLayout ll_content;
    @BindView(R.id.psname)
    HolidaySearchItem psname;
    @BindView(R.id.deptname)
    HolidaySearchItem deptname;
    @BindView(R.id.jobname)
    HolidaySearchItem jobname;
    @BindView(R.id.lastdayorhour)
    HolidaySearchItem lastdayorhour;
    @BindView(R.id.changelength)
    HolidaySearchItem changelength;
    @BindView(R.id.curdayorhour)
    HolidaySearchItem curdayorhour;
    @BindView(R.id.realdayorhour)
    HolidaySearchItem realdayorhour;
    @BindView(R.id.ll_select_time)
    LinearLayout mLlSelectTime;
    @BindView(R.id.yidayorhour)
    HolidaySearchItem yidayorhour;
    @BindView(R.id.restdayorhour)
    HolidaySearchItem restdayorhour;
    @BindView(R.id.freezedayorhour)
    HolidaySearchItem freezedayorhour;
    @BindView(R.id.usefulrestdayorhour)
    HolidaySearchItem usefulrestdayorhour;
    @BindView(R.id.tv_select_time)
    TextView mTvSelectTime;
    @BindView(R.id.tv_no_network)
    TextView mTvNoNetwork;
    @BindView(R.id.sv_container_layout)
    ScrollView mSvContainerLayout;

    private CustomDialogUtils mDialog;
    private HolidayAdapter mTypeAdapter;
    private String searchType;
    private String searchYear;
    private YearDateSelected mYearDateSelected;
    private List<String> mDate = new ArrayList<>();
    private List<String> data = new ArrayList<>();
    private int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_search);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initData();
        initView();
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private void initData() {
        psname.setTextViewKey("姓名");
        psname.setTextViewValue("");
        deptname.setTextViewKey("部门");
        deptname.setTextViewValue("");
        jobname.setTextViewKey("职位名称");
        jobname.setTextViewValue("");
        lastdayorhour.setTextViewKey("上期结余");
        changelength.setTextViewKey("调整时长");
        curdayorhour.setTextViewKey("享有");
        realdayorhour.setTextViewKey("当前享有");
        yidayorhour.setTextViewKey("已休");
        restdayorhour.setTextViewKey("结余");
        freezedayorhour.setTextViewKey("冻结");
        usefulrestdayorhour.setTextViewKey("可用");
        mTopView.setAppTitle("");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        mTvSelectTime.setText(simpleDateFormat.format(calendar.getTimeInMillis()));
        mDate.add(simpleDateFormat.format(calendar.getTimeInMillis()));
        for (int i = 0; i < 1; i++) {
            calendar.add(Calendar.YEAR, -1);
            String date = simpleDateFormat.format(calendar.getTimeInMillis());
            mDate.add(0, date);
        }
        searchYear = Calendar.getInstance().get(Calendar.YEAR) + "";
        searchType = "0";
        data.add("年假查询");
        data.add("带薪病假");
        data.add("转调休");
        mYearDateSelected = new YearDateSelected(HolidaySearchActivity.this, HolidaySearchActivity.this, mDate, "选择查询时间");
        mPresenter.getData(searchYear, searchType);
    }

    private void initView() {
        tv_center_title.setText(data.get(0));
        tv_center_title.setOnClickListener(view -> {
            selectedDialog();
        });
        mLlSelectTime.setOnClickListener(view -> {
            mYearDateSelected.showBeginTimeView();
        });
    }

    private void selectedDialog() {
        try {
            //获取屏幕高宽
            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            int windowsHeight = metric.heightPixels;
            int windowsWight = metric.widthPixels;
            View view = initTypeData();
            CustomDialogUtils.Builder builder = new CustomDialogUtils.Builder(this);
            switch (getIntent().getIntExtra("type", -1)) {
                case 2:
                    mDialog = builder.cancelTouchout(false)
                            .view(view)
                            .heightpx((int) (windowsHeight / 2.5))
                            .widthpx((int) (windowsWight / 1.1))
                            .style(R.style.dialog)
                            .build();
                    break;
                default:
                    mDialog = builder.cancelTouchout(false)
                            .view(view)
                            .heightpx(view.getHeight())
                            .widthpx((int) (windowsWight / 1.1))
                            .style(R.style.dialog)
                            .build();
                    break;
            }
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
            mDialog.show();
            mDialog.setCanceledOnTouchOutside(true);
            mTypeAdapter.notifyDataSetChanged();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    //初始化Dialog数据
    private View initTypeData() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_common_selected_type, null);
        RecyclerView rvSelectedType = (RecyclerView) inflate.findViewById(R.id.rv_selected_type);
        TextView tvTitle = (TextView) inflate.findViewById(R.id.tv_common_type_title);
        View lineView = inflate.findViewById(R.id.tv_line_view);
        tvTitle.setVisibility(View.GONE);
        lineView.setVisibility(View.GONE);
        mTypeAdapter = new HolidayAdapter(this, data);
        rvSelectedType.setLayoutManager(new LinearLayoutManager(this));
        rvSelectedType.addItemDecoration(new ApproveDecorationLine(this));
        rvSelectedType.setAdapter(mTypeAdapter);
        return inflate;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void removeDeatls(HolidayEvent holidayEvent) {
        searchType = holidayEvent.getPosition() + "";
        mPosition = holidayEvent.getPosition();
        tv_center_title.setText(data.get(holidayEvent.getPosition()));
        mPresenter.getData(searchYear, searchType);
        mDialog.dismiss();
    }

    @Override
    public void onSelected(String date, int position) {
        searchYear = date;
        mTvSelectTime.setText(searchYear);
        mPresenter.getData(searchYear, searchType);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void uidNull(int code) {
        catchWarningByCode(code);
    }

    @Override
    public void showLoading() {
        showLoadingView();
    }

    @Override
    public void getDataSuccess(List<HolidaySearchResponse> myAttandanceResponse) {
        if (myAttandanceResponse.size() > 0) {
            mTvNoNetwork.setVisibility(View.GONE);
            mSvContainerLayout.setVisibility(View.VISIBLE);
            HolidaySearchResponse holidaySearchResponse = myAttandanceResponse.get(0);
            psname.setTextViewValue(holidaySearchResponse.getPsname());
            deptname.setTextViewValue(holidaySearchResponse.getDeptname());
            jobname.setTextViewValue(holidaySearchResponse.getJobname());
            if (mPosition != 2) {
                lastdayorhour.setTextViewValue(holidaySearchResponse.getLastdayorhour() + "天");
                changelength.setTextViewValue(holidaySearchResponse.getChangelength() + "天");
                curdayorhour.setTextViewValue(holidaySearchResponse.getCurdayorhour() + "天");
                realdayorhour.setTextViewValue(holidaySearchResponse.getRealdayorhour() + "天");
                yidayorhour.setTextViewValue(holidaySearchResponse.getYidayorhour() + "天");
                restdayorhour.setTextViewValue(holidaySearchResponse.getRestdayorhour() + "天");
                freezedayorhour.setTextViewValue(holidaySearchResponse.getFreezedayorhour() + "天");
                usefulrestdayorhour.setTextViewValue(holidaySearchResponse.getUsefulrestdayorhour() + "天");
            } else {
                lastdayorhour.setTextViewValue(holidaySearchResponse.getLastdayorhour() + "小时");
                changelength.setTextViewValue(holidaySearchResponse.getChangelength() + "小时");
                curdayorhour.setTextViewValue(holidaySearchResponse.getCurdayorhour() + "小时");
                realdayorhour.setTextViewValue(holidaySearchResponse.getRealdayorhour() + "小时");
                yidayorhour.setTextViewValue(holidaySearchResponse.getYidayorhour() + "小时");
                restdayorhour.setTextViewValue(holidaySearchResponse.getRestdayorhour() + "小时");
                freezedayorhour.setTextViewValue(holidaySearchResponse.getFreezedayorhour() + "小时");
                usefulrestdayorhour.setTextViewValue(holidaySearchResponse.getUsefulrestdayorhour() + "小时");
            }
        } else {
            mTvNoNetwork.setText("暂无假期数据！");
        }
    }

    @Override
    public void getDataFailed(int errCode, String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        mTvNoNetwork.setVisibility(View.VISIBLE);
        mSvContainerLayout.setVisibility(View.GONE);
        mTvNoNetwork.setText(msg);
    }

    @Override
    public void getDataFinish() {
        hideLoadingView();
    }
}
