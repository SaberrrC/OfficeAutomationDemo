package com.shanlinjinrong.oa.ui.activity.my;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.widget.ApproveDecorationLine;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.adapter.HolidayAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.event.HolidayEvent;
import com.shanlinjinrong.oa.ui.activity.my.contract.ModificationEmailContract;
import com.shanlinjinrong.oa.ui.activity.my.presenter.ModificationEmailPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.CustomDialogUtils;
import com.shanlinjinrong.views.common.CommonTopView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

//修改邮箱
public class ModificationEmailActivity extends HttpBaseActivity<ModificationEmailPresenter> implements ModificationEmailContract.View {

    @BindView(R.id.top_view)
    CommonTopView mTopView;
    @BindView(R.id.et_email_redact)
    EditText mEtEmailRedact;
    @BindView(R.id.tv_email_selected)
    TextView mTvEmailSelected;
    @BindView(R.id.tv_tips)
    TextView mTvTips;

    private List<String> mData;
    private CustomDialogUtils mDialog;
    private HolidayAdapter mTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_email);
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
        mData = new ArrayList<>();
        mData.add("@shanlinjinrong.com");
        mData.add("@shanlinbao.com");
        mData.add("@shanlincaifu.com");
    }

    private void initView() {
        mTvEmailSelected.setText(mData.get(0));
        mTopView.getRightView().setOnClickListener(view -> {
            if (check()) {
                mPresenter.modificationEmail(mEtEmailRedact.getText().toString().trim() + mTvEmailSelected.getText().toString());
            }
        });

        try {
            //EditText 自动搜索,间隔->输入停止1秒后自动搜索
            RxTextView.textChanges(mEtEmailRedact)
                    .debounce(500, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(charSequence -> {
                        check();
                    });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private boolean check() {
        if (TextUtils.isEmpty(mEtEmailRedact.getText().toString().trim()) && mEtEmailRedact.getText().toString().length() > 0) {
            mTvTips.setText("邮箱名不能为空！");
            return false;
        }

        for (int i = 0; i < mEtEmailRedact.getText().toString().length(); i++) {
            if (mEtEmailRedact.getText().toString().substring(i, i + 1).equals(" ")) {
                mTvTips.setText("邮箱名不能含有空格！");
                return false;
            }
        }

        for (int i = 0; i < mEtEmailRedact.getText().toString().length(); i++) {
            String str = mEtEmailRedact.getText().toString().substring(i, i + 1);
            if (Pattern.matches("[\u4E00-\u9FA5]", str)) {
                mTvTips.setText("邮箱名不能含有中文！");
                return false;
            }
        }
        mTvTips.setText("");
        return true;
    }

    @OnClick(R.id.tv_email_selected)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_email_selected:
                selectedDialog();
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void removeDeatls(HolidayEvent holidayEvent) {
        mTvEmailSelected.setText(mData.get(holidayEvent.getPosition()));
        mDialog.dismiss();
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
        tvTitle.setText("请选择邮箱类型");
        mTypeAdapter = new HolidayAdapter(this, mData);
        rvSelectedType.setLayoutManager(new LinearLayoutManager(this));
        rvSelectedType.addItemDecoration(new ApproveDecorationLine(this));
        rvSelectedType.setAdapter(mTypeAdapter);
        return inflate;
    }

    @Override
    public void uidNull(String code) {
        catchWarningByCode(code);
    }

    @Override
    public void modificationEmailSuccess() {
        hideLoadingView();
        AppConfig.getAppConfig(this).set(AppConfig.PREF_KEY_USER_EMAIL, mEtEmailRedact.getText().toString() + mTvEmailSelected.getText().toString());
        setResult(101);
        showToast("修改成功！");
        finish();
    }

    @Override
    public void modificationEmailFailed(int errorNo, String strMsg) {
        hideLoadingView();
        if (strMsg.equals("auth error")) {
            catchWarningByCode(ApiJava.NOT_EXIST_TOKEN);
            return;
        }
        if (!TextUtils.isEmpty(strMsg)) {
            showToast(strMsg);
            return;
        }
        showToast("修改邮箱失败！");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
