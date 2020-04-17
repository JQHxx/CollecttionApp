package com.huateng.collection.base;

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

}
