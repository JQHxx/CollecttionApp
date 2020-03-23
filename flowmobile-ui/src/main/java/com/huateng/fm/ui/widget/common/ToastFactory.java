package com.huateng.fm.ui.widget.common;

import com.huateng.flowMobile.R;
import com.huateng.fm.app.FmAttributeValues;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public  class ToastFactory  {
	
	private static Toast toast;
	
	public ToastFactory(Context context) {
		initValues();
	}
	
	private void initValues(){
	}
	
	 public static Toast makeText(Context context, int imageResId,String text, boolean longDuration) {
		 if (toast==null) {
			 toast=new Toast(context);
		 } 
		 	int mImageRightMargin=(int)context.getResources().getDimension(R.dimen.ht_widget_toast_image_right_margin); 
		   int mImageSize=(int) context.getResources().getDimension(R.dimen.ht_widget_toast_image_size);
			LinearLayout ll=new LinearLayout(context);
			ll.setOrientation(LinearLayout.HORIZONTAL);
			ll.setGravity(Gravity.CENTER);
			ll.setPadding(FmAttributeValues.TOAST_HORIZONTAL_PADDING,FmAttributeValues.TOAST_VERTICAL_PADDING, FmAttributeValues.TOAST_HORIZONTAL_PADDING,
						FmAttributeValues.TOAST_VERTICAL_PADDING);
			ImageView imageView =new ImageView(context);
			LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(mImageSize,mImageSize);
			layoutParams.rightMargin=mImageRightMargin;
			imageView.setLayoutParams(layoutParams);
			imageView.setImageResource(imageResId);
			imageView.setScaleType(ScaleType.FIT_CENTER);
			if (imageResId==0) {
				imageView.setVisibility(View.GONE);
			}
			TextView textView=new TextView(context);
			textView.setText(text);
			textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
			textView.setTextColor(context.getResources().getColor(R.color.white));
			
			ll.addView(imageView);
			ll.addView(textView);
			ll.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			GradientDrawable backDrawable = new GradientDrawable();
			backDrawable.setCornerRadius(ll.getMeasuredHeight()/2);
			backDrawable.setColor(Color.parseColor("#E034495E"));
			ll.setBackgroundDrawable(backDrawable);
			if (longDuration) {
				toast.setDuration(Toast.LENGTH_LONG);
			}else{
				toast.setDuration(Toast.LENGTH_SHORT);
			}
			toast.setView(ll);
	        return toast;
	    } 
	
}
