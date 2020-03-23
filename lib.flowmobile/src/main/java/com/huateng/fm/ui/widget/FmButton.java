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

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Button
 * 
 * @author Devin
 * 
 */
public class FmButton extends Button implements Observer{
	
	private final String TAG = getClass().getSimpleName();
	private Context context;
	private FmAttributeValues mAttrValues;
	private Drawable background = null;
	private int mCornerStyle;
	private int mBgColor,mStateBgColor;
	
	public FmButton(Context context) {
		super(context);
		this.context = context;
		init(null);
	}

	public FmButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init(attrs);
	}

	public FmButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init(attrs);
	}

	private void init(AttributeSet attrs) {
		mAttrValues= new FmAttributeValues();
		mAttrValues.addObserver(this);
		if (attrs != null) {
			TypedArray a = getContext().obtainStyledAttributes(attrs,
					R.styleable.ht_Button);
			mCornerStyle = a.getInt(R.styleable.ht_Button_cornerStyle,
					FmAttributeValues.CornerStyle.NONE);
			mBgColor=a.getColor(R.styleable.ht_Button_bgColor, FmAttributeValues.PRIMARY_COLOR);
			mStateBgColor=a.getColor(R.styleable.ht_Button_stateBgColor, FmAttributeValues.STATED_COLOR);
			a.recycle();

			int[] attrsArray = new int[] { android.R.attr.background };
			TypedArray b = getContext().obtainStyledAttributes(attrs,
					attrsArray);
			background = b.getDrawable(0);
			b.recycle();
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		initStates();
	}

	@SuppressWarnings("deprecation")
	private void initStates() {  
		setCornerStyleInner(mCornerStyle);
		// 前景 normal Drawable
		ShapeDrawable foreground_normal = new ShapeDrawable(new RoundRectShape(
				mAttrValues.getOuterRadius(), null, null));
		foreground_normal.getPaint().setColor(mBgColor);

		Drawable[] d = { foreground_normal };
		LayerDrawable drawable_normal = new LayerDrawable(d);

		// 前景 pressed Drawable
		ShapeDrawable foreground_pressed = new ShapeDrawable(
				new RoundRectShape(mAttrValues.getOuterRadius(), null, null));
		foreground_pressed.getPaint().setColor(mStateBgColor);

		Drawable[] d2 = {foreground_pressed };
		LayerDrawable drawable_pressed = new LayerDrawable(d2);

		// 前景 disabled dawable
		ShapeDrawable disabledFront = new ShapeDrawable(new RoundRectShape(
				mAttrValues.getOuterRadius(), null, null));
		disabledFront.getPaint().setColor(FmAttributeValues.SPECIAL_COLOR);
		disabledFront.getPaint().setAlpha(0xA0);

		ShapeDrawable disabledBack = new ShapeDrawable(new RoundRectShape(
				mAttrValues.getOuterRadius(), null, null));
		disabledBack.getPaint().setColor(mBgColor);

		Drawable[] d3 = { disabledBack, disabledFront };
		LayerDrawable drawable_disabled = new LayerDrawable(d3);
		StateListDrawable states = new StateListDrawable();

		states.addState(new int[] { android.R.attr.state_pressed,
				android.R.attr.state_enabled }, drawable_pressed);
		states.addState(new int[] { android.R.attr.state_focused,
				android.R.attr.state_enabled }, drawable_pressed);
		states.addState(new int[] { android.R.attr.state_enabled },
				drawable_normal);
		states.addState(new int[] { -android.R.attr.state_enabled },
				drawable_disabled);
		if (background == null) {
			setBackgroundDrawable(states);
		}
		// }

	}
	
	public void setCornerStyle(int cornerStyle){
		this.mCornerStyle=cornerStyle;
		initStates();
	}
	
	public void setBgColor(int colorResId){
		mBgColor=getResources().getColor(colorResId);
		initStates();
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
