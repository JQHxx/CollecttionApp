package com.tools.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.tools.view.progresshud.ProgressHUD;

public class FragmentActivityBase extends FragmentActivity {

    public FragmentActivityBase mContext;
    protected ProgressHUD rxDialogLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        rxDialogLoading = ProgressHUD.create(this)
                .setStyle(ProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setSize(60,60)
                .setDimAmount(0.5f);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void showLoading() {
        if (rxDialogLoading != null && !rxDialogLoading.isShowing()) {
            rxDialogLoading.show();
        }
    }

    protected void hideLoading() {
        if (rxDialogLoading != null && rxDialogLoading.isShowing()) {
            rxDialogLoading.hide();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rxDialogLoading != null) {
            rxDialogLoading.dismiss();
        }
    }
}
