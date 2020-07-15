package com.huateng.phone.collection.ui.dialog.dm;

import com.huateng.phone.collection.app.Perference;
import com.huateng.phone.collection.bean.orm.Dic;
import com.huateng.phone.collection.widget.UniversalInput;

/**
 * @author dengzh
 * @description
 * @time 2016-12-05.
 */
public class AddAddressDM extends BaseDM {

    public UniversalInput csv_caseNo;
    public UniversalInput csv_custNo;
    public UniversalInput csv_addressType;
    public UniversalInput csv_postCode;
    public UniversalInput csv_city;
    public UniversalInput csv_address;
    public UniversalInput csv_name;
    public UniversalInput csv_custRel;

    public AddAddressDM() {
    }

    @Override
    public void init() {
        super.init();
        tv_title.setText("新增地址");
        btn_left.setText("提交");
        btn_right.setText("取消");
        csv_addressType.setDataSource(Dic.getOptions(Dic.ADDRESS));
        csv_caseNo.setText(Perference.getCurrentCaseId());
        csv_custRel.setDataSource(Dic.getOptions(Dic.RELATION));
    }

    public void setCaseNo(String caseNo) {
        csv_caseNo.setText(caseNo);
    }

    public void setCustNo(String custNo) {
        csv_custNo.setText(custNo);
    }

    public String getAddressType() {
        return csv_addressType.getText().toString();
    }

    public String getCity() {
        return csv_city.getText().toString();
    }

    public String getAddress() {
        return csv_address.getText().toString();
    }


    public String getCustRel() {
        return csv_custRel.getText().toString();
    }

}
