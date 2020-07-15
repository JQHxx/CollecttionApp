package com.huateng.phone.collection.ui.dialog.dm;

import com.huateng.phone.collection.widget.UniversalInput;

import java.util.List;

/**
 * @author dengzh
 * @description
 * @time 2016-12-05.
 */
public class SearchDM extends  BaseDM{

    public UniversalInput csv_custName;

    public void init(){
        super.init();
        tv_title.setText("查询");
        btn_left.setText("查询");
        btn_right.setText("清空");
        csv_custName.setHint("案件号/身份证/电话/信用卡卡号");
    }

    public String  getCustName(){
        return csv_custName.getText().toString().trim();
    }

    public void refreshCustNames(List<String> dateList){
        csv_custName.setDataSource(dateList);
    }

    private UniversalInput.SpinnerItemSelectedListener listener;

    public void setCustNamesSelectedListener(
            UniversalInput.SpinnerItemSelectedListener listener) {
        csv_custName.setSpinnerItemSelectedListener(listener);
    }

    public void setCustNamesTouchListener(
            UniversalInput.OnHtOptionTouchListener listener) {
        csv_custName.setOnHtOptionTouchListener(listener);
    }


}
