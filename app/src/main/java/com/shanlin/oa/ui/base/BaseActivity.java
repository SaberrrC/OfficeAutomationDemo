package com.shanlin.oa.ui.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.shanlin.oa.R;
import com.shanlin.oa.common.Api;
import com.shanlin.oa.thirdParty.huanxin.DemoHelper;
import com.shanlin.oa.manager.AppConfig;
import com.shanlin.oa.manager.AppManager;
import com.shanlin.oa.listener.PermissionListener;
import com.shanlin.oa.ui.activity.login.LoginActivity;
import com.shanlin.oa.ui.activity.MainController;
import com.shanlin.oa.utils.LogUtils;
import com.shanlin.oa.utils.ScreenUtils;
import com.shanlin.oa.net.MyKjHttp;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * <h3>Description: 基础Activity</h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/8/26.<br/>
 */
public class BaseActivity extends AppCompatActivity {

    private AlertDialog loadingDialog;
    private TextView msg;
    private KJHttp kjHttp;
    private Toast toast;
    private View empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将activity加入到AppManager堆栈中
        AppManager.sharedInstance().addActivity(this);

        @SuppressLint("InflateParams")
        View loading = LayoutInflater.from(this).inflate(R.layout.public_dialog_loading, null);
        msg = (TextView) loading.findViewById(R.id.message);
        loadingDialog = new AlertDialog.Builder(this, R.style.AppTheme_Dialog_Loading).create();
        loadingDialog.setView(loading);
        loadingDialog.setCancelable(false);
    }

    public KJHttp initKjHttp() {
        if (kjHttp == null) {
            kjHttp = new MyKjHttp();
            HttpConfig config = new HttpConfig();
            HttpConfig.TIMEOUT = 10000;
            kjHttp.setConfig(config);
        } else {
            kjHttp.cleanCache();
            kjHttp.cancelAll();
        }
        return kjHttp;
    }


    public void cancleAllRequest() {
        initKjHttp().cancelAll();
    }

    public void showLoadingView() {
        loadingDialog.show();
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
                AppConfig.getAppConfig(this).clearLoginInfo();
                gotoLoginPage();
                break;
            case Api.RESPONSES_CODE_UID_NULL:
                AppConfig.getAppConfig(this).clearLoginInfo();
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

    private void gotoLoginPage() {
        showToast("您的帐号已在其他设备上登录，请您及时查验！");
        JPushInterface.setAlias(this, null, null);
        JPushInterface.setTags(this, null, null);
        if (EMClient.getInstance().isConnected()) {
            try {
                //退出环信登录
                LogUtils.e("退出环信");

                DemoHelper.getInstance().logout(true, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        Log.d("退出环信成功！！", "退出环信成功！！");
                    }

                    @Override
                    public void onError(int i, String s) {
                        Log.d("退出环信抛出异常", i + s);
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });

                JPushInterface.setAlias(this, "", new TagAliasCallback() {
                    @Override
                    public void gotResult(int i, String s, Set<String> set) {
                    }
                });
                JPushInterface.setTags(this, null, null);

                AppConfig.getAppConfig(this).clearLoginInfo();
                startActivity(new Intent(this, LoginActivity.class));
                MainController.instance.finish();
            } catch (Exception e) {
                LogUtils.e("退出环信抛出异常" + e.toString());
                AppConfig.getAppConfig(this).clearLoginInfo();
                startActivity(new Intent(this, LoginActivity.class));
                MainController.instance.finish();
            }
        }
        startActivity(new Intent(this, LoginActivity.class));
    }

    /**
     * 验证内容为空的返回提示
     */
    public void showBackTip(String msg, final String posiStr, String negaStr) {
        @SuppressLint("InflateParams")
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_exit_editor, null);
        TextView title = (TextView) dialogView.findViewById(R.id.title);
        title.setText("提示");
        TextView message = (TextView) dialogView.findViewById(R.id.message);
        message.setText(msg);

        final AlertDialog alertDialog = new AlertDialog.Builder(this,
                R.style.AppTheme_Dialog).create();
        alertDialog.setView(dialogView);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, posiStr,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, negaStr,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                getResources().getColor(R.color.btn_text_logout));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                getResources().getColor(R.color.btn_text_logout));
    }

    public void showTips(String msg) {
        @SuppressLint("InflateParams")
        View dialogView = LayoutInflater.from(this).inflate(R.layout.public_dialog, null);
        TextView title = (TextView) dialogView.findViewById(R.id.title);
        title.setText("提示");
        TextView message = (TextView) dialogView.findViewById(R.id.message);
        message.setText(msg);

        final AlertDialog alertDialog = new AlertDialog.Builder(this,
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
     * @param content 提示内容
     */
    public void showToast(
            String content) {
        if (toast == null) {
            toast = Toast.makeText(this,
                    content,
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

    /**
     * @param content 提示内容
     */
    public void showToast(Context context,
                          String content) {
        if (toast == null) {
            toast = Toast.makeText(context,
                    content,
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

    /**
     * @param view
     * @param str    要显示的文字
     * @param resId  图片资源id
     * @param isShow 是否显示图片
     */
    public void showEmptyView(ViewGroup view, String str, int resId, boolean isShow) {
        empty = LayoutInflater.from(this).inflate(R.layout.public_empty_view, null);
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

    public void hideEmptyView() {
        if (empty != null) {
            empty.setVisibility(View.GONE);
        }
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
        loadingDialog.dismiss();
    }

    /**
     * 设置系统状态栏
     *
     * @param activity Activity
     */
    public void setTranslucentStatus(Activity activity) {
        //判断版本是4.4以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = activity.getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);

            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            //打开系统状态栏控制
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(Color.parseColor("#000000"));//设置背景

            View layoutAll = activity.findViewById(R.id.layout_root);
            //设置系统栏需要的内偏移
            if (layoutAll != null) {
                layoutAll.setPadding(0, ScreenUtils.getStatusHeight(activity), 0, 0);
            }
        }
    }

    PermissionListener mlistener;

    /**
     * 6.0请求权限函数
     *
     * @param permissions
     * @param listener
     */
    public void requestRunTimePermission(String[] permissions, PermissionListener listener) {
        LogUtils.e("requestRunTimePermission。。。");
        mlistener = listener;
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), 1);
        } else {
            //权限已经申请，doSomething
            mlistener.onGranted();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
                    //TODO 相机拍完相片不能发送 - 待解决,暂时缓解相机崩溃问题
                    if (deniedPermissions.isEmpty()) {
                        if (mlistener != null) {
                            mlistener.onGranted();
                        }
                    } else {
                        if (mlistener != null) {
                            mlistener.onDenied();
                        }
                    }
                }
                break;
            default:

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        initKjHttp().cancelAll();
        AppManager.sharedInstance().removeActivity(this);
    }
}