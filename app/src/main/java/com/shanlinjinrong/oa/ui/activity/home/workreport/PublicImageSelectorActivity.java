package com.shanlinjinrong.oa.ui.activity.home.workreport;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.base.BaseActivity;
import com.shanlinjinrong.oa.utils.FileUtils;
import com.shanlinjinrong.oa.utils.LogUtils;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelectorFragment;

/**
 * <h3>Description:  </h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/9/8.<br />
 */
public class PublicImageSelectorActivity extends BaseActivity implements
        MultiImageSelectorFragment.Callback {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    public static final int PHOTO_REQUEST_IMAGE = 1;// 裁剪结果

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_image_selector);
        ButterKnife.bind(this);
        String topText = getIntent().getStringExtra("text");
        initToolBar(topText);
        setTranslucentStatus(this);

        Bundle bundle = new Bundle();
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_MODE,
                MultiImageSelectorFragment.MODE_SINGLE);
        bundle.putBoolean(MultiImageSelectorFragment.EXTRA_SHOW_CAMERA, true);
        // 添加主Fragment到Activity
        getSupportFragmentManager().beginTransaction()
                .add(R.id.root, Fragment.instantiate(this,
                        MultiImageSelectorFragment.class.getName(), bundle))
                .commit();
    }

    @Override
    public void onSingleImageSelected(String path) {
        //path->/storage/emulated/0/DCIM/Camera/IMG_20170119_173226.jpg


        String filesSize = FileUtils.getAutoFileOrFilesSize(path);
        String size = filesSize.substring(0, filesSize.indexOf("."));
        String unit = filesSize.substring(filesSize.length() - 2, filesSize.length());
        //17.76KB  2.32MB
        LogUtils.e("filesSize->" + filesSize);
        LogUtils.e("文件大小->" + size);
        LogUtils.e("文件单位->" + unit);
        if (unit.equals("MB")) {
            if (Integer.parseInt(size)<20) {
                onSuccess(Uri.fromFile(new File(path)));
            }else{
                showToast("文件大小不能超过20MB");
                finish();
            }
        }else{
            onSuccess(Uri.fromFile(new File(path)));
        }

    }



    @Override
    public void onCameraShot(File file) {
        onSuccess(Uri.fromFile(file));
    }

    private void onSuccess(Uri uri) {
        //uri->file:///storage/emulated/0/DCIM/Camera/IMG_20170119_173226.jpg
        Intent data = new Intent();
        data.putExtra("data", uri.toString());
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onImageSelected(String path) {

    }

    @Override
    public void onImageUnselected(String path) {

    }

    private void initToolBar(String topText) {
        if (toolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);
        tvTitle.setText(topText);
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        tvTitle.setLayoutParams(lp);

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
}