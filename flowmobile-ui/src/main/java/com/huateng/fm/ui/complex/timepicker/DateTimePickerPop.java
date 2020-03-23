package com.huateng.fm.ui.complex.timepicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.huateng.flowMobile.R;
import com.huateng.fm.app.FmAttributeValues.CornerStyle;
import com.huateng.fm.core.util.FmScreenUtil;
import com.huateng.fm.ui.complex.timepicker.DateTimeView.Type;
import com.huateng.fm.ui.widget.FmButton;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Created by Devin
 * on 14-8-25.
 */
public class DateTimePickerPop extends PopupWindow {

	private final String TAG=getClass().getSimpleName();
    private Context context;
    private LinearLayout popView;
    private OnBtnClickListener onBtnClickListener;
    private int popWidth,headerHeight;
    private int popPadding, cancelBtnsize;
    private int bottomMarginValue,btnLayoutHeight,btnLayoutGap,btnHeight;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:ss");
	 private String titleString;
	 private Type mType;

    public DateTimePickerPop(Context context,String titleString,Type type) {
        super(context, null);
        this.context = context;
        this.titleString=titleString;
        this.mType=type;
        init();
    }

    public DateTimePickerPop(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }


    private void init() {
    	  Calendar.getInstance();
        headerHeight = FmScreenUtil.getScaleHeight(context, 0.08f);
        popWidth = FmScreenUtil.getScaleWidth(context, 0.85f);
        popPadding = FmScreenUtil.getScaleWidth(context, 0.065f);

        cancelBtnsize = FmScreenUtil.getScaleWidth(context, 0.22f);
        btnLayoutHeight = FmScreenUtil.getScaleHeight(context, 0.1f);
        btnLayoutGap = FmScreenUtil.getScaleWidth(context, 0.03f);
        btnHeight = FmScreenUtil.getScaleHeight(context, 0.07f);
        bottomMarginValue = FmScreenUtil.getScaleHeight(context, 0.037f);

        initPop();
    }

    private void initPop() {
    	LinearLayout rootView=new LinearLayout(context);
    	 LinearLayout.LayoutParams lp_rootView = new LinearLayout.LayoutParams(
                 LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    	 rootView.setLayoutParams(lp_rootView);
    	 popView = new LinearLayout(context);
    	 popView.setGravity(Gravity.CENTER);
       popView.setBackgroundColor(context.getResources().getColor(R.color.white));
       popView.setOrientation(LinearLayout.VERTICAL);
       LinearLayout.LayoutParams lp_popView = new LinearLayout.LayoutParams(
               LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
       lp_popView.gravity=Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL;
       popView.setPadding(popPadding, 0, popPadding, 0);
//       popView.setGravity(Gravity.BOTTOM);
       popView.setLayoutParams(lp_popView);
  	 rootView.addView(popView);
       setContentView(rootView);
       setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
       setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
       setFocusable(true);
       setOutsideTouchable(false);// 设置允许在外点击消失//

       ColorDrawable dw = new ColorDrawable(0xA0000000);
       this.setBackgroundDrawable(dw);
       mDateTimeView =new DateTimeView(context);
       LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
       mDateTimeView.setLayoutParams(params);
       mDateTimeView.setType(mType);
    }
    
    public void setType(Type type){
    	mDateTimeView.setType(type);
    }
    
    int mStartYear=1900;
    
    
	public DateTimePickerPop addLimit(int startYear,int startMonth,int startDay,int endYear){
		mDateTimeView.addLimit(startYear, startMonth, startDay, endYear);
		return this;
	}
    
    /**
     * 标题栏
     *
     * @param hasRightText
     * @return
     */
    private View createHeaderView(String title ) {
        LinearLayout rootView = new LinearLayout(context);
        rootView.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lp_rootView = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, headerHeight);
        rootView.setLayoutParams(lp_rootView);
        rootView.setGravity(Gravity.CENTER_VERTICAL);
//        rootView.setBackgroundResource(R.drawable.round_up_gray_bg);

        TextView tv_title = new TextView(context);
        LinearLayout.LayoutParams lp_textView = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        lp_textView.leftMargin = btnLayoutGap;
        tv_title.setLayoutParams(lp_textView);
        tv_title.setText(title);
        tv_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tv_title.setTextColor(context.getResources().getColor(R.color.black));
        rootView.addView(tv_title);
        return rootView;
    }
    
	public  DateTimePickerPop initData(int year,int month,int day,int hour,int min){
		mDateTimeView.initData(year, month, day, hour, min);
    	return this;
	}
    
    private View createLine() {
        LinearLayout linearLayout_line = new LinearLayout(context);
        LinearLayout.LayoutParams lp_line = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 1);
        linearLayout_line.setLayoutParams(lp_line);
        linearLayout_line.setBackgroundResource(R.color.gray_diliver);
        return linearLayout_line;
    }
    
