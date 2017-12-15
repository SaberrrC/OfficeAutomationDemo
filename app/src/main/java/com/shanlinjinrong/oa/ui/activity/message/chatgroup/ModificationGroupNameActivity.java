package com.shanlinjinrong.oa.ui.activity.message.chatgroup;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.ui.base.BaseActivity;
import com.shanlinjinrong.views.common.CommonTopView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

//修改 群组名称
public class ModificationGroupNameActivity extends BaseActivity implements TextWatcher {

    @BindView(R.id.top_view)
    CommonTopView mTopView;
    @BindView(R.id.ed_modification_group_name)
    EditText mEdModificationGroupName;
    private final int RESULTMODIFICATIONNAME = -3;
    private String mGroupName;
    private TextView mRightView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_group_name);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        mGroupName = getIntent().getStringExtra(Constants.GroupName);
    }

    private void initView() {
        initEdit();
        initTopView();
    }

    private void initTopView() {
        mRightView = (TextView) mTopView.getRightView();
        mRightView.setEnabled(false);
        mRightView.setTextColor(getResources().getColor(R.color.voip_interface_text_color));
        mTopView.getRightView().setOnClickListener(view -> {
            //TODO 修改群名称
            String group_name = mEdModificationGroupName.getText().toString();
            String groupId = getIntent().getStringExtra(EaseConstant.GROUPID);

            if (group_name.length() == 0) {
                showToast("请输入群组名称！");
                return;
            }
            
            showLoadingView();
            Observable.create(e -> {
                EMClient.getInstance().groupManager().changeGroupName(groupId, group_name);
                e.onComplete();
            }).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(o -> {
                    }, throwable -> {
                        throwable.printStackTrace();
                        hideLoadingView();
                    }, () -> {
                        hideLoadingView();
                        Intent intent = new Intent();
                        intent.putExtra("groupName", group_name);
                        setResult(RESULTMODIFICATIONNAME, intent);
                        finish();
                    });
        });
    }

    private void initEdit() {
        try {
            mEdModificationGroupName.setText(mGroupName);
            mEdModificationGroupName.setSelection(mGroupName.length());
            mEdModificationGroupName.addTextChangedListener(this);
            mEdModificationGroupName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        try {
            if (mGroupName.equals(charSequence.toString())) {
                mRightView.setEnabled(false);
                mRightView.setTextColor(getResources().getColor(R.color.voip_interface_text_color));
            } else {
                mRightView.setEnabled(true);
                mRightView.setTextColor(getResources().getColor(R.color.black));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }
}
