package com.itcrm.GroupInformationPlatform.ui.adapter;

import android.net.Uri;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.itcrm.GroupInformationPlatform.R;
import com.itcrm.GroupInformationPlatform.model.Contacts;
import com.itcrm.GroupInformationPlatform.model.GlightContact;
import com.itcrm.GroupInformationPlatform.ui.activity.WorkReportLaunchActivity;
import com.itcrm.GroupInformationPlatform.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ProjectName: GroupInformationPlatform
 * PackageName: com.itcrm.GroupInformationPlatform.ui.adapter
 * Author:Created by CXP on Date: 2016/9/6 14:50.
 * Description:选择联系人适配器
 */
public class SelectContactsAdapter extends BaseMultiItemQuickAdapter<GlightContact> {
    ArrayList<GlightContact> selectedContacts;
    List<String> selectedLists;

    public SelectContactsAdapter(List<GlightContact> data,ArrayList<GlightContact> selectedContacts) {
        super(data);
        selectedLists = new ArrayList<>();
        this.selectedContacts = selectedContacts;
        addItemType(Contacts.EMPLOYEE, R.layout.contacts_item);
    }

    @Override
    protected void convert(BaseViewHolder holder, final GlightContact glightContact) {
        holder.setText(R.id.user_name, glightContact.username);
        holder.setText(R.id.user_post, glightContact.get_title);
        View view = holder.getConvertView();
        SimpleDraweeView iv = holder.getView(R.id.portrait);
        iv.setImageURI(Uri.parse(glightContact.portraits));
        final CheckBox cb = (CheckBox) view.findViewById(R.id.select_contact_icon);

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!selectedLists.contains(glightContact.uid)) {
                        selectedLists.add(glightContact.uid);
                        LogUtils.e(glightContact.username+"被选中了"+"uid->"+glightContact.uid);
                        LogUtils.e("当前被选中的有-》"+selectedLists.toString());
                        interSelectedPeople.addPeople(glightContact.uid,glightContact.username);
                    }
                }else{
                    if (selectedLists.contains(glightContact.uid)) {
                        selectedLists.remove(glightContact.uid);
                        LogUtils.e(glightContact.username+"被移除了"+"uid->"+glightContact.uid);
                        LogUtils.e("当前被选中的有-》"+selectedLists.toString());
                        interSelectedPeople.removePeople(glightContact.uid,glightContact.username);
                    }
                }
            }
        });



        if (selectedContacts != null && selectedContacts.size() > 0) {
            for (int i = 0; i < selectedContacts.size(); i++) {
                if (glightContact.uid.equals(selectedContacts.get(i).uid)){
                    cb.setChecked(true);
                    break;
                }else{
                    cb.setChecked(false);
                }
            }
        }

    }
    InterSelectedPeople interSelectedPeople;
    public void setSelectedPeopleInter(InterSelectedPeople interSelectedPeople){
        this.interSelectedPeople=interSelectedPeople;
    }
    public interface InterSelectedPeople{
        void addPeople(String uId,String userName);
        void removePeople(String uId,String userName);
    }
}
