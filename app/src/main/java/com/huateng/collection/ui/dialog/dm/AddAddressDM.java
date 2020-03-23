package com.huateng.collection.ui.dialog.dm;

import com.huateng.collection.app.Perference;
import com.huateng.collection.bean.AddAddress;
import com.huateng.collection.bean.orm.Dic;
import com.huateng.collection.utils.StringUtils;
import com.huateng.collection.utils.Validation;
import com.huateng.collection.widget.UniversalInput;

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

    public AddAddress collectData() {
        AddAddress address = new AddAddress();
        address.setCaseId(Perference.getCurrentCaseId());

        String addrType = Dic.queryKey(Dic.ADDRESS, csv_addressType.getNoneNullText());
        String relWithCust = Dic.queryKey(Dic.RELATION, csv_custRel.getText().toString());
        String city = csv_city.getNoneNullText();
        String postCode = csv_postCode.getNoneNullText();
        String address1 = csv_address.getNoneNullText();
        String name=csv_name.getNoneNullText();

        if (StringUtils.isEmpty(addrType,name,relWithCust, city, address1)) {
            return null;
        }

        address.setAddrType(addrType);
        address.setName(name);
        address.setRelWithCust(relWithCust);
        address.setCity(city);
        address.setPostcode(postCode);
        address.setAddress1(address1);
        return address;
    }
}
