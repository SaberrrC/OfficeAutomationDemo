package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.adapter.PredetermineRecordAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeetingPredetermineRecordActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
//    @Bind(R.id.rv_meeting_date_selected)
//    RecyclerView mRvMeetingDateSelected;
    @Bind(R.id.selected_meeting_date1)
    CheckBox mSelectedMeetingDate1;
    @Bind(R.id.selected_meeting_date2)
    CheckBox mSelectedMeetingDate2;
    @Bind(R.id.selected_meeting_date3)
    CheckBox mSelectedMeetingDate3;
    @Bind(R.id.selected_meeting_date4)
    CheckBox mSelectedMeetingDate4;
    @Bind(R.id.selected_meeting_date5)
    CheckBox mSelectedMeetingDate5;
    @Bind(R.id.selected_meeting_date6)
    CheckBox mSelectedMeetingDate6;
    @Bind(R.id.selected_meeting_date7)
    CheckBox mSelectedMeetingDate7;
    @Bind(R.id.selected_meeting_date8)
    CheckBox mSelectedMeetingDate8;
    @Bind(R.id.selected_meeting_date9)
    CheckBox mSelectedMeetingDate9;

    private PredetermineRecordAdapter mRecordAdapter;

    private int DateIndex;
    private List<CheckBox> mCheckBoxes = new ArrayList<>();
    //    private List<String> DataDate = new ArrayList<>();
    public static MeetingPredetermineRecordActivity mRecordActivity;

    private String beginDate;
    private String endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_predetermine_record);
        ButterKnife.bind(this);
//        initData();
        initView();


    }

//    private void initData() {
//        DataDate.add("9：00——10：00");
//        DataDate.add("10：00——11：00");
//        DataDate.add("11：00——12：00");
//        DataDate.add("12：00——13：00");
//        DataDate.add("13：00——14：00");
//        DataDate.add("15：00——16：00");
//        DataDate.add("16：00——17：00");
//        DataDate.add("17：00——18：00");
//    }

    private void initView() {
        mRecordActivity = this;
//        mRecordAdapter = new PredetermineRecordAdapter(DataDate);
//        mRvMeetingDateSelected.setLayoutManager(new LinearLayoutManager(this));
//        mRvMeetingDateSelected.setAdapter(mRecordAdapter);
//        mRecordAdapter.notifyDataSetChanged();

        mCheckBoxes.add(mSelectedMeetingDate1);
        mCheckBoxes.add(mSelectedMeetingDate2);
        mCheckBoxes.add(mSelectedMeetingDate3);
        mCheckBoxes.add(mSelectedMeetingDate4);
        mCheckBoxes.add(mSelectedMeetingDate5);
        mCheckBoxes.add(mSelectedMeetingDate6);
        mCheckBoxes.add(mSelectedMeetingDate7);
        mCheckBoxes.add(mSelectedMeetingDate8);
        mCheckBoxes.add(mSelectedMeetingDate9);
        for (int i = 0; i < mCheckBoxes.size(); i++) {
            mCheckBoxes.get(i).setOnCheckedChangeListener(this);
        }
    }

    int indexStart;

    @OnClick(R.id.btn_meeting_info_complete)
    public void onViewClicked() {
        beginDate = null;
        DateIndex = 0;
        for (int i = 0; i < mCheckBoxes.size(); i++) {
            if (mCheckBoxes.get(i).isChecked()) {
                if (i - DateIndex == 1 || (DateIndex == 0 && beginDate == null)) {
                    if (beginDate == null) {
                        indexStart = mCheckBoxes.get(i).getText().toString().indexOf("—");
                        beginDate = mCheckBoxes.get(i).getText().toString().substring(0, indexStart);
                    }
                    DateIndex = i;
                    endDate = mCheckBoxes.get(i).getText().toString().substring(indexStart + 2, mCheckBoxes.get(i).getText().toString().length());
                } else {
                    Toast.makeText(this, "只能预约连续的时间段", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

        }

        if (beginDate == null || endDate == null) {
            Toast.makeText(this, "请选择预约时间段", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, MeetingInfoFillOutActivity.class);
        intent.putExtra("isWriteMeetingInfo", true);
        intent.putExtra("beginDate", beginDate);
        intent.putExtra("endDate", endDate);
        intent.putExtra("meetingName", getIntent().getStringExtra("meetingName"));
        intent.putExtra("meetingPeopleNumber", getIntent().getStringExtra("meetingPeopleNumber"));
        intent.putExtra("meetingDevice", getIntent().getStringExtra("meetingDevice"));
        startActivity(intent);
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.selected_meeting_date1:
                if (mSelectedMeetingDate1.isChecked()) {
                    mSelectedMeetingDate1.setBackgroundColor(getResources().getColor(R.color.text_blue_color));
                } else {
                    mSelectedMeetingDate1.setBackgroundColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.selected_meeting_date2:
                if (mSelectedMeetingDate2.isChecked()) {
                    mSelectedMeetingDate2.setBackgroundColor(getResources().getColor(R.color.text_blue_color));
                } else {
                    mSelectedMeetingDate2.setBackgroundColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.selected_meeting_date3:
                if (mSelectedMeetingDate3.isChecked()) {
                    mSelectedMeetingDate3.setBackgroundColor(getResources().getColor(R.color.text_blue_color));
                } else {
                    mSelectedMeetingDate3.setBackgroundColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.selected_meeting_date4:
                if (mSelectedMeetingDate4.isChecked()) {
                    mSelectedMeetingDate4.setBackgroundColor(getResources().getColor(R.color.text_blue_color));
                } else {
                    mSelectedMeetingDate4.setBackgroundColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.selected_meeting_date5:
                if (mSelectedMeetingDate5.isChecked()) {
                    mSelectedMeetingDate5.setBackgroundColor(getResources().getColor(R.color.text_blue_color));
                } else {
                    mSelectedMeetingDate5.setBackgroundColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.selected_meeting_date6:
                if (mSelectedMeetingDate6.isChecked()) {
                    mSelectedMeetingDate6.setBackgroundColor(getResources().getColor(R.color.text_blue_color));
                } else {
                    mSelectedMeetingDate6.setBackgroundColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.selected_meeting_date7:
                if (mSelectedMeetingDate7.isChecked()) {
                    mSelectedMeetingDate7.setBackgroundColor(getResources().getColor(R.color.text_blue_color));
                } else {
                    mSelectedMeetingDate7.setBackgroundColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.selected_meeting_date8:
                if (mSelectedMeetingDate8.isChecked()) {
                    mSelectedMeetingDate8.setBackgroundColor(getResources().getColor(R.color.text_blue_color));
                } else {
                    mSelectedMeetingDate8.setBackgroundColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.selected_meeting_date9:
                if (mSelectedMeetingDate9.isChecked()) {
                    mSelectedMeetingDate9.setBackgroundColor(getResources().getColor(R.color.text_blue_color));
                } else {
                    mSelectedMeetingDate9.setBackgroundColor(getResources().getColor(R.color.white));
                }
                break;
        }
    }

}
