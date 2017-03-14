package com.ziwenwen.wellchat.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ziwenwen.wellchat.ChatActivity;
import com.ziwenwen.wellchat.R;

import java.util.List;

/**
 * Created by ziwen.wen on 2017/3/8.
 */
public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.Holder> implements View.OnClickListener{

    List<String> userList;

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_view, parent ,false);
        view.setOnClickListener(this);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        String name = userList.get(position);
        holder.bind(name);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return userList == null ? 0 : userList.size();
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        String userName = userList.get(position);

        Context context = v.getContext();
        context.startActivity(new Intent(context, ChatActivity.class).putExtra("userId", userName));
    }

    public void setUserList(List<String> usernames) {
        userList = usernames;
    }

    public static class Holder extends RecyclerView.ViewHolder {
        TextView tvUser;
        public Holder(View itemView) {
            super(itemView);
            tvUser = (TextView) itemView.findViewById(R.id.tv_user);
        }

        public void bind(String name) {
            tvUser.setText(name);
        }
    }
}
