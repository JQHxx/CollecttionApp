package com.huateng.collection.ui.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huateng.collection.R;
import com.zrht.common.animation.AnimationListener;
import com.zrht.common.animation.ViewAnimator;
import com.zrht.common.bean.BottomDialogBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author: yichuan
 * Created on: 2020-04-17 10:07
 * description:
 */
public class BottomDialogView extends RelativeLayout {
    private int height = 400;
    private OnItemClickListener listener;
    TextView mTvTitle;
    RecyclerView mRecyclerView;
    ImageView mIvClose;
    View mViewLine;
    RelativeLayout mRlView;
    RelativeLayout mRlParent;
    private BottomDialogAdapter mAdapter;
    private List<BottomDialogBean> list;
    private boolean isProcess;

    public BottomDialogView(Context context) {
        super(context);
        init(context);
    }

    public BottomDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BottomDialogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        if (context == null) {
            return;
        }
        View.inflate(context, R.layout.fragment_bottom_dialog2, BottomDialogView.this);
        mTvTitle = this.findViewById(R.id.tv_title);
        mRecyclerView = this.findViewById(R.id.recyclerview);
        mIvClose = this.findViewById(R.id.iv_close);
        mViewLine = this.findViewById(R.id.view_line);
        mRlView = this.findViewById(R.id.rl_view);
        mRlParent = this.findViewById(R.id.rl_parent);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 4));

    }

    public void initData(boolean isProcess, List<BottomDialogBean> list) {

        if (list == null) {
            return;
        }
        this.list = list;
        this.isProcess = isProcess;
        mAdapter = new BottomDialogAdapter(R.layout.item_bottom_dialog2, true);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setNewData(list);
        initListener();

        mRlView.post(new Runnable() {
            @Override
            public void run() {
                if (mRlView != null) {
                    height = mRlView.getMeasuredHeight() == 0 ? 400 : mRlView.getMeasuredHeight();
                }

            }
        });
    }


    public static class BottomDialogAdapter extends BaseQuickAdapter<BottomDialogBean, BaseViewHolder> {
        boolean isProcess;

        public BottomDialogAdapter(int layoutResId, boolean isProcess) {
            super(layoutResId);
            this.isProcess = isProcess;
        }


        @Override
        protected void convert(@NonNull BaseViewHolder helper, BottomDialogBean item) {
            helper.setText(R.id.tv_title, item.getTitle());
            if (item.getImageId() != 0) {
                helper.setImageResource(R.id.iv_icon, item.getImageId());

            }
            if (isProcess && helper.getAdapterPosition() > 3) {

                helper.setAlpha(R.id.tv_title, 0.5f);
                helper.setAlpha(R.id.iv_icon, 0.5f);
            }

            helper.setGone(R.id.iv_icon, item.getImageId() != 0);
        }


    }


    public void showView() {
        if (mRlParent != null) {
            mRlParent.setVisibility(View.VISIBLE);
        }
        //ScreenUtils.dip2px(Utils.getApp(), 350)
        ViewAnimator.animate(mRlView).translationY(height, 0)
                .duration(300)
                .onStart(new AnimationListener.Start() {
                    @Override
                    public void onStart() {
                       /* if (mRlView != null) {
                            mRlView.setVisibility(View.VISIBLE);
                        }*/
                        BottomDialogView.this.setVisibility(View.VISIBLE);

                    }
                })
                .start();
    }

    public void hideView() {
        ViewAnimator
                .animate(mRlView).translationY(0, height)
                .duration(300)
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        BottomDialogView.this.setVisibility(View.GONE);
                    }
                })
                .start();
    }


    protected void initListener() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (list == null || list.size() == 0) {
                    return;
                }

                ViewAnimator
                        .animate(mRlView).translationY(0, height)
                        .duration(300)
                        .onStop(new AnimationListener.Stop() {
                            @Override
                            public void onStop() {
                                BottomDialogView.this.setVisibility(View.GONE);

                                if (listener != null) {
                                    listener.onItemClick(list.get(position));
                                }
                            }
                        })
                        .start();
                // list.get(position).setSelected(true);
                //  mAdapter.notifyDataSetChanged();
                //   hideView();

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


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public interface OnItemClickListener {
        void onItemClick(BottomDialogBean bean);
    }

}
