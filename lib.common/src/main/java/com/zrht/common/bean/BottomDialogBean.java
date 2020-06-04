package com.zrht.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author: yichuan
 * Created on: 2020-04-17 10:24
 * description:
 */
public class BottomDialogBean implements Parcelable {
    private int imageId;//资源图片ID
    private String title;//文字
    private boolean isSelected;//是否选中
    private String id;//当前选中事件ID

    public BottomDialogBean(String title, String id) {
        this.title = title;
        this.id = id;
    }

    public BottomDialogBean(int imageId, String title,boolean isSelected) {
        this.imageId = imageId;
        this.title = title;
        this.isSelected = isSelected;
    }


    protected BottomDialogBean(Parcel in) {
        imageId = in.readInt();
        title = in.readString();
        isSelected = in.readByte() != 0;
        id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(imageId);
        dest.writeString(title);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeString(id);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static Creator<BottomDialogBean> getCREATOR() {
        return CREATOR;
    }
}
