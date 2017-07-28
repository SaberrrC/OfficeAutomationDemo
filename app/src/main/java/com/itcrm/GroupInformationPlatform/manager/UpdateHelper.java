package com.itcrm.GroupInformationPlatform.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.itcrm.GroupInformationPlatform.R;
import com.itcrm.GroupInformationPlatform.common.Constants;
import com.itcrm.GroupInformationPlatform.utils.FileUtils;
import com.itcrm.GroupInformationPlatform.utils.LogUtils;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;

import java.io.File;
import java.util.Date;

/**
 * 概述：更新应用 <br />
 * Created by KevinMeng on 2016/6/13.
 */
public class UpdateHelper {

    private Activity activity;
    private String downloadUrl;

    private AlertDialog dialog;
    private View dialogView;
    private ProgressBar progressBar;
    private TextView progressBarTv;
    private String path;

    private Handler handler;

    public UpdateHelper(Activity activity, String downloadUrl) {
        this.activity = activity;
        this.downloadUrl = downloadUrl;
        path = Constants.FileUrl.UPDATE_APP + new Date().getTime() + ".apk";
        init();
    }

    @SuppressLint({"InflateParams", "HandlerLeak"})
    private void init() {
        handler = new Handler() {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.obj != null) {
                    ProgressModel pm = (ProgressModel) msg.obj;
                    double curr = pm.getCurrent() / 1024.00 / 1024.00;
                    double count = pm.getCount() / 1024.00 / 1024.00;
                    progressBarTv.setText(String.format("%.2f", curr) + "/" +
                            String.format("%.2f", count) + "MB");
                }
            }
        };
        dialogView = LayoutInflater.from(activity).inflate(R.layout.update_app_helper_dialog, null);
        progressBar = (ProgressBar) dialogView.findViewById(R.id.progress_bar);
        progressBarTv = (TextView) dialogView.findViewById(R.id.progress_bar_text);
        dialog = new AlertDialog.Builder(activity, R.style.AppTheme_Dialog).create();
        dialog.setView(dialogView);
        dialog.setCancelable(false);
        download();
    }

    private void download() {
        KJHttp kjh = new KJHttp();
        kjh.download(path, downloadUrl, new HttpCallBack() {
            @Override
            public void onSuccess(byte[] t) {
                super.onSuccess(t);
                dialog.dismiss();
                progressBar.setProgress(progressBar.getMax());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(path)),
                        "application/vnd.android.package-archive");
                activity.startActivity(intent);
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                System.out.println("onFailure-->" + errorNo + "-" + strMsg);
                dialog.dismiss();
                Toast toast = Toast.makeText(activity,
                        "网络连接中断，请检查后重试！",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }

            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
                progressBar.setMax((int) count);
                progressBar.setProgress((int) current);
                Message message = new Message();
                message.obj = new ProgressModel(current, count);
                handler.sendMessage(message);
            }
        });
    }

    public void showUpdateView() {
        dialog.show();
    }

    class ProgressModel {
        private long current;
        private long count;

        public ProgressModel(long current, long count) {
            this.count = count;
            this.current = current;
        }

        public long getCurrent() {
            return current;
        }

        public long getCount() {
            return count;
        }
    }
}