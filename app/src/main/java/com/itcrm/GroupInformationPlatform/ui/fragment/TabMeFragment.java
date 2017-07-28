package com.itcrm.GroupInformationPlatform.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.itcrm.GroupInformationPlatform.R;
import com.itcrm.GroupInformationPlatform.common.Api;
import com.itcrm.GroupInformationPlatform.common.Constants;
import com.itcrm.GroupInformationPlatform.manager.AppConfig;
import com.itcrm.GroupInformationPlatform.manager.UpdateHelper;
import com.itcrm.GroupInformationPlatform.ui.PermissionListener;
import com.itcrm.GroupInformationPlatform.ui.activity.AboutUsActivity;
import com.itcrm.GroupInformationPlatform.ui.activity.FeedbackActivity;
import com.itcrm.GroupInformationPlatform.ui.activity.ModifyPwdActivity;
import com.itcrm.GroupInformationPlatform.ui.activity.UserInfoActivity;
import com.itcrm.GroupInformationPlatform.ui.activity.UsingHelpActivity;
import com.itcrm.GroupInformationPlatform.ui.base.BaseFragment;
import com.itcrm.GroupInformationPlatform.utils.FileUtils;
import com.itcrm.GroupInformationPlatform.utils.LogUtils;
import com.itcrm.GroupInformationPlatform.utils.SharedPreferenceUtil;
import com.pgyersdk.update.PgyUpdateManager;

import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <h3>Description: 首页我的页面</h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/8/26.<br/>
 */
public class TabMeFragment extends BaseFragment {

    @Bind(R.id.user_portrait)
    SimpleDraweeView userPortrait;
    @Bind(R.id.user_name)
    TextView userName;
    @Bind(R.id.position)
    TextView position;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_me_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        displayVersionName();

        userPortrait.setImageURI(Uri.parse(
                AppConfig.getAppConfig(getActivity()).get(AppConfig.PREF_KEY_PORTRAITS)));
        userName.setText(AppConfig.getAppConfig(getActivity()).get(AppConfig.PREF_KEY_USERNAME));
        position.setText(AppConfig.getAppConfig(getActivity()).get(AppConfig.PREF_KEY_POST_NAME));

    }

    @Override
    public void onResume() {
        super.onResume();
        userPortrait.setImageURI(Uri.parse(
                AppConfig.getAppConfig(getActivity()).get(AppConfig.PREF_KEY_PORTRAITS)));
    }

    @OnClick({R.id.user_info, R.id.btn_modify_pwd, R.id.btn_usinghelp,
            R.id.btn_feedback, R.id.btn_update, R.id.btn_about_us, R.id.btn_clear_cache})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_info://用户信息
                startActivity(new Intent(getActivity(), UserInfoActivity.class));
                break;
            case R.id.btn_modify_pwd://修改密码
                startActivity(new Intent(getActivity(), ModifyPwdActivity.class));
                break;
            case R.id.btn_usinghelp://使用帮助
                startActivity(new Intent(getActivity(), UsingHelpActivity.class));
                break;
            case R.id.btn_feedback://用户反馈
                startActivity(new Intent(getActivity(), FeedbackActivity.class));
                break;
            case R.id.btn_update://版本升级
               /* if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            001);
                }*/
//                getNewVersionInfo();

                applyPermission();

                break;
            case R.id.btn_about_us://关于我们
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
                break;
            case R.id.btn_clear_cache://清除缓存
                showToast("缓存已清理");
                break;
        }
    }
    //存储权限判断
    private void applyPermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (SharedPreferenceUtil.getShouldAskPermission(getActivity(), "firstshould") &&
                    ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == false) {//第一次已被拒绝
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("权限开启");
                builder.setMessage("更新功能无法正常使用，去权限列表开启该权限");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettig();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            } else {//
                SharedPreferenceUtil.setShouldAskPermission(getActivity(), "firstshould",
                        ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE));
                ActivityCompat.requestPermissions(getActivity(), new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }

        } else {
            PgyUpdateManager.register(getActivity(), "com.itcrm.GroupInformationPlatform.fileprovider");
        }
    }


    //开启权限列表
    private void startAppSettig() {
        Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        i.setData(uri);
        startActivityForResult(i, 100);
    }

    //位置权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PgyUpdateManager.register(getActivity(), "com.itcrm.GroupInformationPlatform.fileprovider");
                } else {
                    Toast.makeText(getActivity(), "该权限被禁用 无法更新！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void getNewVersionInfo() {
        HttpParams params = new HttpParams();
        params.put("uid", AppConfig.getAppConfig(getActivity()).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(getActivity()).getPrivateToken());
        initKjHttp().post(Api.UPDATE_APP, params, new HttpCallBack() {
            @Override
            public void onPreStart() {
                showLoadingView();
                super.onPreStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoadingView();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e("onSuccess-->" + t);
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (Api.getCode(jo)) {
                        case Api.RESPONSES_CODE_OK:
                            JSONObject data = Api.getDataToJSONObject(jo);
                            int newVersionCode = Integer.parseInt(
                                    data.getString("versionCode"));
                            if (newVersionCode > getLocalVersionCode()) {
                                showUploadInfo(data.getString("updateTips"),
                                        data.getString("changeLog"),
                                        data.getString("apkUrl"));
                            } else {
                                showTips("当前为最新版本！");
                            }
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                LogUtils.e(errorNo + strMsg);
                catchWarningByCode(errorNo);
            }
        });
    }

    private void showUploadInfo(String updateTips, String uploadLog, final String apkUrl) {
        @SuppressLint("InflateParams")
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.public_dialog, null);
        TextView title = (TextView) dialogView.findViewById(R.id.title);
        title.setText(updateTips);
        TextView message = (TextView) dialogView.findViewById(R.id.message);
        message.setText(uploadLog);

        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity(),
                R.style.AppTheme_Dialog).create();
        alertDialog.setView(dialogView);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "立即更新",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        requestRunTimePermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
                            @Override
                            public void onGranted() {
                                if (!FileUtils.isFileExist(Constants.FileUrl.UPDATE_APP)) {
                                    FileUtils.createSDDir(Constants.FileUrl.UPDATE_APP);
                                }
                                UpdateHelper helper = new UpdateHelper(getActivity(), apkUrl);
                                helper.showUpdateView();


                            }

                            @Override
                            public void onDenied() {
                                showToast("权限被拒绝!请手动设置");
                            }
                        });


                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                getResources().getColor(R.color.tab_bar_text_light));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                getResources().getColor(R.color.tab_bar_text_gray));
    }


    private void displayVersionName() {
      /*  try {
            versions.setText(getActivity().getPackageManager().getPackageInfo(
                    getActivity().getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }*/
    }

    private int getLocalVersionCode() {
        int versionCode = -1;
        try {
            PackageInfo info = getActivity().getPackageManager().getPackageInfo(
                    getActivity().getPackageName(), 0);
            versionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionCode;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}