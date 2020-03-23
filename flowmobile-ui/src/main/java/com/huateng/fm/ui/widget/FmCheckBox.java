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
import com.huateng.fm.util.FmImageUtil;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.CheckBox;

/**
 * CheckBox
 * 
 * @author Decheewar
 */
public class FmCheckBox extends CheckBox implements Observer{
	private final String TAG=getClass().getSimpleName();
	private FmAttributeValues mAttrValues;
	private Context context;
	private int mHeight, mWidth;
	private int mDotMargin;
	private int mCornerStyle, mCheckStyle;
	private Drawable mCheckedDrawable,mUnCheckedDrawable;
	private int mBorderColor;
	
	public static class CheckStyle {
		public static final int HOOK = 0x1;
		public static final int DOT = 0x2;
	}

	public FmCheckBox(Context context) {
		super(context);
		this.context = context;
		init(null);
	}

	public FmCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init(attrs);
	}

	public FmCheckBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init(attrs);
	}

	private void init(AttributeSet attrs) {
		initValues();

		mAttrValues= new FmAttributeValues();
		mAttrValues.addObserver(this);
		if (attrs != null) {
			TypedArray a = getContext().obtainStyledAttributes(attrs,
					R.styleable.ht_CheckBox);
			mAttrValues.setSize(a.getDimensionPixelSize(
					R.styleable.ht_CheckBox_totalSize,
					FmAttributeValues.DEFAULT_SIZE_PX));
			Log.i("111", "mAttrValues.getDimensionPixelSize():"+a.getDimensionPixelSize(
					R.styleable.ht_CheckBox_totalSize,
					0));
			Log.i("111", "mAttrValues.getSize():"+mAttrValues.getSize());
			mCornerStyle = a.getInt(R.styleable.ht_CheckBox_cornerStyle,
					FmAttributeValues.CornerStyle.ROUND);
			mDotMargin = a.getDimensionPixelSize(
					R.styleable.ht_CheckBox_dotMargin, mDotMargin);
			mCheckStyle = a.getInt(R.styleable.ht_CheckBox_checkStyle,
					CheckStyle.HOOK);
			mBorderColor=a.getColor(R.styleable.ht_CheckBox_borderColor, FmAttributeValues.GRAY_BORDER_COLOR);
			mCheckedDrawable=a.getDrawable(R.styleable.ht_CheckBox_checkedDrawable);
			mUnCheckedDrawable=a.getDrawable(R.styleable.ht_CheckBox_unCheckedDrawable);
			if (mCheckedDrawable!=null) {
				mCheckedDrawable=FmImageUtil.modifyDrawableSize(mCheckedDrawable,mAttrValues.getSize(),mAttrValues.getSize());
			}
			if (mUnCheckedDrawable!=null) {
				Log.i(TAG, "mUnCheckedDrawable.getIntrinsicWidth()--1--:"+mUnCheckedDrawable.getIntrinsicWidth()
				+"--mAttrValues.getSize():"+mAttrValues.getSize());

				mUnCheckedDrawable=FmImageUtil.modifyDrawableSize(mUnCheckedDrawable,mAttrValues.getSize(),mAttrValues.getSize());
				Log.i(TAG, "mUnCheckedDrawable.getIntrinsicWidth()---2--:"+mUnCheckedDrawable.getIntrinsicWidth());

			}
			a.recycle();
		}
	}
	
	
	

	private void initValues() {
		mDotMargin = (int) getResources().getDimension(
				R.dimen.ht_widget_dot_margin);
	}

	private void initStates() {
		mHeight = getMeasuredHeight();
		mWidth = getMeasuredWidth();
		setCornerStyleInner(mCornerStyle);
		mAttrValues.setBorderWidth(FmAttributeValues.ht2dp);

		Log.i("111", "mAttrValues.getSize():"+mAttrValues.getSize());
		
		Drawable[] checkedEnabledDrawable = {getCheckedOutSideDrawable(), getCheckedInSideDrawable() };
		LayerDrawable checkedEnabled = new LayerDrawable(checkedEnabledDrawable);

		Drawable[] checkedDisabledDrawable = { getCheckedOutSideDisabledDrawable(),
				getCheckedInSideDisabledDrawable() };
		LayerDrawable checkedDisabled = new LayerDrawable(
				checkedDisabledDrawable);

		StateListDrawable states = new StateListDrawable();
		if (mUnCheckedDrawable!=null) {
			states.addState(new int[] { -android.R.attr.state_checked,
					android.R.attr.state_enabled },mUnCheckedDrawable);
		}else {
			states.addState(new int[] { -android.R.attr.state_checked,
					android.R.attr.state_enabled }, getUnCheckedEnabledDrawable());
		}
		
		if (mCheckedDrawable!=null) {
			states.addState(new int[] { android.R.attr.state_checked,
					android.R.attr.state_enabled }, mCheckedDrawable);
		}else {
			states.addState(new int[] { android.R.attr.state_checked,
					android.R.attr.state_enabled }, checkedEnabled);
		}
		
		
		states.addState(new int[] { -android.R.attr.state_checked,
				-android.R.attr.state_enabled }, getUnCheckedDisabledDrawable());
		states.addState(new int[] { android.R.attr.state_checked,
				-android.R.attr.state_enabled }, checkedDisabled);
		setButtonDrawable(states);
		if (getText() != null) {
			setPadding(mDotMargin, 0, 0, 0);
		}
//		setTextColor(FmAttributeValues.PRIMARY_COLOR);
	}
	
	private Drawable getUnCheckedEnabledDrawable(){
		GradientDrawable uncheckedEnabled = new GradientDrawable();
		uncheckedEnabled.setCornerRadius(mAttrValues.getRadius());
		uncheckedEnabled.setSize(mAttrValues.getSize(), mAttrValues.getSize());
		uncheckedEnabled.setColor(Color.TRANSPARENT);
		uncheckedEnabled.setStroke(mAttrValues.getBorderWidth(),
				mBorderColor);
		return uncheckedEnabled;
	}
	
	private Drawable getUnCheckedDisabledDrawable(){
		GradientDrawable uncheckedDisabled = new GradientDrawable();
		uncheckedDisabled.setCornerRadius(mAttrValues.getRadius());
		uncheckedDisabled.setSize(mAttrValues.getSize(), mAttrValues.getSize());
		uncheckedDisabled.setColor(Color.TRANSPARENT);
		uncheckedDisabled.setStroke(mAttrValues.getBorderWidth(),
				mAttrValues.getColor(3));
		return uncheckedDisabled;
	}
	
	private Drawable getCheckedOutSideDisabledDrawable(){
		GradientDrawable checkedOutsideDisabled = new GradientDrawable();
		checkedOutsideDisabled.setCornerRadius(mAttrValues.getRadius());
		checkedOutsideDisabled.setSize(mAttrValues.getSize(),
				mAttrValues.getSize());
		checkedOutsideDisabled.setColor(Color.TRANSPARENT);
		checkedOutsideDisabled.setStroke(mAttrValues.getBorderWidth(),
				mAttrValues.getColor(3));
		return checkedOutsideDisabled;
	}
	
	private Drawable getCheckedInSideDisabledDrawable(){
		PaintDrawable checkedCoreDisabled = new PaintDrawable(
				mAttrValues.getColor(3));
		checkedCoreDisabled.setCornerRadius(mAttrValues.getRadius());
		checkedCoreDisabled.setIntrinsicHeight(mAttrValues.getSize());
		checkedCoreDisabled.setIntrinsicWidth(mAttrValues.getSize());
		InsetDrawable checkedInsideDisabled = new InsetDrawable(
				checkedCoreDisabled, mAttrValues.getBorderWidth() + mDotMargin,
				mAttrValues.getBorderWidth() + mDotMargin,
				mAttrValues.getBorderWidth() + mDotMargin,
				mAttrValues.getBorderWidth() + mDotMargin);
		return checkedInsideDisabled;
	}
	
	private Drawable getCheckedOutSideDrawable(){
		GradientDrawable checkedOutside  = new GradientDrawable();
		checkedOutside.setCornerRadius(mAttrValues.getRadius());
		checkedOutside.setSize(mAttrValues.getSize(), mAttrValues.getSize());
		if (mCheckStyle == CheckStyle.HOOK) {
			checkedOutside.setColor(FmAttributeValues.PRIMARY_COLOR);
		}else {
			checkedOutside.setColor(Color.WHITE);
			checkedOutside.setStroke(mAttrValues.getBorderWidth(),
					mBorderColor);
		}
		return checkedOutside;
	}
	
	private Drawable getCheckedInSideDrawable(){
		Drawable checkedInside;

		if (mCheckStyle == CheckStyle.HOOK) {
			int length = mAttrValues.getSize();
			Paint paint = new Paint();
			paint.setColor(getResources().getColor(R.color.white));
			paint.setStrokeWidth(FmAttributeValues.ht2dp);
			paint.setAntiAlias(true);
			float[] pts = { length*0.2f, length * 1 / 2, length *0.45f, length * 3 / 4,
					length *0.45f, length * 3 / 4,
					(float) (length - length*0.2),
					length/3 };
			Bitmap bitmap = Bitmap.createBitmap(length, length,
					Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);
			canvas.drawColor(Color.TRANSPARENT);
			canvas.drawLines(pts, paint);

			checkedInside = new BitmapDrawable(getResources(), bitmap);

		} else {
			PaintDrawable checkedCore = new PaintDrawable(
					FmAttributeValues.PRIMARY_COLOR);
			checkedCore.setCornerRadius(mAttrValues.getRadius());
			checkedCore.setIntrinsicHeight(mAttrValues.getSize());
			checkedCore.setIntrinsicWidth(mAttrValues.getSize());

			checkedInside = new InsetDrawable(checkedCore,
					mAttrValues.getBorderWidth() + mDotMargin,
					mAttrValues.getBorderWidth() + mDotMargin,
					mAttrValues.getBorderWidth() + mDotMargin,
					mAttrValues.getBorderWidth() + mDotMargin);
		}
		return  checkedInside;

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		initStates();
	}

	public void setCornerStyle(int cornerStyle){
		this.mCornerStyle=cornerStyle;
		initStates();
	}
	
	private void setCornerStyleInner(int cornerStyle) {
		switch (cornerStyle) {
		case FmAttributeValues.CornerStyle.NONE:
			mAttrValues.setRadius(0.01f);
			break;
		case FmAttributeValues.CornerStyle.SMALL:
			mAttrValues.setRadius(FmAttributeValues.SMALL_CORNER_RADIUS_DP);
			break;
		case FmAttributeValues.CornerStyle.ROUND:
			mAttrValues.setRadius(mHeight / 2);
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
