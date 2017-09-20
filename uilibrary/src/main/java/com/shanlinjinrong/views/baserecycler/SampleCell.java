package com.shanlinjinrong.views.baserecycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanlinjinrong.uilibrary.R;
import com.shanlinjinrong.views.baserecycler.base.RVBasicViewHolder;
import com.shanlinjinrong.views.baserecycler.cell.RVBasicCell;


/**
 */

public class SampleCell extends RVBasicCell<String> {
    public static final int TYPE = 1;
    private Context context;
    public SampleCell(Context context,String s) {
        super(s);
        this.context = context;
    }

    @Override
    public int getItemType() {
        return TYPE;
    }

    @Override
    public RVBasicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RVBasicViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_sample_cell_layout,null));
    }

    @Override
    public void onBindViewHolder(RVBasicViewHolder holder, int position) {

        TextView textView = holder.retrieveView(R.id.tv);
        textView.setText(mData);

        ImageView imgView = holder.retrieveView(R.id.img_view);

    }
}
