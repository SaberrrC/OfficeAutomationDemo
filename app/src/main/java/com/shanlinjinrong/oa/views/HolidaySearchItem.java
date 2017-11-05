package com.shanlinjinrong.oa.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;

/**
 * Created by Administrator on 2017/11/5 0005.
 */

public class HolidaySearchItem extends LinearLayout {

    Context context;
    public HolidaySearchItem(Context context) {
        super(context);
        this.context = context;
        init(context);
    }


    public HolidaySearchItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HolidaySearchItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    TextView textViewKey;
    TextView textValue;
    private void init(Context context) {
        this.setOrientation(LinearLayout.HORIZONTAL);
        View view = View.inflate(context, R.layout.holiday_search_item,null);
         textViewKey = (TextView)view.findViewById(R.id.tv_key);
         textValue = (TextView)view.findViewById(R.id.tv_value);
        this.addView(view);
    }

    public void setTextViewKey(String str){
        textViewKey.setText(str);
    }

    public void setTextViewValue(String str){
        textValue.setText(str);
    }

}
