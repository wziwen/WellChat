package com.ziwenwen.wellchat.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.EaseSmileUtils;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.util.DateUtils;
import com.ziwenwen.wellchat.ChatActivity;
import com.ziwenwen.wellchat.R;

import java.util.Date;

import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by ziwen.wen on 2017/3/14.
 */
public class ConversationProvider extends ItemViewProvider<EMConversation, ConversationProvider.ConversationHolder> implements View.OnClickListener {

    @NonNull
    @Override
    protected ConversationHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.ease_row_chat_history, parent, false);
        root.setOnClickListener(this);
        return new ConversationHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ConversationHolder holder, @NonNull EMConversation conversation) {
        holder.list_itease_layout.setBackgroundResource(com.hyphenate.easeui.R.drawable.ease_mm_listitem);

        holder.itemView.setTag(conversation);

        Context context = holder.avatar.getContext();

        // get username or group id
        String username;
        String id = conversation.conversationId();
        if (id.contains("chat_")) {
            username = id.substring(id.indexOf("chat_") + 5);
        } else {
            username = id;
        }

        if (conversation.getType() == EMConversation.EMConversationType.GroupChat) {
            String groupId = conversation.conversationId();
            if(EaseAtMessageHelper.get().hasAtMeMsg(groupId)){
                holder.motioned.setVisibility(View.VISIBLE);
            }else{
                holder.motioned.setVisibility(View.GONE);
            }
            // group message, show group avatar
            holder.avatar.setImageResource(com.hyphenate.easeui.R.drawable.ease_group_icon);
            EMGroup group = EMClient.getInstance().groupManager().getGroup(username);
            holder.name.setText(group != null ? group.getGroupName() : username);
        } else if(conversation.getType() == EMConversation.EMConversationType.ChatRoom){
            holder.avatar.setImageResource(com.hyphenate.easeui.R.drawable.ease_group_icon);
            EMChatRoom room = EMClient.getInstance().chatroomManager().getChatRoom(username);
            holder.name.setText(room != null && !TextUtils.isEmpty(room.getName()) ? room.getName() : username);
            holder.motioned.setVisibility(View.GONE);
        }else {
            EaseUserUtils.setUserAvatar(context, username, holder.avatar);
            EaseUserUtils.setUserNick(username, holder.name);
            holder.motioned.setVisibility(View.GONE);
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
            EMMessage lastMessage = conversation.getLastMessage();
            String content = null;
            holder.message.setText(EaseSmileUtils.getSmiledText(context, EaseCommonUtils.getMessageDigest(lastMessage, context)),
                    TextView.BufferType.SPANNABLE);
            if(content != null){
                holder.message.setText(content);
            }
            holder.time.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
            if (lastMessage.direct() == EMMessage.Direct.SEND && lastMessage.status() == EMMessage.Status.FAIL) {
                holder.msgState.setVisibility(View.VISIBLE);
            } else {
                holder.msgState.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        EMConversation conversation = (EMConversation) v.getTag();
        Context context = v.getContext();
        context.startActivity(new Intent(context, ChatActivity.class).putExtra("userId", conversation.conversationId()));
    }

    static class ConversationHolder extends RecyclerView.ViewHolder {

        /** who you chat with */
        TextView name;
        /** unread message count */
        TextView unreadLabel;
        /** content of last message */
        TextView message;
        /** time of last message */
        TextView time;
        /** avatar */
        ImageView avatar;
        /** status of last message */
        View msgState;
        /** layout */
        RelativeLayout list_itease_layout;
        TextView motioned;

        ConversationHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(com.hyphenate.easeui.R.id.name);
            unreadLabel = (TextView) itemView.findViewById(com.hyphenate.easeui.R.id.unread_msg_number);
            message = (TextView) itemView.findViewById(com.hyphenate.easeui.R.id.message);
            time = (TextView) itemView.findViewById(com.hyphenate.easeui.R.id.time);
            avatar = (ImageView) itemView.findViewById(com.hyphenate.easeui.R.id.avatar);
            msgState = itemView.findViewById(com.hyphenate.easeui.R.id.msg_state);
            list_itease_layout = (RelativeLayout) itemView.findViewById(com.hyphenate.easeui.R.id.list_itease_layout);
            motioned = (TextView) itemView.findViewById(com.hyphenate.easeui.R.id.mentioned);
        }
    }
}
