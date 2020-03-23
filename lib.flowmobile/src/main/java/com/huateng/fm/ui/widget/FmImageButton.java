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
import android.widget.ImageButton;

/**
 * Button
 * 
 * @author Decheewar
 * 
 */
public class FmImageButton extends ImageButton implements Observer {
	
	private final String TAG = getClass().getSimpleName();
	private Context context;
	private FmAttributeValues mAttrValues;
	private boolean mHasShadow;
	private Drawable background = null;
	private int mCornerStyle;
	
	public FmImageButton(Context context) {
		super(context);
		this.context = context;
		init(null);
	}

	public FmImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init(attrs);
	}

	public FmImageButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init(attrs);
	}

	private void init(AttributeSet attrs) {
		mAttrValues= new FmAttributeValues();
		mAttrValues.addObserver(this);

		if (attrs != null) {
			TypedArray a = getContext().obtainStyledAttributes(attrs,
					R.styleable.ht_ImageButton);
			mCornerStyle = a.getInt(R.styleable.ht_ImageButton_cornerStyle,
					FmAttributeValues.CornerStyle.NONE);
			mHasShadow = a.getBoolean(R.styleable.ht_ImageButton_hasShadow,
					FmAttributeValues.hasShadow);

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

	private void initStates() {
		final int paddingTop = getPaddingTop();
		final int paddingRight = getPaddingRight();
		final int paddingLeft = getPaddingLeft();
		final int paddingBottom = getPaddingBottom();
		setCornerStyleInner(mCornerStyle);
		// 前景 normal Drawable
		ShapeDrawable normalDrawable = new ShapeDrawable(new RoundRectShape(
				mAttrValues.getOuterRadius(), null, null));
		normalDrawable.getPaint().setColor(FmAttributeValues.PRIMARY_COLOR);

		Drawable[] d = { normalDrawable };
		LayerDrawable drawable_normal = new LayerDrawable(d);

		// 前景 pressed Drawable
		ShapeDrawable pressedDrawable = new ShapeDrawable(
				new RoundRectShape(mAttrValues.getOuterRadius(), null, null));
		pressedDrawable.getPaint().setColor(FmAttributeValues.STATED_COLOR);


		Drawable[] d2 = { pressedDrawable };
		LayerDrawable drawable_pressed = new LayerDrawable(d2);

		// 前景 disabled dawable
		ShapeDrawable disabledDrawable = new ShapeDrawable(new RoundRectShape(
				mAttrValues.getOuterRadius(), null, null));
		disabledDrawable.getPaint().setColor(FmAttributeValues.PRIMARY_COLOR);
		disabledDrawable.getPaint().setAlpha(0xA0);

		Drawable[] d3 = { disabledDrawable };
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
		setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

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

	public void setCornerStyle(int cornerStyle) {
		this.mCornerStyle=cornerStyle;
		initStates();
	}

	@Override
	public void update(Observable observable, Object data) {
		initStates();		
	}
	
}
