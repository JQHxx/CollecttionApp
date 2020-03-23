package com.huateng.fm.ui.view.loading;

import android.app.Dialog;
import android.content.Context;

/**
  * author: Devin
  * createTime:2015年8月25日
  * desciprion:
  */
public abstract class FmLoadingDialog extends Dialog{

	public FmLoadingDialog(Context context) {
		super(context);
	}
	
	public FmLoadingDialog(Context context, int theme) {
		super(context, theme);
	}

	/**
	 * @param context
	 * @param cancelable
	 * @param cancelListener
	 */
	protected FmLoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public abstract void setTipText(String text);
	
	public abstract void setTipText(int textId);
	public abstract void setLoadingImage(int resId);
}
