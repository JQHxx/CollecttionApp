package com.huateng.fm.app;

public interface FmUInterface {
	
	public void showMsg(String text);
	
	public void showMsg(String text, boolean longDuration);
	
	public void showImageMsg(int imageResId,String text, boolean longDuration);

	public void showImageMsg(int imageResId,String text );

	public void showLoading(int resId);

	public void showLoading(int resId,int textId);
	
	public void showLoading(int resId,String text);
	

	public void hideLoading();
}
