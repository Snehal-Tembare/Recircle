package com.example.synerzip.recircle_android.utilities;

/**
 * Created by Prajakta Patil on 7/3/17.
 * Copyright © 2016 Synerzip. All rights reserved
 */
public class RCWebConstants {

//    public static final String RC_END_POINT = "http://5f0f2b95.ngrok.io";

    public static final int RC_ERROR_CODE_BAD_REQUEST = 400;

    public static final int RC_ERROR_CODE_FORBIDDEN = 403;

    public static final int RC_ERROR_CODE_NOT_FOUND = 404;

    public static final int RC_ERROR_CODE_CONFLICT = 409;

    public static final int RC_ERROR_CODE_INTERNAL_SERVER_ERROR = 500;

    public static final int RC_ERROR_CODE_SERVICE_UNAVAILABLE = 503;

    public static final String RC_GET_PRODUCT_DETAILS="/api/products";

    public static final String RC_SEARCH_PRODUCT="/api/products/searchProd?manufacturerId";

    public static final String RC_SEARCH_PRODUCT_TEXT="/api/products/searchProd?searchText";

}
