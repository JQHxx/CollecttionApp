package com.huateng.collection.ui.fragment.casebox;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flyco.systembar.SystemBarHelper;
import com.huateng.collection.R;
import com.huateng.collection.ui.base.BaseFragment;
import com.huateng.collection.widget.RecordView;
import com.tools.view.RxTitle;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author: yichuan
 * Created on: 2020-03-18 09:17
 * description:
 */
public class FragmentRecord extends BaseFragment {
    private final String TAG = getClass().getSimpleName();
    @BindView(R.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R.id.csv_record)
    RecordView mCsvRecord;
    @BindView(R.id.tv_tip)
    TextView mTvTip;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;


    @Override
    protected void init(Bundle savedInstanceState) {
        SystemBarHelper.immersiveStatusBar((Activity) mContext, 0);
        SystemBarHelper.setHeightAndPadding(mContext, mRxTitle);
        mRxTitle.getLlLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_record, container, false);
        ButterKnife.bind(this, mContentView);
        return mContentView;
    }


    @Override
    public boolean onBackPressedSupport() {

        pop();
        return true;
    }


}
