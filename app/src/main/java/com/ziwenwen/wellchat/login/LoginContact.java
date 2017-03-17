package com.ziwenwen.wellchat.login;

import com.ziwenwen.wellchat.base.BasePresenter;
import com.ziwenwen.wellchat.base.BaseView;

/**
 * Created by ziwen.wen on 2017/3/17.
 */
public interface LoginContact {

    public abstract class IPLogin extends BasePresenter<Object, IVLogin> {
        abstract void login(String user, String password);
    }

    interface IVLogin extends BaseView {
        void onSuccess();
        void onFail(String msg);
        void onPasswordError(String msg);
        void onAccountError(String msg);
    }
}
