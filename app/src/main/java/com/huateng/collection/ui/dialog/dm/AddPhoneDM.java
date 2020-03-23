package com.huateng.collection.ui.dialog.dm;

import com.huateng.collection.app.Perference;
import com.huateng.collection.bean.api.RespPhone;
import com.huateng.collection.bean.orm.Dic;
import com.huateng.collection.utils.StringUtils;
import com.huateng.collection.utils.Validation;
import com.huateng.collection.widget.UniversalInput;

/**
 * @author dengzh
 * @description
 * @time 2016-12-05.
 */
public class AddPhoneDM extends BaseDM {

    public UniversalInput csv_caseNo;
    public UniversalInput csv_custNo;
    public UniversalInput csv_custName;
    public UniversalInput csv_custRel;
    public UniversalInput csv_phoneType;
    public UniversalInput csv_phone;

    public AddPhoneDM() {
    }

    @Override
    public void init() {
        super.init();
        tv_title.setText("新增电话");
        btn_left.setText("提交");
        btn_right.setText("取消");
        csv_caseNo.setText(Perference.getCurrentCaseId());
        csv_phoneType.setDataSource(Dic.getOptions(Dic.TELIPHONE));
        csv_custRel.setDataSource(Dic.getOptions(Dic.RELATION));
    }

    public void setCaseNo(String caseNo) {
        csv_caseNo.setText(caseNo);
    }

    public void setCustNo(String custNo) {
        csv_custNo.setText(custNo);
    }

    public String getCustName() {
        return csv_custName.getText().toString();
    }

    public String getCustRel() {
        return csv_custRel.getText().toString();
    }

    public String getPhoneType() {
        return csv_phoneType.getText().toString();
    }

    public String getPhone() {
        return csv_phone.getText().toString();
    }


    public RespPhone collectData() {
        RespPhone phone = new RespPhone();
        phone.setCaseId(Perference.getCurrentCaseId());
        String name = csv_custName.getNoneNullText();
        String relWithCust = Dic.queryKey(Dic.RELATION, csv_custRel.getText().toString());
        String telNo = csv_phone.getNoneNullText();
        String telType = Dic.queryKey(Dic.TELIPHONE, csv_phoneType.getNoneNullText());


        if (StringUtils.isEmpty(name, relWithCust, telNo, telType)) {
            return null;
        }

//        if(!Validation.mobileCheck(telNo)){
//            return null;
//        }

        phone.setName(name);
        phone.setRelWithCust(relWithCust);
        phone.setTelId("");
        phone.setTelNo(telNo);
        phone.setTelType(telType);
        return phone;
    }
}
