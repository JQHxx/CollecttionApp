package com.huateng.fm.ui.view.loading.titanic;

import com.huateng.flowMobile.R;
import com.huateng.fm.ui.view.loading.FmLoadingDialog;

import android.content.Context;

public class FmTitanicLoadingDialog extends FmLoadingDialog{
	
	private FmTitanicTextView tv_titanic;
	private Context mContext;
	private Titanic mTitanic;
	
	public FmTitanicLoadingDialog(Context context) {
		super(context, R.style.loading_dialog);
		this.mContext=context;
		init();
	}
	
	protected FmTitanicLoadingDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		this.mContext=context;
		init();
	}
	
	public FmTitanicLoadingDialog(Context context, int theme) {
		super(context, R.style.loading_dialog);
		this.mContext=context;
		init();
	}
	
	private void init(){
		setContentView(R.layout.ht_titanic_loading_dialog);
		tv_titanic = (FmTitanicTextView)findViewById(R.id.tv_titanic);
	    setCancelable(true);
	    tv_titanic.setTypeface(Typefaces.get(mContext, "Satisfy-Regular.ttf"));

	    //开启动画
	    mTitanic = new Titanic();
	}
	
	@Override
	public void setTipText(String text){
		tv_titanic.setText(text);
	}
	
	@Override
	public void setTipText(int textId){
		tv_titanic.setText(textId);
	}
	

	@Override
	public void show() {
		super.show();
		mTitanic.start(tv_titanic);
	}

	@Override
	public void dismiss() {
		super.dismiss();
		mTitanic.cancel();
	}
	@Override
	public void setLoadingImage(int resId) {
	}
	
	

}
