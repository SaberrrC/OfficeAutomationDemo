package com.shanlinjinrong.oa.ui.activity.message.chatgroup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.views.common.CommonTopView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

//修改 群组名称
public class ModificationGroupNameActivity extends AppCompatActivity {

    @BindView(R.id.top_view)
    CommonTopView topView;
    @BindView(R.id.ed_modification_group_name)
    EditText edModificationGroupName;
    private final int RESULTMODIFICATIONNAME = -3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_group_name);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        topView.getRightView().setOnClickListener(view -> {
            //TODO 修改群名称
            String group_name = edModificationGroupName.getText().toString();
            String groupId = getIntent().getStringExtra("groupId");
            topView.getRightView().setOnClickListener(view1 -> Observable.create(e -> {
                EMClient.getInstance().groupManager().changeGroupName(groupId, group_name);
                e.onComplete();
            }).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(o -> {
                    }, Throwable::printStackTrace, () -> {
                        Intent intent = new Intent();
                        intent.putExtra("groupName", group_name);
                        setResult(RESULTMODIFICATIONNAME, intent);
                        finish();
                    }));
        });
    }
}
