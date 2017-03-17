package com.ziwenwen.wellchat.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.lang.reflect.ParameterizedType;

/**
 * Created by ziwen.wen on 2017/3/17.
 */
public class BaseActivity <P extends BasePresenter, M> extends AppCompatActivity implements BaseView {

    private ProgressDialog progressDialog;
    private Toast toast;
    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //内部获取第一个类型参数的真实类型  ，反射new出对象
        mPresenter = getT(this, 0);
        //内部获取第二个类型参数的真实类型  ，反射new出对象
        M model = getT(this, 1);
        //使得P层绑定M层和V层，持有M和V的引用
        mPresenter.attachModelView(model, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDetach();
    }

    @Override
    public void showError(int msg) {
        if (toast == null) {
            toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    @Override
    public void showLoading() {
        if (isFinishing()) {
            return;
        }
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (isFinishing()) {
            return;
        }
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }

    static <T> T getT(Object object, int i) {
        try {
            return ((Class<T>) ((ParameterizedType) (object.getClass().getGenericSuperclass())).getActualTypeArguments()[i]).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
