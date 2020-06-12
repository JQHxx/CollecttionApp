package com.huateng.collection.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huateng.collection.R;
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
public class BottomDialogFragment2 extends RxDialogFragment {
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
    private boolean isProcess = false; //案件状态在处理中
    private String title; //标题
    private boolean isCancle;//点击后是否消失


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_dialog2, null);
        unbinder = ButterKnife.bind(this, view);
        initView(view);
        initData();
        initListener();
        return view;
    }


    protected void initView(View view) {
        LinearLayoutManager linearLayoutManager = null;

        linearLayoutManager = new GridLayoutManager(getActivity(), 4);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        showView();
    }


    public static BottomDialogFragment2 newInstance() {
        Bundle args = new Bundle();
        BottomDialogFragment2 fragment = new BottomDialogFragment2();
        fragment.setArguments(args);
        return fragment;
    }

    public BottomDialogFragment2 setData(String title, List<BottomDialogBean> list, boolean isProcess, boolean isCancle) {
        this.title = title;
        this.list = list;
        this.isProcess = isProcess;
        this.isCancle = isCancle;
        return this;
    }


    public static BottomDialogFragment2 newInstance(Bundle args) {
        BottomDialogFragment2 fragment = new BottomDialogFragment2();
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
        mAdapter = new BottomDialogAdapter(R.layout.item_bottom_dialog2,isProcess);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setNewData(list);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public static class BottomDialogAdapter extends BaseQuickAdapter<BottomDialogBean, BaseViewHolder> {
        boolean isProcess;
        public BottomDialogAdapter(int layoutResId,boolean isProcess) {
            super(layoutResId);
            this.isProcess = isProcess;
        }



        @Override
        protected void convert(@NonNull BaseViewHolder helper, BottomDialogBean item) {
            helper.setText(R.id.tv_title, item.getTitle());
            if (item.getImageId() != 0) {
                helper.setImageResource(R.id.iv_icon, item.getImageId());

            }
            if(isProcess && helper.getAdapterPosition()>3) {

                helper.setAlpha(R.id.tv_title,0.5f);
                helper.setAlpha(R.id.iv_icon,0.5f);
            }

            helper.setGone(R.id.iv_icon, item.getImageId() != 0);
        }



    }


    public void showView() {
        if(mRlParent != null) {
            mRlParent.setVisibility(View.VISIBLE);
        }
        ViewAnimator.animate(mRlView).translationY(ScreenUtils.dip2px(Utils.getApp(), 350), 0)
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
                .animate(mRlView).translationY(0, ScreenUtils.dip2px(Utils.getApp(), 350))
                .duration(300)
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        if(mRlView != null) {
                            mRlView.setVisibility(View.GONE);
                        }
                        if(mRlParent != null) {
                            mRlParent.setVisibility(View.GONE);
                        }

                        try{
                            dismiss();
                        }catch (IllegalStateException ignore){
                            if(mRlView != null) {
                                mRlView.setVisibility(View.GONE);
                            }
                            if(mRlParent != null) {
                                mRlParent.setVisibility(View.GONE);
                            }

                        }
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

    public BottomDialogFragment2 setDialogItemClicklistener(DialogItemClick callBack) {
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
