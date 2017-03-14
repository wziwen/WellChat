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
public class ContactProvider extends ItemViewProvider<String, ContactProvider.ConversationHolder> implements View.OnClickListener {

    @NonNull
    @Override
    protected ConversationHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.ease_row_chat_history, parent, false);
        root.setOnClickListener(this);
        return new ConversationHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ConversationHolder holder, @NonNull String conversation) {
        holder.list_itease_layout.setBackgroundResource(com.hyphenate.easeui.R.drawable.ease_mm_listitem);

        holder.itemView.setTag(conversation);

        Context context = holder.avatar.getContext();
        String username;
        if (conversation.contains("chat_")) {
            username = conversation.substring(conversation.indexOf("chat_") + 5);
        } else {
            username = conversation;
        }

        EaseUserUtils.setUserAvatar(context, username, holder.avatar);
        EaseUserUtils.setUserNick(username, holder.name);
        holder.motioned.setVisibility(View.GONE);

        holder.unreadLabel.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        String user = (String) v.getTag();
        Context context = v.getContext();
        context.startActivity(new Intent(context, ChatActivity.class).putExtra("userId", user));
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
