package com.huateng.fm.ui.view;

import com.huateng.flowMobile.R;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;


/** 
 * 圆形加载进度条 
 *  
 * @author way 
 *  
 */  
public class FmCircleLoadingView extends View {  
  
    private final Paint paint;  
    private final Context context;  
    private Resources res;  
    private int max = 100;  
    private int progress = 0;  
    private int ringWidth;  
    // 圆环的颜色  
    private int ringColor;  
    // 进度条颜色  
    private int progressColor;  
    // 字体颜色  
    private int textColor;  
    // 字的大小  
    private int textSize;  
  
    private String textProgress;  
    
    //半径
    private int radius;
  
    public FmCircleLoadingView(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        this.context = context;  
        paint = new Paint();  
        res = context.getResources();  
        paint.setAntiAlias(true); // 消除锯齿  
        init(attrs);
    }  
    
    private void init(AttributeSet attrs){
    	 TypedArray a = getContext().obtainStyledAttributes(attrs,
 				R.styleable.FmCircleLoadingView);
 		radius = (int) a.getDimension(R.styleable.FmCircleLoadingView_radius,
 				30);
 		ringWidth=(int) a.getDimension(R.styleable.FmCircleLoadingView_ringWidth,
 				3);
 		textColor=(int) a.getColor(R.styleable.FmCircleLoadingView_textColor,
 				Color.BLACK);
 		progressColor=(int) a.getColor(R.styleable.FmCircleLoadingView_progressColor,
 				Color.WHITE);
 		textSize=(int) a.getDimension(R.styleable.FmCircleLoadingView_textSize,
 				18);
 		ringColor=a.getColor(R.styleable.FmCircleLoadingView_ringColor, Color.BLACK); 		a.recycle();
    }
  
    public FmCircleLoadingView(Context context, AttributeSet attrs) {  
        this(context, attrs, 0);  
    }  
  
    public FmCircleLoadingView(Context context) {  
        this(context, null);  
    }  
  
    /** 
     * 设置进度条最大值 
     *  
     * @param max 
     */  
    public synchronized void setMax(int max) {  
        if (max < 0)  
            max = 0;  
        if (progress > max)  
            progress = max;  
        this.max = max;  
    }  
  
    /** 
     * 获取进度条最大值 
     *  
     * @return 
     */  
    public synchronized int getMax() {  
        return max;  
    }  
  
    /** 
     * 设置加载进度，取值范围在0~之间 
     *  
     * @param progress 
     */  
    public synchronized void setProgress(int progress) {  
        if (progress >= 0 && progress <= max) {  
            this.progress = progress;  
            invalidate();  
        }  
    }  
  
    /** 
     * 获取当前进度值 
     *  
     * @return 
     */  
    public synchronized int getProgress() {  
        return progress;  
    }  
  
    /** 
     * 设置圆环背景色 
     *  
     * @param ringColor 
     */  
    public void setRingColor(int ringColor) {  
        this.ringColor = res.getColor(ringColor);  
    }  
  
    /** 
     * 设置进度条颜色 
     *  
     * @param progressColor 
     */  
    public void setProgressColor(int progressColor) {  
        this.progressColor = res.getColor(progressColor);  
    }  
  
    /** 
     * 设置字体颜色 
     *  
     * @param textColor 
     */  
    public void setTextColor(int textColor) {  
        this.textColor = res.getColor(textColor);  
    }  
  
    /** 
     * 设置字体大小 
     *  
     * @param textSize 
     */  
    public void setTextSize(int textSize) {  
        this.textSize = textSize;  
    }  
  
    /** 
     * 设置圆环半径 
     *  
     * @param ringWidth 
     */  
    public void setRingWidthDip(int ringWidth) {  
        this.ringWidth = dip2px(context, ringWidth);  
    }  
  
    /** 
     * 通过不断画弧的方式更新界面，实现进度增加 
     */  
    @Override  
    protected void onDraw(Canvas canvas) {  
        int center = getWidth() / 2;  
//        int radios = center - ringWidth / 2;  
  
        // 绘制圆环  
        paint.setStyle(Paint.Style.STROKE); // 绘制空心圆  
        paint.setColor(ringColor);  
        paint.setStrokeWidth(ringWidth);  
        canvas.drawCircle(center, center, radius, this.paint);  
        RectF oval = new RectF(center - radius, center - radius, center  
                + radius, center + radius);  
        paint.setColor(progressColor);  
        canvas.drawArc(oval, 90, 360 * progress / max, false, paint);  
        paint.setStyle(Paint.Style.FILL);  
        paint.setColor(textColor);  
        paint.setStrokeWidth(0);  
        paint.setTextSize(textSize);  
        paint.setTypeface(Typeface.DEFAULT_BOLD);  
        textProgress = (int) (1000 * (progress / (10.0 * max))) + "%";  
        float textWidth = paint.measureText(textProgress);  
        canvas.drawText(textProgress, center - textWidth / 2, center + textSize  
                / 2, paint);  
  
        super.onDraw(canvas);  
    }  
  
    /** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
}  