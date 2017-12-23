package com.shanlinjinrong.oa.ui.activity.notice;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.model.Notice;
import com.shanlinjinrong.oa.ui.activity.common.ShowPictureActivity;
import com.shanlinjinrong.oa.ui.activity.notice.contract.NoticesSingleInfoContract;
import com.shanlinjinrong.oa.ui.activity.notice.presenter.NoticesSingleInfoPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.LogUtils;
import com.shanlinjinrong.oa.utils.Utils;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ProjectName: dev-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by CXP on Date: 2016/9/22 18:04.
 * Description:单个公告通知详细页
 */
public class NoticesSingleInfoActivity extends HttpBaseActivity<NoticesSingleInfoPresenter> implements NoticesSingleInfoContract.View {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar_text_btn)
    TextView toolbarTextBtn;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_notice_detail_single_title_top)
    TextView mTvNoticeDetailSingleTitleTop;
    @BindView(R.id.tv_notice_detail_single_title)
    TextView mTvNoticeDetailSingleTitle;
    @BindView(R.id.tv_notice_detail_single_time)
    TextView mTvNoticeDetailSingleTime;
    @BindView(R.id.tv_notice_detail_single_content)
    TextView mTvNoticeDetailSingleContent;
    @BindView(R.id.ll_pictures)
    LinearLayout llPictures;
    private Notice notice;
    private String nid;
    LinearLayout.LayoutParams picParams;
    private List<String> imgsLists;
    private long lastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail_single_info);
        ButterKnife.bind(this);
        //android:id="@+id/layout_root"需要给根布局设置id
        setTranslucentStatus(this);
        initToolBar();
        initWidget();
        initData();
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private void setPicTureParams() {
        picParams = new LinearLayout.LayoutParams(Utils
                .dip2px(70),
                Utils.dip2px(70));
        picParams.setMargins(20, 0, 0, 0);
        picParams.gravity = Gravity.CENTER;
    }

    private void initData() {
        //singleNotice
        notice = (Notice) getIntent().getSerializableExtra("singleNotice");
        String type = "";
        switch (Integer.parseInt(notice.getType())) {
            case 1:
                type = "集团通告";
                break;
            case 2:
                type = "公司公告";
                break;
            case 3:
                type = "部门通知";
                break;
        }
        mTvNoticeDetailSingleTitleTop.setText(type);

        mTvNoticeDetailSingleTime.setText(notice.getCreatetime());
        mTvNoticeDetailSingleContent.setText(notice.getContent()
                .replace("&nbsp;", " ").replace("<br/>", "\n"));
        mTvNoticeDetailSingleTitle.setText(notice.getTitle());
        imgsLists = notice.getImgsLists();
        if (imgsLists.size() > 0) {
            for (int i = 0; i < imgsLists.size(); i++) {
                View picView = LayoutInflater.from(this).inflate(R.layout.add_pic_view, null);
                SimpleDraweeView pic = (SimpleDraweeView) picView.findViewById(R.id.sdv_pic);
                final String picUrl = (String) imgsLists.get(i);
                pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        long currentTime = Calendar.getInstance().getTimeInMillis();
                        if (currentTime - lastClickTime < 1000) {
                            lastClickTime = currentTime;
                            return;
                        }
                        lastClickTime = currentTime;
                        Intent intent = new Intent(NoticesSingleInfoActivity.this,
                                ShowPictureActivity.class);
                        try {
                            intent.putExtra("picUrl", picUrl);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        startActivity(intent);
                    }
                });
                LogUtils.e("url->" + Uri.parse(picUrl));
                pic.setImageURI(Uri.parse(picUrl));
                llPictures.addView(picView, picParams);
            }
        } else {
            llPictures.setVisibility(View.GONE);
        }

        nid = notice.getNid();
        markReadNotice(nid);
    }

    /**
     * 标记通知公告为已读
     */
    private void markReadNotice(String Nid) {
       mPresenter.markReadNotice(Nid, AppConfig.getAppConfig(this).getDepartmentId());
    }

    private void initWidget() {
        setPicTureParams();
    }

    private void initToolBar() {
        if (toolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);
        tvTitle.setText("公告通知");
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        tvTitle.setLayoutParams(lp);

        toolbarTextBtn.setVisibility(View.VISIBLE);
        toolbar.setNavigationIcon(R.drawable.toolbar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
    }



    @Override
    public void uidNull(int code) {

    }

    @Override
    public void markSuccess() {

    }
}
