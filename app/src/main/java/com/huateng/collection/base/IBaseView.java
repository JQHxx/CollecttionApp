package com.huateng.collection.base;


import com.trello.rxlifecycle3.LifecycleTransformer;

/**
 * File descripition:   基本回调 可自定义添加所需回调
 *
 * @author lp
 * @date 2018/6/19
 */

public interface IBaseView {
    /**
     * 显示dialog
     */
    void showLoading();
    /**
     * 隐藏 dialog
     */
    void hideLoading();

    void showToast(String message);


 <T> LifecycleTransformer<T> getRxlifecycle();
}
