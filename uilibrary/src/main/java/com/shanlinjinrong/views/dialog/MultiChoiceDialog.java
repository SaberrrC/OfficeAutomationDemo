package com.shanlinjinrong.views.dialog;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.shanlinjinrong.uilibrary.R;
import com.shanlinjinrong.views.dialog.adapter.MultiChoiceAdapter;
import com.shanlinjinrong.views.dialog.listener.OnItemClickListener;
import com.shanlinjinrong.views.listview.decoration.LineItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 丁 on 2017/9/12.
 * 多选对话框
 */
public class MultiChoiceDialog extends BaseDialog<MultiChoiceDialog> implements OnItemClickListener {
    private OnSelectListener mOnSelectListener;
    private List<MultiItem> mItems;

    private TextView mSureBtn;
    private TextView mCancelBtn;

    private String mSureBtnText = "";
    private String mCancelBtnText = "";

    public MultiChoiceDialog(Context context, List<MultiItem> mItems) {
        super(context);
        this.mItems = mItems;
        initView(context);
    }

    private void initView(Context context) {
        RecyclerView mMultiList = (RecyclerView) findViewById(R.id.multi_choice_list);
        mMultiList.setLayoutManager(new LinearLayoutManager(context));
        MultiChoiceAdapter adapter = new MultiChoiceAdapter(mItems);
        mMultiList.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        mMultiList.addItemDecoration(new LineItemDecoration(context, LineItemDecoration.VERTICAL_LIST, R.drawable.driver_line));

        mSureBtn = (TextView) findViewById(R.id.dialog_btn_sure);
        mCancelBtn = (TextView) findViewById(R.id.dialog_btn_cancel);

        mSureBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.dialog_multi_choice;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.dialog_btn_sure) {
            if (mOnSelectListener != null) {
                List<MultiItem> mSelectItems = new ArrayList<>();
                for (int i = 0; i < mItems.size(); i++) {
                    if (mItems.get(i).isCheck())
                        mSelectItems.add(mItems.get(i));
                }
                mOnSelectListener.onSelected(mSelectItems);
            }
        }
        dismiss();

    }

    @Override
    public void onItemClicked(RecyclerView.Adapter adapter, int position) {
        mItems.get(position).setCheck(!mItems.get(position).isCheck());
        adapter.notifyItemChanged(position);
    }

    public interface OnSelectListener {
        void onSelected(List<MultiItem> selectItems);
    }

    public MultiChoiceDialog setOnSelectListener(OnSelectListener mOnSelectListener) {
        this.mOnSelectListener = mOnSelectListener;
        return this;
    }

    private void refreshView() {
        if (!TextUtils.isEmpty(mSureBtnText)) {
            mSureBtn.setText(mSureBtnText.trim());
        }

        if (!TextUtils.isEmpty(mCancelBtnText)) {
            mCancelBtn.setText(mCancelBtnText.trim());
        }
    }


    public MultiChoiceDialog setSureBtnText(String mSureBtnText) {
        this.mSureBtnText = mSureBtnText;
        return this;
    }

    public MultiChoiceDialog setCancelBtnText(String mCancelBtnText) {
        this.mCancelBtnText = mCancelBtnText;
        return this;
    }

    @Override
    public void show() {
        refreshView();
        super.show();
    }
}
