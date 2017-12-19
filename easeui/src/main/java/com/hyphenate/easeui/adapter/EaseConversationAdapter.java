package com.hyphenate.easeui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.gson.Gson;
import com.huawei.hms.api.Api;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.UserDetailsBean;
import com.hyphenate.easeui.db.FriendsInfoCacheSvc;
import com.hyphenate.easeui.domain.EaseAvatarOptions;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.utils.DataFormatUtils;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.EaseSmileUtils;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.utils.EncryptionUtil;
import com.hyphenate.easeui.utils.GlideRoundTransformUtils;
import com.hyphenate.easeui.widget.EaseConversationList.EaseConversationListHelper;
import com.hyphenate.easeui.widget.EaseImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * conversation list adapter
 */

//TODO 最外边列表展示 Adapter
public class EaseConversationAdapter extends ArrayAdapter<EMConversation> {
    private static final String TAG = "ChatAllHistoryAdapter";
    private List<EMConversation> conversationList;
    private List<EMConversation> copyConversationList;
    private ConversationFilter   conversationFilter;
    private boolean              notiyfyByFilter;

    protected int     primaryColor;
    protected int     secondaryColor;
    protected int     timeColor;
    protected int     primarySize;
    protected int     secondarySize;
    protected float   timeSize;
    protected Context mContext;
    EMMessage lastMessage;
    private String conversationId;

    public EaseConversationAdapter(Context context, int resource, List<EMConversation> objects) {
        super(context, resource, objects);
        mContext = context;
        conversationList = objects;
        copyConversationList = new ArrayList<EMConversation>();
        copyConversationList.addAll(objects);
    }

    @Override
    public int getCount() {
        return conversationList.size();
    }

    @Override
    public EMConversation getItem(int arg0) {
        if (arg0 < conversationList.size()) {
            return conversationList.get(arg0);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ease_row_chat_history, parent, false);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.ll_conversation = (LinearLayout) convertView.findViewById(R.id.ll_conversation_layout);
            holder.rv_conversation = (RelativeLayout) convertView.findViewById(R.id.rv_conversation_layout);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.unreadLabel = (TextView) convertView.findViewById(R.id.unread_msg_number);
            holder.message = (TextView) convertView.findViewById(R.id.message);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.avatar = (EaseImageView) convertView.findViewById(R.id.avatar);
            holder.msgState = convertView.findViewById(R.id.msg_state);
            holder.motioned = (TextView) convertView.findViewById(R.id.mentioned);

            convertView.setTag(holder);
        }

        //会话 点击颜色
        holder.rv_conversation.setBackgroundResource(R.drawable.ease_mm_listitem);

        // get conversation
        EMConversation conversation = getItem(position);
        // get username or group id
        String username = conversation.conversationId();


