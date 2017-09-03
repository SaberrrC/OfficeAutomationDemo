package com.shanlinjinrong.oa.ui.activity.home.workreport;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.ui.activity.common.ShowPictureActivity;
import com.shanlinjinrong.oa.ui.base.BaseActivity;
import com.shanlinjinrong.oa.utils.LogUtils;
import com.shanlinjinrong.oa.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <h3>Description: 工作汇报详细页 </h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/9/7.<br />
 */
public class WorkReportSingleInfoActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.report_date)
    TextView reportDate;
    @Bind(R.id.send_user)
    TextView sendUser;
    @Bind(R.id.send_date)
    TextView sendDate;
    @Bind(R.id.report_content)
    TextView reportContent;
    @Bind(R.id.report_name_tag)
    TextView reportNameTag;
    @Bind(R.id.tv_reciver)
    TextView tvReciver;
    @Bind(R.id.tv_copyer)
    TextView tvCopyer;


    @Bind(R.id.tv_work_report_bottom_btn_reply)
    Button btnReply;
    @Bind(R.id.ll_pictures)
    LinearLayout mLLPictures;
    @Bind(R.id.ll_replay_container)
    LinearLayout mLLReplyContainer;
    @Bind(R.id.ll_rootView)
    LinearLayout mLLRootView;

    @Bind(R.id.reply_top_parting_line)
    View mReplyTopPartingLine;
    private boolean isReply = false;//是否you回复功能

    public static final String KEY_WORK_REPORT_INFO = "keyWorkReportInfo";
    private String rId;
    LinearLayout.LayoutParams picParams;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_report_info);
        ButterKnife.bind(this);
        initToolBar();
        setTranslucentStatus(this);
        init();
        initData();
    }

    private void init() {
        rId = getIntent().getStringExtra("rid");
        isReply = getIntent().getBooleanExtra("isReply", false);
        setPicTureParams();
    }

    private void setPicTureParams() {
        picParams = new LinearLayout.LayoutParams(Utils
                .dip2px(70),
                Utils.dip2px(70));
        picParams.setMargins(0, 0, 0, 0);
        picParams.gravity = Gravity.CENTER;
    }

    private void initData() {
        readReport(rId);
        hideOrShowSomeView();
    }


    private void hideOrShowSomeView() {
        if (isReply) {
            //带回复，显示接收人，抄送人，回复内容标签，回复内容
            tvReciver.setVisibility(View.VISIBLE);
            tvCopyer.setVisibility(View.VISIBLE);
            btnReply.setVisibility(View.VISIBLE);
        } else {
            //不带回复，显示抄送人，底部布局
            btnReply.setVisibility(View.GONE);
        }
    }

    /**
     * 编辑工作汇报已读
     *
     * @param rid 工作汇报id
     */
    private void readReport(String rid) {
        showLoadingView();
        HttpParams params = new HttpParams();
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        params.put("department_id", AppConfig.getAppConfig(this).getDepartmentId());
        params.put("rid", rid);
        initKjHttp().post(Api.REPORT_DETAIL, params, new HttpCallBack() {
            @Override
            public void onFinish() {
                super.onFinish();
                hideLoadingView();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e(t);
                try {
                    JSONObject object = new JSONObject(t);
                    JSONObject reportInfo = Api.getDataToJSONObject(object);
                    setDataForWidget(reportInfo);
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
     * @param reportInfo 为控件设置数据
     */
    private void setDataForWidget(JSONObject reportInfo) {
        mLLRootView.setVisibility(View.VISIBLE);
        try {
            String reportNameTagTxt = "";
            String type = reportInfo.getString("type");
            switch (Integer.parseInt(type)) {
                case 1:
                    reportNameTagTxt = "周报";
                    break;
                case 2:
                    reportNameTagTxt = "月报";
                    break;
            }
            String can_reply = reportInfo.getString("can_reply");
            if (can_reply.equals("1")) {
                //可以回复
                btnReply.setVisibility(View.VISIBLE);
            } else if (can_reply.equals("2")) {
                btnReply.setVisibility(View.GONE);
            }

            reportNameTag.setText("[" + reportNameTagTxt + "]");
            reportDate.setText(reportInfo.getString("last_time"));
            sendUser.setText(reportInfo.getString("sendname"));
            sendDate.setText(reportInfo.getString("created"));

            reportContent.setText(reportInfo.getString("content").replace("&nbsp;", " ")
                    .replace("<br/>", "\n"));

            tvReciver.setText("接收：" + reportInfo.getString("receivename"));
            if (reportInfo.getString("copyname").equals("") ||
                    reportInfo.getString("copyname") == null) {
                tvCopyer.setText("抄送：" + "无");
            } else {
                tvCopyer.setText("抄送：" + reportInfo.getString("copyname"));

            }
            if (reportInfo.getJSONArray("reply").length() > 0) {
                addReplay(reportInfo.getJSONArray("reply"));
            }
            final JSONArray imgs = reportInfo.getJSONArray("imgs");


            if (imgs.length() > 0) {
                for (int i = 0; i < imgs.length(); i++) {
                    View picView = LayoutInflater.from(WorkReportSingleInfoActivity.this).inflate(R.layout.add_pic_view, null);
                    SimpleDraweeView pic = (SimpleDraweeView) picView.findViewById(R.id.sdv_pic);
                    final String picUrl = (String) imgs.get(i);
                    pic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(WorkReportSingleInfoActivity.this,
                                    ShowPictureActivity.class);
                            try {
                                intent.putExtra("picUrl", "http://"+picUrl);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            startActivity(intent);
                        }
                    });
                    pic.setImageURI("http://"+Uri.parse(picUrl));
                    mLLPictures.addView(picView, picParams);
                }
                mReplyTopPartingLine.setVisibility(View.VISIBLE);
            } else {
                mReplyTopPartingLine.setVisibility(View.GONE);
                mLLPictures.setVisibility(View.GONE);
            }


        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.toString());
        }

    }

    /**
     * @param reply
     */
    private void addReplay(JSONArray reply) {
        for (int i = 0; i < reply.length(); i++) {
            View replyItem = View.inflate(WorkReportSingleInfoActivity.this, R.layout.work_report_reply_item, null);

            try {
                TextView tvCreateTime = (TextView) replyItem.findViewById(R.id.tvCreateTime);
                String created = reply.getJSONObject(i).getString("created");
                tvCreateTime.setText(created);


                TextView tv_replay_content = (TextView) replyItem.findViewById(R.id.tv_reply_content);
                String username = reply.getJSONObject(i).getString("username");
                String content = reply.getJSONObject(i).getString("content");
                tv_replay_content.setText(username + " : " + content);

                LinearLayout picContainer = (LinearLayout) replyItem.findViewById(R.id.pics_container);
                final JSONArray picArray = reply.getJSONObject(i).getJSONArray("pics");
                if (picArray.length() > 0) {
                    for (int j = 0; j < picArray.length(); j++) {
                        View view = View.inflate(WorkReportSingleInfoActivity.this, R.layout.add_pic_view, null);
                        SimpleDraweeView picView = (SimpleDraweeView) view.findViewById(R.id.sdv_pic);
                        final String imgUrl = (String) picArray.get(j);
                        picView.setImageURI(Uri.parse(imgUrl));
                        picView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(WorkReportSingleInfoActivity.this,
                                        ShowPictureActivity.class);
                                try {
                                    intent.putExtra("picUrl", imgUrl);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                startActivity(intent);
                            }
                        });
                        picContainer.addView(view, picParams);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            mLLReplyContainer.addView(replyItem);
        }
    }

    private void initToolBar() {
        if (toolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);
        tvTitle.setText("汇报详情");
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);

    }

    @OnClick(R.id.tv_work_report_bottom_btn_reply)
    public void onClick() {
        Intent intent = new Intent(this, WorkReportReplyActivity.class);
        intent.putExtra("rid", rId);
        startActivity(intent);
        finish();
    }
}