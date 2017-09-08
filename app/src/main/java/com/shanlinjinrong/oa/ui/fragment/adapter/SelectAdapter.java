package com.shanlinjinrong.oa.ui.fragment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.model.selectContacts.Child;

import java.util.ArrayList;

//底部弹出子条目的适配器
public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<Child> dataList;

    public OnDeleteClickListener onDeleteClickListener;

    public interface OnDeleteClickListener{
        void onDeleteClick(View view, int position);
    }


    private LayoutInflater mInflater;
    public SelectAdapter(Context mContext, ArrayList<Child> dataList, OnDeleteClickListener deleteClickListener) {
        this.mContext = mContext;
        this.dataList = dataList;
        this.onDeleteClickListener=deleteClickListener;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_selected_department_people,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Child item = dataList.get(position);
        holder.bindData(item);
        holder.iv_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteClickListener.onDeleteClick(view,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(dataList==null) {
            return 0;
        }
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private Child item;
        private TextView tv_name,tv_department,tv_position;
        private ImageView iv_resume;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_department = (TextView) itemView.findViewById(R.id.tv_department);
            tv_position = (TextView) itemView.findViewById(R.id.tv_position);
            iv_resume = (ImageView) itemView.findViewById(R.id.iv_resume);
        }



        public void bindData(Child item){
            this.item = item;
            tv_name.setText(item.getUsername());
            tv_department.setText(item.getOname());
            tv_position.setText(item.getPost());
        }
    }
}
