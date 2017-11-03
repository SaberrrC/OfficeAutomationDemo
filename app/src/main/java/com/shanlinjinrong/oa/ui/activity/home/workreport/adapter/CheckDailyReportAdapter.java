package com.shanlinjinrong.oa.ui.activity.home.workreport.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.workreport.bean.ReportPageItem;
import com.shanlinjinrong.oa.utils.EmojiFilter;

import java.util.List;


/**
 * Created by 丁 on 2017/8/21.
 * 审核工作汇报页面的adapter
 */
public class CheckDailyReportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static int CLICK = 1; //可点击cell
    public static int WRITE_EVALUATION = 2;//输入评价cell
    public static int SHOW_PLAN = 3;// 分数cell
    public static int WRITE_SCORE = 4;// 分数cell

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<ReportPageItem> mData;

    private boolean isEditEnable = false;

    private OnItemClickListener mItemClickListener;

    public CheckDailyReportAdapter(Context context, List<ReportPageItem> mData, boolean isEditEnable) {
        mContext = context;
        this.mData = mData;
        mLayoutInflater = LayoutInflater.from(context);
        this.isEditEnable = isEditEnable;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == CLICK) {
            return new ClickCellHolder(mLayoutInflater.inflate(R.layout.item_click_cell, parent, false));
        } else if (viewType == WRITE_EVALUATION) {
            return new WriteEvaluationCellHolder(mLayoutInflater.inflate(R.layout.item_write_evaluation_cell, parent, false));
        } else if (viewType == SHOW_PLAN) {
            return new ShowPlanCellHolder(mLayoutInflater.inflate(R.layout.show_plan_cell, parent, false));
        } else {
            return new WriteScoreCellHolder(mLayoutInflater.inflate(R.layout.item_write_cell, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String title = mData.get(position).getTitle();
        String content = mData.get(position).getContent();

        if (holder instanceof ClickCellHolder) {
            ClickCellHolder clickHolder = (ClickCellHolder) holder;
            clickHolder.mLeftTitle.setText(title);
            clickHolder.mRightText.setText(content);
            if (content.equals(mContext.getString(R.string.work_report_no_evaluation))) {
                clickHolder.mRightText.setEnabled(false);
            } else {
                clickHolder.mRightText.setEnabled(true);
            }
        } else if (holder instanceof WriteEvaluationCellHolder) {
            String evaluation1 = mData.get(position).getEvaluationSupervisor();
            String evaluation2 = mData.get(position).getEvaluationCheckMan();
            WriteEvaluationCellHolder writeHolder = (WriteEvaluationCellHolder) holder;
            writeHolder.mLeftTitle.setText(title);
            writeHolder.mContent.setText(content);
            if (!TextUtils.isEmpty(evaluation1)) {
                writeHolder.mSupervisorEvaluate.setText(evaluation1);
            }

            if (!TextUtils.isEmpty(evaluation2)) {
                writeHolder.mCheckManEvaluate.setText(evaluation2);
            }

            if (isEditEnable) {
                writeHolder.mSupervisorEvaluate.setEnabled(false);
                writeHolder.mCheckManEvaluate.setEnabled(false);
            }

            writeHolder.mSupervisorEvaluate.setTag(position);//先设置tag，检测内容改变时用到了
            writeHolder.mSupervisorEvaluate.addTextChangedListener(new WriteTextWatcher(writeHolder.mSupervisorEvaluate));

            writeHolder.mCheckManEvaluate.setTag(position);//先设置tag，检测内容改变时用到了
            writeHolder.mCheckManEvaluate.addTextChangedListener(new WriteTextWatcher(writeHolder.mCheckManEvaluate));

        } else if (holder instanceof ShowPlanCellHolder) {
            ShowPlanCellHolder planHolder = (ShowPlanCellHolder) holder;
            if (!TextUtils.isEmpty(content))
                planHolder.mPlan.setText(content);
        } else {
            WriteScoreCellHolder scoreHolder = (WriteScoreCellHolder) holder;
            scoreHolder.mLeftTitle.setText(title);

            if (!TextUtils.isEmpty(content))
                scoreHolder.mEdit.setText(content);

            if (position == mData.size() - 3)
                scoreHolder.mEdit.setHint("满分60分");
            else
                scoreHolder.mEdit.setHint("满分20分");

            if (isEditEnable) {
                scoreHolder.mEdit.setEnabled(false);
            }

            scoreHolder.mEdit.setTag(position);
            scoreHolder.mEdit.addTextChangedListener(new WriteTextWatcher(scoreHolder.mEdit));

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mData == null)
            return 0;
        if (mData.get(position).getType() == WRITE_EVALUATION)
            return WRITE_EVALUATION;
        if (mData.get(position).getType() == WRITE_SCORE)
            return WRITE_SCORE;
        if (mData.get(position).getType() == SHOW_PLAN)
            return SHOW_PLAN;
        else
            return CLICK;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    //可以点击的cell
    private class ClickCellHolder extends RecyclerView.ViewHolder {
        TextView mLeftTitle;

        TextView mRightText;

        ClickCellHolder(View view) {
            super(view);
            mLeftTitle = (TextView) view.findViewById(R.id.tv_left_title);
            mRightText = (TextView) view.findViewById(R.id.tv_right_text);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    //带有评价的cell
    private class WriteEvaluationCellHolder extends RecyclerView.ViewHolder {

        TextView mLeftTitle;
        TextView mContent;
        EditText mSupervisorEvaluate;
        EditText mCheckManEvaluate;

        WriteEvaluationCellHolder(View view) {
            super(view);
            mLeftTitle = (TextView) view.findViewById(R.id.tv_left_title);
            mContent = (TextView) view.findViewById(R.id.tv_show_content);
            mSupervisorEvaluate = (EditText) view.findViewById(R.id.et_supervisor_evaluate);
            mCheckManEvaluate = (EditText) view.findViewById(R.id.et_check_man_evaluate);
            InputFilter[] filters = new InputFilter[]{new EmojiFilter(50)};
            mSupervisorEvaluate.setFilters(filters);
            mCheckManEvaluate.setFilters(filters);
        }
    }


    //填写分数的cell
    private class WriteScoreCellHolder extends RecyclerView.ViewHolder {

        TextView mLeftTitle;

        EditText mEdit;

        WriteScoreCellHolder(View view) {
            super(view);
            mLeftTitle = (TextView) view.findViewById(R.id.tv_left_title);
            mEdit = (EditText) view.findViewById(R.id.et_text);
            mEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
            InputFilter[] filters = new InputFilter[]{new InputFilter.LengthFilter(2)};
            mEdit.setFilters(filters);
        }
    }

    //计划
    private class ShowPlanCellHolder extends RecyclerView.ViewHolder {

        TextView mPlan;

        ShowPlanCellHolder(View view) {
            super(view);
            mPlan = (TextView) view.findViewById(R.id.tv_show_plan);
        }
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public CheckDailyReportAdapter setItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
        return this;
    }

    private class WriteTextWatcher implements TextWatcher {
        private EditText mEdit;

        WriteTextWatcher(EditText view) {
            mEdit = view;
        }

        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start,
                                      int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String str = "";
            if (s != null)
                str = s.toString();
            int position = (int) mEdit.getTag();
            Log.i("WriteTextWatcher", "position : " + position);
            //最后3项是填写分数的，区别对待一下
            if (position < mData.size() - 3) {
                if (mEdit.getId() == R.id.et_check_man_evaluate)
                    mData.get(position).setEvaluationCheckMan(str);
                if (mEdit.getId() == R.id.et_supervisor_evaluate)
                    mData.get(position).setEvaluationSupervisor(str);
            } else {
                // 当EditText数据发生改变的时候存到data变量中
                mData.get(position).setContent(str);
                if (!TextUtils.isEmpty(str)) {
                    int score = Integer.valueOf(str);
                    if (position == mData.size() - 3 && (score > 60 || score < 0)) {
                        Toast.makeText(mContext, mContext.getString(R.string.work_report_data_work_score_limit), Toast.LENGTH_SHORT).show();
                    } else if (position == mData.size() - 2 && (score > 20 || score < 0)) {
                        Toast.makeText(mContext, mContext.getString(R.string.work_report_data_professional_score_limit), Toast.LENGTH_SHORT).show();
                    } else if (position == mData.size() - 1 && (score > 20 || score < 0)) {
                        Toast.makeText(mContext, mContext.getString(R.string.work_report_data_team_score_limit), Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }
    }
}
