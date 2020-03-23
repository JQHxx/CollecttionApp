package com.huateng.fm.ui.view.loading;

import com.huateng.flowMobile.R;

import android.app.Dialog;
import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class FmRotateLoadingDialog extends FmLoadingDialog{
	
	private ImageView iv_loading;
	private TextView tv_tips;
	private Context mContext;
	
	public FmRotateLoadingDialog(Context context) {
		super(context, R.style.loading_dialog);
		this.mContext=context;
		init();
	}
	
	protected FmRotateLoadingDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		this.mContext=context;
		init();
	}
	
	public FmRotateLoadingDialog(Context context, int theme) {
		super(context, R.style.loading_dialog);
		this.mContext=context;
		init();
	}
	
	private void init(){
		setContentView(R.layout.ht_rotate_loading_dialog);
		iv_loading = (ImageView) findViewById(R.id.image);
		tv_tips = (TextView)findViewById(R.id.tipTextView);
		startRotate();
	    tv_tips.setText("");
	    setCancelable(true);
	}
	
	private void startRotate(){
		  Animation animation = AnimationUtils.loadAnimation(
	        		mContext, R.anim.ht_loading_animation);
		  iv_loading.startAnimation(animation);
//		float mCenterX; // 旋转中心x
//		float mCenterY;
//		// 获取旋转中心
//		DisplayMetrics dm = new DisplayMetrics();
//		dm = mContext.getResources().getDisplayMetrics();
//		mCenterX = dm.widthPixels / 2;
//		mCenterY = dm.heightPixels / 2;
//
//		int duration = 500;
//		LoadingAnimation animation= new LoadingAnimation(90, 0, mCenterX, mCenterY);
//		animation.setFillAfter(true);
//		animation.setDuration(duration);
//	    iv_loading.startAnimation(animation);
	}

	@Override
	public void setTipText(String text){
		tv_tips.setText(text);
	}
	
	@Override
	public void setTipText(int textId){
		tv_tips.setText(textId);
	}
	
	public void setLoadingImage(int resId){
		iv_loading.setImageResource(resId);
	}

	@Override
	public void show() {
		super.show();
		startRotate();
	}

	
	
	

}
