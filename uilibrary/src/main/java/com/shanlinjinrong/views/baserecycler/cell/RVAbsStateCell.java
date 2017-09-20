package com.shanlinjinrong.views.baserecycler.cell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shanlinjinrong.uilibrary.R;
import com.shanlinjinrong.views.baserecycler.base.RVBasicViewHolder;

/**
 * 一个通用默认的Cell的基类，
 * Created by k.huang on 2017/2/20.
 */

public abstract class RVAbsStateCell extends RVBasicCell<Object> {
    protected View mView;
    protected int mHeight = 0;

    public RVAbsStateCell (Object o){
        super(o);
    }

    public void setView(View view) {
        mView = view;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    @Override
    public RVBasicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_state_cell_layout,null);
        //如果调用者没有设置显示的View就用默认的View
        if(mView == null){
            mView = getDefaultView(parent.getContext());
        }
        if(mView!=null){
            LinearLayout container = (LinearLayout) view.findViewById(R.id.rv_cell_state_root_layout);
            container.removeAllViews();
            container.addView(mView);
        }
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if(mHeight > 0){
            params.height = mHeight;
        }
        view.setLayoutParams(params);
        return new RVBasicViewHolder(view);
    }

    @Override
    public void releaseResource() {
        if(mView!=null){
            mView = null;
        }
    }

    /**
     *
     * 子类提供的默认布局，当没有通过设置显示的{@link #setView(View)}View的时候，就显示默认的View
     * @param context
     * @return 返回默认布局
     */
    protected abstract View getDefaultView(Context context);
}
