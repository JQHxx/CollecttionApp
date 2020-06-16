package com.zrht.common.loading;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.tools.R;

/**
 * author: yichuan
 * Created on: 2020/6/15 15:20
 * description:
 */

public class MaterialProgressDialog extends ProgressDialog {
    private TextView tv_loading;
    private Boolean cancelable;
    private String alert;
    private Context context;
    private Boolean canceledOnTouchOutside;


    public MaterialProgressDialog(Context context, String alert, boolean cancelable,boolean canceledOnTouchOutside) {
        super(context, R.style.CustomDialog);
        this.context = context;
        this.alert = alert;
        this.cancelable = cancelable;
        this.canceledOnTouchOutside = canceledOnTouchOutside;

    }

    public MaterialProgressDialog(Context context, String alert, boolean cancelable) {
        super(context, R.style.CustomDialog);
        this.context = context;
        this.alert = alert;
        this.cancelable = cancelable;
        this.canceledOnTouchOutside = cancelable;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }
    public void init(){
        setContentView(R.layout.layout_md_progress);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
        if (null == cancelable){
            cancelable = true;
        }
        if (null == cancelable){
            canceledOnTouchOutside = true;
        }
        setCancelable(cancelable);
        setCanceledOnTouchOutside(cancelable);
       /* if(cancelable){
            //不允许点击外部退出
            setCancelable(true);
            setCanceledOnTouchOutside(true);
        }else {
            //允许点击外部退出
            setCancelable(false);
            setCanceledOnTouchOutside(false);

        }*/
    }

}

