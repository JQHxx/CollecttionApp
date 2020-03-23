package com.huateng.fm.app;

import com.huateng.fm.core.app.FmActivityManager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class FmActivityBase extends FmToolBarActivity implements
        FmUInterface {

    private FmUiPackage uiPackage;
    private FmActivityManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = FmActivityManager.getInstance();
        manager.addActivity(this);
        uiPackage = new FmUiPackage(this);
    }

    @Override
    public void showMsg(String text) {
        uiPackage.showMsg(text);
    }

    @Override
    public void showMsg(String text, boolean longDuration) {
        uiPackage.showMsg(text, longDuration);
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

    @Override
    public boolean blockFitSysWindowTint() {
        return false;
    }

    @Override
    protected void onDestroy() {
        manager.removeActivity(this);
        super.onDestroy();
    }
}
