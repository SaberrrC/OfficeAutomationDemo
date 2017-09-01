package com.shanlinjinrong.oa.ui.activity.home.workreport.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.workreport.bean.ItemBean;
import com.shanlinjinrong.oa.utils.EmojiFilter;

import java.util.List;

/**
 * Created by 丁 on 2017/8/21.
 * 工作汇报list的adapter
 */
public class WorkReportLaunchListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static int WRITE_TYPE = 1;//可点击cell
    public static int CLICK_TYPE = 2;//可输入的cell
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<ItemBean> mData;


    private OnItemClickListener mItemClickListener;

    public WorkReportLaunchListAdapter(Context context, List<ItemBean> mData) {
        mContext = context;
        this.mData = mData;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == CLICK_TYPE) {
            return new ClickCellHolder(mLayoutInflater.inflate(R.layout.item_click_cell, parent, false));
        } else {
            return new WriteCellHolder(mLayoutInflater.inflate(R.layout.item_write_cell, parent, false));
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
            if (content.equals(mContext.getString(R.string.work_report_no_write))) {
                clickHolder.mRightText.setEnabled(false);
            } else {
                clickHolder.mRightText.setEnabled(true);
            }
        } else if (holder instanceof WriteCellHolder) {
            WriteCellHolder writeHolder = (WriteCellHolder) holder;
            writeHolder.mLeftTitle.setText(title);
            Object tag = writeHolder.mEdit.getTag();
//            if (tag != null) {
//                int tagPos = (int) tag;
//            if (!TextUtils.isEmpty(content) && tagPos == position)
            if (!TextUtils.isEmpty(content))
                writeHolder.mEdit.setText(content);
//            }

            writeHolder.mEdit.addTextChangedListener(new WriteTextWatcher(writeHolder));
            writeHolder.mEdit.setTag(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mData == null)
            return 0;
        if (mData.get(position).getType() == WRITE_TYPE)
            return WRITE_TYPE;
        else
            return CLICK_TYPE;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class ClickCellHolder extends RecyclerView.ViewHolder {
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

    class WriteCellHolder extends RecyclerView.ViewHolder {

        TextView mLeftTitle;

        EditText mEdit;

        WriteCellHolder(View view) {
            super(view);
            mLeftTitle = (TextView) view.findViewById(R.id.tv_left_title);
            mEdit = (EditText) view.findViewById(R.id.et_text);
            InputFilter[] filters = new InputFilter[]{new EmojiFilter(50)};
            mEdit.setFilters(filters);
        }
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public WorkReportLaunchListAdapter setItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
        return this;
    }

    private class WriteTextWatcher implements TextWatcher {
        WriteTextWatcher(WriteCellHolder holder) {
            mHolder = holder;
        }

        private WriteCellHolder mHolder;

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
            if (s != null) {
                int position = (int) mHolder.mEdit.getTag();
                // 当EditText数据发生改变的时候存到data变量中
                mData.get(position).setContent(s.toString());
            }
        }
    }
}
