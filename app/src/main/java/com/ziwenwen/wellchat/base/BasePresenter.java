package com.ziwenwen.wellchat.base;

import java.lang.ref.WeakReference;

/**
 * Created by ziwen.wen on 2017/3/17.
 */
public abstract class BasePresenter<M, V> {

    protected M mModel;

    protected V mView;
    private WeakReference<V> mViewRef;


    public void attachModelView(M pModel, V pView) {

        this.mView = pView;
        mViewRef = new WeakReference<V>(pView);

        this.mModel = pModel;
    }


    public V getView() {
        if (isAttach()) {
            return mViewRef.get();
        } else {
            return null;
        }
    }

    public boolean isAttach() {
        return null != mViewRef && null != mViewRef.get();
    }


    public void onDetach() {
        if (null != mViewRef) {
            mViewRef.clear();
            mViewRef = null;
        }
    }
}
