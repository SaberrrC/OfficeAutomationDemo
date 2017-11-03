package com.shanlinjinrong.views.dialog;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shanlinjinrong.uilibrary.R;
import com.shanlinjinrong.views.dialog.adapter.SingleChoiceAdapter;
import com.shanlinjinrong.views.dialog.listener.OnItemClickListener;
import com.shanlinjinrong.views.listview.decoration.LineItemDecoration;

/**
 * Created by 丁 on 2017/9/12.
 * 单选对话框
 */
public class SingleChoiceDialog extends BaseDialog<SingleChoiceDialog> implements OnItemClickListener {
    private RecyclerView mSingleList;
    private OnSelectListener mOnSelectListener;
    private CharSequence[] mItems;


    public SingleChoiceDialog(Context context, CharSequence[] mItems) {
        super(context);
        this.mItems = mItems;
        initView(context);
    }

    private void initView(Context context) {
        mSingleList = (RecyclerView) findViewById(R.id.single_choice_list);
        mSingleList.setLayoutManager(new LinearLayoutManager(context));
        SingleChoiceAdapter adapter = new SingleChoiceAdapter(mItems, 0);
        mSingleList.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        mSingleList.addItemDecoration(new LineItemDecoration(context, LineItemDecoration.VERTICAL_LIST, R.drawable.driver_line));

    }

    @Override
    protected int getContentLayout() {
        return R.layout.dialog_single_choice;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClicked(RecyclerView.Adapter adapter, int position) {
        SingleChoiceAdapter singleAdapter = (SingleChoiceAdapter) adapter;
        if (mOnSelectListener != null) {
            mOnSelectListener.onSelected(position, singleAdapter.getItems()[position].toString());
            dismiss();
        }
    }

    public interface OnSelectListener {
        void onSelected(int position, String content);
    }

    public SingleChoiceDialog setOnSelectListener(OnSelectListener mOnSelectListener) {
        this.mOnSelectListener = mOnSelectListener;
        return this;
    }
}
