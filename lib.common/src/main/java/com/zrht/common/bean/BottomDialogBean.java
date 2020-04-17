package com.zrht.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author: yichuan
 * Created on: 2020-04-17 10:24
 * description:
 */
public class BottomDialogBean implements Parcelable {
    private int imageId;
    private String title;
    private boolean isSelected;

    public BottomDialogBean(int imageId,String title){
        this.imageId = imageId;
        this.title = title;
    }

    protected BottomDialogBean(Parcel in) {
        imageId = in.readInt();
        title = in.readString();
        isSelected = in.readByte() != 0;
    }

    public static final Creator<BottomDialogBean> CREATOR = new Creator<BottomDialogBean>() {
        @Override
        public BottomDialogBean createFromParcel(Parcel in) {
            return new BottomDialogBean(in);
        }

        @Override
        public BottomDialogBean[] newArray(int size) {
            return new BottomDialogBean[size];
        }
    };

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(imageId);
        parcel.writeString(title);
        parcel.writeByte((byte) (isSelected ? 1 : 0));
    }
}
