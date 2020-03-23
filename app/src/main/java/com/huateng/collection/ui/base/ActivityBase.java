package com.huateng.collection.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.flyco.systembar.SystemBarHelper;
import com.huateng.collection.widget.LoadingDialog;

/**
 * ActivityBase
 */

public class ActivityBase extends AppCompatActivity {

    public ActivityBase mContext;
    private LoadingDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showLoading() {
        if (dialog == null) {
            dialog = new LoadingDialog(this);
        }
        dialog.show();
    }

    public void hideLoading() {
        if (dialog != null) {
            dialog.hide();
        }
    }

    public void dismissLoading() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        dismissLoading();
    }

    @Override
    protected void onDestroy() {
        dismissLoading();
        super.onDestroy();
    }
}
