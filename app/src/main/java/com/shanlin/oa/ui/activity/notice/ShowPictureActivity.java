package com.shanlin.oa.ui.activity.notice;


import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.shanlin.oa.R;
import com.shanlin.oa.ui.base.BaseActivity;
import com.shanlin.oa.utils.LogUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.relex.photodraweeview.OnPhotoTapListener;
import me.relex.photodraweeview.OnViewTapListener;
import me.relex.photodraweeview.PhotoDraweeView;

/**
 * ProjectName: ZhiTongOA_BI
 * PackageName: com.itcrm.zhitongoa.bi.activities
 * Author:Created by Tsui on Date:2016/11/14 19:32
 * Description:单个图片
 */
public class ShowPictureActivity extends BaseActivity {
    @Bind(R.id.sd_single_picture)
    PhotoDraweeView sdSinglePicture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_picture);
        setTranslucentStatus(this);
        ButterKnife.bind(this);
        initData();
    }
    protected void initData() {
        LogUtils.e("picUrl--->"+getIntent().getStringExtra("picUrl"));
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

}
