package com.shanlin.oa.ui.activity.notice;

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
import com.shanlin.oa.R;
import com.shanlin.oa.common.Api;
import com.shanlin.oa.manager.AppConfig;
import com.shanlin.oa.model.Notice;
import com.shanlin.oa.ui.base.BaseActivity;
import com.shanlin.oa.utils.LogUtils;
import com.shanlin.oa.utils.Utils;

import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ProjectName: dev-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by CXP on Date: 2016/9/22 18:04.
 * Description:单个公告通知详细页
 */
public class NoticesSingleInfoActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar_text_btn)
    TextView toolbarTextBtn;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_notice_detail_single_title_top)
    TextView mTvNoticeDetailSingleTitleTop;
    @Bind(R.id.tv_notice_detail_single_title)
    TextView mTvNoticeDetailSingleTitle;
    @Bind(R.id.tv_notice_detail_single_time)
    TextView mTvNoticeDetailSingleTime;
    @Bind(R.id.tv_notice_detail_single_content)
    TextView mTvNoticeDetailSingleContent;
    @Bind(R.id.ll_pictures)
    LinearLayout llPictures;
    private Notice notice;
    private String nid;
    LinearLayout.LayoutParams picParams;
    private List<String> imgsLists;

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
                        Intent intent = new Intent(NoticesSingleInfoActivity.this,
                                ShowPictureActivity.class);
                        try {
                            intent.putExtra("picUrl",picUrl);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        startActivity(intent);
                    }
                });
                LogUtils.e("url->"+Uri.parse(picUrl));
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
        HttpParams params = new HttpParams();
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        params.put("department_id", AppConfig.getAppConfig(this).getDepartmentId());
        params.put("nid", Nid);
        initKjHttp().post(Api.NOTICE_HAD_READ, params, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {

                LogUtils.e("标记已读"+t);
                super.onSuccess(t);
            }
        });
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
        ButterKnife.unbind(this);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
