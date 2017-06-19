package com.example.synerzip.recircle_android.models.user_messages;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 2/6/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
@Getter
@Setter
public class RootMessageInfo implements Parcelable {

    private ArrayList<OwnerProdRelatedMsg> ownerProdRelatedMsgs;

    private ArrayList<OwnerProdRelatedMsg> ownerRequestMsgs;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.ownerProdRelatedMsgs);
        dest.writeTypedList(this.ownerRequestMsgs);
    }

    public RootMessageInfo() {
    }

    protected RootMessageInfo(Parcel in) {
        this.ownerProdRelatedMsgs = in.createTypedArrayList(OwnerProdRelatedMsg.CREATOR);
        this.ownerRequestMsgs = in.createTypedArrayList(OwnerProdRelatedMsg.CREATOR);
    }

    public static final Parcelable.Creator<RootMessageInfo> CREATOR = new Parcelable.Creator<RootMessageInfo>() {
        @Override
        public RootMessageInfo createFromParcel(Parcel source) {
            return new RootMessageInfo(source);
        }

        @Override
        public RootMessageInfo[] newArray(int size) {
            return new RootMessageInfo[size];
        }
    };
}