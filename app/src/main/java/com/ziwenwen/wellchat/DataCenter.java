package com.ziwenwen.wellchat;

/**
 * Created by ziwen.wen on 2017/3/8.
 */
public class DataCenter {

    private static DataCenter instance = new DataCenter();
    public boolean isVideoCalling;
    public boolean isVoiceCalling;

    public static DataCenter getInstance() {
        return instance;
    }

    private DataCenter() {
    }

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
