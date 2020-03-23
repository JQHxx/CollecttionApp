package com.huateng.fm.ui.complex.smoothprogressbar;

import com.huateng.flowMobile.R;
import com.huateng.fm.app.FmAttributeValues;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;

public class HTSmoothProgressBar extends ProgressBar {



private static final int INTERPOLATOR_ACCELERATE = 0;
  private static final int INTERPOLATOR_LINEAR = 1;
  private static final int INTERPOLATOR_ACCELERATEDECELERATE = 2;
  private static final int INTERPOLATOR_DECELERATE = 3;
  private  Context context;
  public HTSmoothProgressBar(Context context) {
		super(context);
		this.context=context;
	}

  public HTSmoothProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		init(attrs,0);
	}

  public HTSmoothProgressBar(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
		this.context=context;
		init(attrs,defStyle);
	 
	  }
 
  private void init( AttributeSet attrs, int defStyle){
	   Resources res = context.getResources();
	    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ht_SmoothProgressBar, defStyle, 0);


	    final int color = a.getColor(R.styleable.ht_SmoothProgressBar_color3, Color.parseColor("#33b5e5"));
	    final int sectionsCount = a.getInteger(R.styleable.ht_SmoothProgressBar_sectionsCount, 4);
	    final int separatorLength = a.getDimensionPixelSize(R.styleable.ht_SmoothProgressBar_strokeSeparatorlength,2*FmAttributeValues.ht2dp);
	    final float strokeWidth = a.getDimension(R.styleable.ht_SmoothProgressBar_strokeWidth, 2*FmAttributeValues.ht2dp);
	    final float speed = a.getFloat(R.styleable.ht_SmoothProgressBar_speed, 1.0f);
	    final float speedProgressiveStart = a.getFloat(R.styleable.ht_SmoothProgressBar_progressiveStartSpeed, speed);
	    final float speedProgressiveStop = a.getFloat(R.styleable.ht_SmoothProgressBar_progressiveStopSpeed, speed);
	    final int iInterpolator = a.getInteger(R.styleable.ht_SmoothProgressBar_interpolator, -1);
	    final boolean reversed = a.getBoolean(R.styleable.ht_SmoothProgressBar_reversed, false);
	    final boolean mirrorMode = a.getBoolean(R.styleable.ht_SmoothProgressBar_mirrorMode,false);
	    final int colorsId = a.getResourceId(R.styleable.ht_SmoothProgressBar_colors, 0);
	    final boolean progressiveStartActivated = a.getBoolean(R.styleable.ht_SmoothProgressBar_progressiveStartActivated,false);
	    final Drawable backgroundDrawable = a.getDrawable(R.styleable.ht_SmoothProgressBar_fmBackground);
	    final boolean generateBackgroundWithColors = a.getBoolean(R.styleable.ht_SmoothProgressBar_generateBackgroundWithColors, false);
	    a.recycle();

	    //interpolator
	    Interpolator interpolator = null;
	    if (iInterpolator == -1) {
	      interpolator = getInterpolator();
	    }
	    if (interpolator == null) {
	      switch (iInterpolator) {
	        case INTERPOLATOR_ACCELERATEDECELERATE:
	          interpolator = new AccelerateDecelerateInterpolator();
	          break;
	        case INTERPOLATOR_DECELERATE:
	          interpolator = new DecelerateInterpolator();
	          break;
	        case INTERPOLATOR_LINEAR:
	          interpolator = new LinearInterpolator();
	          break;
	        case INTERPOLATOR_ACCELERATE:
	        default:
	          interpolator = new AccelerateInterpolator();
	      }
	    }

	    int[] colors = null;
	    //colors
	    if (colorsId != 0) {
	      colors = res.getIntArray(colorsId);
	    }

	    SmoothProgressDrawable.Builder builder = new SmoothProgressDrawable.Builder(context)
	        .speed(speed)
	        .progressiveStartSpeed(speedProgressiveStart)
	        .progressiveStopSpeed(speedProgressiveStop)
	        .interpolator(interpolator)
	        .sectionsCount(sectionsCount)
	        .separatorLength(separatorLength)
	        .strokeWidth(strokeWidth)
	        .reversed(reversed)
	        .mirrorMode(mirrorMode)
	        .progressiveStart(progressiveStartActivated);

	    if (backgroundDrawable != null) {
	      builder.backgroundDrawable(backgroundDrawable);
	    }

	    if (generateBackgroundWithColors) {
	      builder.generateBackgroundUsingColors();
	    }

	    if (colors != null && colors.length > 0)
	      builder.colors(colors);
	    else
	      builder.color(color);

	    SmoothProgressDrawable d = builder.build();
	    setIndeterminateDrawable(d);
  }

  public void applyStyle(int styleResId) {
    TypedArray a = getContext().obtainStyledAttributes(null, R.styleable.ht_SmoothProgressBar, 0, styleResId);

    if (a.hasValue(R.styleable.ht_SmoothProgressBar_color3)) {
      setSmoothProgressDrawableColor(a.getColor(R.styleable.ht_SmoothProgressBar_color3, 0));
    }
    if (a.hasValue(R.styleable.ht_SmoothProgressBar_colors)) {
      int colorsId = a.getResourceId(R.styleable.ht_SmoothProgressBar_colors, 0);
      if (colorsId != 0) {
        int[] colors = getResources().getIntArray(colorsId);
        if (colors != null && colors.length > 0)
          setSmoothProgressDrawableColors(colors);
      }
    }
    if (a.hasValue(R.styleable.ht_SmoothProgressBar_sectionsCount)) {
      setSmoothProgressDrawableSectionsCount(a.getInteger(R.styleable.ht_SmoothProgressBar_sectionsCount, 0));
    }
    if (a.hasValue(R.styleable.ht_SmoothProgressBar_strokeSeparatorlength)) {
      setSmoothProgressDrawableSeparatorLength(a.getDimensionPixelSize(R.styleable.ht_SmoothProgressBar_strokeSeparatorlength, 0));
    }
    if (a.hasValue(R.styleable.ht_SmoothProgressBar_strokeWidth)) {
      setSmoothProgressDrawableStrokeWidth(a.getDimension(R.styleable.ht_SmoothProgressBar_strokeWidth, 0));
    }
    if (a.hasValue(R.styleable.ht_SmoothProgressBar_speed)) {
      setSmoothProgressDrawableSpeed(a.getFloat(R.styleable.ht_SmoothProgressBar_speed, 0));
    }
    if (a.hasValue(R.styleable.ht_SmoothProgressBar_progressiveStartSpeed)) {
      setSmoothProgressDrawableProgressiveStartSpeed(a.getFloat(R.styleable.ht_SmoothProgressBar_progressiveStartSpeed, 0));
    }
    if (a.hasValue(R.styleable.ht_SmoothProgressBar_progressiveStopSpeed)) {
      setSmoothProgressDrawableProgressiveStopSpeed(a.getFloat(R.styleable.ht_SmoothProgressBar_progressiveStopSpeed, 0));
    }
    if (a.hasValue(R.styleable.ht_SmoothProgressBar_reversed)) {
      setSmoothProgressDrawableReversed(a.getBoolean(R.styleable.ht_SmoothProgressBar_reversed, false));
    }
    if (a.hasValue(R.styleable.ht_SmoothProgressBar_mirrorMode)) {
      setSmoothProgressDrawableMirrorMode(a.getBoolean(R.styleable.ht_SmoothProgressBar_mirrorMode, false));
    }
    if (a.hasValue(R.styleable.ht_SmoothProgressBar_progressiveStartActivated)) {
      setProgressiveStartActivated(a.getBoolean(R.styleable.ht_SmoothProgressBar_progressiveStartActivated, false));
    }
    if (a.hasValue(R.styleable.ht_SmoothProgressBar_fmBackground)) {
      setSmoothProgressDrawableBackgroundDrawable(a.getDrawable(R.styleable.ht_SmoothProgressBar_fmBackground));
    }
    if (a.hasValue(R.styleable.ht_SmoothProgressBar_generateBackgroundWithColors)) {
      if (a.getBoolean(R.styleable.ht_SmoothProgressBar_generateBackgroundWithColors, false)) {
        setSmoothProgressDrawableBackgroundDrawable(
            SmoothProgressBarUtils.generateDrawableWithColors(checkIndeterminateDrawable().getColors(), checkIndeterminateDrawable().getStrokeWidth()));
      }
    }
    if (a.hasValue(R.styleable.ht_SmoothProgressBar_interpolator)) {
      int iInterpolator = a.getInteger(R.styleable.ht_SmoothProgressBar_interpolator, -1);
      Interpolator interpolator;
      switch (iInterpolator) {
        case INTERPOLATOR_ACCELERATEDECELERATE:
          interpolator = new AccelerateDecelerateInterpolator();
          break;
        case INTERPOLATOR_DECELERATE:
          interpolator = new DecelerateInterpolator();
          break;
        case INTERPOLATOR_LINEAR:
          interpolator = new LinearInterpolator();
          break;
        case INTERPOLATOR_ACCELERATE:
          interpolator = new AccelerateInterpolator();
          break;
        default:
          interpolator = null;
      }
      if (interpolator != null) {
        setInterpolator(interpolator);
      }
    }
    a.recycle();
  }

  @Override
  protected synchronized void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (isIndeterminate() && getIndeterminateDrawable() instanceof SmoothProgressDrawable &&
        !((SmoothProgressDrawable) getIndeterminateDrawable()).isRunning()) {
      getIndeterminateDrawable().draw(canvas);
    }
  }

  private SmoothProgressDrawable checkIndeterminateDrawable() {
    Drawable ret = getIndeterminateDrawable();
    if (ret == null || !(ret instanceof SmoothProgressDrawable))
      throw new RuntimeException("The drawable is not a SmoothProgressDrawable");
    return (SmoothProgressDrawable) ret;
  }

  @Override
  public void setInterpolator(Interpolator interpolator) {
    super.setInterpolator(interpolator);
    Drawable ret = getIndeterminateDrawable();
    if (ret != null && (ret instanceof SmoothProgressDrawable))
      ((SmoothProgressDrawable) ret).setInterpolator(interpolator);
  }

  public void setSmoothProgressDrawableInterpolator(Interpolator interpolator) {
    checkIndeterminateDrawable().setInterpolator(interpolator);
  }

  public void setSmoothProgressDrawableColors(int[] colors) {
    checkIndeterminateDrawable().setColors(colors);
  }

  public void setSmoothProgressDrawableColor(int color) {
    checkIndeterminateDrawable().setColor(color);
  }

  public void setSmoothProgressDrawableSpeed(float speed) {
    checkIndeterminateDrawable().setSpeed(speed);
  }

  public void setSmoothProgressDrawableProgressiveStartSpeed(float speed) {
    checkIndeterminateDrawable().setProgressiveStartSpeed(speed);
  }

  public void setSmoothProgressDrawableProgressiveStopSpeed(float speed) {
    checkIndeterminateDrawable().setProgressiveStopSpeed(speed);
  }

  public void setSmoothProgressDrawableSectionsCount(int sectionsCount) {
    checkIndeterminateDrawable().setSectionsCount(sectionsCount);
  }

  public void setSmoothProgressDrawableSeparatorLength(int separatorLength) {
    checkIndeterminateDrawable().setSeparatorLength(separatorLength);
  }

  public void setSmoothProgressDrawableStrokeWidth(float strokeWidth) {
    checkIndeterminateDrawable().setStrokeWidth(strokeWidth);
  }

  public void setSmoothProgressDrawableReversed(boolean reversed) {
    checkIndeterminateDrawable().setReversed(reversed);
  }

  public void setSmoothProgressDrawableMirrorMode(boolean mirrorMode) {
    checkIndeterminateDrawable().setMirrorMode(mirrorMode);
  }

  public void setProgressiveStartActivated(boolean progressiveStartActivated) {
    checkIndeterminateDrawable().setProgressiveStartActivated(progressiveStartActivated);
  }

  public void setSmoothProgressDrawableCallbacks(SmoothProgressDrawable.Callbacks listener) {
    checkIndeterminateDrawable().setCallbacks(listener);
  }

  public void setSmoothProgressDrawableBackgroundDrawable(Drawable drawable) {
    checkIndeterminateDrawable().setBackgroundDrawable(drawable);
  }

  public void progressiveStart() {
    checkIndeterminateDrawable().progressiveStart();
  }

  public void progressiveStop() {
    checkIndeterminateDrawable().progressiveStop();
  }
}
