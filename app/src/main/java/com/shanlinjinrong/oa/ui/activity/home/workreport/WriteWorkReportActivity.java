package com.shanlinjinrong.oa.ui.activity.home.workreport;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.workreport.adapter.WorkReportAdapter;
import com.shanlinjinrong.oa.ui.activity.home.workreport.bean.HourReportBean;
import com.shanlinjinrong.views.common.CommonTopView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 丁 on 2017/8/21.
 * 填写日报页面
 */
public class WriteWorkReportActivity extends FragmentActivity implements WriteReportFragment.OnFragmentStartChangeListener, WriteReportFragment.OnPageBthClickListener {

    @Bind(R.id.top_view)
    CommonTopView mTopView;

    @Bind(R.id.view_pager)
    ViewPager mViewPager;

    private int mPosition; //条目位置

    private int mPageStatus;//是否来自审核页面

    private List<WriteReportFragment> mFragmentList;
    private List<HourReportBean> mHourReportList;

    private List<String> mTitles;

    private boolean isBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_wrok_report);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            mPosition = extra.getInt("position");
            mPageStatus = extra.getInt(WriteReportFragment.PAGE_STATUS);
            mTitles = createTitles(changeDateFormat(extra.getString("date", "")));
            mTopView.setAppTitle(mTitles.get(mPosition));
            mHourReportList = extra.getParcelableArrayList("hour_report_list");
        }

        mFragmentList = new ArrayList<>();

        //初始化页面数据
        for (int i = 0; i < 8; i++) {
            WriteReportFragment fragment = new WriteReportFragment();
            Bundle fragmentArg = new Bundle();
            fragmentArg.putParcelable("hour_report", mHourReportList.get(i));
            fragmentArg.putInt("page", i);
            fragmentArg.putInt(WriteReportFragment.PAGE_STATUS, mPageStatus);
            fragment.setArguments(fragmentArg);
            fragment.setChangeListener(this);
            fragment.setPageBtnClickListener(this);
            mFragmentList.add(fragment);
        }

        mViewPager.setAdapter(new WorkReportAdapter(getSupportFragmentManager(), mFragmentList));

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mTopView.setAppTitle(mTitles.get(position));
                mFragmentList.get(mPosition).fragmentChange(mPosition);
                mPosition = position;//当前页面位置
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });
        mViewPager.setCurrentItem(mPosition);
    }

    private String changeDateFormat(String date) {
        if (TextUtils.isEmpty(date))
            return "";
        String[] srtArr = date.split("-");
        return srtArr[0] + "年" + srtArr[1] + "月" + srtArr[2] + "日";
    }

    private List<String> createTitles(String date) {
        List<String> titles = new ArrayList<>();
        for (int i = 9; i < 12; i++) {
            String title = date + " " + i + ":00~" + (i + 1) + ":00";
            titles.add(title);
        }

        //下午一点到五点
        for (int i = 13; i < 17; i++) {
            String title = date + " " + i + ":00~" + (i + 1) + ":00";
            titles.add(title);
        }

        //下午五点到五点半
        titles.add(date + " " + "17:00~17:30");
        return titles;
    }

    //暂时没提示了
    public void showTip(String msg, final String posiStr, String negaStr) {
        @SuppressLint("InflateParams")
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_exit_editor, null);
        TextView title = (TextView) dialogView.findViewById(R.id.title);
        title.setText("提示");
        TextView message = (TextView) dialogView.findViewById(R.id.message);
        message.setText(msg);

        final AlertDialog alertDialog = new AlertDialog.Builder(this,
                R.style.AppTheme_Dialog).create();
        alertDialog.setView(dialogView);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, posiStr,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, negaStr,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                getResources().getColor(R.color.btn_text_logout));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                getResources().getColor(R.color.btn_text_logout));
    }


    public void onBackPressed() {
        isBack = true;
        int curPage = mViewPager.getCurrentItem();
        mFragmentList.get(curPage).fragmentChange(curPage);
    }

    /**
     * 设置回调数据
     */
    private void setFinishResult() {
        Intent intent = new Intent();
        Bundle extras = new Bundle();
        extras.putParcelableArrayList("hour_report_list", (ArrayList<? extends Parcelable>) mHourReportList);
        intent.putExtras(extras);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this);
    }

    @Override
    public void fragmentStartChange(int position, String planWork, String realWork, String selfEvaluate, String quantitative, String checkManEvaluate, String supervisorEvaluate) {
        mHourReportList.get(position)
                .setRealWork(realWork)
                .setWorkPlan(planWork)
                .setSelfEvaluate(selfEvaluate)
                .setQuantitative(quantitative)
                .setCheckManEvaluate(checkManEvaluate)
                .setSupervisorEvaluate(supervisorEvaluate);
        if (isBack) {
            setFinishResult();
        }
    }

    @Override
    public void onLastPageClick() {
        int curPage = mViewPager.getCurrentItem();
        if (curPage > 0) {
            mViewPager.setCurrentItem(--curPage);
        }

    }

    @Override
    public void onNextPageClick() {
        int curPage = mViewPager.getCurrentItem();
        if (curPage < mFragmentList.size() - 1) {
            mViewPager.setCurrentItem(++curPage);
        }
    }
}
