package com.example.synerzip.recircle_android.models.user_messages;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.synerzip.recircle_android.models.Product;
import com.example.synerzip.recircle_android.models.User;

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
public class ProdRelatedMsg implements Parcelable {

    private String user_prod_msg_id;

    private String user_product_id;

    private String user_id;

    private String user_msg;

    private String msg_type;

    private boolean is_read;

    private Date created_at;

    private User user;

    private UserProdOrderDetail user_prod_order_detail;

    private ArrayList<UserProdMsgPool> user_prod_msg_pools;

    private Product user_product;

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
        dest.writeByte(this.is_read ? (byte) 1 : (byte) 0);
        dest.writeLong(this.created_at != null ? this.created_at.getTime() : -1);
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.user_prod_order_detail, flags);
        dest.writeList(this.user_prod_msg_pools);
        dest.writeParcelable(this.user_product, flags);
    }

    public ProdRelatedMsg() {
    }

    protected ProdRelatedMsg(Parcel in) {
        this.user_prod_msg_id = in.readString();
        this.user_product_id = in.readString();
        this.user_id = in.readString();
        this.user_msg = in.readString();
        this.msg_type = in.readString();
        this.is_read = in.readByte() != 0;
        long tmpCreated_at = in.readLong();
        this.created_at = tmpCreated_at == -1 ? null : new Date(tmpCreated_at);
        this.user = in.readParcelable(User.class.getClassLoader());
        this.user_prod_order_detail = in.readParcelable(UserProdOrderDetail.class.getClassLoader());
        this.user_prod_msg_pools = new ArrayList<>();
        in.readList(this.user_prod_msg_pools, UserProdMsgPool.class.getClassLoader());
        this.user_product = in.readParcelable(Product.class.getClassLoader());
    }

    public static final Parcelable.Creator<ProdRelatedMsg> CREATOR = new Parcelable.Creator<ProdRelatedMsg>() {
        @Override
        public ProdRelatedMsg createFromParcel(Parcel source) {
            return new ProdRelatedMsg(source);
        }

        @Override
        public ProdRelatedMsg[] newArray(int size) {
            return new ProdRelatedMsg[size];
        }
    };
}
