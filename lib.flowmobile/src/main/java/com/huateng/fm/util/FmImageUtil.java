package com.huateng.fm.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
  * author: Devin
  * createTime:2015年8月3日
  * desciprion:
  */
public class FmImageUtil {
	@SuppressWarnings("deprecation")
	public static Drawable modifyDrawableSize(Drawable drawable, int w, int h) {
		int width = drawable.getIntrinsicWidth(); 
		int height = drawable.getIntrinsicHeight(); 
		 Bitmap oldbmp = drawableToBitmap(drawable);
		 Matrix matrix = new Matrix(); 
		float sx = ((float) w / width);
		float sy = ((float) h / height); 
		matrix.postScale(sx, sy); 
		Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
			matrix, true); 
		return new BitmapDrawable(newbmp); 
	}
	
	public static Bitmap drawableToBitmap(Drawable drawable) {
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight(); 
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 
				 : Bitmap.Config.RGB_565; 
		 Bitmap bitmap = Bitmap.createBitmap(w, h, config); 
		 Canvas canvas = new Canvas(bitmap); 
		 drawable.setBounds(0, 0, w, h); 
		 drawable.draw(canvas); 
		return bitmap; 
	}
}
