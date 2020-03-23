package com.huateng.fm.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class FmFragmentBase extends Fragment implements FmUInterface{
	
	
	private FmUiPackage uiPackage;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		uiPackage=new FmUiPackage(getActivity());
	}

	@Override
	public void showMsg(String text) {
		uiPackage.showMsg(text);
	}

	@Override
	public void showMsg(String text, boolean longDuration) {
		uiPackage.showMsg(text,longDuration);
	}

	@Override
	public void showImageMsg(int imageResId, String text, boolean longDuration) {
		uiPackage.showImageMsg(imageResId, text, longDuration);
	}

	@Override
	public void showImageMsg(int imageResId, String text) {
		uiPackage.showImageMsg(imageResId, text);
	}

	@Override
	public void showLoading(int resId) {
		uiPackage.showLoading(resId);
	}

	@Override
	public void showLoading(int resId, int textId) {
		uiPackage.showLoading(resId, textId);
	}

	@Override
	public void showLoading(int resId, String text) {
		uiPackage.showLoading(resId, text);
	}


	@Override
	public void hideLoading() {
		uiPackage.hideLoading();
	}



}
