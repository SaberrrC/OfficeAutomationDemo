package com.hyphenate.easeui.ui;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.hyphenate.easeui.PermissionListener;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.widget.EaseTitleBar;

import java.util.ArrayList;
import java.util.List;

public abstract class EaseBaseFragment extends Fragment{
    protected EaseTitleBar titleBar;
    protected InputMethodManager inputMethodManager;
    private Toast toast;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //noinspection ConstantConditions
        titleBar = (EaseTitleBar) getView().findViewById(R.id.title_bar);
        
        initView();
        setUpView();
    }
    
    public void showTitleBar(){
        if(titleBar != null){
            titleBar.setVisibility(View.VISIBLE);
        }
    }
    
    public void hideTitleBar(){
        if(titleBar != null){
            titleBar.setVisibility(View.GONE);
        }
    }
    
    protected void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    
    protected abstract void initView();
    
    protected abstract void setUpView();

    public void showToast(
            String content) {
        if (toast == null) {
            toast = Toast.makeText(getContext(),
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
            if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), permissionList.toArray(new String[permissionList.size()]), 1);
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

}
