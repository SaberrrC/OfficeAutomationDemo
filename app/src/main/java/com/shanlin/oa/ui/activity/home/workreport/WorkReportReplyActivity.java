package com.shanlin.oa.ui.activity.home.workreport;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.shanlin.oa.R;
import com.shanlin.oa.common.Api;
import com.shanlin.oa.common.Constants;
import com.shanlin.oa.manager.AppConfig;
import com.shanlin.oa.listener.PermissionListener;
import com.shanlin.oa.ui.base.BaseActivity;
import com.shanlin.oa.utils.BitmapUtils;
import com.shanlin.oa.utils.FileUtils;
import com.shanlin.oa.thirdParty.iflytek.IflytekUtil;
import com.shanlin.oa.utils.LogUtils;
import com.shanlin.oa.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by Tsui on Date:2016/11/11 14:18
 * Description:
 */
public class WorkReportReplyActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.btn_voice_input)
    Button mBtnVoiceInput;
    @Bind(R.id.et_reply)
    EditText mEtReply;
    @Bind(R.id.toolbar_text_btn)
    TextView toolbarTextBtn;
    @Bind(R.id.ll_pictures)
    LinearLayout mLlPictures;
    public static final int UPDATE_PROTRAIT = 2;//上传头像
    public static final int AFFIRM_PICTURE_RESULT = 4;// 确认图片请求码
    private boolean isChangePic = false;
    private String picTag;//图片tag
    private String rId;
    String content;//内容
    private LinearLayout.LayoutParams picParams;
    //存放上传图片的url
    private ConcurrentHashMap<String, String> picUrlMap = new ConcurrentHashMap<>();
    @Bind(R.id.tv_add_pic_tips)
    TextView mTvAddPicTips;
    private List<String> picUrls = new ArrayList<>();
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        ButterKnife.bind(this);
        initToolBar();
        setTranslucentStatus(this);
        initData();
    }

    private void initData() {
        rId = getIntent().getStringExtra("rid");
        mBtnVoiceInput.setOnClickListener(this);
        toolbarTextBtn.setOnClickListener(this);
        mTvAddPicTips.setOnClickListener(this);
        setPicTureParams();
    }

    private void setPicTureParams() {
        picParams = new LinearLayout.LayoutParams(Utils
                .dip2px(70),
                Utils.dip2px(70));
        picParams.setMargins(40, 0, 0, 0);
        picParams.gravity = Gravity.CENTER;
    }

    private void initToolBar() {
        if (toolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);
        tvTitle.setText("回复");
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        tvTitle.setLayoutParams(lp);
        toolbarTextBtn.setText("完成");
        toolbarTextBtn.setVisibility(View.VISIBLE);
        toolbar.setNavigationIcon(R.drawable.toolbar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((!(mEtReply.getText().toString().equals("")))) {
                    showBackTip("是否放弃编辑", "确定", "取消");
                } else {
                    finish();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_voice_input:
                showDialog();
                break;
            case R.id.tv_add_pic_tips:
                upLoadPicture();
                break;
            case R.id.toolbar_text_btn:
                if (mEtReply.getText().toString().trim().equals("")) {
                    showToast("工作汇报回复不能为空");
                    return;
                }
                finishReply();
                break;
        }
    }

    /**
     * 上传图片
     */
    private void upLoadPicture() {

        requestRunTimePermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission
                .CAMERA}, new PermissionListener() {
            @Override
            public void onGranted() {
                //TODO 上传图片
                Intent intent = new Intent(WorkReportReplyActivity.this,
                        PublicImageSelectorActivity.class);
                intent.putExtra("text", "上传图片");
                startActivityForResult(intent, UPDATE_PROTRAIT);
            }

            @Override
            public void onDenied() {
                showToast("权限被拒绝！请手动设置");
            }
        });


    }

    private void addPic(final String pic_url) {
        View view = LayoutInflater.from(WorkReportReplyActivity
                .this).inflate(R.layout.add_pic_view, null);
        SimpleDraweeView pic = (SimpleDraweeView) view.findViewById(R.id.sdv_pic);
        final ImageView ivDelete = (ImageView) view.findViewById(R.id.iv_delete);
        ivDelete.setVisibility(View.VISIBLE);
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout rl = (RelativeLayout) ivDelete.getParent();
                mLlPictures.removeView(rl);
                picUrlMap.remove(pic_url);
            }
        });
        if (isChangePic) {
            LogUtils.e("isChangePic-->picTag-->" + picTag);
            SimpleDraweeView iv = (SimpleDraweeView) mLlPictures.findViewWithTag(picTag);
            iv.setImageURI(pic_url);
            picUrlMap.put(picTag, pic_url);
            isChangePic = false;
            return;
        }
        pic.setImageURI(pic_url);
        pic.setTag(pic_url);
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChangePic = true;
                picTag = pic_url;
                LogUtils.e("picTag-->" + picTag);
                upLoadPicture();
            }
        });
        if (mLlPictures.getChildCount() > 2) {
            mTvAddPicTips.setVisibility(View.GONE);
            mTvAddPicTips.setVisibility(View.INVISIBLE);
        }
        mLlPictures.addView(view, picParams);
        if (mLlPictures.getChildCount() >= 1) {
            mTvAddPicTips.setGravity(Gravity.CENTER_VERTICAL);

        }
        picUrlMap.put(pic_url, pic_url);
        picUrls.add(pic_url);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {

            if (resultCode == RESULT_OK && requestCode == AFFIRM_PICTURE_RESULT) {

                addPicture(file);
            } else if (resultCode == RESULT_OK && requestCode == UPDATE_PROTRAIT) {
                if (data != null) {
                    /****http://www.jb51.net/article/56990.htm*****/
                    Bitmap bitmap = null;
                    //TODO 或者版本》6.0，需要做处理
                    Uri uri = Uri.parse(data.getStringExtra("data"));
                    try {
                        bitmap = BitmapUtils.zoomBitmap(MediaStore.Images.Media
                                .getBitmap(getContentResolver(), uri), Utils.dip2px(160), Utils.dip2px(160));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    /************************/
                    if (!FileUtils.isFileExist(Constants.FileUrl.TEMP)) {
                        FileUtils.createSDDir(Constants.FileUrl.TEMP);
                    }
                    //将要保存图片的路径
                    file = new File(Constants.FileUrl.TEMP +
                            "Cut_image_" + Calendar.getInstance().getTimeInMillis() + ".jpg");
                    LogUtils.e("file->" + file.getAbsolutePath().toString());
                    try {
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));

                        LogUtils.e("bitmap->" + bitmap.toString());
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, bos);
                        bos.flush();
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    Intent intent = new Intent(WorkReportReplyActivity.this,
                            AffirmPictureActivity.class);
                    intent.putExtra("picUrl", data.getStringExtra("data"));
                    LogUtils.e("11picUrl->" + data.getStringExtra("data"));
                    LogUtils.e("file.getName()->" + file.getName());
                    LogUtils.e("file.getPath()->" + file.getPath());
                    LogUtils.e("file.getAbsolutePath()->" + file.getAbsolutePath());
                    startActivityForResult(intent, AFFIRM_PICTURE_RESULT);
                }

            }
        }
    }

    /**
     * @param file 添加图片
     */
    private void addPicture(final File file) {
        upLoadingPortrait(file);
    }

    private void upLoadingPortrait(final File file) {
        showLoadingView();
        final HttpParams params = new HttpParams();

        params.put("department_id", AppConfig.getAppConfig(this).getDepartmentId());
        params.put("file", "report");
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        params.put("portrait", file);


        initKjHttp().post(Api.PIC_UPLOAD, params, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                hideLoadingView();
                try {
                    JSONObject jo = new JSONObject(t);
                    if (Api.getCode(jo) == Api.RESPONSES_CODE_OK) {
                        addPic("http://" + Api.getDataToJSONArray(jo).get(0));


                    } else if (Api.getCode(jo) == Api.RESPONSES_CODE_TOKEN_NO_MATCH) {
                        catchWarningByCode(Api.getCode(jo));
                    } else if (Api.getCode(jo) == Api.RESPONSES_CODE_UID_NULL) {
                        catchWarningByCode(Api.getCode(jo));
                    } else {
                        showToast(Api.getInfo(jo));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.onSuccess(t);
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                hideLoadingView();
                LogUtils.e("上传图片失败后--》" + strMsg);
                catchWarningByCode(errorNo);
            }

            @Override
            public void onFinish() {
                hideLoadingView();
                super.onFinish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if ((!(mEtReply.getText().toString().equals("")))) {
                    showBackTip("是否放弃编辑", "确定", "取消");
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void finishReply() {
        HttpParams params = new HttpParams();
        showLoadingView();
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        params.put("department_id", AppConfig.getAppConfig(this).getDepartmentId());
        params.put("content", mEtReply.getText().toString().trim());
        params.put("rid", rId);
        StringBuilder picBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : picUrlMap.entrySet()) {
            picBuilder.append(entry.getValue()).append(",");
        }
        LogUtils.e("picBuilder-->" + picBuilder.toString());
        params.put("img", picBuilder.toString());


        initKjHttp().post(Api.REPORT_REPLY, params, new HttpCallBack() {
            @Override
            public void onFinish() {
                super.onFinish();
                hideLoadingView();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e("工作汇报回复成功后返回数据：" + t.toString());
                JSONObject jo = null;
                try {
                    jo = new JSONObject(t);
                    if ((Api.getCode(jo) == Api.RESPONSES_CODE_OK)) {
                        showToast("发送成功");
                        finish();
                    }
                    if ((Api.getCode(jo) == Api.RESPONSES_CODE_UID_NULL)) {
                        catchWarningByCode(Api.getCode(jo));
                    } else {
                        showToast(Api.getInfo(jo));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                startActivity(new Intent(WorkReportReplyActivity.this, WorkReportListActivity.class));
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {

                super.onFailure(errorNo, strMsg);
                LogUtils.e(strMsg);
                showToast("工作汇报回复失败：" + strMsg);
            }
        });

    }

    private void showDialog() {
        requestRunTimePermission(new String[]{Manifest.permission.RECORD_AUDIO}, new PermissionListener() {
            @Override
            public void onGranted() {
                IflytekUtil iflytekUtil = new IflytekUtil(WorkReportReplyActivity.this, mEtReply);
                iflytekUtil.showIatDialog();
            }

            @Override
            public void onDenied() {
                showToast("录音权限被拒绝！请手动设置");
            }
        });
    }


}
