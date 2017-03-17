package com.ziwenwen.wellchat.base;

/**
 * Created by ziwen.wen on 2017/3/17.
 */
public interface BaseView {
    void showLoading();

    void hideLoading();

    void showError(int msg);
}
