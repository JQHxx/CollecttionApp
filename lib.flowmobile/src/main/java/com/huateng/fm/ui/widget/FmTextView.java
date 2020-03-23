package com.huateng.fm.ui.widget;



import java.util.Observable;
import java.util.Observer;

import com.huateng.flowMobile.R;
import com.huateng.fm.app.FmAttributeValues;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * TextView
 * @author Devin
 */
public class FmTextView extends TextView  implements Observer{
	
	private final String TAG = getClass().getSimpleName();
	private Context context;
	private FmAttributeValues mAttrValues;
	private Drawable background = null;
	private int mCornerStyle=-1;
    private int mBgColor;
    private int mBorderColor;
    private int mBorderWidth;
    private final int UNDEFINED=-1;
	
	public FmTextView(Context context) {
		super(context);
		this.context = context;
		init(null);
	}

	public FmTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init(attrs);
	}

	public FmTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init(attrs);
	}

	private void init(AttributeSet attrs) {
		mAttrValues= new FmAttributeValues();
		mAttrValues.addObserver(this);

		if (attrs != null) {
			TypedArray a = getContext().obtainStyledAttributes(attrs,
					R.styleable.ht_TextView);
			mCornerStyle = a.getInt(R.styleable.ht_TextView_cornerStyle,
                    UNDEFINED);
            mBgColor=a.getColor(R.styleable.ht_TextView_bgColor, UNDEFINED);
            mBorderColor=a.getColor(R.styleable.ht_TextView_h_borderColor,UNDEFINED);
            mBorderWidth= (int) a.getDimension(R.styleable.ht_TextView_h_borderWidth, 0);
            a.recycle();
            Log.i(TAG,"init--mBorderColor:"+mBorderColor+"--mBgColor:"+mBgColor);

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
		GradientDrawable drawable = new GradientDrawable();
		drawable.setCornerRadius(mAttrValues.getRadius());
//		drawable.setSize(mAttrValues.getSize(), mAttrValues.getSize());
		if (mBgColor!=UNDEFINED){
            drawable.setColor(mBgColor);
        }else {
            drawable.setColor(Color.TRANSPARENT);
        }
        if (mBorderColor!=UNDEFINED&&mBorderWidth!=0){
            drawable.setStroke(mBorderWidth, mBorderColor);
        }else if(mBorderColor==UNDEFINED&&mBorderWidth==0){
            drawable.setStroke(mBorderWidth, FmAttributeValues.PRIMARY_COLOR);
        }

		
		if (background == null) {
			setBackgroundDrawable(drawable);
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
	
	public void setBgColor(int resId){
		mBgColor=getResources().getColor(resId);
		initStates();
	}
	
	@Override
	public void update(Observable observable, Object data) {
		initStates();		
	}

}
