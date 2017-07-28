package com.itcrm.GroupInformationPlatform.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imageutils.BitmapUtil;
import com.hyphenate.chat.EMClient;
import com.itcrm.GroupInformationPlatform.R;
import com.itcrm.GroupInformationPlatform.common.Api;
import com.itcrm.GroupInformationPlatform.common.Constants;
import com.itcrm.GroupInformationPlatform.manager.AppConfig;
import com.itcrm.GroupInformationPlatform.ui.PermissionListener;
import com.itcrm.GroupInformationPlatform.ui.base.BaseActivity;
import com.itcrm.GroupInformationPlatform.utils.BitmapUtils;
import com.itcrm.GroupInformationPlatform.utils.FileUtils;
import com.itcrm.GroupInformationPlatform.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * <h3>Description: 用户信息 </h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/8/31.<br />
 */
public class UserInfoActivity extends BaseActivity {

    public static final int UPDATE_PHONE = 1;//修改电话
    public static final int UPDATE_PROTRAIT = 2;//修改头像
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.user_portrait)
    SimpleDraweeView userPortrait;
    @Bind(R.id.user_sex)
    TextView userSex;
    @Bind(R.id.user_department)
    TextView userDepartment;
    @Bind(R.id.user_post)
    TextView userPost;
    @Bind(R.id.user_phone)
    TextView userPhone;
    @Bind(R.id.layout_root)
    LinearLayout mRootView;
    @Bind(R.id.user_mails)
    TextView user_mails;
    @Bind(R.id.user_jopnumber)
    TextView user_jopnumber;

    public static final int PHOTO_REQUEST_CUT = 3;// 裁剪结果
    private String mtyb;//手机品牌

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        initToolBar();
        setTranslucentStatus(this);

        initData();
    }

    private void initData() {
        userPortrait.setImageURI(Uri.parse(AppConfig.getAppConfig(this).get(
                AppConfig.PREF_KEY_PORTRAITS)));
        userSex.setText(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_SEX));
        userPost.setText(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_POST_NAME));
        userDepartment.setText(AppConfig.getAppConfig(this).get(
                AppConfig.PREF_KEY_DEPARTMENT_NAME));
        userPhone.setText(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_PHONE));
        user_mails.setText(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_USER_EMAIL));
        user_jopnumber.setText(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_CODE));
    }

    @OnClick({R.id.user_portrait_box, R.id.user_sex_box, R.id.user_department_box,
            R.id.user_post_box, R.id.user_phone_box, R.id.user_date_box, R.id.btn_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_portrait_box:
                selectPortrait();
                break;
            case R.id.user_sex_box:
                break;
            case R.id.user_department_box:
                break;
            case R.id.user_post_box:
                break;
            case R.id.user_phone_box://修改电话
                startActivityForResult(
                        new Intent(UserInfoActivity.this, ModifyPhoneActivity.class), UPDATE_PHONE);
                break;
            case R.id.user_date_box:
                break;
            case R.id.btn_logout://登出
                showLogoutTips();
                break;
        }
    }

    private void selectPortrait() {
        requestRunTimePermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission
                .CAMERA}, new PermissionListener() {
            @Override
            public void onGranted() {
                Intent intent = new Intent(UserInfoActivity.this,
                        PublicImageSelectorActivity.class);
                intent.putExtra("text", "选择头像");
                startActivityForResult(intent, UPDATE_PROTRAIT);
            }

            @Override
            public void onDenied() {
                showToast("权限被拒绝！请手动设置");
            }
        });

    }

    /**
     * 剪切图片
     *
     * @param uri uri
     */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");

        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        //缩放开启，避免黑边
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
//        // 图片格式
//        if (!FileUtils.isFileExist(Constants.FileUrl.TEMP)) {
//            FileUtils.createSDDir(Constants.FileUrl.TEMP);
//        }
//        File file = new File(Constants.FileUrl.TEMP +
//                Calendar.getInstance().getTimeInMillis() + ".jpg"); // 以时间秒为文件名
//        intent.putExtra("output", Uri.fromFile(file));  // 专入目标文件
//        intent.putExtra("output", uri);  // 专入目标文件
//        intent.putExtra("outputFormat", "JPEG");
//        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        //需要判断版本号：6.0以上不返回data，否则null指针异常
        //21版本,小米5.0开始不使用返回uri的方式，其他手机6.0以上才不使用
        mtyb = android.os.Build.BRAND;// 手机品牌
        if (mtyb.equals("Xiaomi")) {
            intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
        } else {
            intent.putExtra("return-data", false);// true:不返回uri，false：返回uri
        }
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode ==
                UPDATE_PROTRAIT) {
            if (data != null) {
                LogUtils.e("data.getStringExtra(\"data\")->"+data.getStringExtra("data"));
                crop(Uri.parse(data.getStringExtra("data")));
//                crop(BitmapUtils.getImageContentUri(this,data.getStringExtra("data")));


            }
        } else if (requestCode == PHOTO_REQUEST_CUT) {
            if (data != null) {
                /****http://blog.csdn.net/qq_30379689/article/details/52171215*****/
                Bitmap bitmap = null;

                if (mtyb.equals("Xiaomi")) {
                    bitmap = data.getExtras().getParcelable("data");

                } else {
                    Uri uri = data.getData();
                    if (uri==null) {
                        return;
                    }
                    LogUtils.e("裁剪后Uri:" + uri.toString());
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                /************************/
                if (!FileUtils.isFileExist(Constants.FileUrl.TEMP)) {
                    FileUtils.createSDDir(Constants.FileUrl.TEMP);
                }
                //将要保存图片的路径
                File file = new File(Constants.FileUrl.TEMP +
                        "Cut_image_" + Calendar.getInstance().getTimeInMillis() + ".jpg");
                LogUtils.e("file->" + file.getAbsolutePath().toString());
                try {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                upLoadingPortrait(file);
            }

        } else if (resultCode == RESULT_OK && requestCode == UPDATE_PHONE) {
            initData();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 上传头像
     */
    private void upLoadingPortrait(final File file) {

        HttpParams params = new HttpParams();

        params.put("department_id", AppConfig.getAppConfig(this).getDepartmentId());
        params.put("file", "portrait");
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        params.put("portrait", file);


        initKjHttp().post(Api.PERSON_UPLOAD, params, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                LogUtils.e("更改头像后返回数据..." + t);
                try {
                    JSONObject jo = new JSONObject(t);
                    if (Api.getCode(jo) == Api.RESPONSES_CODE_OK) {

                        showToast("修改成功");
                        //上传成功，设置用户头像
                        //TODO 打包时候更改：智汇的这样写
//                        String portraitUri = "http://" + Api.getDataToJSONObject(jo).get("portrait");
                        //TODO 打包时候更改：善林的这样写
                        String portraitUri = Constants.SLPicBaseUrl + Api.getDataToJSONObject(jo).get("portrait");

                        AppConfig.getAppConfig(UserInfoActivity.this).set(
                                AppConfig.PREF_KEY_PORTRAITS, portraitUri);
                        userPortrait.setImageURI(Uri.parse(portraitUri));
                    } else if (Api.getCode(jo) == Api.RESPONSES_CODE_TOKEN_NO_MATCH) {
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
                super.onFailure(errorNo, strMsg);
                LogUtils.e("上传图片失败" + errorNo + strMsg);
                catchWarningByCode(errorNo);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    public void showLogoutTips() {

        @SuppressLint("InflateParams")
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_logout, null);
        final PopupWindow popupWindow = new PopupWindow(dialogView, LinearLayoutCompat.LayoutParams
                .WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, false);
        Button btnSubmit = (Button) dialogView.findViewById(R.id.btn_submit);
        Button btnCancel = (Button) dialogView.findViewById(R.id.btn_cancel);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    popupWindow.dismiss();
                    //退出环信登录
                    LogUtils.e("退出环信");
                    EMClient.getInstance().logout(true);

                    JPushInterface.setAlias(UserInfoActivity.this, "", new TagAliasCallback() {
                        @Override
                        public void gotResult(int i, String s, Set<String> set) {
                        }
                    });
                    JPushInterface.setTags(UserInfoActivity.this, null, null);

                    AppConfig.getAppConfig(UserInfoActivity.this).clearLoginInfo();
                    startActivity(new Intent(UserInfoActivity.this, LoginActivity.class));
                    MainController.instance.finish();
                    finish();
                } catch (Exception e) {
                    LogUtils.e("退出环信抛出异常" + e.toString());
                    AppConfig.getAppConfig(UserInfoActivity.this).clearLoginInfo();
                    startActivity(new Intent(UserInfoActivity.this, LoginActivity.class));
                    MainController.instance.finish();
                    finish();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        popupWindow.setFocusable(true);

        popupWindow.setAnimationStyle(R.style.dialog_pop_anim_style);
        popupWindow.showAtLocation(mRootView, Gravity.CENTER, 0, 0);
    }

    private void initToolBar() {
        if (toolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);
        tvTitle.setText("资料修改");
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