        EaseAvatarOptions avatarOptions = EaseUI.getInstance().getAvatarOptions();
        if (avatarOptions != null && holder.avatar instanceof EaseImageView) {
            EaseImageView avatarView = ((EaseImageView) holder.avatar);
            if (avatarOptions.getAvatarShape() != 0)
                avatarView.setShapeType(avatarOptions.getAvatarShape());
            if (avatarOptions.getAvatarBorderWidth() != 0)
                avatarView.setBorderWidth(avatarOptions.getAvatarBorderWidth());
            if (avatarOptions.getAvatarBorderColor() != 0)
                avatarView.setBorderColor(avatarOptions.getAvatarBorderColor());
            if (avatarOptions.getAvatarRadius() != 0)
                avatarView.setRadius(avatarOptions.getAvatarRadius());
        }
        if (conversation.getUnreadMsgCount() > 0) {
            // show unread message count
            holder.unreadLabel.setText(String.valueOf(conversation.getUnreadMsgCount()));
            holder.unreadLabel.setVisibility(View.VISIBLE);
        } else {
            holder.unreadLabel.setVisibility(View.INVISIBLE);
        }
        if (conversation.getAllMsgCount() != 0) {
            // show the content of latest message
            lastMessage = conversation.getLastMessage();

            String content = null;
            if (cvsListHelper != null) {
                content = cvsListHelper.onSetItemSecondaryText(lastMessage);
            }

            if (lastMessage.getType() == EMMessage.Type.TXT) {
                Spannable jiamiStr = EaseSmileUtils.getSmiledText(getContext(), EaseSmileUtils.getSmiledText(getContext(), EaseCommonUtils.getMessageDigest(lastMessage, (this.getContext()))));
                holder.message.setText(jiamiStr, BufferType.SPANNABLE);
            } else {
                Spannable jiamiStr = EaseSmileUtils.getSmiledText(getContext(), EaseCommonUtils.getMessageDigest(lastMessage, (this.getContext())));

                holder.message.setText(jiamiStr, BufferType.SPANNABLE);
            }
            if (content != null) {
                if (lastMessage.getType() == EMMessage.Type.TXT) {
                    holder.message.setText(EncryptionUtil.getDecryptStr(content, lastMessage.getFrom()));
                } else {
                    holder.message.setText(content);
                }
            }
            holder.time.setText(DataFormatUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
            if (lastMessage.direct() == EMMessage.Direct.SEND && lastMessage.status() == EMMessage.Status.FAIL) {
                holder.msgState.setVisibility(View.VISIBLE);
            } else {
                holder.msgState.setVisibility(View.GONE);
            }
        }
        try {
            if (lastMessage.conversationId().length() > 11) {
                conversationId = lastMessage.conversationId().substring(0, 12);
            } else {
                conversationId = lastMessage.conversationId();
            }

            if (conversation.getType() == EMConversation.EMConversationType.GroupChat) {
                String groupId = conversation.conversationId();
                if (EaseAtMessageHelper.get().hasAtMeMsg(groupId)) {
                    holder.motioned.setVisibility(View.VISIBLE);
                } else {
                    holder.motioned.setVisibility(View.GONE);
                }
                // group message, show group avatar
                holder.avatar.setImageResource(R.drawable.icon_default_group_portraits);
//                EMGroup group = EMClient.getInstance().groupManager().getGroup(username);
//                holder.name.setText(group != null ? group.getGroupName() : username);
                //获取群组名称
                String groupName = FriendsInfoCacheSvc.getInstance(mContext).getNickName(username);
                holder.name.setText(groupName);
            } else if (conversation.getType() == EMConversation.EMConversationType.ChatRoom) {
                holder.avatar.setImageResource(R.drawable.icon_default_group_portraits);
                EMChatRoom room = EMClient.getInstance().chatroomManager().getChatRoom(username);
                holder.name.setText(room != null && !TextUtils.isEmpty(room.getName()) ? room.getName() : username);
                holder.motioned.setVisibility(View.GONE);
            }

//            else if (!FriendsInfoCacheSvc.getInstance(mContext).getUserId(lastMessage.getFrom()).equals("") && username.equals(FriendsInfoCacheSvc.getInstance(mContext).getUserId(lastMessage.getFrom()))) {
//                ImageLoader.getInstance().displayImage(FriendsInfoCacheSvc.getInstance(mContext).getPortrait(lastMessage.getFrom()),
//                        holder.avatar, new DisplayImageOptions.Builder()
//                                .showImageForEmptyUri(R.drawable.ease_user_portraits)
//                                .showImageOnFail(R.drawable.ease_user_portraits)
//                                .resetViewBeforeLoading(true)
//                                .cacheOnDisk(true)
//                                .imageScaleType(ImageScaleType.EXACTLY)
//                                .bitmapConfig(Bitmap.Config.RGB_565)
//                                .considerExifParams(true)
//                                .displayer(new FadeInBitmapDisplayer(300))
//                                .build());
//
//                holder.name.setText(FriendsInfoCacheSvc.getInstance(mContext).getNickName(lastMessage.getFrom()));
//
//                holder.motioned.setVisibility(View.GONE);
//            } else if (!FriendsInfoCacheSvc.getInstance(mContext).getUserId(lastMessage.getTo()).equals("") && username.equals(FriendsInfoCacheSvc.getInstance(mContext).getUserId(lastMessage.getTo()))) {
//                try {
//                    ImageLoader.getInstance().displayImage(FriendsInfoCacheSvc.getInstance(mContext).getPortrait(lastMessage.getTo()),
//                            holder.avatar, new DisplayImageOptions.Builder()
//                                    .showImageForEmptyUri(R.drawable.ease_user_portraits)
//                                    .showImageOnFail(R.drawable.ease_user_portraits)
//                                    .resetViewBeforeLoading(true)
//                                    .cacheOnDisk(true)
//                                    .imageScaleType(ImageScaleType.EXACTLY)
//                                    .bitmapConfig(Bitmap.Config.RGB_565)
//                                    .considerExifParams(true)
//                                    .displayer(new FadeInBitmapDisplayer(0))
//                                    .build());
//                    String nickName = FriendsInfoCacheSvc.getInstance(mContext).getNickName(lastMessage.getTo());
//                    holder.name.setText(FriendsInfoCacheSvc.getInstance(mContext).getNickName(lastMessage.getTo()));
//                    holder.motioned.setVisibility(View.GONE);
//                } catch (Throwable throwable) {
//                    throwable.printStackTrace();
//                }
//            }
            else if (lastMessage.conversationId().contains("admin")) {
                holder.name.setText("会议邀请");
                holder.avatar.setImageResource(R.drawable.meeting_invite_icon);
            } else if (lastMessage.conversationId().contains("notice")) {
                holder.name.setText("公告通知");
                holder.avatar.setImageResource(R.drawable.notice_message_icon);
            } else if (!FriendsInfoCacheSvc.getInstance(mContext).getNickName(conversationId).equals("")) {
                try {
                    Glide.with(mContext)
                            .load(FriendsInfoCacheSvc.getInstance(mContext).getPortrait(conversationId))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.ease_user_portraits)
                            .error(R.drawable.ease_user_portraits)
                            .transform(new CenterCrop(mContext), new GlideRoundTransformUtils(mContext, 5))
                            .into(holder.avatar);

                    holder.name.setText(FriendsInfoCacheSvc.getInstance(mContext).getNickName(conversationId));
                    holder.motioned.setVisibility(View.GONE);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            } else if (FriendsInfoCacheSvc.getInstance(mContext).getNickName(conversationId).equals("")) {
                holder.rv_conversation.setVisibility(View.GONE);
            } else {
                holder.rv_conversation.setVisibility(View.VISIBLE);
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }

        //set property
        holder.name.setTextColor(primaryColor);
        holder.message.setTextColor(secondaryColor);
        holder.time.setTextColor(timeColor);
        if (primarySize != 0)
            holder.name.setTextSize(TypedValue.COMPLEX_UNIT_PX, primarySize);
        if (secondarySize != 0)
            holder.message.setTextSize(TypedValue.COMPLEX_UNIT_PX, secondarySize);
        if (timeSize != 0)
            holder.time.setTextSize(TypedValue.COMPLEX_UNIT_PX, timeSize);

        return convertView;
    }

    static String mName;
    static String mPic;

    public static void requestNamePic(String name, String pic) {
        mName = name;
        mPic = pic;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (!notiyfyByFilter) {
            copyConversationList.clear();
            copyConversationList.addAll(conversationList);
            notiyfyByFilter = false;
        }
    }

    @Override
    public Filter getFilter() {
        if (conversationFilter == null) {
            conversationFilter = new ConversationFilter(conversationList);
        }
        return conversationFilter;
    }


    public void setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
    }

