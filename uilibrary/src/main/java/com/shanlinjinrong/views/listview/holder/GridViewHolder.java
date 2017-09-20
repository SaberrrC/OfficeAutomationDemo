package com.shanlinjinrong.views.listview.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanlinjinrong.uilibrary.R;

/**
 * Created by 丁 on 2017/9/11.
 */

public class GridViewHolder extends BaseViewHolder {
    public ImageView image;
    public TextView title;

    public GridViewHolder(View itemView) {
        super(itemView);
        image = (ImageView) itemView.findViewById(R.id.image);
        title = (TextView) itemView.findViewById(R.id.title);

    }
}
