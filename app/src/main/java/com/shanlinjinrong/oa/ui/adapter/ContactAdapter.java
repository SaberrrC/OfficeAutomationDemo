package com.shanlinjinrong.oa.ui.adapter;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.model.selectContacts.Child;
import com.shanlinjinrong.oa.model.selectContacts.Group;

import java.util.ArrayList;

/**
 * create by lvdinghao
 * 选择接收人Adapter
 * 无力吐槽，单选项，竟然要先选中，然后再点确定按钮返回，没有难度制造难度迎上去啊
 */
public class ContactAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<Group> groups;

    public ContactAdapter(Context context, ArrayList<Group> groups) {
        this.context = context;
        this.groups = groups;
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).getChildItem(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).getChildrenCount();
    }

    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    public int getGroupCount() {
        return groups.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     * 设置Group
     */
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Group group = (Group) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layInflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = layInflater.inflate(R.layout.contact_group_layout, null);
        }

        TextView tv = (TextView) convertView.findViewById(R.id.tvGroup);
        tv.setText(group.getTitle());
        ImageView rightArrow = (ImageView) convertView.findViewById(R.id.right_arrow);
        rightArrow.setImageDrawable(ResourcesCompat.getDrawable(parent.getContext().getResources(), R.drawable.arrow_down, null));
        if (!isExpanded) {
            rightArrow.setImageDrawable(ResourcesCompat.getDrawable(parent.getContext().getResources(), R.drawable.arrow_right, null));
        }
        return convertView;
    }


    /**
     * 设置childview的内容
     */
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        Child child = groups.get(groupPosition).getChildItem(childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.contact_child_layout, null);
        }

        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvName);
        tvUserName.setText(child.getUsername());
        TextView tvPost = (TextView) convertView.findViewById(R.id.tvPost);
        tvPost.setText(child.getPost());

        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.child_check);
        checkBox.setChecked(child.getChecked());
        return convertView;
    }


}