package com.huateng.collection.bean.orm;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shanyong on 2017/1/18.
 */
@Table
public class VisitCode implements Serializable {
    private String visitSumCode;
    private String visitSumCodeName;

    public String getVisitSumCode() {
        return visitSumCode;
    }

    public void setVisitSumCode(String visitSumCode) {
        this.visitSumCode = visitSumCode;
    }

    public String getVisitSumCodeName() {
        return visitSumCodeName;
    }

    public void setVisitSumCodeName(String visitSumCodeName) {
        this.visitSumCodeName = visitSumCodeName;
    }


    public static List<VisitCode> visitCodes() {
        List<VisitCode> visitCodes = SugarRecord.listAll(VisitCode.class);
        return visitCodes;
    }

    public static List<String> getOptions() {
        List<VisitCode> visitCodes = visitCodes();
        List<String> options = new ArrayList<>();
        for (VisitCode code : visitCodes) {
            options.add(code.getVisitSumCodeName());
        }
        return options;
    }

    public static String queryKey(String value) {
        List<VisitCode> visitCodes = visitCodes();
        for (VisitCode code : visitCodes) {
            if (code.getVisitSumCodeName().equals(value)) {
                return code.getVisitSumCode();
            }
        }
        return null;
    }

}
