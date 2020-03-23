package com.huateng.fm.ui.widget;

import java.util.Observable;
import java.util.Observer;

import com.huateng.flowMobile.R;
import com.huateng.fm.app.FmAttributeValues;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.PaintDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.SeekBar;


/**
 * SeekBar
 * @author Decheewar
 */
public class FmSeekBar extends SeekBar implements Observer{

    private FmAttributeValues mAttrValues;
    private Context context;

    public FmSeekBar(Context context) {
        super(context);
		this.context=context;
        init(null);
    }

    public FmSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
		this.context=context;
        init(attrs);
    }

    public FmSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
		this.context=context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {

		mAttrValues= new FmAttributeValues();
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs,R.styleable.ht_SeekBar);
            mAttrValues.setSize(a.getDimensionPixelSize(R.styleable.ht_SeekBar_totalSize, FmAttributeValues.DEFAULT_SIZE_PX));
            a.recycle();
        }

    
    }
    
    private void initStates(){
        // setting thumb
        PaintDrawable thumb = new PaintDrawable(getResources().getColor(FmAttributeValues.PRIMARY_COLOR));
        thumb.setCornerRadius(mAttrValues.getSize() * 9 / 8);
        thumb.setIntrinsicWidth(mAttrValues.getSize() * 9 / 4);
        thumb.setIntrinsicHeight(mAttrValues.getSize() * 9 / 4);
        setThumb(thumb);

        // progress
        PaintDrawable progress = new PaintDrawable(getResources().getColor(FmAttributeValues.PRIMARY_COLOR));
        progress.setCornerRadius(mAttrValues.getSize());
        progress.setIntrinsicHeight(mAttrValues.getSize());
        progress.setIntrinsicWidth(mAttrValues.getSize());
        progress.setDither(true);
        ClipDrawable progressClip = new ClipDrawable(progress, Gravity.LEFT, ClipDrawable.HORIZONTAL);

        // secondary progress
        PaintDrawable secondary = new PaintDrawable(getResources().getColor(FmAttributeValues.STATED_COLOR));
        secondary.setCornerRadius(mAttrValues.getSize());
        secondary.setIntrinsicHeight(mAttrValues.getSize());
        ClipDrawable secondaryProgressClip = new ClipDrawable(secondary, Gravity.LEFT, ClipDrawable.HORIZONTAL);

        // background
        PaintDrawable background = new PaintDrawable(getResources().getColor(FmAttributeValues.SPECIAL_COLOR));
        background.setCornerRadius(mAttrValues.getSize());
        background.setIntrinsicHeight(mAttrValues.getSize());

        LayerDrawable ld = (LayerDrawable) getProgressDrawable();
        ld.setDrawableByLayerId(android.R.id.background, background);
        ld.setDrawableByLayerId(android.R.id.progress, progressClip);
        ld.setDrawableByLayerId(android.R.id.secondaryProgress, secondaryProgressClip);
    }

	@Override
	public void update(Observable observable, Object data) {
		initStates();		
	}
    
}
