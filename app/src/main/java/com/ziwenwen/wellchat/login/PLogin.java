package com.ziwenwen.wellchat.login;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

/**
 * Created by ziwen.wen on 2017/3/17.
 */
public class PLogin extends LoginContact.IPLogin {

    public void login(String user, String password) {
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mView.onPasswordError("密码字符太少");
            return;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(user) | !isEmailValid(user)) {
            mView.onAccountError("用户名字符太少");
            return;
        }

        doLogin(user, password);
    }

    private void doLogin(String email, String password) {
        mView.showLoading();
        EMClient.getInstance().login(email,password,new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.d("main", "登录聊天服务器成功！");
                onLoginResult(true);
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d("main", "登录聊天服务器失败！" + code + "    " + message);
                onLoginResult(false);
            }
        });
    }

    private void onLoginResult(boolean success) {
        mView.hideLoading();
        if (success) {
            mView.onSuccess();
        } else {
            mView.onFail("账号或密码错误");
        }
    }

    private boolean isEmailValid(String email) {
        return email.length() > 1;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
}
