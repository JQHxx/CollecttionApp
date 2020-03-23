package com.huateng.fm.util;

import com.huateng.fm.core.app.FmApplication;

public class UiValuesUtil {
	
	
	public static int getColor(int colorResId){
		return FmApplication.getInstance().getResources().getColor(colorResId);
	}
	
	public static int getDimen(int dimension){
		return (int) FmApplication.getInstance().getResources().getDimension(dimension);
	}
	
	public static String getString(int id){
		return  FmApplication.getInstance().getResources().getString(id);
	}
	
}