    public void setSecondaryColor(int secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public void setTimeColor(int timeColor) {
        this.timeColor = timeColor;
    }

    public void setPrimarySize(int primarySize) {
        this.primarySize = primarySize;
    }

    public void setSecondarySize(int secondarySize) {
        this.secondarySize = secondarySize;
    }

    public void setTimeSize(float timeSize) {
        this.timeSize = timeSize;
    }

    private class ConversationFilter extends Filter {
        List<EMConversation> mOriginalValues = null;

        public ConversationFilter(List<EMConversation> mList) {
            mOriginalValues = mList;
        }

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                mOriginalValues = new ArrayList<EMConversation>();
            }
            if (prefix == null || prefix.length() == 0) {
                results.values = copyConversationList;
                results.count = copyConversationList.size();
            } else {
                if (copyConversationList.size() > mOriginalValues.size()) {
                    mOriginalValues = copyConversationList;
                }
                String prefixString = prefix.toString();
                final int count = mOriginalValues.size();
                final ArrayList<EMConversation> newValues = new ArrayList<EMConversation>();

                for (int i = 0; i < count; i++) {
                    final EMConversation value = mOriginalValues.get(i);
                    String username = value.conversationId();

                    EMGroup group = EMClient.getInstance().groupManager().getGroup(username);
                    if (group != null) {
                        username = group.getGroupName();
                    } else {
                        EaseUser user = EaseUserUtils.getUserInfo(username);
                        // TODO: not support Nick anymore
//                        if(user != null && user.getNick() != null)
//                            username = user.getNick();
                    }

                    // First match against the whole ,non-splitted value
                    if (username.startsWith(prefixString)) {
                        newValues.add(value);
                    } else {
                        final String[] words = username.split(" ");
                        final int wordCount = words.length;

                        // Start at index 0, in case valueText starts with space(s)
                        for (String word : words) {
                            if (word.startsWith(prefixString)) {
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            conversationList.clear();
            if (results.values != null) {
                conversationList.addAll((List<EMConversation>) results.values);
            }
            if (results.count > 0) {
                notiyfyByFilter = true;
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

    private EaseConversationListHelper cvsListHelper;

    public void setCvsListHelper(EaseConversationListHelper cvsListHelper) {
        this.cvsListHelper = cvsListHelper;
    }

    private static class ViewHolder {

        LinearLayout ll_conversation;

        RelativeLayout rv_conversation;

        TextView       name;

        TextView       unreadLabel;

        TextView       message;

        TextView       time;

        ImageView      avatar;

        View           msgState;

        TextView       motioned;
    }
}

