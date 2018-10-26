package com.ibagou.dou.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by liumingyu on 2018/8/21.
 */

public class NetStateBean implements Parcelable{
    private boolean netAccess;

    public NetStateBean(boolean netAccess){
        this.netAccess = netAccess;
    }

    protected NetStateBean(Parcel in) {
        netAccess = in.readByte() != 0;
    }

    public static final Creator<NetStateBean> CREATOR = new Creator<NetStateBean>() {
        @Override
        public NetStateBean createFromParcel(Parcel in) {
            return new NetStateBean(in);
        }

        @Override
        public NetStateBean[] newArray(int size) {
            return new NetStateBean[size];
        }
    };

    public boolean isNetAccess() {
        return netAccess;
    }

    public void setNetAccess(boolean netAccess) {
        this.netAccess = netAccess;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (netAccess ? 1 : 0));
    }
}
