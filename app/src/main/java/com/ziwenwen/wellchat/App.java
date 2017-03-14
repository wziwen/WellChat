package com.ziwenwen.wellchat;

import android.app.Application;
import android.text.TextUtils;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;

/**
 * Created by ziwen.wen on 2017/3/8.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        EMOptions options = new EMOptions();
        // TODO: 2017/3/9 动态配置key
        // options.setAppKey();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        //初始化
//        EMClient.getInstance().init(this, options);

        EaseUI.getInstance().init(this, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(BuildConfig.DEBUG);

        String userName = EMClient.getInstance().getCurrentUser();
        if (!TextUtils.isEmpty(userName)) {
            User user = new User(userName);
            DataCenter.getInstance().setUser(user);
        }
    }
}
