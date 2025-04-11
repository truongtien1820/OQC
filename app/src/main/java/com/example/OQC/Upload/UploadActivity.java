package com.example.OQC.Upload;

import android.content.Context;
import android.database.Cursor;

import com.example.OQC.Constant_Class;
import com.example.OQC.Database.TableImageCheck;
import com.example.OQC.Interface.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadActivity {
    private TableImageCheck tableImageCheck=  null;
    private UploadImages uploadimages = null;
    ApiInterface apiService;
    private Context context;
    private UploadDialog uploadDialog;
    private final String input_bdate;
    private final String input_edate;
    private final String input_POID;
    private final String input_PONO;

    public UploadActivity(Context context, UploadDialog uploadDialog, String input_bdate, String input_edate, String input_POID, String input_PONO) {
        this.context = context;
        this.uploadDialog = uploadDialog;
        this.input_bdate = input_bdate;
        this.input_edate = input_edate;
        this.input_POID = input_POID;
        this.input_PONO = input_PONO;

        tableImageCheck = new TableImageCheck(context);

        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.16.40.20/" + Constant_Class.server + "/ShippingOQCAPP/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiService = retrofit.create(ApiInterface.class);

        Call_transfer();
    }

    private void Call_transfer() {
        Cursor c_gettc_fct ;
            if (input_POID=="" && input_PONO == "" ) {
                c_gettc_fct    = tableImageCheck.getData("tc_imgcheck_file", "*", " SUBSTR(TC_IMG006, 1, 10) BETWEEN '" + input_bdate + "' AND '" + input_edate + "' AND TC_IMG007 = 'N'");
            }else {
                c_gettc_fct    = tableImageCheck.getData("tc_imgcheck_file", "*", "TC_IMG001= '" + input_POID + "' AND TC_IMG002 = '" + input_PONO + "' AND SUBSTR(TC_IMG006, 1, 10) BETWEEN '" + input_bdate + "' AND '" + input_edate + "' AND TC_IMG007 = 'N'");
            }
        if (c_gettc_fct.getCount() > 0) {

            JsonArray jarray_tc_fct = CursorToJsonConverter.cursorToJson(c_gettc_fct);

            JsonObject jsonObject = new JsonObject();
            jsonObject.add("jarr_tc_fct", jarray_tc_fct);


            Call<ResponseBody> call = apiService.sendDataToServer(jsonObject);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.isSuccessful()) {
                            InputStream inputStream = response.body().byteStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                            StringBuilder sb = new StringBuilder();
                            String line;

                            while (true) {
                                try {
                                    if (!((line = reader.readLine()) != null)) break;
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                sb.append(line);
                            }
                            String responseData = sb.toString(); // Dữ liệu JSON

                            // Sử dụng Gson để giải mã dữ liệu JSON thành JsonObjec
                            Gson gson = new Gson();
                            JsonObject jsonObject = gson.fromJson(responseData, JsonObject.class);

                            // Trích xuất các trường từ JSON
                            String status = jsonObject.get("status").getAsString();
                            String message = jsonObject.get("message").getAsString();

                            if (status.equals("success")) {
                                tableImageCheck.call_upd_tc_ìmg007(c_gettc_fct);
                                uploadimages = new UploadImages(context, c_gettc_fct, uploadDialog);
                            } else {
                                uploadDialog.setStatus(message);
                            }
                        } else {
                            // Xử lý lỗi
                            String s = String.valueOf(response.body());
                            uploadDialog.setStatus(s);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Xử lý khi có lỗi xảy ra trong quá trình gửi dữ liệu
                    String s = String.valueOf(t.toString());
                    uploadDialog.setStatus(s);
                }
            });

        } else {
            uploadDialog.setStatus("Không có dữ liệu cập nhật");
        }

    }


}