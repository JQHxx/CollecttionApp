package com.huateng.fm.app;

import com.huateng.fm.ui.view.FmCircleLoadingDialog;
import com.huateng.fm.ui.view.loading.FmLoadingDialog;
import com.huateng.fm.ui.view.loading.FmRotateLoadingDialog;
import com.huateng.fm.ui.view.loading.LoadingType;
import com.huateng.fm.ui.view.loading.circular.FmCircularLoadingDialog;
import com.huateng.fm.ui.view.loading.titanic.FmTitanicLoadingDialog;
import com.huateng.fm.util.UiUtil;

import android.app.Activity;
import android.os.SystemClock;
import android.text.InputType;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;

public class FmUiPackage implements FmUInterface{
	
	private final String TAG=getClass().getSimpleName();
	
	private Activity activity;
	private FmLoadingDialog loadingDialog;
	private boolean alreadyShow = false;
	private static LoadingType mLoadingType;
	
	public static void setLoadingType(LoadingType type){
		mLoadingType=type;
	}
	
	public FmUiPackage(Activity activity){
		this.activity=activity;
	}
	
	public void showMsg(String text){
		UiUtil.makeText(activity, text, false).show();
	}
	
	public void showMsg(String text, boolean longDuration){
		UiUtil.makeText(activity, text, longDuration).show();
	}

	@Override
	public void showImageMsg(int imageResId, String text, boolean longDuration) {
		UiUtil.makeText(activity,imageResId, text, longDuration).show();
	}

	@Override
	public void showImageMsg(int imageResId, String text) {
		UiUtil.makeText(activity,imageResId, text, false).show();
	}

	@Override
	public void showLoading(int resId) {
		 if (loadingDialog==null) {
			if (mLoadingType==LoadingType.ROTATE) {
				 loadingDialog=new FmRotateLoadingDialog(activity);
				 loadingDialog.setTipText("");
				 loadingDialog.setLoadingImage(resId);

			}else if (mLoadingType==LoadingType.TANTIC) {
				 loadingDialog=new FmTitanicLoadingDialog(activity);
				 loadingDialog.setTipText("loading");
				 loadingDialog.setLoadingImage(resId);

			}else if (mLoadingType==LoadingType.CIRCULAR) {
				 loadingDialog=new FmCircularLoadingDialog(activity);
			}
		}
		 loadingDialog.show();

	}
	
	@Override
	public void showLoading(int resId,int textId) {
		if (loadingDialog==null) {
			if (mLoadingType==LoadingType.ROTATE) {
				 loadingDialog=new FmRotateLoadingDialog(activity);
				 loadingDialog.setLoadingImage(resId);
			}else if (mLoadingType==LoadingType.TANTIC) {
				 loadingDialog=new FmTitanicLoadingDialog(activity);
			}else if (mLoadingType==LoadingType.CIRCULAR) {
				 loadingDialog=new FmCircularLoadingDialog(activity);
			}
		}
		 loadingDialog.setTipText(textId);
		 loadingDialog.show();

	}
	
	@Override
	public void showLoading(int resId,String text) {
		if (loadingDialog==null) {
			if (mLoadingType==LoadingType.ROTATE) {
				 loadingDialog=new FmRotateLoadingDialog(activity);
				 loadingDialog.setLoadingImage(resId);
			}else if (mLoadingType==LoadingType.TANTIC) {
				 loadingDialog=new FmTitanicLoadingDialog(activity);
			}else if (mLoadingType==LoadingType.CIRCULAR) {
				 loadingDialog=new FmCircularLoadingDialog(activity);
			}
		}
		 loadingDialog.setTipText(text);
		 loadingDialog.show();

	}
	
	@Override
	public void hideLoading() {
		 if (loadingDialog!=null&&loadingDialog.isShowing()) {
			 loadingDialog.dismiss();
		}
	}

}
