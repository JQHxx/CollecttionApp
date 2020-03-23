/*
 * Copyright 2015 by ShangHai Huateng Software Systems CO.,LTD.
 * 
 * All rights reserved.
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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.RatingBar;

/**
 *  RatingBar
 *  支持直接换星，设间距，星星大小
  * @author devin
  *  2014-12-5 下午1:10:09
 */
public class FmRatingBar extends RatingBar implements Observer{
	private final String TAG=getClass().getSimpleName();
	private Bitmap mStarBitmap;
	private FmAttributeValues mAttrValues;
	private Context context;
	private int mRadius;
	private int mSpace;
	private Drawable mThumbDrawable;
	private Drawable mBgDrawable;
	private int mThumbColor,mBgColor;
		
	public FmRatingBar(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		init(attrs);
	}
	
	private void init(AttributeSet attrs) {
		mAttrValues= new FmAttributeValues();
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs,R.styleable.ht_RatingBar);
            mSpace=a.getDimensionPixelSize(R.styleable.ht_RatingBar_space,FmAttributeValues.RATINGBAR_DEFAULT_SPACE);
            mRadius=a.getDimensionPixelSize(R.styleable.ht_RatingBar_radius,FmAttributeValues.RATINGBAR_DEFAULT_RADIUS);
            mThumbDrawable=a.getDrawable(R.styleable.ht_RatingBar_thumbDrawable);
            mBgDrawable=a.getDrawable(R.styleable.ht_RatingBar_bgDrawable);
            mBitmapSize=a.getDimensionPixelSize(R.styleable.ht_RatingBar_size,FmAttributeValues.RATINGBAR_DEFAULT_SIZE);
            mThumbColor=a.getColor(R.styleable.ht_RatingBar_thumbColor, FmAttributeValues.PRIMARY_COLOR);
            mBgColor=a.getColor(R.styleable.ht_RatingBar_bgColor, FmAttributeValues.GRAY_BORDER_COLOR);
            a.recycle();
        }
        Log.i(TAG, "init--mThumbDrawable:"+mThumbDrawable);
        if (mThumbDrawable!=null&&mBgDrawable!=null) {
        	setProgressDrawable(createOverallDrawableWithDrawable());
        	
		}else {
			setProgressDrawable(createOverallDrawableWithColor(mThumbColor,mBgColor));
		}
	 }

	@SuppressLint("NewApi")
	@Override
	protected synchronized void onMeasure(int widthMeasureSpec,
			int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (mStarBitmap != null) {
			final int width = mStarBitmap.getWidth() * getNumStars();
			final int height = mStarBitmap.getHeight() ;
			Log.i(TAG, "onMeasure-height:"+height);
			setMeasuredDimension(
					resolveSizeAndState(width, widthMeasureSpec, 0),
					resolveSizeAndState(height, heightMeasureSpec, 0));
		}
	}

	protected LayerDrawable createOverallDrawableWithDrawable() {
		Log.i(TAG, "createOverallDrawableWithDrawable--");
		Drawable backgroundDrawable = createBackgroundWithDrawable();
		Drawable progressDrawable = createProgressWithDrawable();
		LayerDrawable layerDrawable = new LayerDrawable(new Drawable[] {
				backgroundDrawable, backgroundDrawable,
				progressDrawable });
		layerDrawable.setId(0, android.R.id.background);
		layerDrawable.setId(1, android.R.id.secondaryProgress);
		layerDrawable.setId(2, android.R.id.progress);
		return layerDrawable;
	}
	
	protected LayerDrawable createOverallDrawableWithColor(int thumbColor,int bgColor) {
		 Drawable backgroundDrawable = createBackgroundWithColor(bgColor);
		 Drawable progressDrawable = createProgressWithColor(thumbColor);

		 LayerDrawable layerDrawable = new LayerDrawable(new Drawable[] {
				backgroundDrawable, backgroundDrawable,
				progressDrawable });
		layerDrawable.setId(0, android.R.id.background);
		layerDrawable.setId(1, android.R.id.secondaryProgress);
		layerDrawable.setId(2, android.R.id.progress);
		return layerDrawable;
	}

	/**
	 * 根据指定图画背景
	 * @return
	 */
	protected Drawable createBackgroundWithDrawable() {
		Log.i(TAG, "createBackgroundWithDrawable--");

		if (mBgDrawable==null) {
			throw new RuntimeException("background must not be empty");
		}
		final Bitmap tileBitmap = drawableToBitmap(mBgDrawable);
		if (mStarBitmap == null) {
			mStarBitmap = tileBitmap;
		}
		final ShapeDrawable shapeDrawable = new ShapeDrawable(
				getDrawableShape());
		final BitmapShader bitmapShader = new BitmapShader(tileBitmap,
				Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
		shapeDrawable.getPaint().setShader(bitmapShader);
		return shapeDrawable;
	}
	
	

	/**
	 * 根据指定图画进度
	 * @return
	 */
	protected Drawable createProgressWithDrawable() {
		if (mThumbDrawable==null) {
			throw new RuntimeException("thumb drawable must not be empty");
		}
		final Bitmap tileBitmap = drawableToBitmap(mThumbDrawable);
		final ShapeDrawable shapeDrawable = new ShapeDrawable(
				getDrawableShape());
		final BitmapShader bitmapShader = new BitmapShader(tileBitmap,
				Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
		shapeDrawable.getPaint().setShader(bitmapShader);
		return new ClipDrawable(shapeDrawable, Gravity.LEFT,
				ClipDrawable.HORIZONTAL);
	}
	
	/**
	 * 根据色值画进度
	 * @param color
	 * @return
	 */
	protected Drawable createProgressWithColor(int color) {
		final Bitmap tileBitmap = drawStar(color);
		final ShapeDrawable shapeDrawable = new ShapeDrawable(
				getDrawableShape());
		final BitmapShader bitmapShader = new BitmapShader(tileBitmap,
				Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
		shapeDrawable.getPaint().setShader(bitmapShader);
		return new ClipDrawable(shapeDrawable, Gravity.LEFT,
				ClipDrawable.HORIZONTAL);
	}
	
	/**
	 * 根据色值画背景
	 * @param color
	 * @return
	 */
	protected Drawable createBackgroundWithColor(int color) {
//		final Bitmap tileBitmap = drawStar(FmAttributeValues.GRAY_BORDER_COLOR);

		final Bitmap tileBitmap = drawStar(color);
		if (mStarBitmap == null) {
			mStarBitmap = tileBitmap;
		}
		final ShapeDrawable shapeDrawable = new ShapeDrawable(
				getDrawableShape());
		final BitmapShader bitmapShader = new BitmapShader(tileBitmap,
				Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
		shapeDrawable.getPaint().setShader(bitmapShader);
		return shapeDrawable;
	}

	private Shape getDrawableShape() {
		final float[] roundedCorners = new float[] { 5, 5, 5, 5, 5, 5, 5, 5 };
		return new RoundRectShape(roundedCorners, null, null);
	}

	public  Bitmap drawableToBitmap(Drawable drawable) {
//		if (drawable instanceof BitmapDrawable) {
//			return ((BitmapDrawable) drawable).getBitmap();
//		}
//		int width = drawable.getIntrinsicWidth();
//		width = width > 0 ? width : 1;
//		int height = drawable.getIntrinsicHeight();
//		height = height > 0 ? height : 1;
		final Bitmap bitmap = Bitmap.createBitmap(mBitmapSize, mBitmapSize,
				Bitmap.Config.ARGB_8888);
		final Canvas canvas = new Canvas(bitmap);
		Log.i(TAG, "mSize:"+mBitmapSize+"===canvas.getWidth():"+canvas.getWidth());

		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);
		return bitmap;
	}
	
	private int mBitmapSize;
	
	 /**
		 * 角度转弧度
		 * @param degree
		 * @return
		 */
	private float degree2Radian(float degree){
		return (float) (Math.PI * degree / 180);
	}
	    
	/**
	 * 画星
	 * @param color
	 * @return
	 */
    private Bitmap drawStar(int color){
    	Paint paint = new Paint();
		paint.setColor(color);
		paint.setAntiAlias(true);
		Path path = new Path(); 
		float radian = degree2Radian(36f);
		float radius_in = (float) ((mRadius) * Math.sin(radian / 2) / Math.cos(radian)); //中间五边形的半径
        
		path.moveTo((float) (mRadius * Math.cos(radian / 2)+mSpace/2), 0);
		path.lineTo((float) (mRadius * Math.cos(radian / 2)+mSpace/2 + radius_in * Math.sin(radian)), (float) (mRadius - mRadius * Math.sin(radian / 2)));
		path.lineTo((float) (mRadius * Math.cos(radian / 2) * 2+mSpace/2), (float) (mRadius - mRadius * Math.sin(radian / 2)));
		path.lineTo((float) (mRadius * Math.cos(radian / 2) +mSpace/2+ radius_in * Math.cos( radian /2)),(float) (mRadius + radius_in * Math.sin( radian /2)));
		path.lineTo((float) (mRadius * Math.cos(radian / 2)+mSpace/2 + mRadius * Math.sin(radian)), (float) (mRadius + mRadius * Math.cos(radian)));
		path.lineTo((float) (mRadius * Math.cos(radian / 2)+mSpace/2), (float) (mRadius + radius_in));
		path.lineTo((float) (mRadius * Math.cos(radian / 2)+mSpace/2 - mRadius * Math.sin(radian)), (float) (mRadius + mRadius * Math.cos(radian)));
		path.lineTo((float) (mRadius * Math.cos(radian / 2)+mSpace/2 - radius_in * Math.cos( radian /2)), (float) (mRadius + radius_in * Math.sin(radian / 2)));
		path.lineTo(mSpace/2, (float) (mRadius - mRadius * Math.sin(radian /2)));
		path.lineTo((float) (mRadius * Math.cos(radian / 2)+mSpace/2 - radius_in * Math.sin(radian)) , (float) (mRadius - mRadius * Math.sin(radian /2)));
		
		path.close();
		Bitmap bitmap = Bitmap.createBitmap(2*mRadius+mSpace, 2*mRadius+mSpace, Bitmap.Config.ARGB_8888);
		Canvas canvas=new Canvas(bitmap);
		canvas.drawPath(path, paint); 
		return bitmap;
    }
    
	@Override
	public void update(Observable observable, Object data) {
		//TODO
	}
}
