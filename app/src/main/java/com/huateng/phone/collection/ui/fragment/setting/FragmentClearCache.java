package com.huateng.phone.collection.ui.fragment.setting;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.huateng.phone.collection.R;
import com.huateng.phone.collection.base.BaseActivity;
import com.huateng.phone.collection.base.BasePresenter;
import com.huateng.phone.collection.utils.DataCleanManager;
import com.huateng.phone.collection.utils.Utils;
import com.huateng.fm.ui.widget.FmButton;
import com.tools.bean.EventBean;
import com.tools.view.RxTitle;
import com.tools.view.RxToast;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.tools.bean.BusEvent;

import org.apache.poi.ss.formula.functions.T;
import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * Created by shanyong on 2016/12/13.
 */

public class FragmentClearCache extends BaseActivity {

    @BindView(R.id.btn_confirm)
    FmButton btnConfirm;
    @BindView(R.id.rx_title)
    RxTitle rxTitle;


    @Override
    protected void initView(Bundle savedInstanceState) {
//        setFragmentAnimator(new DefaultHorizontalAnimator());
        immersiveStatusBar(rxTitle);
        rxTitle.getLlLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                DataCleanManager.clearAllCache(FragmentClearCache.this);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //刷新发件箱
                        EventBus.getDefault().post(new EventBean(BusEvent.REFRESH_DONE_CASES));
                        Utils.scanAllMedia(FragmentClearCache.this);
                       // ((ActivityMain) getActivity()).updateCacheSize();
                        hideLoading();
                        RxToast.showToast("缓存清除成功");
                    }
                }, 2000);

            }
        });
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }



    /**
     * 获取布局ID
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_clear_cache;
    }

    /**
     * 数据初始化操作
     */
    @Override
    protected void initData() {

    }

    /**
     * 此处设置沉浸式地方
     */
    @Override
    protected void setStatusBar() {

    }

    @Override
    public LifecycleTransformer<T> getRxlifecycle() {
        return bindToLifecycle();
    }
}
