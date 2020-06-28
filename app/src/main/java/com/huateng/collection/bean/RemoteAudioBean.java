package com.huateng.collection.bean;

import java.util.List;

/**
 * author: yichuan
 * Created on: 2020/5/27 20:49
 * description:
 */
public class RemoteAudioBean {


    /**
     * current : 0
     * nextPage : 1
     * pageNo : 0
     * pageSize : 20
     * prePage : 1
     * records : [{"crtTime":1590580722000,"crtUser":"093809","fileId":"20200527000100001645","fileName":"0938091590579894.mp3","filePath":"1aff/54/6ab46a461395cd535ea745bc3d.mp3","fileSize":"140","fileTime":1590508800000,"fileType":"mp3","isLongSave":"Y","relaBusiCode":"20200102000603198868","relaBusiType":"APP","remark":"录音","updTime":1590580722000,"updUser":"093809"},{"crtTime":1590580723000,"crtUser":"093809","fileId":"20200527000100001646","fileName":"0938091590580019.mp3","filePath":"7416/4b/cbc73b48ef8989ebd909e74b6f.mp3","fileSize":"13","fileTime":1590508800000,"fileType":"mp3","isLongSave":"Y","relaBusiCode":"20200102000603198868","relaBusiType":"APP","remark":"录音","updTime":1590580723000,"updUser":"093809"},{"crtTime":1590580723000,"crtUser":"093809","fileId":"20200527000100001647","fileName":"0938091590580163.mp3","filePath":"0327/cf/04cdd4428abd64e46d99e1389f.mp3","fileSize":"13","fileTime":1590508800000,"fileType":"mp3","isLongSave":"Y","relaBusiCode":"20200102000603198868","relaBusiType":"APP","remark":"录音","updTime":1590580723000,"updUser":"093809"},{"crtTime":1590580724000,"crtUser":"093809","fileId":"20200527000100001648","fileName":"0938091590580177.mp3","filePath":"9d54/96/e9df1c4b7a8010841afbb8ed79.mp3","fileSize":"3","fileTime":1590508800000,"fileType":"mp3","isLongSave":"Y","relaBusiCode":"20200102000603198868","relaBusiType":"APP","remark":"录音","updTime":1590580724000,"updUser":"093809"},{"crtTime":1590580724000,"crtUser":"093809","fileId":"20200527000100001649","fileName":"0938091590580489.mp3","filePath":"12c1/77/be5da047debaf0d3d643dfb0fc.mp3","fileSize":"3","fileTime":1590508800000,"fileType":"mp3","isLongSave":"Y","relaBusiCode":"20200102000603198868","relaBusiType":"APP","remark":"录音","updTime":1590580724000,"updUser":"093809"},{"crtTime":1590580725000,"crtUser":"093809","fileId":"20200527000100001650","fileName":"0938091590580678.mp3","filePath":"a9c5/d1/0222c942f3ad81b5721adec785.mp3","fileSize":"89","fileTime":1590508800000,"fileType":"mp3","isLongSave":"Y","relaBusiCode":"20200102000603198868","relaBusiType":"APP","remark":"录音","updTime":1590580725000,"updUser":"093809"}]
     * startIndex : -19
     * totalPage : 1
     * totalRecord : 6
     */

    private int current;
    private int nextPage;
    private int pageNo;
    private int pageSize;
    private int prePage;
    private int startIndex;
    private int totalPage;
    private int totalRecord;
    private List<RecorderBean> records;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public List<RecorderBean> getRecords() {
        return records;
    }

    public void setRecords(List<RecorderBean> records) {
        this.records = records;
    }

}
