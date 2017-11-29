package com.shanlinjinrong.oa.ui.activity.message.chatgroup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.views.common.CommonTopView;

import butterknife.BindView;
import butterknife.ButterKnife;

//修改 群组名称
public class ModificationGroupNameActivity extends AppCompatActivity {

    @BindView(R.id.top_view)
    CommonTopView topView;
    @BindView(R.id.ed_modification_group_name)
    EditText edModificationGroupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_group_name);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        topView.setOnClickListener(view -> {
            //TODO 修改群名称
            String group_name = edModificationGroupName.getText().toString();
        });
    }
}
