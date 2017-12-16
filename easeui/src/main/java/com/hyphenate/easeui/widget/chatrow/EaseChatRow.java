package com.hyphenate.easeui.widget.chatrow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.Direct;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.adapter.EaseMessageAdapter;
import com.hyphenate.easeui.db.FriendsInfoCacheSvc;
import com.hyphenate.easeui.domain.EaseAvatarOptions;
import com.hyphenate.easeui.model.styles.EaseMessageListItemStyle;
import com.hyphenate.easeui.utils.DataFormatUtils;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.widget.EaseChatMessageList;
import com.hyphenate.easeui.widget.EaseChatMessageList.MessageListItemClickListener;
import com.hyphenate.easeui.widget.EaseImageView;
import com.hyphenate.util.DateUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.Date;

public abstract class EaseChatRow extends LinearLayout {
    protected static final String TAG = EaseChatRow.class.getSimpleName();

    protected LayoutInflater inflater;
    protected Context context;
    protected BaseAdapter adapter;
    protected EMMessage message;
    protected int position;

    protected TextView timeStampView;
    protected ImageView userAvatarView;
    protected View bubbleLayout;
    protected TextView usernickView;

    protected TextView percentageView;
    protected ProgressBar progressBar;
    protected ImageView statusView;
    protected Activity activity;

    protected TextView ackedView;
    protected TextView deliveredView;

    protected EMCallBack messageSendCallback;
    protected EMCallBack messageReceiveCallback;

