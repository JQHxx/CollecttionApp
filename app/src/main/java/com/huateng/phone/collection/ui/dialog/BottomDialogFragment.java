package com.huateng.phone.collection.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huateng.phone.collection.R;
import com.luck.picture.lib.tools.ScreenUtils;
import com.tools.utils.Utils;
import com.trello.rxlifecycle3.components.support.RxDialogFragment;
import com.zrht.common.animation.AnimationListener;
import com.zrht.common.animation.ViewAnimator;
import com.zrht.common.bean.BottomDialogBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * author: yichuan
 * Created on: 2020-04-17 10:07
 * description:
 */
public class BottomDialogFragment extends RxDialogFragment {
    private DialogItemClick callBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.iv_close)
    ImageView mIvClose;
    @BindView(R.id.view_line)
    View mViewLine;
    @BindView(R.id.rl_view)
    RelativeLayout mRlView;
    @BindView(R.id.rl_parent)
    RelativeLayout mRlParent;
    private Unbinder unbinder;
    private BottomDialogAdapter mAdapter;
    private List<BottomDialogBean> list;
    private boolean isGrid = false; //
    private String title; //标题
    private boolean isCancle;//点击后是否消失


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
        LinearLayoutManager linearLayoutManager = null;
        if (!isGrid) {
            linearLayoutManager = new LinearLayoutManager(getContext());
        } else {
            linearLayoutManager = new GridLayoutManager(getActivity(), 4);

        }
        mRecyclerView.setLayoutManager(linearLayoutManager);
        showView();
    }


    public static BottomDialogFragment newInstance() {
        Bundle args = new Bundle();
        BottomDialogFragment fragment = new BottomDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public BottomDialogFragment setData(String title, List<BottomDialogBean> list, boolean isGrid, boolean isCancle) {
        this.title = title;
        this.list = list;
        this.isGrid = isGrid;
        this.isCancle = isCancle;
        return this;
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

        if (list == null) {
            return;
        }
        mTvTitle.setText(title);
        mAdapter = new BottomDialogAdapter(R.layout.item_bottom_dialog);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setNewData(list);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public static class BottomDialogAdapter extends BaseQuickAdapter<BottomDialogBean, BaseViewHolder> {

        public BottomDialogAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, BottomDialogBean item) {
            helper.setText(R.id.tv_title, item.getTitle());
            if (item.getImageId() != 0) {
                helper.setImageResource(R.id.iv_icon, item.getImageId());

            }
            if(item.isSelected()) {
                helper.setTextColor(R.id.tv_title,mContext.getResources().getColor(R.color.accent_color));

            }else {
                helper.setTextColor(R.id.tv_title,mContext.getResources().getColor(R.color.text_color_343434));

            }

            helper.setGone(R.id.iv_icon, item.getImageId() != 0);
        }
    }


    public void showView() {
        ViewAnimator.animate(mRlView).translationY(ScreenUtils.dip2px(Utils.getApp(), 270), 0)
                .duration(300)
                .onStart(new AnimationListener.Start() {
                    @Override
                    public void onStart() {
                        if(mRlView != null) {
                            mRlView.setVisibility(View.VISIBLE);
                        }

                    }
                })
                .start();
    }

    public void hideView() {
        ViewAnimator
                .animate(mRlView).translationY(0, ScreenUtils.dip2px(Utils.getApp(), 270))
                .duration(300)
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        if(mRlView != null) {
                            mRlView.setVisibility(View.GONE);
                        }



                        dismiss();
                    }
                })
                .start();
    }

    public interface DialogItemClick {

        void onItemClick(BottomDialogBean bottomDialogBean);
    }


    protected void initListener() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (list == null || list.size() == 0) {
                    return;
                }
                list.get(position).setSelected(true);
                mAdapter.notifyDataSetChanged();
                if (callBack != null) {
                    callBack.onItemClick(list.get(position));
                }
                hideView();
            }
        });

        mRlParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideView();
            }
        });

        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideView();
            }
        });
    }

    public BottomDialogFragment setDialogItemClicklistener(DialogItemClick callBack) {
        this.callBack = callBack;
        return this;
    }

    public void show(FragmentManager fragmentManager) {

        this.show(fragmentManager, "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
