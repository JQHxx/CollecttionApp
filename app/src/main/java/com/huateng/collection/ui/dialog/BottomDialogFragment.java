package com.huateng.collection.ui.dialog;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huateng.collection.R;
import com.luck.picture.lib.tools.ScreenUtils;
import com.tools.utils.Utils;
import com.zrht.common.animation.AnimationListener;
import com.zrht.common.animation.ViewAnimator;
import com.zrht.common.bean.BottomDialogBean;
import com.trello.rxlifecycle3.components.support.RxDialogFragment;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * author: yichuan
 * Created on: 2020-04-17 10:07
 * description:
 */
public class BottomDialogFragment extends RxDialogFragment {
    @BindView(R.id.tv_close)
    TextView mTvClose;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.ll_layout)
    LinearLayout mLlLayout;
    @BindView(R.id.rl_view)
    RelativeLayout mRlView;
    private Unbinder unbinder;
    private BottomDialogAdapter mAdapter;
    private List<BottomDialogBean> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_dialog, null);
        unbinder = ButterKnife.bind(this, view);
        initView(view);
        initData();
        initListener();
        return view;
    }


    protected void initView(View view) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        show();
    }


    public static BottomDialogFragment newInstance() {
        Bundle args = new Bundle();
        BottomDialogFragment fragment = new BottomDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static BottomDialogFragment newInstance(Bundle args) {
        BottomDialogFragment fragment = new BottomDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.Dialog_FullScreen);
    }


    protected void initData() {
        Bundle arguments = getArguments();
        list = arguments.getParcelableArrayList("list");
        if (list == null) {
            return;
        }
        mAdapter = new BottomDialogAdapter(R.layout.item_bottom_dialog);
        mAdapter.setNewData(list);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.tv_close)
    public void onClick() {
    }


    public static class BottomDialogAdapter extends BaseQuickAdapter<BottomDialogBean, BaseViewHolder> {

        public BottomDialogAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, BottomDialogBean item) {
            helper.setText(R.id.tv_title, item.getTitle());
           /* if (item.isSelected()) {
                helper.setImageResource(R.id.iv_circle, R.drawable.iv_circle_selected);
            } else {
                helper.setImageResource(R.id.iv_circle, R.drawable.iv_circle_unselected);
            }*/
        }
    }


    public void show() {
        Log.e("NBCB", "show show show");
        ViewAnimator.animate(mLlLayout).translationY(ScreenUtils.dip2px(Utils.getApp(), 270), 0)
                .duration(300)
                .onStart(new AnimationListener.Start() {
                    @Override
                    public void onStart() {
                        mLlLayout.setVisibility(View.VISIBLE);
                    }
                })
                .start();
    }

    public void hide() {
        ViewAnimator
                .animate(mLlLayout).translationY(0, ScreenUtils.dip2px(Utils.getApp(), 270))
                .duration(300)
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        mLlLayout.setVisibility(View.GONE);
                        dismiss();
                    }
                })
                .start();
    }


    protected void initListener() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setSelected(position == i);
                }
                mAdapter.notifyDataSetChanged();
                hide();
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
