package com.huateng.phone.collection.ui.dialog.dm;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huateng.phone.collection.widget.dialogplus.DialogPlus;
import com.huateng.fm.ui.widget.FmButton;
import com.tools.SystemUtils;

/**
 * @author dengzh
 * @description
 * @time 2016-12-05.
 */
public class BaseDM {
    private DialogPlus dialog;
    public TextView tv_title;
    public ImageView iv_close;
    public FmButton btn_left;
    public FmButton btn_right;

    public BaseDM(){
    }

    public void init(){
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemUtils.hideInputmethod(v);
                dialog.dismiss();
            }
        });
    }

    public DialogPlus getDialog(){
        return dialog;
    }

    public void setDialog(DialogPlus dialog){
        this.dialog=dialog;
    }

    public interface OnFooterButtonClickListener{
        public void onLeftClicked(View v);
        public void onRightClicked(View v);
    }

    public void setOnFooterButtonClickListener(
           final OnFooterButtonClickListener listener) {
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    SystemUtils.hideInputmethod(v);
                    listener.onLeftClicked(v);

                }
            }
        });
        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    SystemUtils.hideInputmethod(v);
                    listener.onRightClicked(v);
                }
            }
        });
    }

    public void setTitle(String title){
        tv_title.setText(title);
    }

}
