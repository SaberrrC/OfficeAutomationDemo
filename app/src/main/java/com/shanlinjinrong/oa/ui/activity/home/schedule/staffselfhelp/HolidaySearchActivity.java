package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.retrofit.model.responsebody.HolidaySearchResponse;
import com.example.retrofit.net.HttpMethods;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.widget.ApproveDecorationLine;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.adapter.HolidayAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.event.HolidayEvent;
import com.shanlinjinrong.oa.ui.base.BaseActivity;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;

//假期查询
public class HolidaySearchActivity extends BaseActivity implements YearTimeSelectedListener {

    @Bind(R.id.top_view)
    CommonTopView mTopView;
    @Bind(R.id.tv_center_title)
    TextView tv_center_title;
    @Bind(R.id.ll_content)
    LinearLayout ll_content;
    @Bind(R.id.psname)
    HolidaySearchItem psname;
    @Bind(R.id.deptname)
    HolidaySearchItem deptname;
    @Bind(R.id.jobname)
    HolidaySearchItem jobname;
    @Bind(R.id.lastdayorhour)
    HolidaySearchItem lastdayorhour;
    @Bind(R.id.changelength)
    HolidaySearchItem changelength;
    @Bind(R.id.curdayorhour)
    HolidaySearchItem curdayorhour;
    @Bind(R.id.realdayorhour)
    HolidaySearchItem realdayorhour;
    @Bind(R.id.ll_select_time)
    LinearLayout mLlSelectTime;
    @Bind(R.id.yidayorhour)
    HolidaySearchItem yidayorhour;
    @Bind(R.id.restdayorhour)
    HolidaySearchItem restdayorhour;
    @Bind(R.id.freezedayorhour)
    HolidaySearchItem freezedayorhour;
    @Bind(R.id.usefulrestdayorhour)
    HolidaySearchItem usefulrestdayorhour;
    @Bind(R.id.tv_select_time)
    TextView mTvSelectTime;

    private CustomDialogUtils mDialog;
    private HolidayAdapter mTypeAdapter;
    private String searchType;
    private String searchYear;
    private YearDateSelected mYearDateSelected;
    private List<String> mDate = new ArrayList<>();
    private List<String> data = new ArrayList<>();

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

    private void initData() {
        psname.setTextViewKey("姓名");
        deptname.setTextViewKey("部门");
        jobname.setTextViewKey("职位名称");
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
        doHolidaySearch(searchYear, searchType);
    }

    private void initView() {
        tv_center_title.setText(data.get(0));
        tv_center_title.setOnClickListener(view -> {
            NonTokenDialog();
        });
        mLlSelectTime.setOnClickListener(view -> {
            mYearDateSelected.showBeginTimeView();
        });
    }

    private void NonTokenDialog() {
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
        tvTitle.setText(data.get(0));
        mTypeAdapter = new HolidayAdapter(this, data);
        rvSelectedType.setLayoutManager(new LinearLayoutManager(this));
        rvSelectedType.addItemDecoration(new ApproveDecorationLine(this));
        rvSelectedType.setAdapter(mTypeAdapter);
        return inflate;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void removeDeatls(HolidayEvent holidayEvent) {
        searchType = holidayEvent.getPosition() + "";
        tv_center_title.setText(data.get(holidayEvent.getPosition()));
        doHolidaySearch(searchYear, searchType);
        mDialog.dismiss();
    }

    public void doHolidaySearch(String year, String type) {
        HttpMethods.getInstance().getHolidaySearchData(searchYear, searchType, new Subscriber<ArrayList<HolidaySearchResponse>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(ArrayList<HolidaySearchResponse> holidaySearchResponses) {
                HolidaySearchResponse holidaySearchResponse = holidaySearchResponses.get(0);
                psname.setTextViewValue(holidaySearchResponse.getPsname());
                deptname.setTextViewValue(holidaySearchResponse.getDeptname());
                jobname.setTextViewValue(holidaySearchResponse.getJobname());
                lastdayorhour.setTextViewValue(holidaySearchResponse.getLastdayorhour());
                changelength.setTextViewValue(holidaySearchResponse.getChangelength());
                curdayorhour.setTextViewValue(holidaySearchResponse.getCurdayorhour());
                realdayorhour.setTextViewValue(holidaySearchResponse.getRealdayorhour());
                yidayorhour.setTextViewValue(holidaySearchResponse.getYidayorhour());
                restdayorhour.setTextViewValue(holidaySearchResponse.getRealdayorhour());
                freezedayorhour.setTextViewValue(holidaySearchResponse.getFreezedayorhour());
                usefulrestdayorhour.setTextViewValue(holidaySearchResponse.getUsefulrestdayorhour());
            }
        });
    }

    @Override
    public void onSelected(String date, int position) {
        searchYear = date;
        mTvSelectTime.setText(searchYear);
        doHolidaySearch(searchYear, searchType);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
