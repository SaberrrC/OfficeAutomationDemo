package com.itcrm.GroupInformationPlatform.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.flipboard.bottomsheet.commons.BottomSheetFragment;
import com.itcrm.GroupInformationPlatform.R;
import com.itcrm.GroupInformationPlatform.manager.AppManager;
import com.itcrm.GroupInformationPlatform.model.selectContacts.Child;
import com.itcrm.GroupInformationPlatform.ui.activity.SelectJoinPeopleActivity;
import com.itcrm.GroupInformationPlatform.ui.adapter.SelectAdapter;

import java.util.ArrayList;

public class MyJoinPeopleFragment extends BottomSheetFragment{

    private ArrayList<Child> l_items;

    private RecyclerView recyclerView;
    private SelectAdapter selectAdapter;
    private LinearLayout lin_bottom;

    private AppManager appManager = null;
    private SelectJoinPeopleActivity.MyJoinHandler mJoinHandler = null;

    @SuppressLint("ValidFragment")
    public MyJoinPeopleFragment(ArrayList<Child> items) {
        l_items = items;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        lin_bottom= (LinearLayout) view.findViewById(R.id.lin_bottom);
        recyclerView = (RecyclerView) view.findViewById(R.id.list_bottomsheet);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        selectAdapter = new SelectAdapter(getActivity(),l_items,onDeleteClickListener);
        recyclerView.setAdapter(selectAdapter);

        appManager = (AppManager)getActivity().getApplication();
        // 获得该共享变量实例
        mJoinHandler = appManager.getJoinhandler();

        return view;
    }

   SelectAdapter.OnDeleteClickListener onDeleteClickListener=new SelectAdapter.OnDeleteClickListener() {
       @Override
       public void onDeleteClick(View view, int position) {
           l_items.remove(position);
           mJoinHandler.sendEmptyMessage(l_items.size());
           selectAdapter = new SelectAdapter(getActivity(),l_items,onDeleteClickListener);
           recyclerView.setAdapter(selectAdapter);
       }
   };
}
