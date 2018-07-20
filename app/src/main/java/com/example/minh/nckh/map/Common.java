package com.example.minh.nckh.map;

/**
 * Created by LaVanDuc on 3/26/2018.
 */

public class Common {
    public static final String baseURL="https://googleapis.com";
    public static IGoogleApi getGoogleApi(){
        return RetrofitClient.getClient(baseURL).create(IGoogleApi.class);
    }
}
