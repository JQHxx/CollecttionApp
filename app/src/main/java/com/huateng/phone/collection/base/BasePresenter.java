package com.huateng.phone.collection.base;

import com.huateng.network.RetrofitManager;
import com.huateng.network.bean.ResponseStructure;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * File descripition:   创建Presenter基类
 *
 * @author lp
 * @date 2018/6/19
 */
public abstract class BasePresenter<V extends IBaseView> {
    public V mView;

    public BasePresenter(V view) {
        this.mView = view;
    }
    /**
     * 解除绑定
     */
    public void detachView() {
        mView = null;
    }
    /**
     * 返回 view
     *
     * @return
     */
    public V getBaseView() {
        return mView;
    }

    /**
     * 络数据加载方法
     */
    public Observable<ResponseStructure> request(String root,String method, Map<String, Object> map){

      return  RetrofitManager.getInstance()
                .request(root, method, map)
                .compose(mView.getRxlifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

}
