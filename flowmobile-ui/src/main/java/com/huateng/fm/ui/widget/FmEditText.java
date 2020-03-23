/*
 * Copyright 2015 by ShangHai Huateng Software Systems CO.,LTD.
 * 
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of Huateng
 * Corporation ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with Huateng Corporation.
 */
package com.huateng.fm.ui.widget;

import java.util.Observable;
import java.util.Observer;

import com.huateng.flowMobile.R;
import com.huateng.fm.app.FmAttributeValues;
import com.huateng.fm.util.UiValuesUtil;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.Toast;

/**
 * EditText
  * @author Decheewar
  *  2014-12-10 下午5:03:04
 */
public class FmEditText extends EditText implements Observer{

	private final String TAG = getClass().getSimpleName();
	private Context context;
	private FmAttributeValues mAttrValues;
	private String mLabelText;
	float baseLine2 = 0;
	float baseLine = 0;
	private float mLeftLabelSize, mRightLabelSize;
	private Drawable background = null;
	private int mCornerStyle;
	private int mLabelPadding;
	private int mLabelFixedPadding;
	private boolean mRequired;
	private int mRequireRightPadding,mOptionIconPadding;
	private  int mInitLeftPadding,mInitRightPadding,mInitTopPadding,mInitBottomPadding;
	private int optionIconResId;
	private Drawable mOptionIconDrawable;
	private int mOptionIconSize=0;
	private float  mOptionIconLeft,mOptionIconTop;
	private OnHtOptionTouchListener onHtOptionTouchListener;
	private int mBgColor;
	
	public FmEditText(Context context) {
		super(context);
		this.context = context;
		init(null);
	}

