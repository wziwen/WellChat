package com.ziwenwen.wellchat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.exceptions.HyphenateException;
import com.ziwenwen.wellchat.adapter.ContactProvider;
import com.ziwenwen.wellchat.adapter.ConversationProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import me.drakeet.multitype.MultiTypeAdapter;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MultiTypeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MultiTypeAdapter();
        adapter.register(EMConversation.class, new ConversationProvider());
        adapter.register(String.class, new ContactProvider());
        List<?> items = new ArrayList();
        adapter.setItems(items);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUser();
    }

    private void loadUser() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 加载聊天历史
                List conversations = loadConversationList();
                if (conversations != null) {
                    adapter.getItems().addAll(conversations);
                    refreshList();
                }

                // 加载通讯录
                try {
                    List userList = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    if (userList != null) {
                        if (conversations != null) {
                            List<String> users = userList;
                            List<EMConversation> conversationList = conversations;
                            // 去除重复内容
                            for (String user : users) {
                                for (EMConversation conversation : conversationList) {
                                    if (user.equals(conversation.conversationId())) {
                                        userList.remove(user);
                                        break;
                                    }
                                }
                            }
                            adapter.getItems().addAll(userList);
                        } else {
                            adapter.getItems().addAll(userList);
                        }
                        refreshList();
                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void refreshList() {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    protected List<EMConversation> loadConversationList(){
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<>();
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> sortList) {
        Collections.sort(sortList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(Pair<Long, EMConversation> o1, Pair<Long, EMConversation> o2) {
                long left = o1.first;
                long right = o2.first;

                if (left == right) {
                    return 0;
                }
                if (left < right) {
                    return 1;
                }

                return -1;
            }
        });
    }
}
