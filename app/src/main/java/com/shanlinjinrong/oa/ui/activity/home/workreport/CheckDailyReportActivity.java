package com.shanlinjinrong.oa.ui.activity.home.workreport;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.workreport.adapter.WorkReportLaunchListAdapter;
import com.shanlinjinrong.oa.ui.activity.home.workreport.bean.LaunchReportItem;
import com.shanlinjinrong.oa.ui.base.BaseActivity;
import com.shanlinjinrong.oa.views.AllRecyclerView;
import com.shanlinjinrong.views.common.CommonTopView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 丁 on 2017/9/21.
 * 审核日报页面
 */
public class CheckDailyReportActivity extends BaseActivity {
    public static int CLICK = 1; //可点击cell
    public static int WRITE_EVALUATION = 2;//输入评价cell
    public static int WRITE_SCORE = 3;// 分数cell


    @Bind(R.id.work_report_list)
    AllRecyclerView mWorkReportList;

    @Bind(R.id.ll_select_date)
    RelativeLayout mSelectDate; // 选择日期

    @Bind(R.id.tv_date)
    TextView mDate; // 日期

    @Bind(R.id.top_view)
    CommonTopView mTopView;//标题栏


    @Bind(R.id.report_scroll_view)
    ScrollView mScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_daily_report);
        ButterKnife.bind(this);

    }

    /**
     * 初始化整个列表的显示数据
     *
     * @return List<LaunchReportItem>
     */
    private List<LaunchReportItem> initListData() {
        List<LaunchReportItem> listData = new ArrayList<>();
        //时报 上午九点到12点
        for (int i = 9; i < 12; i++) {
            String title = i + ":00~" + (i + 1) + ":00";
            listData.add(new LaunchReportItem(title, getString(R.string.work_report_no_write), WorkReportLaunchListAdapter.CLICK_TYPE));
        }

        //下午一点到五点
        for (int i = 13; i < 17; i++) {
            String title = i + ":00~" + (i + 1) + ":00";
            listData.add(new LaunchReportItem(title, getString(R.string.work_report_no_write), WorkReportLaunchListAdapter.CLICK_TYPE));
        }

        //下午五点到五点半
        listData.add(new LaunchReportItem("17:00~17:30", getString(R.string.work_report_no_write), WorkReportLaunchListAdapter.CLICK_TYPE));

        //职业素养
        listData.add(new LaunchReportItem(getString(R.string.work_report_personal_behavior), "", WorkReportLaunchListAdapter.WRITE_TYPE, true, getString(R.string.work_report_professional_qualities)));
        listData.add(new LaunchReportItem(getString(R.string.work_report_environmental_hygiene), "", WorkReportLaunchListAdapter.WRITE_TYPE));
        listData.add(new LaunchReportItem(getString(R.string.work_report_save), "", WorkReportLaunchListAdapter.WRITE_TYPE));
        listData.add(new LaunchReportItem(getString(R.string.work_report_communication_skills), "", WorkReportLaunchListAdapter.WRITE_TYPE));
        listData.add(new LaunchReportItem(getString(R.string.work_report_appearance), "", WorkReportLaunchListAdapter.WRITE_TYPE));
        listData.add(new LaunchReportItem(getString(R.string.work_report_work_discipline), "", WorkReportLaunchListAdapter.WRITE_TYPE));

        //团队合作
        listData.add(new LaunchReportItem(getString(R.string.work_report_initiative), "", WorkReportLaunchListAdapter.WRITE_TYPE, true, getString(R.string.work_report_teamwork)));
        listData.add(new LaunchReportItem(getString(R.string.work_report_cooperation), "", WorkReportLaunchListAdapter.WRITE_TYPE));
        listData.add(new LaunchReportItem(getString(R.string.work_report_dedication), "", WorkReportLaunchListAdapter.WRITE_TYPE));
        listData.add(new LaunchReportItem(getString(R.string.work_report_obey), "", WorkReportLaunchListAdapter.WRITE_TYPE));

        return listData;
    }
}
