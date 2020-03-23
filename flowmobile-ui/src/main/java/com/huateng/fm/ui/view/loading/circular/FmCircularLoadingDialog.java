package com.huateng.fm.ui.view.loading.circular;

import java.util.Timer;
import java.util.TimerTask;

import com.huateng.flowMobile.R;
import com.huateng.fm.ui.view.loading.FmLoadingDialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Handler;
import android.os.SystemClock;
import android.sax.StartElementListener;
import android.util.Log;

public class FmCircularLoadingDialog extends FmLoadingDialog{
	
	private FmCircularProgressView mProgressView;
	private Context mContext;
	
	public FmCircularLoadingDialog(Context context) {
		super(context, R.style.loading_dialog);
		this.mContext=context;
		init();
	}
	
	protected FmCircularLoadingDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		this.mContext=context;
		init();
	}
	
	public FmCircularLoadingDialog(Context context, int theme) {
		super(context, R.style.loading_dialog);
		this.mContext=context;
		init();
	}
	
	private void init(){
		setContentView(R.layout.ht_circular_loading_dialog);
		mProgressView = (FmCircularProgressView)findViewById(R.id.progressView);
	    setCancelable(true);
	    
	}
	
	@Override
	public void setTipText(String text){
	}
	
	@Override
	public void setTipText(int textId){
	}
	

	@Override
	public void show() {
		super.show();
		Log.e("FmCircularLoadingDialog", "---show---");
		mProgressView.startAnimation();
	}
		
	

	@Override
	public void hide() {
		super.hide();
	}
	@Override
	public void setLoadingImage(int resId) {
	}
	
	

}
