package com.itcrm.GroupInformationPlatform.utils;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.itcrm.GroupInformationPlatform.ui.parser.JsonParser;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.utils
 * Author:Created by Tsui on Date:2016/12/9 14:55
 * Description:讯飞工具类
 */
public class IflytekUtil {
    Context mContext;
    private RecognizerDialog iatDialog;
    private Toast toast;
    private EditText mEtContent;
    private String content;
    StringBuilder builderResult = new StringBuilder();

    public IflytekUtil(Context context, EditText etContent) {
        mContext = context;
        initXunFenData();
        mEtContent = etContent;
    }

    public void showIatDialog() {
        builderResult.setLength(0);
        iatDialog.show();
    }

    public void dismissIatDialog() {
        iatDialog.dismiss();
    }

    private void initXunFenData() {
        // 初始化识别对象
        //1.创建SpeechRecognizer对象，第二个参数：本地听写时传InitListener
        if (iatDialog == null) {
            SpeechRecognizer mIat = SpeechRecognizer.createRecognizer(mContext, initListener);
            //2.设置听写参数，详见《科大讯飞MSC API手册(Android)》SpeechConstant类
            mIat.setParameter(SpeechConstant.DOMAIN, "iat");
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");
            mIat.startListening(mRecoListener);


            //1.创建SpeechRecognizer对象，第二个参数：本地听写时传InitListener
            iatDialog = new RecognizerDialog(mContext, initListener);
            //2.设置听写参数，同上节
            //3.设置回调接口
            iatDialog.setListener(recognizerDialogListener);
        }
    }

    RecognizerDialogListener recognizerDialogListener = new RecognizerDialogListener() {
        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            if (null != recognizerResult) {
                String text;
                text = JsonParser.parseIatResult(recognizerResult.getResultString());
                if (!b) {
                    builderResult.append(text).append(",");
                } else {

                    content = mEtContent.getText().toString() + builderResult.toString();
                    content.replace("，", ",");
                    mEtContent.setText(content);

                    //mEtContent.setSelection(builderResult.toString().length() - 1);

                    //修复光标越界跟光标显示不准
                    mEtContent.setSelection(content.length());
                }
            } else {
                showToast("recognizer result : null");
            }
            LogUtils.e("识别结果：" + recognizerResult.getResultString());

        }

        @Override
        public void onError(SpeechError speechError) {
            iatDialog.dismiss();
            LogUtils.e(speechError.toString());
        }
    };

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

    private InitListener initListener = new InitListener() {

        @Override
        public void onInit(int i) {

        }
    };
    //听写监听器
    private RecognizerListener mRecoListener = new RecognizerListener() {


        @Override
        public void onVolumeChanged(int i, byte[] bytes) {

        }

        //开始录音
        public void onBeginOfSpeech() {
        }

        //结束录音
        public void onEndOfSpeech() {
        }

        @Override
        public void onResult(RecognizerResult result, boolean b) {

        }

        @Override
        public void onError(SpeechError speechError) {

            //扩展用接口
        }

        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };
}
