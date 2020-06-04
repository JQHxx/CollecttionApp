package com.huateng.collection.ui.dialog;

import androidx.fragment.app.FragmentActivity;

/**
 * author: yichuan
 * Created on: 2020/5/26 15:13
 * description:
 */

public class AlertFragmentUtil {

    public static AlertDialogFragment showAlertDialog(FragmentActivity fragmentActivity, String mMessage, AlertDialogFragment.OnDialogButtonClickListener clickListener) {
        AlertDialogFragment dialogFragment = AlertDialogFragment.getInstance(mMessage);
        createDialog(fragmentActivity, dialogFragment, clickListener);
        return dialogFragment;
    }


    public static AlertDialogFragment showAlertDialog(FragmentActivity fragmentActivity, String title, String mMessage, String mLeftMessage, String mRightMessage, AlertDialogFragment.OnDialogButtonClickListener clickListener) {
        AlertDialogFragment dialogFragment = AlertDialogFragment.getInstance(title, mMessage, mLeftMessage, mRightMessage);
        createDialog(fragmentActivity, dialogFragment, clickListener);
        return dialogFragment;
    }

    private static void createDialog(FragmentActivity fragmentActivity, AlertDialogFragment dialogFragment, AlertDialogFragment.OnDialogButtonClickListener clickListener) {
        dialogFragment.setOnButtonClickListener(clickListener);
        dialogFragment.show(fragmentActivity.getSupportFragmentManager(), fragmentActivity.getLocalClassName());
    }
}
