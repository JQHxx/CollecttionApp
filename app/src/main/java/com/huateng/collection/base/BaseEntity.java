package com.huateng.collection.base;

import android.os.Parcel;
import android.os.Parcelable;

public class BaseEntity<T> implements Parcelable {
    private String respCode;//000000-成功  999999-失败
    private String respMsg;//code描述
    private T data;
    private int pageTotal;
    private int sizeTotal;

    protected BaseEntity(Parcel in) {
        respCode = in.readString();
        respMsg = in.readString();
        pageTotal = in.readInt();
        sizeTotal = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(respCode);
        dest.writeString(respMsg);
        dest.writeInt(pageTotal);
        dest.writeInt(sizeTotal);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BaseEntity> CREATOR = new Creator<BaseEntity>() {
        @Override
        public BaseEntity createFromParcel(Parcel in) {
            return new BaseEntity(in);
        }

        @Override
        public BaseEntity[] newArray(int size) {
            return new BaseEntity[size];
        }
    };

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public int getSizeTotal() {
        return sizeTotal;
    }

    public void setSizeTotal(int sizeTotal) {
        this.sizeTotal = sizeTotal;
    }

    public static Creator<BaseEntity> getCREATOR() {
        return CREATOR;
    }
}
