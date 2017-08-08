package com.shanlin.oa.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.shanlin.oa.R;
import com.shanlin.oa.common.Api;
import com.shanlin.oa.common.Constants;
import com.shanlin.oa.manager.AppConfig;
import com.shanlin.oa.model.selectContacts.Child;
import com.shanlin.oa.ui.PermissionListener;
import com.shanlin.oa.ui.base.BaseActivity;
import com.shanlin.oa.ui.draft.LaunchWorkReportDraft;
import com.shanlin.oa.utils.AndroidAdjustResizeBugFix;
import com.shanlin.oa.utils.FileUtils;
import com.shanlin.oa.utils.IflytekUtil;
import com.shanlin.oa.utils.LogUtils;
import com.shanlin.oa.utils.Utils;
import com.shanlin.oa.views.KeyboardLinearLayout;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.qqtheme.framework.picker.DatePicker;

/**
 * ProjectName: GroupInformationPlatform
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by CXP on Date: 2016/9/2 9:11.
 * Description: 工作汇报Activity
 */
public class WorkReportLaunchActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.toolbar_text_btn)
    TextView mTolbarTextBtn;
    @Bind(R.id.reporter_portrait)
    SimpleDraweeView mReporterPortrait;
    @Bind(R.id.report_radioGroup)
    RadioGroup mRadioGroup;
    @Bind(R.id.report_rb_weekly)
    RadioButton mRbWeekly;
    @Bind(R.id.report_rb_monthly)
    RadioButton mRbMonthly;
    @Bind(R.id.report_ll_update_date)
    LinearLayout mUpdateDate;
    @Bind(R.id.report_iv_add_person)
    ImageView mIvAddPerson;
    @Bind(R.id.report_et)
    EditText mEtReport;
    @Bind(R.id.layout_root)
    KeyboardLinearLayout mRootView;
    @Bind(R.id.tv_start_date)
    TextView mTvStartDate;

    @Bind(R.id.tv_get_weekly_tips)
    TextView mTvGetWeekLyTips;
    @Bind(R.id.et_geted_weekly)
    EditText mEtGetedWeekly;

    @Bind(R.id.tv_end_date)
    TextView mTvEndDate;
    @Bind(R.id.report_tv_copyTo)
    TextView mtvCopyTo;
    @Bind(R.id.report_scroll_view)
    ScrollView mScrollView;
    @Bind(R.id.tv_read_weekly)
    Button mTvReadWeekly;
    @Bind(R.id.btn_voice_input)
    Button mBtnVoiceInput;
    @Bind(R.id.tv_add_pic_tips)
    TextView mTvAddPicTips;
    @Bind(R.id.ll_pictures)
    LinearLayout mLlPictures;
    @Bind(R.id.tv_leader_name)
    TextView mTvLeaderName;
    @Bind(R.id.ll_monthly_geted_weekly_layout)
    LinearLayout mLlGetedWeekly;
    @Bind(R.id.tv_report_title)
    TextView mTvReportTitle;

    private boolean isChangePic = false;
    private String picTag;//图片tag
    public static final int UPDATE_PROTRAIT = 2;//上传头像
    public static final int PHOTO_REQUEST_CUT = 3;// 裁剪结果
    public static final int AFFIRM_PICTURE_RESULT = 4;// 确认图片请求码
    /**
     * 多选
     */
    public static final int REQUEST_CODE_MULTIPLE = 1;
    private ArrayList<Child> contactsList; //抄送人数组
    private StringBuilder copy;//抄送人id
    private String mReportDateStyle = "1";
    private String mtyb;
    private LinearLayout.LayoutParams PicParams;
    private List<String> picUrls = new ArrayList<>();
    //存放上传图片的url
    private ConcurrentHashMap<String, String> picUrlMap = new ConcurrentHashMap<>();
    private File file;
    private PopupWindow popupWindow;
    private LaunchWorkReportDraft launchWorkReportDraft;
    private StringBuilder picBuilder;
    private StringBuilder copyNameBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_report);
        ButterKnife.bind(this);
        initToolBar();
        setTranslucentStatus(this);
        //修复软键盘adjustResize属性的bug
        AndroidAdjustResizeBugFix.assistActivity(this);
        initWidget();
        initData();
    }

    /**
     * 初始化常用数据
     */
    private void initData() {
        setPicTureParams();
        getLeader();
        contactsList = new ArrayList<>();
        copyNameBuilder = new StringBuilder();

        mTvReadWeekly.setVisibility(View.GONE);
        copy = new StringBuilder();
        launchWorkReportDraft = new LaunchWorkReportDraft();

        initDataFromSP();

    }

    /**
     * 从SP中初始化数据给控件
     */
    private void initDataFromSP() {

        String whichDate = launchWorkReportDraft.getDraft(this, "whichDate");
        if (!whichDate.equals("")) {
            switch (Integer.parseInt(whichDate)) {
                case 1:

                    mRbWeekly.setChecked(true);
                    break;
                case 2:
                    mRbMonthly.setChecked(true);

                    break;
            }

            mTvStartDate.setText(launchWorkReportDraft.getDraft(this, "startDate"));
            mTvEndDate.setText(launchWorkReportDraft.getDraft(this, "endDate"));
            mEtGetedWeekly.setText(launchWorkReportDraft.getDraft(this, "weeklyContent"));

            if (!(launchWorkReportDraft.getDraft(this, "weeklyContent").equals(""))) {
                mLlGetedWeekly.setVisibility(View.VISIBLE);
                mTvReadWeekly.setVisibility(View.GONE);
                mTvGetWeekLyTips.setVisibility(View.VISIBLE);
                mEtGetedWeekly.setVisibility(View.VISIBLE);
            }


            mEtReport.setText(launchWorkReportDraft.getDraft(this, "reportContent"));

            String picUrl = launchWorkReportDraft.getDraft(this, "picUrl");


            String copyId = launchWorkReportDraft.getDraft(this, "copyId");
            List<String> copyIds = Arrays.asList(copyId.split(","));
            for (int i = 0; i < copyIds.size(); i++) {
                contactsList.add(new Child("", "", "", copyIds.get(i), "", "", "", "", true));
            }

            List<String> pics = Arrays.asList(picUrl.split(","));
            if (!pics.equals("")) {
                for (int i = 0; i < pics.size(); i++) {
                    addPic(pics.get(i));
                }
            }
            mtvCopyTo.setText(launchWorkReportDraft.getDraft(this, "copyNames").replace(",", "、"));
        }

    }

    private void setPicTureParams() {
        PicParams = new LinearLayout.LayoutParams(Utils
                .dip2px(70),
                Utils.dip2px(70));
        PicParams.setMargins(0, 0, 0, 0);
        PicParams.gravity = Gravity.CENTER;
    }

    /**
     * 获得直属领导
     */
    private void getLeader() {
        HttpParams params = new HttpParams();
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        params.put("department_id", AppConfig.getAppConfig(this).getDepartmentId());
        initKjHttp().post(Api.GET_LEADER, params, new HttpCallBack() {
            @Override
            public void onFinish() {
                super.onFinish();
                hideLoadingView();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                //判断上级联系人不存在
                String[] strings = t.split(":");
                if (strings.length > 2) {
                    LogUtils.e(strings[2]);
                    if (strings[2].contains("返回结果为空")) {
                        Toast.makeText(WorkReportLaunchActivity.this, "上级申批人不存在", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                try {
                    JSONObject object = new JSONObject(t);
                    JSONObject reportInfo = Api.getDataToJSONObject(object);
                    setLeaderNameAndIcon(reportInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                hideLoadingView();
                catchWarningByCode(errorNo);
                super.onFailure(errorNo, strMsg);
            }
        });
    }

    /**
     * @param reportInfo 设置直属领导姓名头像
     */
    private void setLeaderNameAndIcon(JSONObject reportInfo) {
        try {
            LogUtils.e("reportInfo.getString(portrait)->" + reportInfo.getString("portrait"));
            mReporterPortrait.setImageURI("http://" + reportInfo.getString("portrait"));
            mTvLeaderName.setText(reportInfo.getString("username"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化界面控件
     */
    private void initWidget() {

        ((RadioButton) mRadioGroup.getChildAt(0)).setChecked(true);
        mUpdateDate.setOnClickListener(this);
        mTolbarTextBtn.setOnClickListener(this);
        mIvAddPerson.setOnClickListener(this);
        mTvAddPicTips.setOnClickListener(this);
        mBtnVoiceInput.setOnClickListener(this);
        mTvReadWeekly.setOnClickListener(this);
        mLlGetedWeekly.setVisibility(View.GONE);
        mTvStartDate.setOnClickListener(this);
        mTvEndDate.setOnClickListener(this);

        setListenerForRootView();
        setListenerForRadioGroup();
    }

    /**
     * 给RadioGroup设置选中监听
     */
    private void setListenerForRadioGroup() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.report_rb_weekly:
                        mLlGetedWeekly.setVisibility(View.GONE);
                        mBtnVoiceInput.setVisibility(View.VISIBLE);
                        mTvReportTitle.setText("汇报内容");
                        mReportDateStyle = "1";
                        mTvReadWeekly.setVisibility(View.GONE);

                        break;
                    case R.id.report_rb_monthly:

                        LogUtils.e("mEtGetedWeekly.getText().toString()->" + mEtGetedWeekly.getText().toString());
                        if (!(mEtGetedWeekly.getText().toString().equals(""))) {
                            mTvReadWeekly.setVisibility(View.GONE);
                        } else {
                            mTvReadWeekly.setVisibility(View.VISIBLE);
                        }
                        mLlGetedWeekly.setVisibility(View.VISIBLE);
                        mTvReportTitle.setText("完成情况");
                        mBtnVoiceInput.setVisibility(View.VISIBLE);
                        mReportDateStyle = "2";

                        break;

                }
            }
        });
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
                Intent intent = new Intent(WorkReportLaunchActivity.this,
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


    /**
     * 给根视图设置监听事件,需要重新测量scrollView的高度
     */
    private void setListenerForRootView() {
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                mRootView.getWindowVisibleDisplayFrame(rect);
                int screenHeight = mRootView.getRootView().getHeight();
                int heightDifference = screenHeight - rect.bottom;
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)
                        mScrollView.getLayoutParams();
                layoutParams.setMargins(0, 0, 0, heightDifference);
                mScrollView.requestLayout();
            }
        });


    }


    private void initToolBar() {
        if (mToolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        mToolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(mToolbar);
        mTvTitle.setText("发起工作汇报");
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        mTvTitle.setLayoutParams(lp);
        mTolbarTextBtn.setText("发起");
        mTolbarTextBtn.setVisibility(View.VISIBLE);
        mToolbar.setNavigationIcon(R.drawable.toolbar_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(mEtReport.getText().toString().equals(""))
                        || !(mEtGetedWeekly.getText().toString().equals(""))
                        ) {
                    showSaveDraftTips();
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_start_date:
                showDatePicker(mTvStartDate);
                break;
            case R.id.tv_end_date:
                showDatePicker(mTvEndDate);
                break;
            case R.id.report_iv_add_person://添加抄送人
                Intent intentMutli = new Intent(this, SelectContactsActivity.class);
                intentMutli.putParcelableArrayListExtra("selectedContacts", contactsList);
                startActivityForResult(intentMutli, REQUEST_CODE_MULTIPLE);
                break;

            case R.id.toolbar_text_btn://toolbar发送
                if (mEtReport.getText().toString().equals("")) {
                    if (mReportDateStyle.equals("1")) {
                        showToast("汇报内容不能为空");
                    } else {
                        showToast("完成情况不能为空");
                    }
                } else {
                    if (checkDateIsRight()) {
                        sendToBackground();
                    } else {
                        showToast("请设置正确的开始结束时间");
                    }
                }
                break;

            case R.id.tv_read_weekly:
                if (checkDateIsRight()) {
                    //读取周报
                    getWeekly();
                } else {
                    showToast("请设置正确的开始结束时间");
                }

                break;
            case R.id.tv_add_pic_tips:
                isChangePic = false;
                upLoadPicture();
                break;

            case R.id.btn_voice_input:
                voiceInput();

                break;
        }
    }

    /**
     * 语音录入操作
     */
    private void voiceInput() {
        requestRunTimePermission(new String[]{Manifest.permission.RECORD_AUDIO}, new PermissionListener() {
            @Override
            public void onGranted() {
                IflytekUtil iflytekUtil = new IflytekUtil(WorkReportLaunchActivity.this, mEtReport);
                iflytekUtil.showIatDialog();
            }

            @Override
            public void onDenied() {
                showToast("录音权限被拒绝！请手动设置");
            }
        });

    }

    private boolean checkDateIsRight() {
        if (mTvStartDate.getText().toString().trim().equals("点击选择开始时间")
                || mTvEndDate.getText().toString().trim().equals("点击选择结束时间")) {
            return false;
        }
        return true;
    }


    /**
     * 发送报告到后台
     */
    private void sendToBackground() {
        HttpParams params = new HttpParams();

        showLoadingView();
        params.put("complete", mEtReport.getText().toString());
        if (mReportDateStyle.equals("1")) {
            params.put("content", mEtReport.getText().toString());
        } else {
            params.put("content", mEtGetedWeekly.getText().toString());
        }
        params.put("copy", copy.toString());
        params.put("department_id", AppConfig.getAppConfig(this).getDepartmentId());
        params.put("first_time", mTvStartDate.getText().toString().trim().replace("-", "/"));
        params.put("last_time", mTvEndDate.getText().toString().trim().replace("-", "/"));
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        params.put("type", mReportDateStyle);
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        picBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : picUrlMap.entrySet()) {
            picBuilder.append(entry.getValue().replace("http://", "")).append(",");
        }
        LogUtils.e("picBuilder-->" + picBuilder.toString());
        //params.put("upload_path", picBuilder.toString().substring(picBuilder.length()-1));
        //修复上传图片picBuilder崩溃问题
        params.put("upload_path", picBuilder.toString().substring(0, picBuilder.length() == 0 ? 0 : picBuilder.length() - 1));


        initKjHttp().post(Api.REPORT_SEND_TO_BACKGROUND, params, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                hideLoadingView();
                LogUtils.e("onSuccess->" + t);
                try {
                    JSONObject jo = new JSONObject(t);
                    if ((Api.getCode(jo) == Api.RESPONSES_CODE_OK)) {
                        showToast("发送成功");
                        launchWorkReportDraft.setwhichDate(WorkReportLaunchActivity.this);
                        finish();
                    } else if (Api.getCode(jo) ==Api.RESPONSES_CODE_UID_NULL){
                        catchWarningByCode(Api.getCode(jo));
                    }else {
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
                LogUtils.e("工作汇报发送失败返回数据：" + strMsg);
                super.onFailure(errorNo, strMsg);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {

            if (requestCode == REQUEST_CODE_MULTIPLE) {
                addCopyPersonOperate(data);
            } else if (resultCode == RESULT_OK && requestCode == AFFIRM_PICTURE_RESULT) {
                addPicture(file);
            } else if (resultCode == RESULT_OK && requestCode == UPDATE_PROTRAIT) {
                if (data != null) {
                    /****http://www.jb51.net/article/56990.htm*****/
                    Bitmap bitmap = null;
                    //TODO 或者版本》6.0，需要做处理
                    mtyb = Build.BRAND;// 手机品牌，小米的5.0做了相关处理
                    Uri uri = Uri.parse(data.getStringExtra("data"));
                    LogUtils.e("UPDATE_PROTRAIT返回后：" + uri.toString());
                    try {

                        bitmap = getThumbnail(uri, 300);
                        if (null == bitmap) {
                            return;
                        }

                        //暂不压缩
//                        bitmap = BitmapUtils.zoomBitmap(bitmapOrign, Utils.dip2px(160), Utils.dip2px(160));

                    } catch (Exception e) {
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
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        bos.flush();
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    Intent intent = new Intent(WorkReportLaunchActivity.this,
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
        super.onActivityResult(requestCode, resultCode, data);
    }

    public Bitmap getThumbnail(Uri uri, int size) throws FileNotFoundException, IOException {

        LogUtils.e("getThumbnail:1->");

        InputStream
                input = this.getContentResolver().openInputStream(uri);

        LogUtils.e("getThumbnail:2->");

        BitmapFactory.Options
                onlyBoundsOptions = new

                BitmapFactory.Options();
        LogUtils.e("getThumbnail:3->");
        onlyBoundsOptions.inJustDecodeBounds
                = true;
        LogUtils.e("getThumbnail:4->onlyBoundsOptions.outWidth:" + onlyBoundsOptions.outWidth);
        onlyBoundsOptions.inDither = true;//optional

        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_4444;//optional

        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        LogUtils.e("getThumbnail:5->onlyBoundsOptions.outWidth:" + onlyBoundsOptions.outWidth);
        input.close();

        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
            return null;
        LogUtils.e("getThumbnail:6->");
        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;
        LogUtils.e("getThumbnail:7->");
        double ratio = (originalSize > size) ? (originalSize / size) : 1.0;
        LogUtils.e("getThumbnail:8->");
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        LogUtils.e("getThumbnail:9->");
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        LogUtils.e("getThumbnail:10->");
        bitmapOptions.inDither = true;//optional
        LogUtils.e("getThumbnail:11->");
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional

        input = this.getContentResolver().openInputStream(uri);
        LogUtils.e("getThumbnail:12->");
        Bitmap bitmap = BitmapFactory.decodeStream(input, null,
                bitmapOptions);
        LogUtils.e("getThumbnail:13->");
        input.close();

        return bitmap;

    }

    private int getPowerOfTwoForSampleRatio(double ratio) {

        int k = Integer.highestOneBit((int) Math.floor(ratio));

        if (k == 0)
            return 1;

        else return k;

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
                LogUtils.e("上传图片后返回数据：" + t);
                try {
                    JSONObject jo = new JSONObject(t);
                    if (Api.getCode(jo) == Api.RESPONSES_CODE_OK) {
                        LogUtils.e("上传图片后返回数据：" + Api.getDataToJSONArray(jo).get(0));
                        try {
                            //TODO 打包时候更改：智汇的这样写
//                        addPic("http://" + Api.getDataToJSONArray(jo).get(0));
                            //TODO 打包时候更改：善林的这样写
                            addPic(Constants.SLPicBaseUrl + Api.getDataToJSONArray(jo).get(0));
                        } catch (Exception e) {
                        }


                    } else if (Api.getCode(jo) == Api.RESPONSES_CODE_TOKEN_NO_MATCH) {
                        catchWarningByCode(Api.getCode(jo));
                    }else if (Api.getCode(jo) ==Api.RESPONSES_CODE_UID_NULL){
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

    private void addPic(final String pic_url) {
        try {
            View view = LayoutInflater.from(WorkReportLaunchActivity
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
            mLlPictures.addView(view, PicParams);
            if (mLlPictures.getChildCount() >= 1) {
                mTvAddPicTips.setGravity(Gravity.CENTER_VERTICAL);

            }
            picUrlMap.put(pic_url, pic_url);
            picUrls.add(pic_url);
        } catch (Exception e) {
        }
    }

    /**
     * 添加抄送人操作
     */
    private void addCopyPersonOperate(Intent data) {

        this.contactsList = data.getParcelableArrayListExtra("contacts");

        copy.setLength(0);
        copyNameBuilder.setLength(0);
        for (int i = 0; i < contactsList.size(); i++) {
            String username = contactsList.get(i).getUsername();


            if (i != contactsList.size() - 1) {
                copyNameBuilder.append(username).append("、");
                copy.append(contactsList.get(i).getUid()).append(",");
            } else if (i == contactsList.size() - 1) {
                copyNameBuilder.append(username);
                copy.append(contactsList.get(i).getUid());
            }
        }
        LogUtils.e("要抄送的联系人:" + copyNameBuilder.toString());
        mtvCopyTo.setText(copyNameBuilder.toString());
    }


    /**
     * 显示下一步的日期选择器
     */
    private void showDatePicker(final TextView targetTextView) {
        final DatePicker picker = new DatePicker(this, DatePicker.YEAR_MONTH_DAY);
        Calendar cal = Calendar.getInstance();
        picker.setSelectedItem(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH));
        picker.setSubmitText("确定");
        picker.setSubmitTextColor(Color.parseColor("#2d9dff"));
        picker.setTextColor(Color.parseColor("#2d9dff"));
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                targetTextView.setText(year + "/" + month + "/" + day);
            }
        });
        picker.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if ((!(mEtReport.getText().toString().equals("")))) {
                    showSaveDraftTips();
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showSaveDraftTips() {
        View view = View.inflate(this, R.layout.launch_work_report_draft, null);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams
                .WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, false);
        Button btnSaveDraft = (Button) view.findViewById(R.id.btn_save_draft);
        btnSaveDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDraft();
                finish();
            }
        });
        Button btnCancelSaveDraft = (Button) view.findViewById(R.id.btn_cancel_save_draft);
        btnCancelSaveDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchWorkReportDraft.setwhichDate(WorkReportLaunchActivity.this);
                popupWindow.dismiss();
                finish();
            }
        });
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

        popupWindow.setAnimationStyle(R.style.dialog_pop_anim_style);
        popupWindow.showAtLocation(mRootView, Gravity.CENTER, 0, 0);
    }

    /**
     * 保存草稿
     */
    private void saveDraft() {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("whichDate", mReportDateStyle);
        map.put("startDate", mTvStartDate.getText().toString());
        map.put("endDate", mTvEndDate.getText().toString());
        map.put("weeklyContent", mEtGetedWeekly.getText().toString());
        map.put("reportContent", mEtReport.getText().toString());
        StringBuilder picUrls = new StringBuilder();
        for (Map.Entry<String, String> entry : picUrlMap.entrySet()) {
            //TODO
            picUrls.append(entry.getValue()).append(",");

        }
        LogUtils.e("picUrls.toString()->" + picUrls.toString());
        map.put("picUrl", picUrls.toString());
        map.put("copyId", copy.toString());
        map.put("copyNames", mtvCopyTo.getText().toString().replace("、", ","));

        launchWorkReportDraft.setDraft(this, map);
    }


    /**
     * 读取周报
     */
    private void getWeekly() {
        final HttpParams params = new HttpParams();
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        params.put("department_id", AppConfig.getAppConfig(this).getDepartmentId());
        LogUtils.e("first_time" + mTvStartDate.getText().toString().trim());
        LogUtils.e("last_time" + mTvStartDate.getText().toString().trim());
        params.put("first_time", mTvStartDate.getText().toString().trim());
        params.put("last_time", mTvEndDate.getText().toString().trim());
        initKjHttp().post(Api.GET_WEEKLY_REPORT, params, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                try {
                    JSONObject jo = new JSONObject(t);
                    if (Api.getCode(jo) == Api.RESPONSES_CODE_OK) {
                        mLlGetedWeekly.setVisibility(View.VISIBLE);
                        mTvReadWeekly.setVisibility(View.GONE);
                        mTvGetWeekLyTips.setVisibility(View.VISIBLE);
                        mEtGetedWeekly.setVisibility(View.VISIBLE);
                        LogUtils.e("读取周报返回数据：" + Api.getDataToJSONObject(jo));
                        JSONObject obj = Api.getDataToJSONObject(jo);
                        mEtGetedWeekly.setText(obj.getString("content").replace("&nbsp;", " ")
                                .replace("<br/>", "\n"));

                    } else if (Api.getCode(jo) == Api.RESPONSES_CODE_TOKEN_NO_MATCH) {
                        catchWarningByCode(Api.getCode(jo));
                    } else if (Api.getCode(jo) == Api.RESPONSES_CODE_DATA_EMPTY) {
                        showToast("当前时间段没有周报");
                    }else if (Api.getCode(jo) ==Api.RESPONSES_CODE_UID_NULL){
                        catchWarningByCode(Api.getCode(jo));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.onSuccess(t);
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                catchWarningByCode(errorNo);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

}
