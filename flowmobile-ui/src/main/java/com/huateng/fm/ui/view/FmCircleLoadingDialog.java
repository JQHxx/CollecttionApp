package com.huateng.fm.ui.view;

import com.huateng.flowMobile.R;
import com.huateng.fm.ui.view.loading.FmLoadingDialog;

import android.app.Dialog;
import android.content.Context;

public class FmCircleLoadingDialog extends FmLoadingDialog{
	
	private FmCircleLoadingView circleLoadingView;
	private Context mContext;
	
	public FmCircleLoadingDialog(Context context) {
		super(context, R.style.loading_dialog);
		this.mContext=context;
		init();
	}
	
	protected FmCircleLoadingDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		this.mContext=context;
		init();
	}
	
	public FmCircleLoadingDialog(Context context, int theme) {
		super(context, R.style.loading_dialog);
		this.mContext=context;
		init();
	}
	
	private void init(){
		setContentView(R.layout.ht_circle_loading_dialog);
		circleLoadingView = (FmCircleLoadingView) findViewById(R.id.circleLoadingView);
	    setCancelable(true);
	}
	
	public FmCircleLoadingView getCircleLoadingView(){
		return circleLoadingView;
	}

	@Override
	public void setTipText(String text) {
		
	}

	@Override
	public void setTipText(int textId) {
		
	}

	@Override
	public void setLoadingImage(int resId) {
		
	}

}