    private int mLeftColorResId,mRightColorResId;
    private int mCornerStyle;
    
    public DateTimePickerPop setButtonStyle(int leftColorResId,int rightColorResId,int cornerStyle){
    	mLeftColorResId=leftColorResId;
    	mRightColorResId=rightColorResId;
    	mCornerStyle=cornerStyle;
    	return this;
    }
    
    public DateTimePickerPop setSeparator(String separator){
		mDateTimeView.setSeparator(separator);
    	return this;
	}
    
    /**
     * 底部按钮
     *
     * @param text1
     * @param text2
     * @return
     */
    private View createConfirmView(String text1, String text2) {

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, btnLayoutHeight);

        linearLayout.setLayoutParams(lp);
        linearLayout.setGravity(Gravity.CENTER);
//        linearLayout.setBackgroundResource(R.drawable.round_down_gray_bg);
        //确认键
        FmButton button1 = new FmButton(context);
        LinearLayout.LayoutParams lp_btn1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, btnHeight, 1);
        lp_btn1.leftMargin = btnLayoutGap;
        button1.setLayoutParams(lp_btn1);
        button1.setGravity(Gravity.CENTER);
        button1.setText(text1);
        button1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        button1.setTextColor(context.getResources().getColor(R.color.white));
        button1.setCornerStyle(mCornerStyle);
        button1.setBgColor(mLeftColorResId);
        //        button1.setBackgroundResource(R.drawable.round_zong_bg);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBtnClickListener != null) {
                    onBtnClickListener.onLeftBtnClick(mDateTimeView.getDateTime());
                   dismiss();
                }
            }
        });
        //取消键
        FmButton button2 = new FmButton(context);
        LinearLayout.LayoutParams lp_btn2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, btnHeight, 1);
        lp_btn2.leftMargin = btnLayoutGap;
        lp_btn2.rightMargin = btnLayoutGap;
        button2.setBgColor(mRightColorResId);
        button2.setLayoutParams(lp_btn2);
        button2.setGravity(Gravity.CENTER);
        button2.setCornerStyle(mCornerStyle);
        button2.setText(text2);
        button2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        button2.setTextColor(context.getResources().getColor(R.color.gray_textcolor));
//        button2.setBackgroundResource(R.drawable.round_gray_bg);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        linearLayout.addView(button1);
        linearLayout.addView(button2);
        return linearLayout;
    }
    
    private  DateTimeView mDateTimeView;

    public void show() {
    	popView.removeAllViews();
    	Log.i(TAG, "TimePickPop--show--");
        popView.addView(createHeaderView(titleString));
        popView.addView(createLine());
        popView.addView(mDateTimeView);
        popView.addView(createLine());

        popView.addView(createConfirmView("确认", "取消"));

        this.showAtLocation(((Activity)context).getWindow().getDecorView(), Gravity.BOTTOM, 0, 0); //设置layout在PopupWindow中显示的位置
        ScaleAnimation animation = new ScaleAnimation(0.5f, 1f, 0.5f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(300);
        animation.setFillAfter(true);
        popView.startAnimation(animation);
    }

    @Override
    public void dismiss() {
        ScaleAnimation animation = new ScaleAnimation(1f, 0f, 1f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(300);
        animation.setFillAfter(true);
        popView.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                DateTimePickerPop.super.dismiss();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void setOnBtnClickListener(OnBtnClickListener onBtnClickListener) {
        this.onBtnClickListener = onBtnClickListener;
    }


    public interface OnBtnClickListener {
        public void onLeftBtnClick(String timeStr);
    }


}
