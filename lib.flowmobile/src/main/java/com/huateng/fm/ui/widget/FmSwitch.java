package com.huateng.fm.ui.widget;

import java.util.Observable;
import java.util.Observer;

import com.huateng.flowMobile.R;
import com.huateng.fm.app.FmAttributeValues;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * author: Devin 
 * createTime:2015年7月20日
 *  desciprion:
 */
public class FmSwitch extends View implements Observer{
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG); // 抗锯齿
	private boolean isOn = false;
	private float curX = 0;
	private float centerY; // y固定
	private float viewWidth;
	private float radius;
	private float lineStart; // 直线段开始的位置（横坐标，即
	private float lineEnd; // 直线段结束的位置（纵坐标
	private float lineWidth;
	private final int SCALE = 4; // 控件长度为滑动的圆的半径的倍数
	private OnStateChangedListener onStateChangedListener;
	private FmAttributeValues mAttrValues;
	
	public FmSwitch(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();

	}

	public FmSwitch(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();

	}

	public FmSwitch(Context context) {
		super(context);
		init();
	}
	
	private void init(){
		mAttrValues= new FmAttributeValues();
		mAttrValues.addObserver(this);	
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		curX = event.getX();
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (curX > viewWidth / 2) {
				curX = lineEnd;
				if (false == isOn) {
					// 只有状态发生改变才调用回调函数， 下同
					if (onStateChangedListener!=null) {
						onStateChangedListener.onStateChanged(true);
					}
					isOn = true;
				}
			} else {
				curX = lineStart;
				if (true == isOn) {
					if (onStateChangedListener!=null) {
						onStateChangedListener.onStateChanged(false);
					}
						isOn = false;
				}
			}
		}
		postInvalidate();
		return true;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		/* 保持宽是高的SCALE / 2倍， 即圆的直径 */
		this.setMeasuredDimension(this.getMeasuredWidth(), this.getMeasuredWidth() * 2 / SCALE);
		viewWidth = this.getMeasuredWidth();
		radius = viewWidth / SCALE;
		lineWidth = radius * 2f; // 直线宽度等于滑块直径
		curX = radius;
		centerY = this.getMeasuredWidth() / SCALE; // centerY为高度的一半
		lineStart = radius;
		lineEnd = (SCALE - 1) * radius;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		/* 限制滑动范围 */
		curX = curX > lineEnd ? lineEnd : curX;
		curX = curX < lineStart ? lineStart : curX;
		/* 划线 */
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(lineWidth);
		/* 左边部分的线，蓝色 */
		mPaint.setColor(FmAttributeValues.PRIMARY_COLOR);
		canvas.drawLine(lineStart, centerY, curX, centerY, mPaint);
		/* 右边部分的线，灰色 */
		mPaint.setColor(getResources().getColor(R.color.gray_button_border));
		canvas.drawLine(curX, centerY, lineEnd, centerY, mPaint);
		/* 画圆 */
		/* 画最左和最右的圆，直径为直线段宽度， 即在直线段两边分别再加上一个半圆 */
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(getResources().getColor(R.color.gray_button_border));
		canvas.drawCircle(lineEnd, centerY, lineWidth / 2, mPaint);
		mPaint.setColor(FmAttributeValues.PRIMARY_COLOR);
		canvas.drawCircle(lineStart, centerY, lineWidth / 2, mPaint);
		/* 圆形滑块 */
		mPaint.setColor(Color.WHITE);
		canvas.drawCircle(curX, centerY, radius, mPaint);
	}

	/* 设置开关状态改变监听器 */
	public void setOnStateChangedListener(OnStateChangedListener o) {
		this.onStateChangedListener = o;
	}

	/* 内部接口，开关状态改变监听器 */
	public interface OnStateChangedListener {
		public void onStateChanged(boolean state);
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object data) {
		
	}
}
