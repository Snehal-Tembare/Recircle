package com.example.synerzip.recircle_android.models;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Snehal Tembare on 4/4/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

@Getter
@Setter

public class UserProdReview implements Parcelable{
    private String reviewer_id;
    private Integer prod_rating;
    private String prod_review;
    private String review_date;
    private User user;

    protected UserProdReview(Parcel in) {
        reviewer_id = in.readString();
        prod_review = in.readString();
        prod_rating = in.readInt();
        review_date = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<UserProdReview> CREATOR = new Creator<UserProdReview>() {
        @Override
        public UserProdReview createFromParcel(Parcel in) {
            return new UserProdReview(in);
        }

        @Override
        public UserProdReview[] newArray(int size) {
            return new UserProdReview[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reviewer_id);
        dest.writeString(prod_review);
        dest.writeInt(prod_rating);
        dest.writeString(review_date);
        dest.writeParcelable(user, flags);
    }
}
