package com.huateng.collection.widget;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.huateng.collection.R;

public class DeletableImageView extends ImageView{
	
	private Bitmap mBitmapP,mBitmap;
	private Paint mPaint;
	private Rect mSrcRect,mDesRect;
	
	public DeletableImageView(Context context) {
		super(context);
		init();
	}
	
	private void init(){
		mBitmapP=BitmapFactory.decodeResource(getResources(), R.drawable.delete);
		mBitmap=getResizedBitmap(mBitmapP, 40, 40);
		mPaint=new Paint();
		mPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_OVER));
		

	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		mSrcRect = new Rect(0, 0, getMeasuredWidth(),getMeasuredHeight());
		mDesRect = new Rect(getMeasuredWidth()-30, 0, getMeasuredWidth(),30);
		canvas.drawBitmap(mBitmap, getMeasuredWidth()-40, 0, mPaint);
//        canvas.drawBitmap(mBitmap, mDesRect, mDesRect, mPaint);

	}
	private OnDeleteTouchListener mListener;
	public interface OnDeleteTouchListener{
		public void onDeleteTouched();
	}
	
	public void setOnDeleteTouchListener(OnDeleteTouchListener listener){
		this.mListener=listener;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
	  
	    switch(event.getAction()){
	        case MotionEvent.ACTION_DOWN:
	        	  float x = event.getX();
	      	    float y = event.getY();
    		  if( x > getMeasuredWidth()-40 && x < getMeasuredWidth() &&  y < 40 ){
	        	if (mListener!=null) {
		        	mListener.onDeleteTouched();
				}
//	        	Toast.makeText(getContext(), "点击了OptionIcon", 0).show();
	        	return false;
		      }
	        break;
	    }
	    return  super.onTouchEvent(event);
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

}