	public FmEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init(attrs);
	}

	public FmEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init(attrs);
	}
	
	private int mLabelLeftMargin,mLabelColor;

	private void init(AttributeSet attrs) {
		mAttrValues= new FmAttributeValues();
		mAttrValues.addObserver(this);
		
		mLabelLeftMargin=UiValuesUtil.getDimen(R.dimen.ht_widget_edittext_label_left_margin);
		if (attrs != null) {
			TypedArray a = getContext().obtainStyledAttributes(attrs,
					R.styleable.ht_EditText);
			mCornerStyle = a.getInt(R.styleable.ht_EditText_cornerStyle,
					FmAttributeValues.CornerStyle.ROUND);
			mLabelText = a.getString(R.styleable.ht_EditText_labelText);
			mLabelPadding=(int) a.getDimension(R.styleable.ht_EditText_labelPadding, FmAttributeValues.ht2dp);
			mRequired=a.getBoolean(R.styleable.ht_EditText_required, false);
			mOptionIconDrawable=a.getDrawable(R.styleable.ht_EditText_optionIcon);
			mBgColor=a.getColor(R.styleable.ht_EditText_bgColor, Color.WHITE);
			mLabelColor=a.getColor(R.styleable.ht_EditText_labelColor, getResources().getColor(R.color.gray_textcolor));

			a.recycle();
			
			int[] attrsArray = new int[] { android.R.attr.background };
			TypedArray b = getContext().obtainStyledAttributes(attrs,
					attrsArray);
			background = b.getDrawable(0);
			b.recycle();
		}
		setSingleLine(false);
		getLabelWidth();
		if (mRequired) {
			mRequireRightPadding=(int) getResources().getDimension(R.dimen.ht_widget_edittext_star_rightPadding);
		}
		if (mOptionIconDrawable!=null) {
			mOptionIconPadding=(int) getResources().getDimension(R.dimen.ht_widget_edittext_star_rightPadding);
			mOptionIconSize=UiValuesUtil.getDimen(R.dimen.ht_widget_edittext_option_icon_size);
		}
		mInitTopPadding = getPaddingTop();
		mInitRightPadding = getPaddingRight();
		mInitLeftPadding = getPaddingLeft();
		mInitBottomPadding = getPaddingBottom();
		setPadding(mLabelFixedPadding+mLabelPadding + mInitLeftPadding+2*FmAttributeValues.ht2dp, mInitTopPadding, mInitRightPadding + mRequireRightPadding+mOptionIconPadding+mOptionIconSize,
				mInitBottomPadding);
	}
	
	

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		initStates();
	}
	
	private void getLabelWidth(){
		if (mLabelText!=null) {
			mLabelFixedPadding = (int) (mLabelText.length() * getTextSize());
		}
	}
	public void setLabel(String labelText){
		this.mLabelText=labelText;
		invalidate();
		getLabelWidth();
		setPadding(mLabelFixedPadding+mLabelPadding + mInitLeftPadding+2*FmAttributeValues.ht2dp, getPaddingTop(), getPaddingRight(),
				getPaddingBottom());
	}
	
	public void setOptionIcon(int resId){
		this.optionIconResId=resId;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mLabelText!=null) {
			drawLabel(canvas);
		}
		if (mRequired) {
			drawStar(canvas);
		}
		if (mOptionIconDrawable!=null) {
			drawOptionIcon(canvas);
		}
		super.onDraw(canvas);

	}
	
	//label
	private void drawLabel(Canvas canvas){
		Paint textPaint = new Paint();
		textPaint.setColor(mLabelColor);
		textPaint.setTextSize(getTextSize());
		if (baseLine == 0) {
			FontMetrics metrics = textPaint.getFontMetrics();
			baseLine = getMeasuredHeight() / 2 + (metrics.bottom - metrics.top)
					/ 2 - metrics.bottom;
		}
		canvas.drawText(mLabelText, mLabelLeftMargin + getScrollX(), baseLine, textPaint);
	}
	
	//icon
	private void drawOptionIcon(Canvas canvas){
		Paint textPaint1 = new Paint();
		textPaint1.setTextSize(30);
		BitmapDrawable bd = (BitmapDrawable)mOptionIconDrawable;
		Bitmap bitmap = bd.getBitmap();
		Bitmap newBitmap=getResizedBitmap(bitmap,mOptionIconSize,mOptionIconSize);
		if (mRequired) {
			mOptionIconLeft=(getMeasuredWidth() - textPaint1.measureText("*") - 10)
					+ getScrollX()-mOptionIconSize-10;
		}else {
			mOptionIconLeft= getMeasuredWidth()
				+ getScrollX()-mOptionIconSize-10;
		}
		
		mOptionIconTop=(getMeasuredHeight()-mOptionIconSize)/2;
		canvas.drawBitmap(newBitmap, mOptionIconLeft, mOptionIconTop, null);
	}
	
	public interface OnHtOptionTouchListener{
		public void onHtOptionTouched();
	}
	
	public void setOnHtOptionTouchListener(OnHtOptionTouchListener onHtOptionTouchListener){
		this.onHtOptionTouchListener=onHtOptionTouchListener;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
	  
	    switch(event.getAction()){
	        case MotionEvent.ACTION_DOWN:
	        	  float x = event.getX();
	      	    float y = event.getY();
    		  if(mOptionIconDrawable!=null&& x > mOptionIconLeft && x < mOptionIconLeft + mOptionIconSize && y > mOptionIconTop && y < mOptionIconTop + mOptionIconSize ){
	        	if (onHtOptionTouchListener!=null) {
		        	onHtOptionTouchListener.onHtOptionTouched();
				}
	        	Toast.makeText(context, "点击了OptionIcon", 0).show();
	        	return false;
		      }
	        break;
	    }
	    return  super.onTouchEvent(event);
	}
	
	//star
	private void drawStar(Canvas canvas){
		Paint textPaint1 = new Paint();
		textPaint1.setTextSize(30);
		textPaint1.setColor(getResources().getColor(R.color.red));
		textPaint1.setTextAlign(Paint.Align.CENTER);
		if (baseLine2 == 0) {
			FontMetrics metrics = textPaint1.getFontMetrics();
			baseLine2 = getMeasuredHeight() / 2 + (metrics.bottom - metrics.top)
					/ 2 - metrics.bottom+ 2* FmAttributeValues.ht2dp;
		}
		canvas.drawText("*", getMeasuredWidth() - textPaint1.measureText("*") - 10
				+ getScrollX(), baseLine2, textPaint1);
	}


	private void initStates() {
		setCornerStyleInner(mCornerStyle);
		GradientDrawable drawable_pressed = new GradientDrawable();
		drawable_pressed.setCornerRadius(mAttrValues.getRadius());
		drawable_pressed.setSize(mAttrValues.getSize(), mAttrValues.getSize());
		drawable_pressed.setColor(mBgColor);
		drawable_pressed.setStroke(mAttrValues.getBorderWidth(),
				FmAttributeValues.PRIMARY_COLOR);

		GradientDrawable drawable_normal = new GradientDrawable();
		drawable_normal.setCornerRadius(mAttrValues.getRadius());
		drawable_normal.setSize(mAttrValues.getSize(), mAttrValues.getSize());
		drawable_normal.setColor(mBgColor);
		drawable_normal.setStroke(mAttrValues.getBorderWidth(),
				FmAttributeValues.GRAY_BORDER_COLOR);

		StateListDrawable states = new StateListDrawable();

		// states.addState(new int[] { android.R.attr.state_pressed,
		// android.R.attr.state_enabled }, drawable_pressed);
		states.addState(new int[] { android.R.attr.state_focused,
				android.R.attr.state_enabled }, drawable_pressed);
		states.addState(new int[] { -android.R.attr.state_enabled },
				drawable_normal);
		states.addState(new int[] { android.R.attr.state_enabled },
				drawable_normal);
		if (background == null) {
			setBackgroundDrawable(states);
		}
	}

	public void setCornerStyle(int cornerStyle){
		this.mCornerStyle=cornerStyle;
		initStates();
	}
	
	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);   
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
		return resizedBitmap;
	}
	
	private void setCornerStyleInner(int cornerStyle) {
		switch (cornerStyle) {
		case FmAttributeValues.CornerStyle.NONE:
			mAttrValues.setRadius(0);
			break;
		case FmAttributeValues.CornerStyle.SMALL:
			mAttrValues.setRadius(FmAttributeValues.SMALL_CORNER_RADIUS_DP);
			break;
		case FmAttributeValues.CornerStyle.ROUND:
			int height = (getMeasuredHeight()) / 2;
			mAttrValues.setRadius(height);
			break;

		default:
			break;
		}
	}

	@Override
	public void update(Observable observable, Object data) {
		initStates();		
	}
	
}
