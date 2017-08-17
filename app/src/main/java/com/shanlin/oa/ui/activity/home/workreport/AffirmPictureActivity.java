package com.shanlin.oa.ui.activity.home.workreport;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.shanlin.oa.R;
import com.shanlin.oa.ui.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.relex.photodraweeview.OnPhotoTapListener;
import me.relex.photodraweeview.OnViewTapListener;
import me.relex.photodraweeview.PhotoDraweeView;

/**
 * ProjectName: ZhiTongOA_BI
 * PackageName: com.itcrm.zhitongoa.bi.activities
 * Author:Created by Tsui on Date:2016/11/14 19:32
 * Description:确认上传的图片activity
 */
public class AffirmPictureActivity extends BaseActivity {
    @Bind(R.id.sd_single_picture)
    PhotoDraweeView sdSinglePicture;

    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.toolbar_text_btn)
    TextView mTolbarTextBtn;
    private String picUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affirm_picture);
        setTranslucentStatus(this);
        ButterKnife.bind(this);
        initToolBar();
        initData();
    }

    protected void initData() {
        picUrl = getIntent().getStringExtra("picUrl");
        sdSinglePicture.setPhotoUri(Uri.parse(getIntent().getStringExtra("picUrl")));
        sdSinglePicture.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                finish();
            }
        });
        sdSinglePicture.setOnViewTapListener(new OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                finish();
            }
        });
    }

    private void initToolBar() {
        if (mToolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        mToolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(mToolbar);
        mTvTitle.setText("确认图片");
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        mTvTitle.setLayoutParams(lp);
        mTolbarTextBtn.setText("上传");
        mTolbarTextBtn.setVisibility(View.VISIBLE);
        mTolbarTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //picUrl
                Intent intent = new Intent();
                intent.putExtra("data", picUrl);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        mToolbar.setNavigationIcon(R.drawable.toolbar_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
