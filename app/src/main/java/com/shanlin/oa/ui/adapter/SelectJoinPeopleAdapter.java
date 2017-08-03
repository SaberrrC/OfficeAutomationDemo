package com.shanlin.oa.ui.adapter;

import android.net.Uri;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.shanlin.oa.R;
import com.shanlin.oa.model.JoinPeople;
import com.shanlin.oa.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ProjectName: GroupInformationPlatform
 * PackageName: com.itcrm.GroupInformationPlatform.ui.adapter
 * Author:Created by CXP on Date: 2016/9/6 14:50.
 * Description:选择参会人适配器
 */
public class SelectJoinPeopleAdapter extends BaseQuickAdapter<JoinPeople> {
    ArrayList<JoinPeople> selectedContacts;
    List<String> selectedLists;

    public SelectJoinPeopleAdapter(List<JoinPeople> data, ArrayList<JoinPeople> selectedContacts) {
        super(R.layout.join_people_rv_item, data);
        this.selectedContacts = selectedContacts;
        selectedLists = new ArrayList<>();
        LogUtils.e("SelectJoinPeopleAdapter与会人-》" + selectedContacts.toString());
    }

    @Override
    protected void convert(BaseViewHolder holder, final JoinPeople joinPeople) {
        holder.setText(R.id.user_name, joinPeople.username);
        holder.setText(R.id.user_post, joinPeople.post);
        View view = holder.getConvertView();
        SimpleDraweeView iv = holder.getView(R.id.portrait);
        iv.setImageURI(Uri.parse(joinPeople.portraits));
        final CheckBox cb = (CheckBox) view.findViewById(R.id.select_contact_icon);
      /*  cb.setTag(joinPeople.uid);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!selectedLists.contains(cb.getTag())) {
                        selectedLists.add(joinPeople.portraits);
                    }
                }else{
                    if (selectedLists.contains(cb.getTag())) {
                        selectedLists.remove(cb.getTag());
                    }
                }
            }
        });*/

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!selectedLists.contains(joinPeople.uid)) {
                        selectedLists.add(joinPeople.uid);
                        LogUtils.e(joinPeople.username+"被选中了"+"uid->"+joinPeople.uid);
                        LogUtils.e("当前被选中的有-》"+selectedLists.toString());
                        interSelectedJoinPeople.addJoinPeople(joinPeople.uid,joinPeople.username);
                    }
                }else{
                    if (selectedLists.contains(joinPeople.uid)) {
                        selectedLists.remove(joinPeople.uid);
                        LogUtils.e(joinPeople.username+"被移除了"+"uid->"+joinPeople.uid);
                        LogUtils.e("当前被选中的有-》"+selectedLists.toString());
                        interSelectedJoinPeople.removeJoinPeople(joinPeople.uid,joinPeople.username);
                    }
                }
            }
        });





        if (selectedContacts != null && selectedContacts.size() > 0) {
            for (int i = 0; i < selectedContacts.size(); i++) {
                if ((joinPeople.uid).equals(selectedContacts.get(i).uid)) {
                    cb.setChecked(true);
                    break;
                } else {
                    cb.setChecked(false);
                }
            }
        }else{
            cb.setTag(joinPeople.uid);
            if (selectedLists.contains(cb.getTag())) {
                cb.setChecked(true);
            }else{
                cb.setChecked(false);
            }

        }


    }

    InterSelectedJoinPeople interSelectedJoinPeople;
    public void setSelectedJoinPeopleInter(InterSelectedJoinPeople interSelectedJoinPeople){
        this.interSelectedJoinPeople=interSelectedJoinPeople;
    }
    public interface InterSelectedJoinPeople{
        void addJoinPeople(String uId,String userName);
        void removeJoinPeople(String uId,String userName);
    }
}
