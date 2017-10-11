package com.shanlinjinrong.oa.ui.activity.home.schedule;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;

/**
 * Created by 丁 on 2017/10/10.
 * 会议邀请adapter
 */
public class MeetingInviteAdapter extends RecyclerView.Adapter<MeetingInviteAdapter.InviteHolder> {


    @Override
    public InviteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new InviteHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.meeting_invite_item, parent, false));
    }

    @Override
    public void onBindViewHolder(InviteHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class InviteHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView title;
        private TextView content;

        InviteHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            content = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }
}
