package com.huateng.collection.widget.tab;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.huateng.collection.R;

import java.util.List;

import androidx.appcompat.widget.LinearLayoutCompat;


/**
 * Created by shanyong on 17/2/15.
 */
public class NavigatorView extends LinearLayoutCompat {

    OnNavigatorViewItemClickListener mOnNavigatorViewItemClickListener;

    private View rootView;
    private List<View> items;
    private Context context;
    private long lastClickMillis;
    private int forePosition = -1;
    public static final int MIN_CLICK_DELAY_TIME = 500;


    public interface OnNavigatorViewItemClickListener {
        void onNavigatorViewItemClick(int position, View view);
    }

    public NavigatorView(Context context) {
        this(context, null);
    }

    public NavigatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavigatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
        this.context = context;
    }

    public View setLayout(int layoutId) {
        rootView = inflate(context, layoutId, this);
        return rootView;
    }

    public void setItems(List<View> items) {
        this.items = items;
        init();
    }

    private void init() {
        for (int i = 0; i < items.size(); i++) {
            View child = items.get(i);
            selectChild(child, false);
        }

        for (int i = 0; i < items.size(); i++) {
            View view = items.get(i);
            final int finalI = i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (select(finalI)) {
                        mOnNavigatorViewItemClickListener.onNavigatorViewItemClick(finalI, v);
                    }
                }
            });
        }
    }

    public boolean freeze() {
        long currentClickMillis = System.currentTimeMillis();
        long range = currentClickMillis - lastClickMillis;
        if (range < MIN_CLICK_DELAY_TIME) {
//            this.setEnabled(false);
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    NavigatorView.this.setEnabled(true);
//                }
//            }, 2000);
            return true;
        }
        lastClickMillis = currentClickMillis;
        return false;
    }

    public boolean select(int position) {
        //限制切换速度
        if (freeze()) {
            return false;
        }
        //与上次点击相同
        if (position == forePosition) {
            return false;
        }

        for (int i = 0; i < items.size(); i++) {
            View child = items.get(i);
            if (i == position) {
                selectChild(child, true);
            } else {
                selectChild(child, false);
            }
        }

        forePosition = position;

        return true;
    }

    private void selectChild(View child, boolean select) {
        if (child instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) child;
            group.setSelected(select);
            for (int i = 0; i < group.getChildCount(); i++) {
                selectChild(group.getChildAt(i), select);
            }
        } else {
            child.setSelected(select);
            if (child instanceof ImageView) {
                ImageView iv = (ImageView) child;
                Drawable drawable = iv.getDrawable().mutate();
                if (select) {
                    drawable.setColorFilter(getResources().getColor(R.color.colorTabSelected), PorterDuff.Mode.SRC_ATOP);
                } else {
                    drawable.setColorFilter(getResources().getColor(R.color.colorTabNormal), PorterDuff.Mode.SRC_ATOP);
                }
            }
        }
    }

    public void setOnNavigatorViewItemClickListener(OnNavigatorViewItemClickListener listener) {
        this.mOnNavigatorViewItemClickListener = listener;
    }
}
