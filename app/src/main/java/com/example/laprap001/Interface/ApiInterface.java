package com.example.laprap001.Interface;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {

    //Kiểm tra phiên bản ứng dụng
    @GET("check_ver.php")
    Call<JsonObject> checkAppVersion(@Query("app") String appName);


    //Lấy thông tin nhân viên
    @GET
    Call<List<JsonObject>> getData(@Url String url);

    //Lấy dữ liệu cơ bản của các table
    @GET
    Call<JsonArray> getDataTable(@Url String url);


}


