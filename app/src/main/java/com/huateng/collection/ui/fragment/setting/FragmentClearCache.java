package com.huateng.collection.ui.fragment.setting;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huateng.collection.R;
import com.huateng.collection.event.BusEvent;
import com.huateng.collection.ui.activity.ActivityMain;
import com.huateng.collection.ui.base.BaseFragment;
import com.huateng.collection.utils.DataCleanManager;
import com.huateng.collection.utils.Utils;
import com.huateng.fm.ui.widget.FmButton;
import com.tools.view.RxTitle;
import com.tools.view.RxToast;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.eventbusactivityscope.EventBusActivityScope;

/**
 * Created by shanyong on 2016/12/13.
 */

public class FragmentClearCache extends BaseFragment {

    @BindView(R.id.btn_confirm)
    FmButton btnConfirm;
    @BindView(R.id.rx_title)
    RxTitle rxTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clear_cache, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    protected void init(Bundle savedInstanceState) {
//        setFragmentAnimator(new DefaultHorizontalAnimator());
        immersiveStatusBar(rxTitle);
        rxTitle.getLlLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                DataCleanManager.clearAllCache(mContext);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //刷新发件箱
                        EventBusActivityScope.getDefault(_mActivity).post(BusEvent.REFRESH_DONE_CASES);
                        Utils.scanAllMedia(mContext);
                        ((ActivityMain) getActivity()).updateCacheSize();
                        hideLoading();
                        RxToast.showToast("缓存清除成功");
                    }
                }, 2000);

            }
        });
    }
}
