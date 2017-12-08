package com.shanlinjinrong.oa.ui.activity.message;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.util.DateUtils;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.message.contract.MessageSearchContract;
import com.shanlinjinrong.oa.ui.activity.message.presenter.MessageSearchPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.views.ClearEditText;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageSearchActivity extends HttpBaseActivity<MessageSearchPresenter> implements MessageSearchContract.View {
    @BindView(R.id.tv_title)
    TextView      mTvTitle;
    @BindView(R.id.lv_list)
    ListView      mLvList;
    @BindView(R.id.search_et_input)
    ClearEditText mSearchEtInput;
    @BindView(R.id.tv_no_result)
    TextView      mTvNoResult;
    private Bundle         mBundle;
    private int            chatType;
    private String         toChatUsername;
    private ProgressDialog pd;
    private EMConversation mConversation;
    private SearchedMessageAdapter messageaAdapter;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void uidNull(int code) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_search);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        mBundle = intent.getParcelableExtra("EXTRAS");
        chatType = intent.getIntExtra("chatType", EaseConstant.CHATTYPE_SINGLE);
        toChatUsername = mBundle.getString("toChatUsername");
        mLvList.setEmptyView(mTvNoResult);
        mTvNoResult.setVisibility(View.GONE);
        mSearchEtInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    searchMessages();
                    InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(mSearchEtInput.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }
                return false;
            }
        });
        mSearchEtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    messageList.clear();
                    messageaAdapter.notifyDataSetChanged();
                    mTvNoResult.setVisibility(View.GONE);
                }
            }
        });
        if (chatType == EaseConstant.CHATTYPE_SINGLE) {
            mConversation = EMClient.getInstance().chatManager().getConversation(toChatUsername, EaseCommonUtils.getConversationType(chatType), true);
        } else {
            String groupId = intent.getStringExtra("groupId");
            mConversation = EMClient.getInstance().chatManager().getConversation(groupId);
        }
    }

    private List<EMMessage> messageList;

    private void searchMessages() {
        pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.searching));
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        new Thread(new Runnable() {
            public void run() {
                List<EMMessage> resultList = mConversation.searchMsgFromDB(mSearchEtInput.getText().toString().trim(), System.currentTimeMillis(), 50, null, EMConversation.EMSearchDirection.UP);
                if (messageList == null) {
                    messageList = resultList;
                } else {
                    messageList.clear();
                    messageList.addAll(resultList);
                }
                onSearchResulted();
            }
        }).start();
    }

    private void onSearchResulted() {
        runOnUiThread(new Runnable() {
            public void run() {
                pd.dismiss();
                if (messageaAdapter == null) {
                    messageaAdapter = new SearchedMessageAdapter(MessageSearchActivity.this, 1, messageList);
                    mLvList.setAdapter(messageaAdapter);
                } else {
                    messageaAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    private class SearchedMessageAdapter extends ArrayAdapter<EMMessage> {

        public SearchedMessageAdapter(Context context, int resource, List<EMMessage> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.em_row_search_message, parent, false);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.message = (TextView) convertView.findViewById(R.id.message);
                holder.time = (TextView) convertView.findViewById(R.id.time);
                holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
                convertView.setTag(holder);
            }

            EMMessage message = getItem(position);
            EaseUserUtils.setUserNick(message.getFrom(), holder.name);
            EaseUserUtils.setUserAvatar(getContext(), message.getFrom(), holder.avatar);
            holder.time.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
            holder.message.setText(((EMTextMessageBody)message.getBody()).getMessage());
            return convertView;
        }
    }


    private static class ViewHolder {
        TextView name;
        TextView message;
        TextView time;
        ImageView avatar;
    }
}
