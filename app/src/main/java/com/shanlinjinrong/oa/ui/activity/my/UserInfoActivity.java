package com.shanlinjinrong.oa.ui.activity.my;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyphenate.EMCallBack;
import com.hyphenate.easeui.Constant;
import com.hyphenate.easeui.UserDetailsBean;
import com.hyphenate.easeui.db.FriendsInfoCacheSvc;
import com.hyphenate.easeui.utils.GlideRoundTransformUtils;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.listener.PermissionListener;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.thirdParty.huanxin.DemoHelper;
import com.shanlinjinrong.oa.ui.activity.login.LoginActivity;
import com.shanlinjinrong.oa.ui.activity.my.contract.UserInfoActivityContract;
import com.shanlinjinrong.oa.ui.activity.my.presenter.UserInfoActivityPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.FileUtils;
import com.shanlinjinrong.oa.utils.LogUtils;
import com.shanlinjinrong.oa.views.BaseBottomPushPopupWindow;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * <h3>Description: 用户信息 </h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/8/31.<br />
 */
public class UserInfoActivity extends HttpBaseActivity<UserInfoActivityPresenter> implements UserInfoActivityContract.View {

    public static final int UPDATE_PHONE    = 0x4;//修改电话
    public static final int UPDATE_PROTRAIT = 2;//修改头像
    @BindView(R.id.tv_title)
    TextView         tvTitle;
    @BindView(R.id.toolbar)
    Toolbar          toolbar;
    @BindView(R.id.user_portrait)
    SimpleDraweeView userPortrait;
    @BindView(R.id.user_sex)
    TextView         userSex;
    @BindView(R.id.user_department)
    TextView         userDepartment;
    @BindView(R.id.user_post)
    TextView         userPost;
    @BindView(R.id.user_phone)
    TextView         userPhone;
    @BindView(R.id.layout_root)
    LinearLayout     mRootView;
    @BindView(R.id.user_mails)
    TextView         user_mails;
    @BindView(R.id.user_jopnumber)
    TextView         user_jopnumber;

    private BaseBottomPushPopupWindow mPop;
    private static final int CODE_GALLERY_REQUEST = 0x1;//相册
    private static final int CODE_CAMERA_REQUEST  = 0x2;//拍照
    private static final int CROP_PICTURE_REQUEST = 0x3;//图片路径
    private static final int MODIFICATION_EMAIL   = 101;//邮箱修改

    private static final String TEMP_FILE_NAME   = "temp_icon.jpg";
    private static final String CAMERA_FILE_NAME = "camera_pic.jpg";

    private Uri icon_path;
    private Uri camera_path;
    private long lastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        initToolBar();
        setTranslucentStatus(this);
        initData();

        File dir = getExternalFilesDir("user_icon");
        File fileIconPath = new File(dir, TEMP_FILE_NAME);
        File fileCameraPath = new File(dir, CAMERA_FILE_NAME);

