package com.example.synerzip.recircle_android.network;

import android.content.Context;
import android.util.Log;

import com.example.synerzip.recircle_android.utilities.RCWebConstants;

import java.io.IOException;

import okhttp3.CertificatePinner;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Snehal Tembare on 7/3/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

public class RCRestAdapter {
    private static RCRestAdapter rcRestAdapter;
    private static Context mContext;




    /**
     * Get the object of singleton class
     * @return object
    */

    public static RCRestAdapter getInStance(Context context){
        mContext=context;
        if (null==rcRestAdapter){
            rcRestAdapter=new RCRestAdapter();
            rcRestAdapter.setRcEndPoint();
        }
        return rcRestAdapter;
    }

    /**
     * Set RC endpoint
    */
    private void setRcEndPoint() {
        Interceptor interceptor=new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response=chain.proceed(chain.request());
                if (response.code()== RCWebConstants.RC_ERROR_CODE_FORBIDDEN){
                    Log.d("dj","forbidden");

                    /**
                     * TODO for relogin os session expires
                    */
//                    reLogin();
                }
                return response;
            }
        };

        OkHttpClient okHttpClient=new OkHttpClient();
        okHttpClient.interceptors().add(interceptor);

//        Retrofit builder=new Retrofit.Builder()
//                .baseUrl("http://api.nuuneoi.com/base/")
//                .build();

        OkHttpClient client = new OkHttpClient.Builder()
                .certificatePinner(new CertificatePinner.Builder()
                        .add("publicobject.com", "sha1/DmxUShsZuNiqPQsX2Oi9uv2sCnw=")
                        .add("publicobject.com", "sha1/SXxoaOSEzPC6BgGmxAt/EAcsajw=")
                        .add("publicobject.com", "sha1/blhOM3W9V/bVQhsWAcLYwPU6n24=")
                        .add("publicobject.com", "sha1/T5x9IXmcrQ7YuQxXnxoCmeeQ84c=")
                        .build())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.nuuneoi.com/base/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();




    }

    /**
     * Returns the api interface object
     * @return object
    */

    public RCRestAdapter getApiCall(){
        return rcRestAdapter;
    }

    public static void clear(){
        rcRestAdapter=null;
    }


}
