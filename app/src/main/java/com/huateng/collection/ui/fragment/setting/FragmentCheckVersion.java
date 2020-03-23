package com.huateng.collection.ui.fragment.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huateng.collection.R;
import com.huateng.collection.network.UpdateHelper;
import com.huateng.collection.ui.base.ActivityBase;
import com.huateng.collection.ui.base.BaseFragment;
import com.huateng.fm.ui.widget.FmButton;
import com.tools.utils.AppUtils;
import com.tools.view.RxTitle;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sumincy on 2016/12/13.
 * 版本更新
 */

public class FragmentCheckVersion extends BaseFragment {

    @BindView(R.id.tv_appVersion)
    TextView tvAppVersion;
    @BindView(R.id.btn_check)
    FmButton btnCheck;
    @BindView(R.id.rx_title)
    RxTitle rxTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_version, container, false);
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

        tvAppVersion.setText(AppUtils.getAppVersionName());

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateHelper.checkupdate((ActivityBase) mContext, true);
            }
        });
    }

}