    protected MessageListItemClickListener itemClickListener;
    protected EaseMessageListItemStyle itemStyle;
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.drawable.ease_default_avatar)
            .showImageOnFail(R.drawable.ease_default_avatar)
            .resetViewBeforeLoading(true)
            .cacheOnDisk(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .considerExifParams(true)
            .displayer(new FadeInBitmapDisplayer(300))
            .build();

    public EaseChatRow(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context);
        this.context = context;
        this.activity = (Activity) context;
        this.message = message;
        this.position = position;
        this.adapter = adapter;
        inflater = LayoutInflater.from(context);
        initView();
    }

    private void initView() {


        onInflateView();
        timeStampView = (TextView) findViewById(R.id.timestamp);
        userAvatarView = (ImageView) findViewById(R.id.iv_userhead);
        bubbleLayout = findViewById(R.id.bubble);
        usernickView = (TextView) findViewById(R.id.tv_userid);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        statusView = (ImageView) findViewById(R.id.msg_status);
        ackedView = (TextView) findViewById(R.id.tv_ack);
        deliveredView = (TextView) findViewById(R.id.tv_delivered);
        ackedView.setVisibility(GONE);
        deliveredView.setVisibility(GONE);
        onFindViewById();
    }

    /**
     * set property according message and postion
     *
     * @param message
     * @param position
     */
    public void setUpView(EMMessage message, int position, EaseChatMessageList.MessageListItemClickListener itemClickListener, EaseMessageListItemStyle itemStyle) {
        this.message = message;
        this.position = position;
        this.itemClickListener = itemClickListener;
        this.itemStyle = itemStyle;
        boolean showUserNick = itemStyle.isShowUserNick();
        setUpBaseView();
        onSetUpView();
        setClickListener();
    }

    EMMessage mPrevMessage;

    private void setUpBaseView() {
        // set nickname, avatar and background of bubble
        if (timeStampView != null) {
            if (position == 0) {
                message.getMsgTime();
                timeStampView.setText(DataFormatUtils.getTimestampString(new Date(message.getMsgTime())));
                timeStampView.setVisibility(View.VISIBLE);
                try {
                    mPrevMessage = (EMMessage) adapter.getItem(position);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            } else {
                mPrevMessage = (EMMessage) adapter.getItem(position);
                // show time stamp if interval with last message is > 30 seconds
                EMMessage prevMessage = (EMMessage) adapter.getItem(position - 1);
                if (prevMessage != null && DateUtils.isCloseEnough(message.getMsgTime(), prevMessage.getMsgTime())) {
                    timeStampView.setVisibility(View.GONE);
                } else {
                    timeStampView.setText(DataFormatUtils.getTimestampString(new Date(message.getMsgTime())));
                    timeStampView.setVisibility(View.VISIBLE);
                }
            }
        }
        if (userAvatarView != null) {
            try {
                if (message.getFrom().contains("admin") || message.getTo().contains("admin")) {
                    userAvatarView.setImageResource(R.drawable.meeting_invite_icon);
                } else if (message.getFrom().contains("notice") || message.getTo().contains("notice")) {
                    userAvatarView.setImageResource(R.drawable.notice_message_icon);
                }

                if (message.direct() == Direct.SEND) {
                    if (message.getUserName().equals(FriendsInfoCacheSvc.getInstance(context).getUserId(message.getTo()))) {
                        //     Glide.with(context).load(FriendsInfoCacheSvc.getInstance(context).getPortrait(message.getFrom())).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.ease_default_avatar).placeholder(R.drawable.ease_default_avatar).into(userAvatarView);
                        ImageLoader.getInstance().displayImage(FriendsInfoCacheSvc.getInstance(context).getPortrait(message.getFrom()), userAvatarView, options);
                    } else if ((message.getUserName().contains("admin") || message.getUserName().contains("notice"))) {
                        if (!message.getUserName().equals(message.getFrom())) {
                            //         Glide.with(context).load(FriendsInfoCacheSvc.getInstance(context).getPortrait(message.getFrom())).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.ease_default_avatar).placeholder(R.drawable.ease_default_avatar).into(userAvatarView);
                            ImageLoader.getInstance().displayImage(FriendsInfoCacheSvc.getInstance(context).getPortrait(message.getFrom()), userAvatarView, options);

                        } else {
                            //  Glide.with(context).load(FriendsInfoCacheSvc.getInstance(context).getPortrait(message.getTo())).error(R.drawable.ease_default_avatar).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ease_default_avatar).into(userAvatarView);
                            ImageLoader.getInstance().displayImage(FriendsInfoCacheSvc.getInstance(context).getPortrait(message.getTo()), userAvatarView, options);

                        }
                    } else {
                        //      Glide.with(context).load(FriendsInfoCacheSvc.getInstance(context).getPortrait(message.getTo())).error(R.drawable.ease_default_avatar).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ease_default_avatar).into(userAvatarView);
                        ImageLoader.getInstance().displayImage(FriendsInfoCacheSvc.getInstance(context).getPortrait(message.getTo()), userAvatarView, options);

                    }
                } else if (message.direct() == Direct.RECEIVE && !FriendsInfoCacheSvc.getInstance(context).getPortrait(message.getUserName()).equals("")) {
                    //    Glide.with(context).load(FriendsInfoCacheSvc.getInstance(context).getPortrait(message.getUserName())).error(R.drawable.ease_default_avatar).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ease_default_avatar).into(userAvatarView);
                    ImageLoader.getInstance().displayImage(FriendsInfoCacheSvc.getInstance(context).getPortrait(message.getUserName()), userAvatarView, options);

                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        if (deliveredView != null) {
            if (message.isDelivered()) {
                deliveredView.setVisibility(View.VISIBLE);
            } else {
                deliveredView.setVisibility(View.INVISIBLE);
            }
        }

        if (ackedView != null) {
            //隐藏已读状态
            ackedView.setVisibility(View.INVISIBLE);
            //            if (message.isAcked()) {
            //                if (deliveredView != null) {
            //                    deliveredView.setVisibility(View.INVISIBLE);
            //                }
            //                ackedView.setVisibility(View.VISIBLE);
            //            } else {
            //                ackedView.setVisibility(View.INVISIBLE);
            //            }
        }
        if (usernickView != null) {
            usernickView.setVisibility(View.GONE);
        }
        if (usernickView != null && userAvatarView != null) {
            if (itemStyle.isShowUserNick()) {
                usernickView.setVisibility(View.VISIBLE);
            } else {
                usernickView.setVisibility(View.GONE);
            }
            if (message.direct() == Direct.RECEIVE) {
                String nickName = FriendsInfoCacheSvc.getInstance(context).getNickName(message.getFrom());
                EaseUserUtils.setUserNick(nickName, usernickView);
            }
        }
        if (itemStyle != null) {
            if (userAvatarView != null) {
                if (itemStyle.isShowAvatar()) {
                    userAvatarView.setVisibility(View.VISIBLE);
                    EaseAvatarOptions avatarOptions = EaseUI.getInstance().getAvatarOptions();
                    if (avatarOptions != null && userAvatarView instanceof EaseImageView) {
                        EaseImageView avatarView = ((EaseImageView) userAvatarView);
                        if (avatarOptions.getAvatarShape() != 0)
                            avatarView.setShapeType(avatarOptions.getAvatarShape());
                        if (avatarOptions.getAvatarBorderWidth() != 0)
                            avatarView.setBorderWidth(avatarOptions.getAvatarBorderWidth());
                        if (avatarOptions.getAvatarBorderColor() != 0)
                            avatarView.setBorderColor(avatarOptions.getAvatarBorderColor());
                        if (avatarOptions.getAvatarRadius() != 0)
                            avatarView.setRadius(avatarOptions.getAvatarRadius());
                    }
                } else {
                    userAvatarView.setVisibility(View.GONE);
                }
            }
            if (bubbleLayout != null) {
                if (message.direct() == Direct.SEND) {
                    if (itemStyle.getMyBubbleBg() != null) {
                        bubbleLayout.setBackgroundDrawable(((EaseMessageAdapter) adapter).getMyBubbleBg());
                    }
                } else if (message.direct() == Direct.RECEIVE) {
                    if (itemStyle.getOtherBubbleBg() != null) {
                        bubbleLayout.setBackgroundDrawable(((EaseMessageAdapter) adapter).getOtherBubbleBg());
                    }
                }
            }
        }

    }

    /**
     * set callback for sending message
     */
    protected void setMessageSendCallback() {
        if (messageSendCallback == null) {
            messageSendCallback = new EMCallBack() {

                @Override
                public void onSuccess() {
                    updateView();
                }

                @Override
                public void onProgress(final int progress, String status) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (percentageView != null)
                                percentageView.setText(progress + "%");

                        }
                    });
                }

                @Override
                public void onError(int code, String error) {
                    updateView(code, error);
                }
            };
        }
        message.setMessageStatusCallback(messageSendCallback);
    }

    /**
     * set callback for receiving message
     */
    protected void setMessageReceiveCallback() {
        if (messageReceiveCallback == null) {
            messageReceiveCallback = new EMCallBack() {

                @Override
                public void onSuccess() {
                    updateView();
                }

                @Override
                public void onProgress(final int progress, String status) {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            if (percentageView != null) {
                                percentageView.setText(progress + "%");
                            }
                        }
                    });
                }

                @Override
                public void onError(int code, String error) {
                    updateView();
                }
            };
        }
        message.setMessageStatusCallback(messageReceiveCallback);
    }


    private void setClickListener() {
        if (bubbleLayout != null) {
            bubbleLayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        if (!itemClickListener.onBubbleClick(message)) {
                            // if listener return false, we call default handling
                            onBubbleClick();
                        }
                    }
                }
            });

            bubbleLayout.setOnLongClickListener(new OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onBubbleLongClick(message);
                    }
                    return true;
                }
            });
        }

        if (statusView != null) {
            statusView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onResendClick(message);
                    }
                }
            });
        }

        if (userAvatarView != null) {
            userAvatarView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        if (message.direct() == Direct.SEND) {
                            itemClickListener.onUserAvatarClick(EMClient.getInstance().getCurrentUser());
                        } else {
                            itemClickListener.onUserAvatarClick(message.getFrom());
                        }
                    }
                }
            });
            userAvatarView.setOnLongClickListener(new OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    if (itemClickListener != null) {
                        if (message.direct() == Direct.SEND) {
                            itemClickListener.onUserAvatarLongClick(EMClient.getInstance().getCurrentUser());
                        } else {
                            itemClickListener.onUserAvatarLongClick(message.getFrom());
                        }
                        return true;
                    }
                    return false;
                }
            });
        }
    }


    protected void updateView() {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (message.status() == EMMessage.Status.FAIL) {
                    Toast.makeText(activity, activity.getString(R.string.send_fail) + activity.getString(R.string.connect_failuer_toast), Toast.LENGTH_SHORT).show();
                }

                onUpdateView();
            }
        });
    }

    protected void updateView(final int errorCode, final String desc) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (errorCode == EMError.MESSAGE_INCLUDE_ILLEGAL_CONTENT) {
                    Toast.makeText(activity, activity.getString(R.string.send_fail) + activity.getString(R.string.error_send_invalid_content), Toast.LENGTH_SHORT).show();
                } else if (errorCode == EMError.GROUP_NOT_JOINED) {
                    Toast.makeText(activity, activity.getString(R.string.send_fail) + activity.getString(R.string.error_send_not_in_the_group), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, activity.getString(R.string.send_fail) + activity.getString(R.string.connect_failuer_toast), Toast.LENGTH_SHORT).show();
                }
                onUpdateView();
            }
        });
    }

    protected abstract void onInflateView();

    /**
     * find view by id
     */
    protected abstract void onFindViewById();

    /**
     * refresh list view when message status change
     */
    protected abstract void onUpdateView();

    /**
     * setup view
     */
    protected abstract void onSetUpView();

    /**
     * on bubble clicked
     */
    protected abstract void onBubbleClick();

}
