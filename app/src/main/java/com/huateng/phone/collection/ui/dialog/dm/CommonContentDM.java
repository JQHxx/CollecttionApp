package com.huateng.phone.collection.ui.dialog.dm;

import android.view.View;
import android.widget.TextView;

/**
 * @author dengzh
 * @description
 * @time 2016-12-05.
 */
public class CommonContentDM extends  BaseDM{

    public TextView tv_content;
    public TextView tv_description;

    public void showDescription(boolean show){
        if (show){
            tv_description.setVisibility(View.VISIBLE);
        }else{
            tv_description.setVisibility(View.GONE);
        }
    }

    public void setContent(String content){
        tv_content.setText(content);
    }

    public void setDescription(String description){
        tv_description.setText(description);
    }
}
