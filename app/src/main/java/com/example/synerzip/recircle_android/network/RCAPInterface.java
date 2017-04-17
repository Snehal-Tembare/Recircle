package com.example.synerzip.recircle_android.network;

import com.example.synerzip.recircle_android.models.AllProductInfo;
import com.example.synerzip.recircle_android.models.ForgotPwdRequest;
import com.example.synerzip.recircle_android.models.ListAnItemRequest;
import com.example.synerzip.recircle_android.models.LogInRequest;
import com.example.synerzip.recircle_android.models.RootObject;
import com.example.synerzip.recircle_android.models.SearchProduct;
import com.example.synerzip.recircle_android.models.SignUpRequest;
import com.example.synerzip.recircle_android.models.User;
import com.example.synerzip.recircle_android.models.ZipcodeRoot;
import com.example.synerzip.recircle_android.utilities.RCWebConstants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by Snehal Tembare on 7/3/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public interface RCAPInterface {
    /**
     * Get all products details
     *
     * @return
     */
    @GET(RCWebConstants.RC_GET_PRODUCT_DETAILS)
    Call<AllProductInfo> getProductDetails();

    /**
     * Search product
     *
     * @param productId
     * @param manufacturerId
     * @param searchText
     * @return
     */
    @GET(RCWebConstants.RC_SEARCH_PRODUCT)
    Call<SearchProduct> searchProduct(@Query("productId") String productId,
                                      @Query("manufacturerId") String manufacturerId,
                                      @Query("searchText") String searchText,
                                      @Query("searchFromDate") String fromDate,
                                      @Query("searchToDate") String toDate);

    /**
     * Get all product names
     *
     * @return
     */
    @GET(RCWebConstants.RC_PRODUCT_NAMES)
    Call<RootObject> productNames();

    /**
     * user sign up
     *
     * @param signUpRequest
     * @return
     */
    @POST(RCWebConstants.RC_USER_SIGN_UP)
    Call<User> userSignUp(@Body SignUpRequest signUpRequest);

    /**
     * user log in
     *
     * @param logInRequest
     * @return
     */
    @POST(RCWebConstants.RC_USER_LOG_IN)
    Call<User> userLogIn(@Body LogInRequest logInRequest);

    /**
     * get verification code
     *
     * @param email
     * @param mobileNo
     * @return
     */
    @GET(RCWebConstants.RC_GET_OTP)
    Call<User> verificationCode(@Query("email") String email,
                                @Query("user_mob_no") long mobileNo);

    /**
     * get otp for forgot password
     *
     * @param userName
     * @return
     */
    @GET(RCWebConstants.RC_FORGOT_PWD)
    Call<User> otpForgotPassword(@Query("user_name") String userName);

    /**
     * reset password
     *
     * @param forgotPwdRequest
     * @return
     */
    @PUT(RCWebConstants.RC_FORGOT_PWD)
    Call<User> forgotPassword(@Body ForgotPwdRequest forgotPwdRequest);

    /**
     * list an item
     *
     * @param listAnItemRequest
     * @return
     */
    @POST(RCWebConstants.RC_GET_PRODUCT_DETAILS)
    Call<AllProductInfo> listAnItem(@Header("Authorization") String token, @Body ListAnItemRequest listAnItemRequest);

    /**
     * get zipcodes from google api
     *
     * @param zipcode
     * @return
     */
    @GET(RCWebConstants.RC_GOOGLE_ZIPCODES)
    Call<ZipcodeRoot> zipcodeCheck(@Query("address") long zipcode);
}
