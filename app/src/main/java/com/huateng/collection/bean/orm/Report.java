package com.huateng.collection.bean.orm;

import java.util.List;

/**
 * @author dengzh
 * @description
 * @time 2016-12-15.
 */
public class Report {
    public List<PendingReportData> content;

    public List<PendingReportData> getContent() {
        return content;
    }

    public void setContent(List<PendingReportData> content) {
        this.content = content;
    }
}
