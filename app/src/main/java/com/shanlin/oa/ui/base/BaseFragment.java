package com.shanlin.oa.ui.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shanlin.oa.R;
import com.shanlin.oa.common.Api;
import com.shanlin.oa.manager.AppConfig;
import com.shanlin.oa.ui.PermissionListener;
import com.shanlin.oa.ui.activity.LoginActivity;
import com.shanlin.oa.utils.LogUtils;
import com.shanlin.oa.utils.netutil.MyKjHttp;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpConfig;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * <h3>Description: 基础Fragment</h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/8/26.<br/>
 */
public class BaseFragment extends Fragment {

    private AlertDialog loadingDialog;
    private TextView msg;
    private KJHttp kjHttp;
    public Activity mContext;//CXP添加，供子类使用
    private Toast toast;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        @SuppressLint("InflateParams")
        View loading = LayoutInflater.from(getActivity()).inflate(
                R.layout.public_dialog_loading, null);
        msg = (TextView) loading.findViewById(R.id.message);
        loadingDialog = new AlertDialog.Builder(getActivity(),
                R.style.AppTheme_Dialog_Loading).create();
        loadingDialog.setView(loading);
        loadingDialog.setCancelable(false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
    }

    public void showLoadingView() {
        if (!loadingDialog.isShowing()) {
            LogUtils.e("showLoadingView");
            loadingDialog.show();
        }
    }

    public void showLoadingView(String text) {
        msg.setText(text);
        loadingDialog.show();
    }

    /**
     * 捕获服务器接口返回异常状态码，并统一进行处理
     *
     * @param code 错误代码
     */
    public void catchWarningByCode(int code) {
        switch (code) {
            case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
                AppConfig.getAppConfig(getActivity()).clearLoginInfo();
                gotoLoginPage();
                break;
            case Api.RESPONSES_CODE_NO_NETWORK:
                showTips("请确认是否已连接网络！");
                break;
            case Api.RESPONSES_CODE_NO_RESPONSE:
                showTips("网络不稳定，请重试！");
                break;
        }
    }
        //判断账号在不同设备上登录
    private void gotoLoginPage() {
        Toast.makeText(getActivity(), "您的帐号已在其他设备上登录，请您及时查验！",
                Toast.LENGTH_LONG).show();
        JPushInterface.setAlias(mContext, null, null);
        JPushInterface.setTags(mContext, null, null);
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    public void showTips(String msg) {
        @SuppressLint("InflateParams")
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.public_dialog, null);
        TextView title = (TextView) dialogView.findViewById(R.id.title);
        title.setText("提示");
        TextView message = (TextView) dialogView.findViewById(R.id.message);
        message.setText(msg);

        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity(),
                R.style.AppTheme_Dialog).create();
        alertDialog.setView(dialogView);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "知道了",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                getResources().getColor(R.color.btn_text_logout));
    }

    /**
     * @param view
     * @param str    要显示的文字
     * @param resId  图片资源id
     * @param isShow 是否显示图片
     */
    public void showEmptyView(ViewGroup view, String str, int resId, boolean isShow) {
        @SuppressLint("InflateParams")
        View empty = LayoutInflater.from(getActivity()).inflate(R.layout.public_empty_view, null);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        empty.setLayoutParams(lp);
        if (isShow) {
            ImageView imageView = (ImageView) empty.findViewById(R.id.empty_image);
            imageView.setImageResource(resId);
        }
        TextView msg = (TextView) empty.findViewById(R.id.message);
        msg.setText(str);
        view.addView(empty);
    }

    /**
     * @param view 移除空的view，如果有
     */
    public void removeEmptyView(ViewGroup view) {
        try {
            View emptyView = view.findViewById(R.id.rl_empty_view);
            view.removeView(emptyView);
        } catch (Exception e) {
            LogUtils.e("当前没有空的View");
        }

    }

    public void hideLoadingView() {
        LogUtils.e("hideLoadingView");
        loadingDialog.dismiss();
    }

    public void showToast(
            String content) {
        if (toast == null) {
            toast = Toast.makeText(mContext,
                    content,
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

    public KJHttp initKjHttp() {
        if (kjHttp == null) {
            kjHttp = new MyKjHttp();
            HttpConfig.TIMEOUT = 10000;
        } else {
            kjHttp.cancelAll();
            kjHttp.cleanCache();
        }
        return kjHttp;
    }

    PermissionListener mlistener;

    @Override
    public void onPause() {
        super.onPause();
        initKjHttp().cancelAll();
        hideLoadingView();
    }

    /**
     * 6.0请求权限函数
     *
     * @param permissions
     * @param listener
     */
    public void requestRunTimePermission(String[] permissions, PermissionListener listener) {

        mlistener = listener;
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            //
            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        if (!permissionList.isEmpty()) {
            requestPermissions(permissionList.toArray(new String[permissionList.size()]), 1);
        } else {
            //权限已经申请，doSomething
            mlistener.onGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    List<String> deniedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int grandResult = grantResults[i];
                        String permission = permissions[i];
                        if (grandResult != PackageManager.PERMISSION_GRANTED) {
                            deniedPermissions.add(permission);
                        }
                    }
                    if (deniedPermissions.isEmpty()) {
                        mlistener.onGranted();
                    } else {
                        mlistener.onDenied();
                    }
                }
                break;
            default:

                break;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        initKjHttp().cancelAll();
        if (loadingDialog != null) {
            loadingDialog.dismiss();

        }
    }
}