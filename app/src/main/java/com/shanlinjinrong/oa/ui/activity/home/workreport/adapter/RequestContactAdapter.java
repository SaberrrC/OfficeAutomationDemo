package com.shanlinjinrong.oa.ui.activity.home.workreport.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.model.selectContacts.Child;
import com.shanlinjinrong.oa.ui.activity.home.workreport.OnSelectedContract;


import java.util.List;

public class RequestContactAdapter extends BaseQuickAdapter<Child> {

    private List<Child> mData;
    private OnSelectedContract mOnSelected;

    public RequestContactAdapter(List<Child> data, OnSelectedContract onSelected) {
        super(R.layout.contact_child_layout, data);
        mData = data;
        mOnSelected = onSelected;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Child child) {

        baseViewHolder.setText(R.id.tvName, child.getUsername());
        baseViewHolder.setChecked(R.id.child_check, child.getChecked());
        baseViewHolder.setOnClickListener(R.id.ll_contract_selected, view -> {
            for (int i = 0; i < mData.size(); i++) {
                mData.get(i).setChecked(false);
                if (mData.get(i).getUsername().equals(child.getUsername()) && baseViewHolder.getPosition() == baseViewHolder.getAdapterPosition()) {
                    mData.get(baseViewHolder.getPosition()).setChecked(true);
                } else {
                    mData.get(i).setChecked(false);
                }
            }
            mOnSelected.onClick(mData, baseViewHolder.getPosition());
        });
    }

    @Override
    public void setNewData(List<Child> data) {
        super.setNewData(data);
        mData = data;
    }
}
