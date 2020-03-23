package com.huateng.fm.ui.toolbar;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.huateng.flowMobile.R;

import androidx.appcompat.widget.Toolbar;

/**
 * author: Devin
 * createTime:2015年7月10日
 * desciprion:
 */
public class ToolbarBuilder extends ToolBarPrototype {

    private Activity mActivity;
    private Toolbar mToolBar;
    private View mCustomView;
    private int mGravity = Gravity.CENTER;
    private Toolbar.LayoutParams mLayoutParams;
    private ImageButton ib_left_left_arrow, ib_left_down_arrow,
            ib_left_image, ib_middle_down_arrow, ib_right_down_arrow, ib_right_image, ib_middle_image;
    private TextView tv_left, tv_right, tv_middle;
    private ViewGroup csv_middle;

    private View leftLayout, rightLayout;

    public ToolbarBuilder(Activity activity, Toolbar toolbar) {
        this.mActivity = activity;
        this.mToolBar = toolbar;
        init();
    }

    private void init() {
        mLayoutParams = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.MATCH_PARENT,
                Toolbar.LayoutParams.MATCH_PARENT,
                mGravity);
        build();
    }

    private void initListener() {
        initViews();
        tv_left.setOnClickListener(mLeftListener);
        ib_left_left_arrow.setOnClickListener(mLeftListener);
//        ib_left_down_arrow.setOnClickListener(mLeftListener);
//        ib_left_image.setOnClickListener(mLeftListener);

        tv_middle.setOnClickListener(mMiddleListener);
        ib_middle_down_arrow.setOnClickListener(mMiddleListener);
        ib_middle_image.setOnClickListener(mMiddleListener);

        tv_right.setOnClickListener(mRightListener);
//        ib_right_down_arrow.setOnClickListener(mRightListener);
        ib_right_image.setOnClickListener(mRightListener);
        leftLayout.setOnClickListener(mLeftListener);
        rightLayout.setOnClickListener(mRightListener);

    }

    private void initViews() {
        tv_left = (TextView) mCustomView.findViewById(R.id.tv_left);
        tv_middle = (TextView) mCustomView.findViewById(R.id.tv_middle);
        tv_right = (TextView) mCustomView.findViewById(R.id.tv_right);
        ib_left_image = (ImageButton) mCustomView.findViewById(R.id.ib_left_image);

        ib_left_left_arrow = (ImageButton) mCustomView.findViewById(R.id.ib_left_left_arrow);
        ib_left_down_arrow = (ImageButton) mCustomView.findViewById(R.id.ib_left_down_arrow);
        ib_middle_down_arrow = (ImageButton) mCustomView.findViewById(R.id.ib_middle_down_arrow);
        ib_middle_image = (ImageButton) mCustomView.findViewById(R.id.ib_middle_image);

        ib_right_down_arrow = (ImageButton) mCustomView.findViewById(R.id.ib_right_down_arrow);
        ib_right_image = (ImageButton) mCustomView.findViewById(R.id.ib_right_image);
        csv_middle = (ViewGroup) mCustomView.findViewById(R.id.csv_middle);
        leftLayout = mCustomView.findViewById(R.id.layout_left);
        rightLayout = mCustomView.findViewById(R.id.layout_right);
    }

    public View getRightLayout() {
        return rightLayout;
    }


    public ToolbarBuilder setModelType(int leftType, int titleType, int rightType) {
        //左边
        switch (leftType) {

            case ModelType.ARROW.LEFT:
                tv_left.setVisibility(View.GONE);
                ib_left_left_arrow.setVisibility(View.VISIBLE);
                ib_left_down_arrow.setVisibility(View.GONE);
                ib_left_image.setVisibility(View.GONE);
                break;

            case ModelType.TEXT.ARROW.LEFT:
                tv_left.setVisibility(View.VISIBLE);
                ib_left_left_arrow.setVisibility(View.VISIBLE);
                ib_left_down_arrow.setVisibility(View.GONE);
                ib_left_image.setVisibility(View.GONE);
                break;

            case ModelType.TEXT.ARROW.DOWN:
                tv_left.setVisibility(View.GONE);
                ib_left_left_arrow.setVisibility(View.GONE);
                ib_left_down_arrow.setVisibility(View.VISIBLE);
                ib_left_image.setVisibility(View.GONE);
                break;
            case ModelType.TEXT.PLAIN:
                tv_left.setVisibility(View.VISIBLE);
                ib_left_left_arrow.setVisibility(View.GONE);
                ib_left_down_arrow.setVisibility(View.GONE);
                ib_left_image.setVisibility(View.GONE);
                break;
            case ModelType.IMAGE.PLAIN:
                tv_left.setVisibility(View.GONE);
                ib_left_left_arrow.setVisibility(View.GONE);
                ib_left_down_arrow.setVisibility(View.GONE);
                ib_left_image.setVisibility(View.VISIBLE);
                break;

            case ModelType.HIDE:
                tv_left.setVisibility(View.GONE);
                ib_left_left_arrow.setVisibility(View.GONE);
                ib_left_down_arrow.setVisibility(View.GONE);
                ib_left_image.setVisibility(View.GONE);

            default:
                break;
        }
        //中间
        switch (titleType) {
            case ModelType.TEXT.ARROW.DOWN:
                tv_middle.setVisibility(View.VISIBLE);
                ib_middle_down_arrow.setVisibility(View.VISIBLE);
                ib_middle_image.setVisibility(View.GONE);
                csv_middle.setVisibility(View.GONE);
                break;
            case ModelType.TEXT.PLAIN:
                tv_middle.setVisibility(View.VISIBLE);
                ib_middle_down_arrow.setVisibility(View.GONE);
                ib_middle_image.setVisibility(View.GONE);
                csv_middle.setVisibility(View.GONE);
                break;
            case ModelType.IMAGE.PLAIN:
                ib_middle_image.setVisibility(View.VISIBLE);
                ib_middle_down_arrow.setVisibility(View.GONE);
                tv_middle.setVisibility(View.GONE);
                csv_middle.setVisibility(View.GONE);
                break;
            case ModelType.HIDE:
                ib_middle_image.setVisibility(View.GONE);
                ib_middle_down_arrow.setVisibility(View.GONE);
                tv_middle.setVisibility(View.GONE);
                csv_middle.setVisibility(View.GONE);
                break;
            case ModelType.CUSTOM:
                ib_middle_image.setVisibility(View.GONE);
                ib_middle_down_arrow.setVisibility(View.GONE);
                tv_middle.setVisibility(View.GONE);
                csv_middle.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        //右边
        switch (rightType) {
            case ModelType.TEXT.ARROW.DOWN:
                tv_right.setVisibility(View.VISIBLE);
                ib_right_down_arrow.setVisibility(View.VISIBLE);
                ib_right_image.setVisibility(View.GONE);
                break;
            case ModelType.TEXT.PLAIN:
                tv_right.setVisibility(View.VISIBLE);
                ib_right_down_arrow.setVisibility(View.GONE);
                ib_right_image.setVisibility(View.GONE);
                break;
            case ModelType.IMAGE.PLAIN:
                tv_right.setVisibility(View.GONE);
                ib_right_down_arrow.setVisibility(View.GONE);
                ib_right_image.setVisibility(View.VISIBLE);
                break;
            case ModelType.HIDE:
                tv_right.setVisibility(View.GONE);
                ib_right_down_arrow.setVisibility(View.GONE);
                ib_right_image.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        return this;
    }


    public ToolbarBuilder setRightTextColorResource(int colorId) {
        int color = mActivity.getResources().getColor(colorId);
        tv_right.setTextColor(color);
        return this;
    }

    public ToolbarBuilder setLeftTextColorResource(int colorId) {
        int color = mActivity.getResources().getColor(colorId);
        tv_left.setTextColor(color);
        return this;
    }

    public ToolbarBuilder setMiddleTextColorResource(int colorId) {
        int color = mActivity.getResources().getColor(colorId);
        tv_middle.setTextColor(color);
        return this;
    }

    public ToolbarBuilder setMiddleCustom(View v, android.widget.LinearLayout.LayoutParams params) {
        csv_middle.removeAllViews();
        csv_middle.addView(v, params);
        return this;
    }

    public ToolbarBuilder setLeftArrow(int id) {
        ib_left_left_arrow.setImageResource(id);
        return this;
    }

    public ToolbarBuilder setBackgroundResource(int id) {
        mToolBar.setBackgroundDrawable(mActivity.getResources().getDrawable(id));
        return this;
    }

    public ToolbarBuilder setDownArrow(int id) {
        ib_left_down_arrow.setImageResource(id);
        ib_right_down_arrow.setImageResource(id);
        return this;
    }

    public ToolbarBuilder setLeftImage(int id) {
        ib_left_image.setImageResource(id);
        return this;
    }

    public ToolbarBuilder setMiddleImage(int id) {
        ib_middle_image.setImageResource(id);
        return this;
    }

    public ToolbarBuilder setRightImage(int id) {
        ib_right_image.setImageResource(id);
        return this;
    }

    public ToolbarBuilder setMiddleText(String text) {
        tv_middle.setText(text);
        return this;
    }

    public ToolbarBuilder setLeftText(String text) {
        tv_left.setText(text);
        return this;
    }

    public ToolbarBuilder setRightText(String text) {
        tv_right.setText(text);
        return this;
    }

    public ToolbarBuilder hideToolBar() {
        mToolBar.setVisibility(View.GONE);
        return this;
    }

    public ToolbarBuilder showToolBar() {
        mToolBar.setVisibility(View.VISIBLE);
        return this;
    }


    private ToolbarBuilder setCustomLayout(int layoutId) {
        View customView = mActivity.getLayoutInflater().inflate(layoutId, null);
        this.mCustomView = customView;
        return this;
    }

    public ToolbarBuilder setGravity(int gravity) {
        this.mGravity = gravity;
        return this;
    }

    public ToolbarBuilder setLayoutParams(Toolbar.LayoutParams lp) {
        this.mLayoutParams = lp;
        return this;
    }

    public Toolbar build() {
        setCustomLayout(R.layout.ht_actionbar_custom_default);
        mToolBar.addView(mCustomView, mLayoutParams);
        initListener();
        return mToolBar;
    }

    public OnActionBarLeftClickListener leftListener;
    public OnActionBarMiddleClickListener middleListener;
    public OnActionBarRightClickListener rightListener;
    public OnActionBarClickListener listener;


    public interface OnActionBarLeftClickListener {
        public void actionBarLeftClicked();
    }

    public interface OnActionBarMiddleClickListener {
        public void actionBarMiddleClicked();
    }

    public interface OnActionBarRightClickListener {
        public void actionBarRightClicked();
    }

    public interface OnActionBarClickListener {
        public void actionBarRightClicked();

        public void actionBarLeftClicked();

    }

    public ToolbarBuilder setOnActionBarLeftClickListener(OnActionBarLeftClickListener listener) {
        leftListener = listener;
        return this;
    }

    public ToolbarBuilder setOnActionBarRightClickListener(OnActionBarRightClickListener listener) {
        rightListener = listener;
        return this;
    }

    public ToolbarBuilder setOnActionBarMiddleClickListener(OnActionBarMiddleClickListener listener) {
        middleListener = listener;
        return this;
    }

    public ToolbarBuilder setOnActionBarClickListener(OnActionBarClickListener listener) {
        this.listener = listener;
        return this;
    }

    private OnClickListener mLeftListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (leftListener != null) {
                leftListener.actionBarLeftClicked();
            }
            if (listener != null) {
                listener.actionBarLeftClicked();
            }
        }
    };

    private OnClickListener mMiddleListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (middleListener != null) {
                middleListener.actionBarMiddleClicked();
            }
        }
    };

    private OnClickListener mRightListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (rightListener != null) {
                rightListener.actionBarRightClicked();
            }
            if (listener != null) {
                listener.actionBarRightClicked();
            }
        }
    };


}
