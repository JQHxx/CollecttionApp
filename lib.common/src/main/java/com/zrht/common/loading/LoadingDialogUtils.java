package com.zrht.common.loading;

import android.app.Activity;
import android.content.Context;

/**
 * author: yichuan
 * Created on: 2020/6/15 15:19
 * description:
 */

public class LoadingDialogUtils {

    private static MaterialProgressDialog dialog;

    public static void show(Context context) {

        show(context, true, false);
    }


    public static void show(Context context, boolean canNotCancel, boolean canceledOnTouchOutside) {

        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = new MaterialProgressDialog(context, "", canNotCancel, canceledOnTouchOutside);
        dialog.show();

    }

    public static void dismiss(Context context) {
        try {
            if (context instanceof Activity) {

                if (((Activity) context).isFinishing()) {
                    dialog = null;
                    return;
                }
            }

            if (dialog != null && dialog.isShowing()) {

                Context loadContext = dialog.getContext();
                if (loadContext != null && loadContext instanceof Activity) {

                    if (((Activity) loadContext).isFinishing()) {

                        dialog = null;
                    }
                }

                dialog.dismiss();
                dialog = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            dialog = null;
        }
    }

}
