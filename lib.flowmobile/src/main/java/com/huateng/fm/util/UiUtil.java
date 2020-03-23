package com.huateng.fm.util;

import com.huateng.fm.ui.widget.common.ToastFactory;

import android.content.Context;
import android.widget.Toast;

public class UiUtil {
	
	
	 public static Toast makeText(Context context,String text, boolean longDuration) {
		 return makeText(context,0,text,longDuration);
	 }
	
	 public static Toast makeText(Context context,String text) {
		 return makeText(context,0,text,false);
	 }
	
	 public static Toast makeText(Context context, int imageResId,String text, boolean longDuration) {
		 return ToastFactory.makeText(context, imageResId, text, longDuration);
	 }
	
	
}
