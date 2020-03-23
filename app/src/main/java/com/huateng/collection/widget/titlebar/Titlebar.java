package com.huateng.collection.widget.titlebar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huateng.collection.R;


/**
 * author: Devin
 * createTime:2015年7月10日
 * desciprion:
 */
public class Titlebar  extends  LinearLayout{

    private View mCustomView;
    private int mGravity = Gravity.CENTER;
    private LayoutParams mLayoutParams;
    private ImageButton ib_left_left_arrow, ib_left_down_arrow,
            ib_left_image, ib_middle_down_arrow, ib_right_down_arrow, ib_right_image, ib_middle_image;
    private TextView tv_left, tv_right, tv_middle;
    private ViewGroup csv_middle;

    private View leftLayout, rightLayout;

    public Titlebar(Context context) {
        super(context);
        init();
    }

    public Titlebar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Titlebar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mLayoutParams = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT,
                mGravity);
        build();
    }

    private void initListener() {
        tv_left.setOnClickListener(mLeftListener);
        ib_left_image.setOnClickListener(mLeftListener);

        tv_middle.setOnClickListener(mMiddleListener);
        ib_middle_down_arrow.setOnClickListener(mMiddleListener);
        ib_middle_image.setOnClickListener(mMiddleListener);

        tv_right.setOnClickListener(mRightListener);
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


    public Titlebar setModelType(int leftType, int titleType, int rightType) {
        //左边
        switch (leftType) {

            case TitlebarModelType.ARROW.LEFT:
                tv_left.setVisibility(View.GONE);
                ib_left_left_arrow.setVisibility(View.VISIBLE);
                ib_left_down_arrow.setVisibility(View.GONE);
                ib_left_image.setVisibility(View.GONE);
                break;

            case TitlebarModelType.TEXT.ARROW.LEFT:
                tv_left.setVisibility(View.VISIBLE);
                ib_left_left_arrow.setVisibility(View.VISIBLE);
                ib_left_down_arrow.setVisibility(View.GONE);
                ib_left_image.setVisibility(View.GONE);
                break;

            case TitlebarModelType.TEXT.ARROW.DOWN:
                tv_left.setVisibility(View.GONE);
                ib_left_left_arrow.setVisibility(View.GONE);
                ib_left_down_arrow.setVisibility(View.VISIBLE);
                ib_left_image.setVisibility(View.GONE);
                break;
            case TitlebarModelType.TEXT.PLAIN:
                tv_left.setVisibility(View.VISIBLE);
                ib_left_left_arrow.setVisibility(View.GONE);
                ib_left_down_arrow.setVisibility(View.GONE);
                ib_left_image.setVisibility(View.GONE);
                break;
            case TitlebarModelType.IMAGE.PLAIN:
                tv_left.setVisibility(View.GONE);
                ib_left_left_arrow.setVisibility(View.GONE);
                ib_left_down_arrow.setVisibility(View.GONE);
                ib_left_image.setVisibility(View.VISIBLE);
                break;

            case TitlebarModelType.HIDE:
                tv_left.setVisibility(View.GONE);
                ib_left_left_arrow.setVisibility(View.GONE);
                ib_left_down_arrow.setVisibility(View.GONE);
                ib_left_image.setVisibility(View.GONE);

            default:
                break;
        }
        //中间
        switch (titleType) {
            case TitlebarModelType.TEXT.ARROW.DOWN:
                tv_middle.setVisibility(View.VISIBLE);
                ib_middle_down_arrow.setVisibility(View.VISIBLE);
                ib_middle_image.setVisibility(View.GONE);
                csv_middle.setVisibility(View.GONE);
                break;
            case TitlebarModelType.TEXT.PLAIN:
                tv_middle.setVisibility(View.VISIBLE);
                ib_middle_down_arrow.setVisibility(View.GONE);
                ib_middle_image.setVisibility(View.GONE);
                csv_middle.setVisibility(View.GONE);
                break;
            case TitlebarModelType.IMAGE.PLAIN:
                ib_middle_image.setVisibility(View.VISIBLE);
                ib_middle_down_arrow.setVisibility(View.GONE);
                tv_middle.setVisibility(View.GONE);
                csv_middle.setVisibility(View.GONE);
                break;
            case TitlebarModelType.HIDE:
                ib_middle_image.setVisibility(View.GONE);
                ib_middle_down_arrow.setVisibility(View.GONE);
                tv_middle.setVisibility(View.GONE);
                csv_middle.setVisibility(View.GONE);
                break;
            case TitlebarModelType.CUSTOM:
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
            case TitlebarModelType.TEXT.ARROW.DOWN:
                tv_right.setVisibility(View.VISIBLE);
                ib_right_down_arrow.setVisibility(View.VISIBLE);
                ib_right_image.setVisibility(View.GONE);
                break;
            case TitlebarModelType.TEXT.PLAIN:
                tv_right.setVisibility(View.VISIBLE);
                ib_right_down_arrow.setVisibility(View.GONE);
                ib_right_image.setVisibility(View.GONE);
                break;
            case TitlebarModelType.IMAGE.PLAIN:
                tv_right.setVisibility(View.GONE);
                ib_right_down_arrow.setVisibility(View.GONE);
                ib_right_image.setVisibility(View.VISIBLE);
                break;
            case TitlebarModelType.HIDE:
                tv_right.setVisibility(View.GONE);
                ib_right_down_arrow.setVisibility(View.GONE);
                ib_right_image.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        return this;
    }


    public Titlebar setRightTextColorResource(int colorId) {
        int color = getContext().getResources().getColor(colorId);
        tv_right.setTextColor(color);
        return this;
    }

    public Titlebar setLeftTextColorResource(int colorId) {
        int color = getContext().getResources().getColor(colorId);
        tv_left.setTextColor(color);
        return this;
    }

    public Titlebar setMiddleTextColorResource(int colorId) {
        int color = getContext().getResources().getColor(colorId);
        tv_middle.setTextColor(color);
        return this;
    }

    public Titlebar setMiddleCustom(View v, LinearLayout.LayoutParams params) {
        csv_middle.removeAllViews();
        csv_middle.addView(v, params);
        return this;
    }

    public Titlebar setLeftArrow(int id) {
        ib_left_left_arrow.setImageResource(id);
        return this;
    }


    public Titlebar setDownArrow(int id) {
        ib_left_down_arrow.setImageResource(id);
        ib_right_down_arrow.setImageResource(id);
        return this;
    }

    public Titlebar setLeftImage(int id) {
        ib_left_image.setImageResource(id);
        return this;
    }

    public Titlebar setMiddleImage(int id) {
        ib_middle_image.setImageResource(id);
        return this;
    }

    public Titlebar setRightImage(int id) {
        ib_right_image.setImageResource(id);
        return this;
    }

    public Titlebar setMiddleText(String text) {
        tv_middle.setText(text);
        return this;
    }

    public Titlebar setLeftText(String text) {
        tv_left.setText(text);
        return this;
    }

    public Titlebar setRightText(String text) {
        tv_right.setText(text);
        return this;
    }

    public Titlebar hideTitleBar() {
        setVisibility(View.GONE);
        return this;
    }

    public Titlebar showTitleBar() {
        setVisibility(View.VISIBLE);
        return this;
    }


    private Titlebar setCustomLayout(int layoutId) {
        View customView = LayoutInflater.from(getContext()).inflate(layoutId, null);
        this.mCustomView = customView;
        return this;
    }


    public Titlebar setLayoutParams(LayoutParams lp) {
        this.mLayoutParams = lp;
        return this;
    }

    public Titlebar build() {
        setCustomLayout(R.layout.csv_titlebar);
        addView(mCustomView, mLayoutParams);
        initViews();
//        initListener();
        return this;
    }

    public OnTitlebarLeftClickListener leftListener;
    public OnTitlebarMiddleClickListener middleListener;
    public OnTitlebarRightClickListener rightListener;
    public OnTitlebarClickListener listener;


    public interface OnTitlebarLeftClickListener {
        public void titlebarLeftClicked();
    }

    public interface OnTitlebarMiddleClickListener {
        public void titlebarMiddleClicked();
    }

    public interface OnTitlebarRightClickListener {
        public void titlebarRightClicked();
    }

    public interface OnTitlebarClickListener {
        public void titlebarRightClicked();

        public void titlebarLeftClicked();

    }

    public Titlebar setOnTitlebarLeftClickListener(OnTitlebarLeftClickListener listener) {
        leftListener = listener;
        initListener();
        return this;
    }

    public Titlebar setOnTitlebarRightClickListener(OnTitlebarRightClickListener listener) {
        rightListener = listener;
        initListener();
        return this;
    }

    public Titlebar setOnTitlebarMiddleClickListener(OnTitlebarMiddleClickListener listener) {
        middleListener = listener;
        initListener();
        return this;
    }

    public Titlebar setOnTitlebarClickListener(OnTitlebarClickListener listener) {
        this.listener = listener;
        initListener();
        return this;
    }

    private OnClickListener mLeftListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (leftListener != null) {
                leftListener.titlebarLeftClicked();
            }
            if (listener != null) {
                listener.titlebarLeftClicked();
            }
        }
    };

    private OnClickListener mMiddleListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (middleListener != null) {
                middleListener.titlebarMiddleClicked();
            }
        }
    };

    private OnClickListener mRightListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (rightListener != null) {
                rightListener.titlebarRightClicked();
            }
            if (listener != null) {
                listener.titlebarRightClicked();
            }
        }
    };


}
