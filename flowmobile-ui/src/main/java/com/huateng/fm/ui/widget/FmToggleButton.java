package com.huateng.fm.ui.widget;

import java.util.Observable;
import java.util.Observer;

import com.huateng.flowMobile.R;
import com.huateng.fm.app.FmAttributeValues;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.widget.ToggleButton;

/**
 * ToggleButton
 * 
 * @author dengzh
 */
public class FmToggleButton extends ToggleButton implements Observer{
	private final String TAG = getClass().getSimpleName();
	private FmAttributeValues mAttrValues;
	private Context context;
	private int padding = 0;
	private int ht2dp, ht3dp;
	private int mHeight = 0, mWidth = 0;
	private int mCornerStyle;
	public FmToggleButton(Context context) {
		super(context);
		this.context = context;
		init(null);
	}

	public FmToggleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init(attrs);
	}

	public FmToggleButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init(attrs);
	}

	private void initValues() {
		ht2dp = (int) context.getResources().getDimension(R.dimen.ht_2dp);
		ht3dp = (int) context.getResources().getDimension(R.dimen.ht_3dp);
	}

	

	private void init(AttributeSet attrs) {
		initValues();
		mAttrValues= new FmAttributeValues();
		mAttrValues.addObserver(this);
		if (attrs != null) {
			TypedArray a = getContext().obtainStyledAttributes(attrs,
					R.styleable.ht_ToggleButton);
			mCornerStyle = a.getInt(R.styleable.ht_ToggleButton_cornerStyle,
					FmAttributeValues.CornerStyle.ROUND);
			a.recycle();
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(resolveSize(FmAttributeValues.COMPOUNDBUTTON_DEFAULT_WIDTH, widthMeasureSpec)
				,resolveSize(FmAttributeValues.COMPOUNDBUTTON_DEFAULT_HEIGHT, heightMeasureSpec));
		initStates();
	}

	private void initStates() {
		mHeight = getMeasuredHeight();
		mWidth = getMeasuredWidth();
		setCornerStyleInner(mCornerStyle);

		GradientDrawable uncheckedEnabledFrontCore = new GradientDrawable();
		uncheckedEnabledFrontCore
				.setColor(FmAttributeValues.TOGGLE_BUTTON_THUMB_COLOR);
		uncheckedEnabledFrontCore.setStroke(1,
				getResources().getColor(R.color.gray_button_border));
		uncheckedEnabledFrontCore
				.setCornerRadius(mAttrValues.getOuterRadius()[0]);
		InsetDrawable uncheckedEnabledFront = new InsetDrawable(
				uncheckedEnabledFrontCore, ht3dp, mHeight / 12, mWidth
						- mHeight * 5 / 6 - 2 * ht3dp, mHeight / 12);
		
		GradientDrawable uncheckedEnabledBack = new GradientDrawable();
		uncheckedEnabledBack.setColor(FmAttributeValues.TOGGLE_BUTTON_BG_COLOR);
		uncheckedEnabledBack.setStroke(1,
				getResources().getColor(R.color.gray_button_border));
		uncheckedEnabledBack.setCornerRadius(mAttrValues.getOuterRadius()[0]);

		Drawable[] d1 = { uncheckedEnabledBack, uncheckedEnabledFront };
		LayerDrawable uncheckedEnabled = new LayerDrawable(d1);

		GradientDrawable checkedEnabledFrontCore = new GradientDrawable();
		checkedEnabledFrontCore
				.setColor(FmAttributeValues.TOGGLE_BUTTON_THUMB_COLOR);
		checkedEnabledFrontCore.setStroke(1,
				getResources().getColor(R.color.gray_button_border));
		checkedEnabledFrontCore.setCornerRadius(mAttrValues.getOuterRadius()[0]);
		InsetDrawable checkedEnabledFront = new InsetDrawable(
				uncheckedEnabledFrontCore,
				mWidth - mHeight * 5 / 6 - 2 * ht3dp, mHeight / 12, ht3dp,
				mHeight / 12);
		ShapeDrawable checkedEnabledBack = new ShapeDrawable(
				new RoundRectShape(mAttrValues.getOuterRadius(), null, null));
		checkedEnabledBack.getPaint().setColor(FmAttributeValues.PRIMARY_COLOR);

		Drawable[] d2 = { checkedEnabledBack, checkedEnabledFront };
		LayerDrawable checkedEnabled = new LayerDrawable(d2);

		ShapeDrawable uncheckedDisabledFrontCore = new ShapeDrawable(
				new RoundRectShape(mAttrValues.getOuterRadius(), null, null));
		uncheckedDisabledFrontCore.getPaint().setColor(
				FmAttributeValues.TOGGLE_BUTTON_THUMB_COLOR);
		InsetDrawable uncheckedDisabledFront = new InsetDrawable(
				uncheckedDisabledFrontCore,0);

		ShapeDrawable uncheckedDisabledBack = new ShapeDrawable(
				new RoundRectShape(mAttrValues.getOuterRadius(), null, null));
		uncheckedDisabledBack.getPaint().setColor(FmAttributeValues.TOGGLE_BUTTON_BG_COLOR);
		uncheckedDisabledBack.setPadding(ht3dp, ht3dp,
				mWidth - mHeight * 5 / 6- 2 * ht3dp, ht3dp);

		Drawable[] d3 = { uncheckedDisabledBack, uncheckedDisabledFront };
		LayerDrawable uncheckedDisabled = new LayerDrawable(d3);

		ShapeDrawable checkedDisabledFrontCore = new ShapeDrawable(
				new RoundRectShape(mAttrValues.getOuterRadius(), null, null));
		checkedDisabledFrontCore.getPaint().setColor(
				FmAttributeValues.TOGGLE_BUTTON_THUMB_COLOR);
		InsetDrawable checkedDisabledFront = new InsetDrawable(
				checkedDisabledFrontCore, padding);

		ShapeDrawable checkedDisabledBack = new ShapeDrawable(
				new RoundRectShape(mAttrValues.getOuterRadius(), null, null));
		checkedDisabledBack.getPaint().setColor(mAttrValues.getColor(3));
		checkedDisabledBack.getPaint().setAlpha(0xA0);

		checkedDisabledBack.setPadding(mWidth - mHeight * 5 / 6 - 2 * ht3dp,
				ht3dp, ht3dp, ht3dp);

		Drawable[] d4 = { checkedDisabledBack, checkedDisabledFront };
		LayerDrawable checkedDisabled = new LayerDrawable(d4);

		StateListDrawable states = new StateListDrawable();

		states.addState(new int[] { -android.R.attr.state_checked,
				android.R.attr.state_enabled }, uncheckedEnabled);
		states.addState(new int[] { android.R.attr.state_checked,
				android.R.attr.state_enabled }, checkedEnabled);
		states.addState(new int[] { -android.R.attr.state_checked,
				-android.R.attr.state_enabled }, uncheckedDisabled);
		states.addState(new int[] { android.R.attr.state_checked,
				-android.R.attr.state_enabled }, checkedDisabled);

		setBackgroundDrawable(states);

		// setText("");
		setTextOff("");
		setTextOn("");

		setTextSize(0);
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
			mAttrValues.setRadius(mHeight / 2);
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