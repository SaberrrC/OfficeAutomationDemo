package com.shanlinjinrong.oa.ui.activity.home.schedule.manage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.views.common.CommonTopView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScheduleWeekCalendarActivity extends AppCompatActivity {

    @BindView(R.id.topView)
    CommonTopView mTopView;
    @BindView(R.id.tv_nine)
    TextView      mTvNine;
    @BindView(R.id.tv_ten)
    TextView      mTvTen;
    @BindView(R.id.tv_eleven)
    TextView      mTvEleven;
    @BindView(R.id.tv_twelve)
    TextView      mTvTwelve;
    @BindView(R.id.tv_thirteen)
    TextView      mTvThirteen;
    @BindView(R.id.tv_fourteen)
    TextView      mTvFourteen;
    @BindView(R.id.tv_fifteen)
    TextView      mTvFifteen;
    @BindView(R.id.tv_sixteen)
    TextView      mTvSixteen;
    @BindView(R.id.tv_seventeen)
    TextView      mTvSeventeen;
    @BindView(R.id.tv_eighteen)
    TextView      mTvEighteen;
    @BindView(R.id.rv_week_calendar_list)
    RecyclerView  mRvWeekCalendarList;
    @BindView(R.id.ed_input_content)
    EditText      mEdInputContent;
    @BindView(R.id.ll_nine)
    LinearLayout  mLlNine;
    @BindView(R.id.ll_ten)
    LinearLayout  mLlTen;
    @BindView(R.id.ll_eleven)
    LinearLayout  mLlEleven;
    @BindView(R.id.ll_twelve)
    LinearLayout  mLlTwelve;
    @BindView(R.id.ll_thirteen)
    LinearLayout  mLlThirteen;
    @BindView(R.id.ll_fourteen)
    LinearLayout  mLlFourteen;
    @BindView(R.id.ll_fifteen)
    LinearLayout  mLlFifteen;
    @BindView(R.id.ll_sixteen)
    LinearLayout  mLlSixteen;
    @BindView(R.id.ll_seventeen)
    LinearLayout  mLlSeventeen;
    @BindView(R.id.ll_eighteen)
    LinearLayout  mLlEighteen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_week_calendar);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
    }

    private void initView() {
    }

    @OnClick({R.id.ll_nine, R.id.ll_ten, R.id.ll_eleven, R.id.ll_twelve, R.id.ll_thirteen, R.id.ll_fourteen, R.id.ll_fifteen, R.id.ll_sixteen, R.id.ll_seventeen, R.id.ll_eighteen, R.id.voice_input,R.id.ed_input_content})
    public void onViewClicked(View view) {

        initTextView();
        switch (view.getId()) {
            case R.id.ll_nine:
                mTvNine.setTextColor(getResources().getColor(R.color.text_common_blue_color));
                mTvTen.setTextColor(getResources().getColor(R.color.text_common_blue_color));

                mLlNine.setBackgroundColor(getResources().getColor(R.color.btn_white_pressed));
                break;
            case R.id.ll_ten:
                mTvTen.setTextColor(getResources().getColor(R.color.text_common_blue_color));
                mTvEleven.setTextColor(getResources().getColor(R.color.text_common_blue_color));

                mLlTen.setBackgroundColor(getResources().getColor(R.color.btn_white_pressed));
                break;
            case R.id.ll_eleven:
                mTvEleven.setTextColor(getResources().getColor(R.color.text_common_blue_color));
                mTvTwelve.setTextColor(getResources().getColor(R.color.text_common_blue_color));

                mLlEleven.setBackgroundColor(getResources().getColor(R.color.btn_white_pressed));
                break;
            case R.id.ll_twelve:
                mTvTwelve.setTextColor(getResources().getColor(R.color.text_common_blue_color));
                mTvThirteen.setTextColor(getResources().getColor(R.color.text_common_blue_color));

                mLlTwelve.setBackgroundColor(getResources().getColor(R.color.btn_white_pressed));
                break;
            case R.id.ll_thirteen:
                mTvThirteen.setTextColor(getResources().getColor(R.color.text_common_blue_color));
                mTvFourteen.setTextColor(getResources().getColor(R.color.text_common_blue_color));

                mLlThirteen.setBackgroundColor(getResources().getColor(R.color.btn_white_pressed));
                break;
            case R.id.ll_fourteen:
                mTvFourteen.setTextColor(getResources().getColor(R.color.text_common_blue_color));
                mTvFifteen.setTextColor(getResources().getColor(R.color.text_common_blue_color));

                mLlFourteen.setBackgroundColor(getResources().getColor(R.color.btn_white_pressed));
                break;
            case R.id.ll_fifteen:
                mTvFifteen.setTextColor(getResources().getColor(R.color.text_common_blue_color));
                mTvSixteen.setTextColor(getResources().getColor(R.color.text_common_blue_color));

                mLlFifteen.setBackgroundColor(getResources().getColor(R.color.btn_white_pressed));
                break;
            case R.id.ll_sixteen:
                mTvSixteen.setTextColor(getResources().getColor(R.color.text_common_blue_color));
                mTvSeventeen.setTextColor(getResources().getColor(R.color.text_common_blue_color));

                mLlSixteen.setBackgroundColor(getResources().getColor(R.color.btn_white_pressed));
                break;
            case R.id.ll_seventeen:
                mTvSeventeen.setTextColor(getResources().getColor(R.color.text_common_blue_color));
                mTvEighteen.setTextColor(getResources().getColor(R.color.text_common_blue_color));

                mLlSeventeen.setBackgroundColor(getResources().getColor(R.color.btn_white_pressed));
                break;
            case R.id.ll_eighteen:
                mTvEighteen.setTextColor(getResources().getColor(R.color.text_common_blue_color));

                mLlEighteen.setBackgroundColor(getResources().getColor(R.color.btn_white_pressed));
                break;
            case R.id.voice_input:
                mTvNine.setTextColor(getResources().getColor(R.color.text_common_blue_color));
                mTvTen.setTextColor(getResources().getColor(R.color.text_common_blue_color));
                break;

            default:
                break;
        }
    }

    private void initTextView() {
        mTvNine.setTextColor(getResources().getColor(R.color.gray_normal));
        mTvTen.setTextColor(getResources().getColor(R.color.gray_normal));
        mTvEleven.setTextColor(getResources().getColor(R.color.gray_normal));
        mTvTwelve.setTextColor(getResources().getColor(R.color.gray_normal));
        mTvThirteen.setTextColor(getResources().getColor(R.color.gray_normal));
        mTvFourteen.setTextColor(getResources().getColor(R.color.gray_normal));
        mTvFifteen.setTextColor(getResources().getColor(R.color.gray_normal));
        mTvSixteen.setTextColor(getResources().getColor(R.color.gray_normal));
        mTvSeventeen.setTextColor(getResources().getColor(R.color.gray_normal));
        mTvEighteen.setTextColor(getResources().getColor(R.color.gray_normal));

        mLlNine.setBackgroundColor(getResources().getColor(R.color.white));
        mLlTen.setBackgroundColor(getResources().getColor(R.color.white));
        mLlEleven.setBackgroundColor(getResources().getColor(R.color.white));
        mLlTwelve.setBackgroundColor(getResources().getColor(R.color.white));
        mLlThirteen.setBackgroundColor(getResources().getColor(R.color.white));
        mLlFourteen.setBackgroundColor(getResources().getColor(R.color.white));
        mLlFifteen.setBackgroundColor(getResources().getColor(R.color.white));
        mLlSixteen.setBackgroundColor(getResources().getColor(R.color.white));
        mLlSeventeen.setBackgroundColor(getResources().getColor(R.color.white));
        mLlEighteen.setBackgroundColor(getResources().getColor(R.color.white));
    }
}
