package com.huateng.phone.collection.ui.dialog.dm;

/**
 * @author dengzh
 * @description
 * @time 2016-12-05.
 */
public class CorrentEndCaseDM extends  CommonContentDM{


    public void init(){
        super.init();
        tv_title.setText("结束处理");
        btn_left.setText("确定");
        btn_right.setText("取消");
        showDescription(true);
    }


}
