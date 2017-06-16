package com.example.synerzip.recircle_android.models.user_messages;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.synerzip.recircle_android.ui.messages.OwnerMsgFragment;

import java.util.ArrayList;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 2/6/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
@Getter
@Setter
public class OwnerRequestMsg implements Parcelable {
    private String user_prod_msg_id;

    private String user_product_id;

    private String user_id;

    private String user_msg;

    private String msg_type;

    private String created_at;

    private boolean is_read;

    private UserRequest user;

    private UserReqProdOrderDetail user_prod_order_detail;

    private ArrayList<UserReqProdMsgPool> user_prod_msg_pools;

    private UserReqProduct user_product;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.user_prod_msg_id);
        dest.writeString(this.user_product_id);
        dest.writeString(this.user_id);
        dest.writeString(this.user_msg);
        dest.writeString(this.msg_type);
        dest.writeString(this.created_at);
        dest.writeByte(this.is_read ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.user_prod_order_detail, flags);
        dest.writeList(this.user_prod_msg_pools);
        dest.writeParcelable(this.user_product, flags);
    }

    public OwnerRequestMsg() {
    }

    protected OwnerRequestMsg(Parcel in) {
        this.user_prod_msg_id = in.readString();
        this.user_product_id = in.readString();
        this.user_id = in.readString();
        this.user_msg = in.readString();
        this.msg_type = in.readString();
        this.created_at = in.readString();
        this.is_read = in.readByte() != 0;
        this.user = in.readParcelable(UserRequest.class.getClassLoader());
        this.user_prod_order_detail = in.readParcelable(UserReqProdOrderDetail.class.getClassLoader());
        this.user_prod_msg_pools = new ArrayList<UserReqProdMsgPool>();
        in.readList(this.user_prod_msg_pools, UserReqProdMsgPool.class.getClassLoader());
        this.user_product = in.readParcelable(UserReqProduct.class.getClassLoader());
    }

    public static final Parcelable.Creator<OwnerRequestMsg> CREATOR = new Parcelable.Creator<OwnerRequestMsg>() {
        @Override
        public OwnerRequestMsg createFromParcel(Parcel source) {
            return new OwnerRequestMsg(source);
        }

        @Override
        public OwnerRequestMsg[] newArray(int size) {
            return new OwnerRequestMsg[size];
        }
    };
}
