package com.example.OQC.Interface;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    //Đẩy dữ liệu lên server
    @POST("uploadDatatToOracle.php")
    Call<ResponseBody> sendDataToServer(@Body JsonObject jsonObject);

    @Multipart
    @POST("uploadImages.php")
    Call<ResponseBody> uploadImage(
            @Part MultipartBody.Part image,
            @Part("description") RequestBody description
    );

}


