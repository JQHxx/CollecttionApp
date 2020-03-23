package com.huateng.collection.ui.dialog.dm;

import com.huateng.collection.app.Perference;
import com.huateng.collection.bean.orm.VisitCode;
import com.huateng.collection.utils.StringUtils;
import com.huateng.collection.widget.UniversalInput;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dengzh
 * @description
 * @time 2016-12-05.
 */
public class VisitSummarizeDM extends BaseDM {

    public UniversalInput csv_visitAddress;
    public UniversalInput csv_visitDate;
    public UniversalInput csv_visitResult;
    public UniversalInput csv_visitOfficer;
    public UniversalInput csv_visitOtherOfficer;
    public UniversalInput csv_visitInfo;

    private String addrId;

    public VisitSummarizeDM() {
    }

    @Override
    public void init() {
        super.init();
        tv_title.setText("上门总结");
        csv_visitResult.setDataSource(VisitCode.getOptions());
    }

    public void setVisitOfficer(String officer) {
        csv_visitOfficer.setText(officer);
    }

    public void setVisitAddress(String address) {
        csv_visitAddress.setText(address);
    }

    public void setAddrId(String addrId) {
        this.addrId = addrId;
    }

    public String getVisitOtherOfficer() {
        return csv_visitOtherOfficer.getText().toString();
    }

    public String getVisitInfo() {
        return csv_visitInfo.getText().toString();
    }

    public String getVisitDate() {
        return csv_visitDate.getText().toString();
    }

    public String getVisitResult() {
        return csv_visitResult.getText().toString();
    }

    public Map<String, String> collectData() {
        Map<String, String> map = new HashMap<>();
        String visitSumCode = VisitCode.queryKey(csv_visitResult.getNoneNullText());
        String visitTime = csv_visitDate.getNoneNullText();
        String collector = csv_visitOfficer.getNoneNullText();
        String coCollector = csv_visitOtherOfficer.getNoneNullText();
        String visitCondition = csv_visitInfo.getNoneNullText();

        if (StringUtils.isEmpty(visitSumCode, visitTime, collector, visitCondition)) {
            return null;
        }

        map.put("visitSumCode", visitSumCode);
        map.put("visitTime", visitTime);
        map.put("collector", collector);
        map.put("coCollector", coCollector);
        map.put("custNo", Perference.getCurrentCustId());
        map.put("caseId", Perference.getCurrentCaseId());
        map.put("addrId", addrId);
        map.put("visitCondition", visitCondition);
        return map;
    }
}
