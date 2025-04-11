package com.example.OQC.Upload;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.widget.Toast;

import com.example.OQC.Constant_Class;
import com.example.OQC.Database.TableImageCheck;
import com.example.OQC.Interface.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadImages  {
     ApiInterface apiService;
    private final Context context;
    private final Cursor c_gettc_img;
    private UploadDialog uploadDialog;

    public UploadImages(Context context, Cursor c_gettc_img, UploadDialog uploadDialog) {
        this.context = context;
        this.c_gettc_img = c_gettc_img;
        this.uploadDialog = uploadDialog;


        Gson gson = new GsonBuilder().create();

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS) // Ví dụ: timeout sau 60 giây
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.16.40.20/" + Constant_Class.server + "/ShippingOQCAPP/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiService = retrofit.create(ApiInterface.class);

        if (c_gettc_img.getCount() > 0) {
            Call_transPhoto();
        }
    }

    private void Call_transPhoto() {
        c_gettc_img.moveToFirst();
        uploadDialog.setProgressBar(c_gettc_img.getCount());
        // Sử dụng một danh sách các tệp tin cần tải lên
        List<FileInfo> filesToUpload = new ArrayList<>();
        for (int i = 0; i < c_gettc_img.getCount(); i++) {
            String image_name= c_gettc_img.getString(c_gettc_img.getColumnIndexOrThrow("TC_IMG005"));



            File file = new File(getImagePathFromName(image_name));
            //filesToUpload.add(file);

            // Tạo một đối tượng FileInfo từ thông tin tên tệp và ngày
            FileInfo fileInfo = new FileInfo(image_name,file);

            filesToUpload.add(fileInfo);
            c_gettc_img.moveToNext();
        }

        uploadFileRecursive(filesToUpload, 0);

    }
    private String getImagePathFromName(String imageName) {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media.DATA},
                MediaStore.Images.Media.DISPLAY_NAME + "=?",
                new String[]{imageName},
                null);

        String imagePath = "";
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            imagePath = cursor.getString(columnIndex);
            cursor.close();
        }
        return imagePath;
    }
    public class FileInfo {


        private final String image_name;
        private final File file;


        public String getImage_name() {
            return image_name;
        }

        public File getFilePath() {
            return file;
        }

        public FileInfo( String image_name, File file) {
            this.image_name = image_name;
            this.file = file;
        }

    }

    // Hàm đệ quy để tải lên từng tệp tin một
    void uploadFileRecursive(final List<FileInfo> files, final int currentIndex) {
        if (currentIndex >= files.size()) {
            uploadDialog.setStatus("2");
            uploadDialog.setVisibleProgressBar(2);
            uploadDialog.setEnableBtn(false,true);
            // Tất cả tệp tin đã được tải lên
            return;
        }

        FileInfo fileToUpload = files.get(currentIndex);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileToUpload.getFilePath());
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", fileToUpload.getImage_name(), requestFile);

        Call<ResponseBody> callImage = apiService.uploadImage(imagePart, null);
        callImage.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Xử lý kết quả ở đây
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
                    // Sử dụng Gson để phân tích dữ liệu JSON thành đối tượng
                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.fromJson(responseData, JsonObject.class);

                    // Trích xuất các trường từ JSON
                    String status = jsonObject.get("status").getAsString();
                    String message = jsonObject.get("message").getAsString();

                    if (status.equals("OK")) {
                        // tableImageCheck.update_tc_imgpost(fileToUpload.image_no,fileToUpload.image_date,fileToUpload.image_dept,fileToUpload.image_employ,fileToUpload.image_name);
                        uploadDialog.updateProgressBar(currentIndex + 1);
                        // Gọi đệ quy để tải lên tệp tin tiếp theo
                        uploadFileRecursive(files, currentIndex + 1);
                    } else {
                        Toast.makeText(context, "Lỗi : " + message, Toast.LENGTH_LONG).show();
                    }


                } else {
                    // Xử lý lỗi ở đây
                    Toast.makeText(context, "Lỗi : " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Xử lý khi có lỗi xảy ra trong quá trình gửi dữ liệu

                // Gọi đệ quy để tải lên tệp tin tiếp theo
                uploadFileRecursive(files, currentIndex + 1);
            }
        });
    }
}