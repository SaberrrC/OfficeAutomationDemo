package com.shanlinjinrong.oa.ui.base;

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

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.listener.PermissionListener;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.thirdParty.huanxin.DemoHelper;
import com.shanlinjinrong.oa.ui.activity.login.LoginActivity;
import com.shanlinjinrong.oa.ui.activity.main.MainController;
import com.shanlinjinrong.oa.ui.base.component.DaggerFragmentComponent;
import com.shanlinjinrong.oa.ui.base.component.FragmentComponent;
import com.shanlinjinrong.oa.ui.base.module.FragmentModule;
import com.shanlinjinrong.oa.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * <h3>Description: 基础Fragment</h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/8/26.<br/>
 */
public abstract class MyBaseFragment<T extends BasePresenter> extends Fragment implements BaseView {

    private AlertDialog loadingDialog;
    private TextView msg;
    public Activity mContext;//CXP添加，供子类使用
    private Toast toast;

    //Fragment的View加载完毕的标记
    private boolean isViewCreated;
    //Fragment对用户可见的标记
    private boolean isUIVisible;

    @Inject
    protected T mPresenter;

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
        loadingDialog.setCancelable(true);
        initInject();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }

        //懒加载
        isViewCreated = true;
        lazyLoad();
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

    protected abstract void initInject();


    protected FragmentComponent getFragmentComponent() {
        return DaggerFragmentComponent.builder()
                .appComponent(AppManager.sharedInstance().getAppComponent())
                .fragmentModule(getFragmentModule()).build();
    }

    protected FragmentModule getFragmentModule() {
        return new FragmentModule(this);
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
            case Api.RESPONSES_CODE_UID_NULL:
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
        if (EMClient.getInstance().isConnected()) {
            try {
                //退出环信登录
                LogUtils.e("退出环信");
                DemoHelper.getInstance().logout(true, new EMCallBack() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });

                JPushInterface.setAlias(getContext(), "", new TagAliasCallback() {
                    @Override
                    public void gotResult(int i, String s, Set<String> set) {
                    }
                });
                JPushInterface.setTags(getContext(), null, null);

                AppConfig.getAppConfig(getContext()).clearLoginInfo();
                startActivity(new Intent(getContext(), LoginActivity.class));
                MainController.instance.finish();
            } catch (Exception e) {
                LogUtils.e("退出环信抛出异常" + e.toString());
                AppConfig.getAppConfig(getContext()).clearLoginInfo();
                startActivity(new Intent(getContext(), LoginActivity.class));
                MainController.instance.finish();
            }
        }
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
    @SuppressLint("InflateParams")
    View empty;

    public void showEmptyView(ViewGroup view, String str, int resId, boolean isShow) {
//        if (empty == null) {
        empty = LayoutInflater.from(getActivity()).inflate(R.layout.public_empty_view, null);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        empty.setLayoutParams(lp);
        if (isShow) {
            ImageView imageView = (ImageView) empty.findViewById(R.id.empty_image);
            imageView.setImageResource(resId);
        }
        TextView msg = (TextView) empty.findViewById(R.id.message);
        msg.setText(str);
//        }
        view.addView(empty);
    }

    public void hideEmptyView() {
        if (empty != null) {
            empty.setVisibility(View.GONE);
        }
    }

    /**
     * @param view 移除空的view，如果有
     */
    public void removeEmptyView(ViewGroup view) {
        // modify by lvdinghao 2017/8/10 之前view使用findviewbyid，始终是一个新的对象，无法把原来的view删除
        try {
            for (int i = 0; i < view.getChildCount(); i++) {
                LogUtils.d(view.getChildAt(i).getId() + "");
                if (empty != null && view.getChildAt(i) == empty) {
                    view.removeView(view.getChildAt(i));
                    return;
                }
            }
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


    PermissionListener mlistener;

    @Override
    public void onPause() {
        super.onPause();
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

    //懒加载
    private void lazyLoad() {
        //这里进行双重标记判断,是因为setUserVisibleHint会多次回调,并且会在onCreateView执行前回调,必须确保onCreateView加载完毕且页面可见,才加载数据
        if (isViewCreated && isUIVisible) {
            lazyLoadData();
            //数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false;
            isUIVisible = false;
        }
    }

    protected abstract void lazyLoadData();

    /**
     * @param isVisibleToUser 用来判断Fragment的UI 用户是否可见
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isUIVisible = true;
            lazyLoad();
        } else {
            isUIVisible = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        if (mPresenter != null)
            mPresenter.detachView();
    }
}