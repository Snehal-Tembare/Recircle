package com.example.synerzip.recircle_android.utilities;

/**
 * Created by Prajakta Patil on 7/3/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */
public class RCWebConstants {

    public static final String RC_BASE_URL = "  https://api.recirc.com";

    public static final int RC_SUCCESS_CODE = 200;

    public static final int RC_ERROR_CODE_BAD_REQUEST = 400;

    public static final int RC_ERROR_CODE_FORBIDDEN = 403;

    public static final int RC_ERROR_CODE_NOT_FOUND = 404;

    public static final int RC_ERROR_CODE_CONFLICT = 409;

    public static final int RC_ERROR_CODE_INTERNAL_SERVER_ERROR = 500;

    public static final int RC_ERROR_CODE_SERVICE_UNAVAILABLE = 503;

    public static final int RC_ERROR_UNAUTHORISED = 401;

    public static final String RC_GET_PRODUCT_DETAILS = "/api/products";

    public static final String RC_SEARCH_PRODUCT = "/api/products/searchProd?city=austin";

    public static final String RC_PRODUCT_NAMES = "/api/products/prodNames";

    public static final String RC_PRODUCT_DETAILS = "/api/products/{userProductId}";

    public static final String RC_RENT_ITEM = "/api/products/rentItem";

    public static final String RC_USER_SIGN_UP = "/api/users";

    public static final String RC_USER_LOG_IN = "/api/users/signin";

    public static final String RC_FORGOT_PWD = "/api/users/forgotPwd";

    public static final String RC_GET_OTP = "/api/users/otp?";

    public static final String RC_GOOGLE_ZIPCODES = "https://maps.googleapis.com/maps/api/geocode/json?";

    public static final String RC_ORDER_DETAILS = "/api/products/userProd/OrderDetails";

    public static final String RC_EDIT_USER = "/api/users";

    public static final String RC_CHANGE_PWD = "/api/users/changePwd";

    public static final String RC_GET_USER_DETAILS = "/api/users/{userId}";

    public static final String RC_EDIT_CREDIT_CARD = "/api/stripe/:userPaymentMethodId";

    public static final String RC_VERIFY_EDIT_MOB_NO = "/api/users/{otp}";

    public static final String RC_GET_ALL_USER_MESSAGES = "/api/user_message/{userProdMsgId}";

    public static final String RC_USER_MESSAGE = "/api/user_message";

    public static final String RC_USER_QUE_ANS = "/api/user_message";

    public static final String RC_ACTION_ON_USER_REQUEST = "/api/user_message/{userProdMsgId}";

    public static final String RC_GET_USER_PRODUCT_PROFILE = "/api/products/userProducts/{userId}";

    public static final String RC_CANCEL_ORDER = "/api/products/cancelOrder";

    public static final String RC_EDIT_PRODUCT_DETAILS = "/api/products";

    public static final String RC_CANCEL_PROD_REQUEST = "/api/products/cancelOrder";
}