        try {
            if (!fileIconPath.exists()) {
                fileIconPath.createNewFile();
            }
            if (!fileCameraPath.exists()) {
                fileCameraPath.createNewFile();
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                // 修改名称和包名保持一致
                icon_path = FileProvider.getUriForFile(AppManager.mContext, "com.shanlinjinrong.oa.fileprovider", fileIconPath);
                camera_path = FileProvider.getUriForFile(AppManager.mContext, "com.shanlinjinrong.oa.fileprovider", fileCameraPath);
            } else {
                icon_path = Uri.fromFile(fileIconPath);
                camera_path = Uri.fromFile(fileCameraPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private void initData() {
        mPresenter.queryUserInfo();
        if (!TextUtils.isEmpty(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_PORTRAITS))) {
            Glide.with(AppManager.mContext)
                    .load(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_PORTRAITS))
                    .dontAnimate()
                    .error(R.drawable.ease_default_avatar)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transform(new CenterCrop(AppManager.mContext), new GlideRoundTransformUtils(AppManager.mContext, 5))
                    .placeholder(R.drawable.ease_default_avatar)
                    .into(userPortrait);
        } else {
            Glide.with(AppManager.mContext).load(R.drawable.ease_default_avatar).asBitmap().into(userPortrait);
        }

        userSex.setText(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_SEX));
        userPost.setText(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_POST_NAME));
        userDepartment.setText(AppConfig.getAppConfig(this).get(
                AppConfig.PREF_KEY_DEPARTMENT_NAME));
        userPhone.setText(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_PHONE));
        user_mails.setText(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_USER_EMAIL));
        user_jopnumber.setText(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_CODE));
    }

    @OnClick({R.id.user_portrait_box, R.id.user_sex_box, R.id.user_department_box, R.id.user_post_box, R.id.user_phone_box, R.id.user_date_box, R.id.btn_logout})
    public void onClick(View view) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime < 1000) {
            lastClickTime = currentTime;
            return;
        }
        lastClickTime = currentTime;
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
                startActivityForResult(new Intent(UserInfoActivity.this, ModifyPhoneActivity.class), UPDATE_PHONE);
                break;
            case R.id.user_date_box://邮箱修改
                startActivityForResult(new Intent(this, ModificationEmailActivity.class), 101);
                break;
            case R.id.btn_logout://登出
                showLogoutTips();
                break;
        }
    }

    //修改头像方法
    private void selectPortrait() {
        requestRunTimePermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, new PermissionListener() {
            @Override
            public void onGranted() {
                mPop = new BottomPopAvatar(UserInfoActivity.this);
                mPop.show(UserInfoActivity.this);
            }

            @Override
            public void onDenied() {
                showToast("权限被拒绝！请手动设置");
            }
        });

    }

    @Override
    public void uidNull(String code) {
        catchWarningByCode(code);
    }

    @Override
    public void upLoadSuccess(String portrait) {
        showToast("修改成功");
        //上传成功，设置用户头像
        String portraitUri =portrait;
        AppConfig.getAppConfig(UserInfoActivity.this).set(AppConfig.PREF_KEY_PORTRAITS, portraitUri);
        if (!TextUtils.isEmpty(portraitUri)) {
            Glide.with(AppManager.mContext)
                    .load(portraitUri)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.ease_default_avatar)
                    .transform(new CenterCrop(AppManager.mContext), new GlideRoundTransformUtils(AppManager.mContext, 5))
                    .placeholder(R.drawable.ease_default_avatar)
                    .into(userPortrait);
        } else {
            Glide.with(AppManager.mContext).load(R.drawable.ease_default_avatar).asBitmap().into(userPortrait);
        }

        //更新数据库
        FriendsInfoCacheSvc.getInstance(AppManager.mContext).setPortrait("sl_" + portrait, AppConfig.getAppConfig(AppManager.mContext).getPrivateCode());
    }

    @Override
    public void upLoadFailed(int errorCode, String msg) {
        showToast(msg);
    }

    @Override
    public void upLoadFinish() {
        hideLoadingView();
    }

    @Override
    public void queryUserInfoSuccess(UserDetailsBean userInfo) {
        try {
            if (userInfo != null) {
                String portraits = userInfo.getData().getPortrait();

                if (!TextUtils.isEmpty(portraits) || !portraits.equals("null")) {
                    Glide.with(AppManager.mContext)
                            .load(portraits)
                            .dontAnimate()
                            .error(R.drawable.ease_default_avatar)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .transform(new CenterCrop(AppManager.mContext), new GlideRoundTransformUtils(AppManager.mContext, 5))
                            .placeholder(R.drawable.ease_default_avatar)
                            .into(userPortrait);
                } else {
                    Glide.with(AppManager.mContext).load(R.drawable.ease_user_portraits).asBitmap().into(userPortrait);
                }
                userSex.setText(userInfo.getData().getSex());
                userPost.setText(userInfo.getData().getPostname());
                userDepartment.setText(userInfo.getData().getOrgan());
                userPhone.setText(userInfo.getData().getPhone());
                user_mails.setText(userInfo.getData().getEmail());
                user_jopnumber.setText(userInfo.getData().getCode());
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void queryUserInfoFailed() {

    }

    /**
     * 头像弹出框：拍照、相册、取消
     */
    private class BottomPopAvatar extends BaseBottomPushPopupWindow<Void> {
        public BottomPopAvatar(Context context) {
            super(context, null);
        }

        @Override
        protected View generateCustomView(Void data) {
            View root = View.inflate(context, R.layout.layout_menu_2, null);
            TextView menuBtn1 = (TextView) root.findViewById(R.id.menuBtn1);
            TextView menuBtn2 = (TextView) root.findViewById(R.id.menuBtn2);
            menuBtn1.setText("拍照");
            menuBtn1.setOnClickListener(v -> {
                dismiss();
                fromCamera();
            });
            menuBtn2.setText("从相册选取");
            menuBtn2.setOnClickListener(v -> {
                dismiss();
                fromGallery();
            });
            View cancelView = root.findViewById(R.id.cancel);
            cancelView.setOnClickListener(v -> dismiss());
            return root;
        }
    }

    /**
     * Select images from a local photo album
     */
    private void fromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, CODE_GALLERY_REQUEST);
    }

    /**
     * Start the phone camera photos
     */
    private void fromCamera() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, camera_path);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            intentFromCapture.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == MODIFICATION_EMAIL) {
            user_mails.setText(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_USER_EMAIL));
        }
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                if (intent == null) {
                    return;
                }
                cropImage(intent.getData(), 450, 450, CROP_PICTURE_REQUEST);
                break;
            case CODE_CAMERA_REQUEST:
                cropImage(camera_path, 450, 450, CROP_PICTURE_REQUEST);
                break;
            case CROP_PICTURE_REQUEST:
                Bitmap bitmap = decodeUriAsBitmap(icon_path);
                //userPortrait.setImageBitmap(bitmap);
                if (!FileUtils.isFileExist(Constants.FileUrl.TEMP)) {
                    FileUtils.createSDDir(Constants.FileUrl.TEMP);
                }
                //将要保存图片的路径
                File file = new File(Constants.FileUrl.TEMP + "Cut_image_" + Calendar.getInstance().getTimeInMillis() + ".jpg");
                LogUtils.e("file->" + file.getAbsolutePath());
                try {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                    assert bitmap != null;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                upLoadingPortrait(file);


                break;
            case UPDATE_PHONE:
                userPhone.setText(AppConfig.getAppConfig(UserInfoActivity.this).get(AppConfig.PREF_KEY_PHONE));
                break;
        }
        super.onActivityResult(requestCode, resultCode, intent);

    }


    /**
     * According to the incoming a length-width ratio began to cut out pictures
     *
     * @param uri Image source
     */
    private void cropImage(Uri uri, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // Set the cutting
        intent.putExtra("crop", "true");
        // aspectX , aspectY :In proportion to the width of high
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX , outputY : High cutting image width
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, icon_path);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            addurlPram(this, intent, camera_path, icon_path);
        }
        startActivityForResult(intent, requestCode);
    }

    private void addurlPram(Activity activity, Intent intent, Uri... uris) {
        List<ResolveInfo> resInfoList = activity.getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            for (Uri uri : uris) {
                activity.grantUriPermission(packageName, uri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                                Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }
    }

    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    /**
     * 上传头像
     */
    private void upLoadingPortrait(final File file) {
        showLoadingView();
        mPresenter.upLoadPortrait(file);
    }

    public void showLogoutTips() {
        @SuppressLint("InflateParams")
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_logout, null);
        final PopupWindow popupWindow = new PopupWindow(dialogView, LinearLayoutCompat.LayoutParams
                .WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, false);
        Button btnSubmit = (Button) dialogView.findViewById(R.id.btn_submit);
        Button btnCancel = (Button) dialogView.findViewById(R.id.btn_cancel);

        btnSubmit.setOnClickListener(v -> {
            try {
                popupWindow.dismiss();
                //退出环信登录
                LogUtils.e("退出环信");
                DemoHelper.getInstance().logout(true, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        Log.d("退出环信", "退出环信成功！！");
                    }

                    @Override
                    public void onError(int i, String s) {
                        Log.d("退出环信", i + s);
                    }

                    @Override
                    public void onProgress(int i, String s) {
                        Log.d("退出环信", i + s);
                    }
                });

                JPushInterface.setAlias(UserInfoActivity.this, "", (i, s, set) -> {
                });
                JPushInterface.setTags(UserInfoActivity.this, null, null);

                exitToLogin();
            } catch (Exception e) {
                LogUtils.e("退出环信抛出异常" + e.toString());
                exitToLogin();
            }
        });
        btnCancel.setOnClickListener(v -> popupWindow.dismiss());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(() -> {
            WindowManager.LayoutParams lp1 = getWindow().getAttributes();
            lp1.alpha = 1f;
            getWindow().setAttributes(lp1);
        });
        popupWindow.setFocusable(true);

        popupWindow.setAnimationStyle(R.style.dialog_pop_anim_style);
        popupWindow.showAtLocation(mRootView, Gravity.CENTER, 0, 0);
    }

    private void exitToLogin() {

        ShortcutBadger.removeCount(UserInfoActivity.this);
        AppConfig.getAppConfig(UserInfoActivity.this).clearLoginInfo();
        startActivity(new Intent(UserInfoActivity.this, LoginActivity.class));
        AppManager.sharedInstance().finishAllActivity();
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
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}