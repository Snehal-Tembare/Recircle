package com.example.synerzip.recircle_android.network;

import com.example.synerzip.recircle_android.models.AllProductInfo;
import com.example.synerzip.recircle_android.models.ChangePwdRequest;
import com.example.synerzip.recircle_android.models.ForgotPwdRequest;
import com.example.synerzip.recircle_android.models.ListAnItemRequest;
import com.example.synerzip.recircle_android.models.LogInRequest;
import com.example.synerzip.recircle_android.models.OrderDetails;
import com.example.synerzip.recircle_android.models.Products;
import com.example.synerzip.recircle_android.models.RentItem;
import com.example.synerzip.recircle_android.models.RootProductsData;
import com.example.synerzip.recircle_android.models.RootUserInfo;
import com.example.synerzip.recircle_android.models.SearchProduct;
import com.example.synerzip.recircle_android.models.SignUpRequest;
import com.example.synerzip.recircle_android.models.User;
import com.example.synerzip.recircle_android.models.user_messages.UserAskQueResponse;
import com.example.synerzip.recircle_android.models.user_messages.RootMessageInfo;
import com.example.synerzip.recircle_android.models.user_messages.UserAskQueRequest;
import com.example.synerzip.recircle_android.models.ZipcodeRoot;
import com.example.synerzip.recircle_android.utilities.RCWebConstants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Snehal Tembare on 7/3/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public interface RCAPInterface {

    /**
     * Get all products details
     */
    @GET(RCWebConstants.RC_GET_PRODUCT_DETAILS)
    Call<AllProductInfo> getProductDetails();

    /**
     * Search product
     *
     * @param productId
     * @param manufacturerId
     * @param searchText
     */
    @GET(RCWebConstants.RC_SEARCH_PRODUCT)
    Call<SearchProduct> searchProduct(@Query("userProductMsgId") String productId,
                                      @Query("manufacturerId") String manufacturerId,
                                      @Query("searchText") String searchText,
                                      @Query("searchFromDate") String fromDate,
                                      @Query("searchToDate") String toDate);

    /**
     * Get all product names
     */
    @GET(RCWebConstants.RC_PRODUCT_NAMES)
    Call<RootProductsData> productNames();

    /**
     * user sign up
     *
     * @param signUpRequest
     */

    @POST(RCWebConstants.RC_USER_SIGN_UP)
    Call<User> userSignUp(@Body SignUpRequest signUpRequest);

    /**
     * user log in
     *
     * @param logInRequest
     */
    @POST(RCWebConstants.RC_USER_LOG_IN)
    Call<User> userLogIn(@Body LogInRequest logInRequest);

    /**
     * get verification code
     *
     * @param email
     * @param mobileNo
     */
    @GET(RCWebConstants.RC_GET_OTP)
    Call<User> verificationCode(@Query("email") String email,
                                @Query("user_mob_no") long mobileNo);

    /**
     * get otp for forgot password
     *
     * @param userName
     */
    @GET(RCWebConstants.RC_FORGOT_PWD)
    Call<User> otpForgotPassword(@Query("user_name") String userName);

    /**
     * reset password
     *
     * @param forgotPwdRequest
     */
    @PUT(RCWebConstants.RC_FORGOT_PWD)
    Call<User> forgotPassword(@Body ForgotPwdRequest forgotPwdRequest);

    /**
     * list an item
     *
     * @param listAnItemRequest
     */
    @POST(RCWebConstants.RC_GET_PRODUCT_DETAILS)
    Call<AllProductInfo> listAnItem(@Header("Authorization") String token,
                                    @Body ListAnItemRequest listAnItemRequest);

    /**
     * get zipcodes from google api
     *
     * @param zipcode
     */
    @GET(RCWebConstants.RC_GOOGLE_ZIPCODES)
    Call<ZipcodeRoot> zipcodeCheck(@Query("address") long zipcode);

    /**
     * Get product details
     *
     * @param userProductId
     */

    @GET(RCWebConstants.RC_PRODUCT_DETAILS)
    Call<Products> getProductDetailsByID(@Path("userProductId") String userProductId);

    /**
     * edit user details
     *
     * @param userDetailsRequest
     */
    @PUT(RCWebConstants.RC_EDIT_USER)
    Call<RootUserInfo> editUser(@Header("Authorization") String token,@Body RootUserInfo userDetailsRequest);

    /**
     * Request for change password
     *
     * @param token
     * @param changePwdRequest
     */

    @PUT(RCWebConstants.RC_CHANGE_PWD)
    Call<User> changePwd(@Header("Authorization") String token,
                         @Body ChangePwdRequest changePwdRequest);

    /**
     * get user details by userId
     */

    @GET(RCWebConstants.RC_GET_USER_DETAILS)
    Call<RootUserInfo> getUserDetails(@Header("Authorization") String token,
                                      @Path("userId") String userId);
    /**
     * Request for Rent and Item
     *
     * @param rentItem
     */

    @POST(RCWebConstants.RC_RENT_ITEM)
    Call<RentItem> rentItem(@Header("Authorization") String token,
                            @Body RentItem rentItem);

    /**
     * otp to verify user mobile number
     * @param token
     * @return
     */
    @GET(RCWebConstants.RC_VERIFY_EDIT_MOB_NO)
    Call<RootUserInfo> verifyUserMobNo(@Header("Authorization") String token);

    /**
     * get user message
     * @param token
     * @return
     */
    @GET(RCWebConstants.RC_USER_MESSAGE)
    Call<RootMessageInfo> getUserMessage(@Header("Authorization") String token);

    /**
     * get user question and answer
     * @param token
     * @return
     */
    @POST(RCWebConstants.RC_USER_QUE_ANS)
    Call<RootMessageInfo> getUserQueAns(@Header("Authorization") String token,
                                        @Body UserAskQueRequest userAskQueRequest);

    /**
     * api call to reply to user
     * @param token
     * @param userAskQueResponse
     * @return
     */
    @POST(RCWebConstants.RC_USER_QUE_ANS)
    Call<RootMessageInfo> getMsgResponse(@Header("Authorization") String token,
                                         @Body UserAskQueResponse userAskQueResponse);

    /**
     *  Get order details
     * @param token
     * @return
     */
    @GET(RCWebConstants.RC_ORDER_DETAILS)
    Call<OrderDetails> getOrderDetails(@Header("Authorization") String token);
}
