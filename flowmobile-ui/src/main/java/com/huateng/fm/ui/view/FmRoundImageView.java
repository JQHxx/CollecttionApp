
package com.huateng.fm.ui.view;

import com.huateng.flowMobile.R;
import com.huateng.fm.util.UiValuesUtil;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 圆形ImageView，可设置最多两个宽度不同且颜色不同的圆形边框
 * @author Devin
 * 2015年1月15日  下午5:03:03
 */
public class FmRoundImageView extends ImageView {
	private final String TAG=getClass().getSimpleName();
	
    private int mOutterBorderColor = 0; // 外边框颜色
    private int mInnerBorderColor = 0xffffffff; // 内边框颜色
    private int mOutterBorderWidth;  //外边框宽度
    private int mInnerBorderWidth;   //内边框宽度
    private Bitmap currentBitmap; // 当前显示的bitmap

    public FmRoundImageView(Context context) {
        super(context);
    }

    public FmRoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }
    

    public FmRoundImageView(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }
    
	private void init(AttributeSet attrs) {
    	TypedArray a = getContext().obtainStyledAttributes(attrs,
				R.styleable.ht_RoundImageView);
    	mOutterBorderWidth = (int) a.getDimension(R.styleable.ht_RoundImageView_outterBorderWidth,
				UiValuesUtil.getDimen(R.dimen.default_ht_round_imageview_outter_border_width));
    	mInnerBorderWidth = (int) a.getDimension(R.styleable.ht_RoundImageView_innerBorderWidth,
				UiValuesUtil.getDimen(R.dimen.default_ht_round_imageview_inner_border_width));
    	mOutterBorderColor=a.getColor(R.styleable.ht_RoundImageView_outterBorderColor,
    			UiValuesUtil.getColor(R.color.default_ht_round_imageview_outter_border_color));
    	mInnerBorderColor=a.getColor(R.styleable.ht_RoundImageView_innerBorderColor,
    			UiValuesUtil.getColor(R.color.default_ht_round_imageview_inner_border_color));
    	a.recycle();
    }

    private void initCurrentBitmap() {
        Bitmap temp = null;
        Drawable drawable = getDrawable();
        if (drawable instanceof BitmapDrawable) {
            temp = ((BitmapDrawable) drawable).getBitmap();
        }
        if (temp != null) {
            currentBitmap = temp;
        }
    }
    
    
    @Override
    protected void onDraw(Canvas canvas) {
        initCurrentBitmap();
        if (currentBitmap == null || getWidth() == 0
                || getHeight() == 0) {
            return;
        }
        this.measure(0, 0);
        int width = getWidth();
        int height = getHeight();
        int radius = 0; // 半径
        radius = (width < height ? width : height) / 2 - 
                (mInnerBorderWidth+mOutterBorderWidth);
        if (mInnerBorderWidth != 0 && mOutterBorderWidth != 0) { // 画两个边框
        	 // 画内圆
            drawCircleBorder(canvas, radius,width, height,
            		mInnerBorderColor,mInnerBorderWidth);
            // 画外圆
            drawCircleBorder(canvas, radius+mInnerBorderWidth
                   , width, height,mOutterBorderColor,mOutterBorderWidth);
        } else if (mOutterBorderWidth != 0
                && mInnerBorderWidth == 0) { // 画外圆
            drawCircleBorder(canvas, radius,
                    width, height, mOutterBorderColor,mOutterBorderWidth);
        } else if (mOutterBorderWidth == 0
                && mInnerBorderWidth != 0) {// 画内圆
            drawCircleBorder(canvas, radius,
                    width, height, mInnerBorderColor,mInnerBorderWidth);
        } else { // 没有边框
            radius = (width < height ? width : height) / 2;
        }
        Bitmap roundBitmap = getCroppedRoundBitmap(currentBitmap,
                radius);
        canvas.drawBitmap(roundBitmap, width / 2 - radius, height / 2
                - radius, null);
    }

    /**
     * 获取裁剪后的圆形图片
     * 
     * @param radius
     *            半径
     * @param bmp
     *            要被裁减的图片
     */
    public Bitmap getCroppedRoundBitmap(Bitmap bmp, int radius) {
        Bitmap scaledSrcBmp;
        int diameter = radius * 2;
        // 为了防止宽高不相等，造成圆形图片变形，因此截取长方形中处于中间位置最大的正方形图片
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();
        int squareWidth = 0, squareHeight = 0;
        int x = 0, y = 0;
        Bitmap squareBitmap;
        if (bmpHeight > bmpWidth) {// 高大于宽
            squareWidth = squareHeight = bmpWidth;
            x = 0;
            y = (bmpHeight - bmpWidth) / 2;
            // 截取正方形图片
            squareBitmap = Bitmap.createBitmap(bmp, x, y,
                    squareWidth, squareHeight);
        } else if (bmpHeight < bmpWidth) {// 宽大于高
            squareWidth = squareHeight = bmpHeight;
            x = (bmpWidth - bmpHeight) / 2;
            y = 0;
            squareBitmap = Bitmap.createBitmap(bmp, x, y,
                    squareWidth, squareHeight);
        } else {
            squareBitmap = bmp;
        }

        if (squareBitmap.getWidth() != diameter
                || squareBitmap.getHeight() != diameter) {
            scaledSrcBmp = Bitmap.createScaledBitmap(squareBitmap,
                    diameter, diameter, true);

        } else {
            scaledSrcBmp = squareBitmap;
        }
        Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(),
                scaledSrcBmp.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(),
                scaledSrcBmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(scaledSrcBmp.getWidth() / 2,
                scaledSrcBmp.getHeight() / 2,
                scaledSrcBmp.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);
        // 回收资源
        // bmp.recycle();
        // squareBitmap.recycle();
        // scaledSrcBmp.recycle();
        bmp = null;
        squareBitmap = null;
        scaledSrcBmp = null;
        return output;
    }

    /**
     * 边缘画圆
     */
    private void drawCircleBorder(Canvas canvas, int radius, int w,
            int h, int color,int borderWidth) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setColor(color);
        /* 设置paint的　style　为STROKE：空心 */
        paint.setStyle(Paint.Style.STROKE);
        /* 设置paint的外框宽度 */
        paint.setStrokeWidth(borderWidth);
        canvas.drawCircle(w / 2, h / 2, radius+borderWidth/2, paint);
    }

    /************************ 供外部调用的方法 ******************************/

    /**
     * 必须直接或间接被调用至少一次，也就是如果currentBitmap不赋初值，则不会显示图片
     */
    public void setBitmapRes(Bitmap bitmap) {
        currentBitmap = bitmap;
        invalidate();
    }
    
    @Override
    public void setImageDrawable(Drawable drawable) {
    	
    	super.setImageDrawable(drawable);
    	if (drawable!=null) {
    		setBitmapRes(drawable2Bitmap(drawable));
		}
    }
    
    private Bitmap drawable2Bitmap(Drawable drawable){
            // 取 drawable 的长宽   
            int w = drawable.getIntrinsicWidth();   
            int h = drawable.getIntrinsicHeight();   
       
            // 取 drawable 的颜色格式   
            Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888   
                    : Bitmap.Config.RGB_565;   
            // 建立对应 bitmap   
            Bitmap bitmap = Bitmap.createBitmap(w, h, config);   
            // 建立对应 bitmap 的画布   
            Canvas canvas = new Canvas(bitmap);   
            drawable.setBounds(0, 0, w, h);   
            // 把 drawable 内容画到画布中   
            drawable.draw(canvas);   
            return bitmap;   
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        setBitmapRes(bm);
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        setBitmapRes(BitmapFactory.decodeResource(getResources(),
                resId));
    }

   

    public int getOutterBorderWidth() {
		return mOutterBorderWidth;
	}

	public void setOutterBorderWidth(int outterBorderWidth) {
		this.mOutterBorderWidth = outterBorderWidth;
	}

	public int getInnerBorderWidth() {
		return mInnerBorderWidth;
	}

	public void setInnerBorderWidth(int innerBorderWidth) {
		this.mInnerBorderWidth = innerBorderWidth;
	}

	/**
     * 外圈颜色
     */
    public int getOutterBorderColor() {
        return mOutterBorderColor;
    }

    /**
     * 设置外圈颜色
     */
    public void setOutterBorderColor(int outterBorderColor) {
        this.mOutterBorderColor = outterBorderColor;
    }

    /**
     * 内圈颜色
     */
    public int getInnerBorderColor() {
        return mInnerBorderColor;
    }

    /**
     * 设置内圈颜色
     */
    public void setInnerBorderColor(int innerBorderColor) {
        this.mInnerBorderColor = innerBorderColor;
    }
}
