package com.shanlinjinrong.oa.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.shanlinjinrong.oa.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.qqtheme.framework.widget.WheelView;


/**
 * Created by Sxf on 2017/4/24.
 */

public class MonthSelectPopWindow extends PopupWindow {

    private View mMenuView;
    private TextView tv_cancle;
    private TextView tv_confirm;
    private WheelView mWheelView;
    PopListener popListener;
    private Context context;
    private List<DateItem> dataItems = new ArrayList<>();
    private List<String> strings = new ArrayList<>();
    List<String> dataList;
    private String selectedYear="";
    private String selectedMonth="";

    public MonthSelectPopWindow(Activity context, PopListener popListener) {
        super(context);
        this.popListener=popListener;
        this.context=context;
        mMenuView = LayoutInflater.from(context).inflate(R.layout.time_month_select_layout, null);
        initView();
        initData();

    }

    private void initData() {
        dataList = getData();
        mWheelView.setItems(dataList);
        mWheelView.setSelectedItem(dataList.get(0));
        mWheelView.setLineColor(context.getResources().getColor(R.color.pickerview_timebtn_pre));
        mWheelView.setTextColor(context.getResources().getColor(R.color.pickerview_timebtn_nor));
        mWheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                selectedYear=dataItems.get(selectedIndex).year;
                selectedMonth=dataItems.get(selectedIndex).month;
            }
        });

    }

    public void initView() {
        tv_cancle = (TextView) mMenuView.findViewById(R.id.tv_cancle);
        tv_confirm = (TextView) mMenuView.findViewById(R.id.tv_confirm);
        mWheelView=(WheelView) mMenuView.findViewById(R.id.wheelView);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popListener.cancle();
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popListener.confirm(selectedYear,selectedMonth);
            }
        });


        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // 点击其他地方消失
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
//        this.mMenuView.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
//                int y = (int) event.getY();
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (y < height) {
//                        dismiss();
//                    }
//                }
//                return true;
//            }
//        });
        this.mMenuView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    public List<DateItem> getDateItemList() {
        Calendar cal = Calendar.getInstance();
        int curYear = cal.get(Calendar.YEAR);
        int curMonth = cal.get(Calendar.MONTH)+1;
        List<DateItem> list = new ArrayList<>();
        int i=0;
        
        for(i=0;i<12;i++ ){
            String year="";
            String month = "";
            if((curMonth-i)==0){
                month="12";
                year=curYear-1+"";
            }else if((curMonth-i)>0){
                month=curMonth-i+"";
                year=curYear+"";
            }else {
                month=Math.abs(curMonth-i)+"";
                year=curYear-1+"";
            }
            DateItem dateItem = new DateItem(year,month);
            list.add(dateItem);
        }
        return list;
    }

    public List<String> getData() {
        dataItems = getDateItemList();
        List<String> strings = new ArrayList<>();
        for(DateItem dateItem:dataItems){
            strings.add(dateItem.year+"年"+dateItem.month+"月");
        }
        return strings;
    }

    public  interface PopListener{

        void cancle();
        void confirm(String year,String month);
    }

    private class  WheelAdapter  extends android.widget.BaseAdapter{
        @Override
        public int getCount() {
            return strings.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(context,R.layout.wheel_dateselect_list_item,null);
            TextView viewById = (TextView)view.findViewById(R.id.tv_time);
            return view;
        }
    }

    private class DateItem {
        public DateItem(String year, String month){
            this.year=year;
            this.month=month;
        }
        public String month;
        public String year;
    }

}
