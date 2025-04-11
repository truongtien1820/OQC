package com.example.OQC.PageFragment;


import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.OQC.AdapterAndDetail.PageDetail.ImageCheckAdapter;
import com.example.OQC.AdapterAndDetail.PageDetail.ImagePODetail;
import com.example.OQC.AdapterAndDetail.PageSearch.DataPO;
import com.example.OQC.AdapterAndDetail.PageDetail.POAdapter;
import com.example.OQC.Constant_Class;
import com.example.OQC.Database.TableImageDateCode;
import com.example.OQC.Database.TableMain;
import com.example.OQC.Interface.OnItemClickListener;
import com.example.OQC.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PageSearchFragment extends Fragment  {
    private ViewGroup container;
    private String POIDOLD, POID;
    private SearchView searchView;
    private RecyclerView recyclerView_PO;;
    private Handler handler ;
    private Runnable searchTask;
    private List<DataPO> data_model_detail = new ArrayList<>();
    private POAdapter poAdapter;
    private List<Bitmap> imageList,imagedatecode;
    private static TableMain Cre_db = null;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imageDatecode;
    private int imageNumber = 1,solan = 1;
    private String status = "Free";
    private RecyclerView recyclerViewImagesCheck;
    private ImageCheckAdapter adapter;
    private Uri photoURI = null;
    private String maxIsNo = null ;
    private TableImageDateCode tableImageDateCode;
    private File photoFile;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pagesearch, container, false);
        this.container = container;



        addControls(view);
        addEvents();

        return view;
    }

    private void addEvents() {
        handler = new Handler();
        final long delay = 1000; // Thời gian đợi 0.5 giây
        searchTask = null; // Tác vụ tìm kiếm
        if (container != null) {
            Cre_db = new TableMain(container.getContext());
            Cre_db.open();
            Cre_db.openTable();
            tableImageDateCode = new TableImageDateCode(container.getContext());
            try{

                String table_name = "tc_imgdatecode_file";
                String table_schema = "tc_datecode001 TEXT, tc_datecode002 TEXT, tc_datecode003 TEXT, tc_datecode004 TEXT, tc_datecode005 TEXT, tc_datecode006 TEXT, tc_datecode007 TEXT , tc_datecode008 TEXT, tc_datecode009 TEXT";
                // Xóa bảng nếu tồn tại
                tableImageDateCode.createTemporaryTable(table_name, table_schema);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        POIDOLD = getSelectedPo();
        curDesplay(POIDOLD);
        poAdapter.notifyDataSetChanged();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Thực hiện tìm kiếm ngay khi người dùng nhấn Enter
                handler.removeCallbacks(searchTask);
                handleSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Hủy các tác vụ đang chờ xử lý
                if (searchTask != null) {
                    handler.removeCallbacks(searchTask);
                }

                // Đặt lại tác vụ sau 0.5 giây
                searchTask = new Runnable() {
                    @Override
                    public void run() {
                        handleSearch(newText);
                    }
                };
                handler.postDelayed(searchTask, delay);

                return true;
            }
        });
        poAdapter.setOnItemClickListenerDetial(new POAdapter.OnItemClickListenerDetial() {
            @Override
            public void onItemClick(DataPO item,String id) {
                PageSearchFragment.this.showDialogDetail(item, id);
            }


        });
    }

    private void addControls(View view) {
        recyclerView_PO = view.findViewById(R.id.recyclerView);
        recyclerView_PO.setLayoutManager(new LinearLayoutManager(getContext()));
        poAdapter = new POAdapter(data_model_detail);
        recyclerView_PO.setAdapter(poAdapter);
        searchView = view.findViewById(R.id.action_search);
        searchView.clearFocus();

    }
    private void handleSearch(String query) {
        // Xử lý logic tìm kiếm, ví dụ: lọc dữ liệu trong RecyclerView
        if (query.isEmpty()) {

        } else {
            if (!isValidFormat(query) ){
                Toast.makeText(getContext(), "Định dạng không hợp lệ. Vui lòng nhập đúng định dạng (VD: AA321-).", Toast.LENGTH_SHORT).show();
                return;
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        POID = query.trim();
                    Integer count  =  Cre_db.get_tc_infwno001( POID );
                    if (count > 0) {

                        curDesplay(POID);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Cập nhật giao diện
                                savePOChoice(POID);
                                poAdapter.notifyDataSetChanged();
                            }
                        });;
                            return;
                        }
                     URL url = new URL(Constant_Class.mbaseUrl    + "/getPOID.php?POID=" + POID);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(10000);
                    conn.setReadTimeout(10000);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    //conn.connect();
                    // Kiểm tra kết nối trước khi thực hiện kết nối
                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        String result = reader.readLine();
                        reader.close();
                        try {
                            JSONArray jsonarray = new JSONArray(result);
                            if (jsonarray.length() > 0) {
                                data_model_detail.clear();
                            }
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonObject = jsonarray.getJSONObject(i);
                                String g_tc_infwno001 = jsonObject.getString("tc_infwno001");
                                String g_tc_infwno002 = jsonObject.getString("tc_infwno002");
                                String g_tc_infwno003 = jsonObject.getString("tc_infwno003");
                                String g_tc_infwno004 = jsonObject.getString("tc_infwno004");
                                String g_tc_infwno005 = jsonObject.getString("tc_infwno005");
                                String g_tc_infwno006 = jsonObject.getString("tc_infwno006");
                                String g_tc_infwno007 = jsonObject.getString("tc_infwno007");
                                String g_tc_infwno008 = jsonObject.getString("tc_infwno008");
                                String g_tc_infwno009 = jsonObject.getString("tc_infwno009");
                                String g_tc_infwno010 = jsonObject.getString("tc_infwno010");
                                String g_tc_infwno011 = jsonObject.getString("tc_infwno011");
                                String g_tc_infwno012 = jsonObject.getString("tc_infwno012");
                                String g_tc_infwno013 = jsonObject.getString("tc_infwno013");
                                Cre_db.insert_tc_infwno(g_tc_infwno001, g_tc_infwno002, g_tc_infwno003, g_tc_infwno004, g_tc_infwno005, g_tc_infwno006, g_tc_infwno007,g_tc_infwno008,g_tc_infwno009,g_tc_infwno010,g_tc_infwno011,g_tc_infwno012,g_tc_infwno013);
                                data_model_detail.add(new DataPO(g_tc_infwno001, g_tc_infwno002, g_tc_infwno003, g_tc_infwno004, g_tc_infwno005, g_tc_infwno006 ,g_tc_infwno007, g_tc_infwno008, g_tc_infwno009, g_tc_infwno010, g_tc_infwno011, g_tc_infwno012, g_tc_infwno013));
                                selectImg(g_tc_infwno002,g_tc_infwno011);
                                savePOChoice(POID);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Cập nhật giao diện
                                poAdapter.notifyDataSetChanged();
                            }
                        });

                    }


                }catch (Exception e){
                e.printStackTrace();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(container.getContext(), "Có lỗi xảy ra khi thực hiện tìm kiếm.", Toast.LENGTH_SHORT).show();
                            }
                        });
                }
                }
            }).start();




        }
        }

    private void selectImg( String Hangmuc, String nhanhieu) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    URL url = new URL(Constant_Class.mbaseUrl    + "/getIMG.php?POID=" + POID + "&HANGMUC=" + Hangmuc + "&NHANHIEU=" + nhanhieu);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(10000);
                    conn.setReadTimeout(10000);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    //conn.connect();
                    // Kiểm tra kết nối trước khi thực hiện kết nối
                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        String result = reader.readLine();
                        reader.close();
                        try {
                            JSONArray jsonarray = new JSONArray(result);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonObject = jsonarray.getJSONObject(i);
                                String g_tc_tc_img001 = jsonObject.getString("TC_IMG001");
                                String g_tc_tc_img002 = jsonObject.getString("TC_IMG002");
                                String g_tc_tc_img003 = jsonObject.getString("TC_IMG003");
                                String g_tc_tc_img004 = jsonObject.getString("TC_IMG004");
                                String g_tc_tc_img005 = jsonObject.getString("TC_IMG005");
                                String g_tc_tc_img006 = jsonObject.getString("TC_IMG006");
                                String g_tc_tc_img007 = jsonObject.getString("TC_IMG007");
                                String g_tc_tc_img008 = jsonObject.getString("TC_IMG008");
                                String g_tc_tc_img009 = jsonObject.getString("TC_IMG009");
                                String g_tc_tc_img010 = jsonObject.getString("TC_IMG010");
                                String g_tc_tc_img011 = jsonObject.getString("TC_IMG011");
                                Cre_db.insert_tc_img(g_tc_tc_img001, g_tc_tc_img002, g_tc_tc_img003, g_tc_tc_img004, g_tc_tc_img005, g_tc_tc_img006, g_tc_tc_img007, g_tc_tc_img008, g_tc_tc_img009, g_tc_tc_img010, g_tc_tc_img011);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }catch (Exception e){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(container.getContext(), "Có lỗi xảy ra khi thực hiện tìm kiếm.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private boolean isValidFormat(String query) {
        // Kiểm tra định dạng: AA321-1901000047, BA312-1903000013,...
        String regex = "^[A-B]{2}\\d{3}-[0-9,*]{10}$";
        return query.matches(regex);
        }
    private void savePOChoice(String poID) {
        SharedPreferences preferences = container.getContext().getSharedPreferences("Poid", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("SelectedPo", poID);
        editor.apply();
    }
    private String getSelectedPo() {
        SharedPreferences preferences = container.getContext().getSharedPreferences("Poid", Context.MODE_PRIVATE);
        return preferences.getString("SelectedPo", "");
    }

    private void curDesplay(String poid){
        Cursor cur = Cre_db.get_tc_infwno(poid);
        cur.moveToFirst();
        int num = cur.getCount();
        data_model_detail.clear();
        for (int i = 0; i < num; i++) {
            String g_tc_infwno001 = cur.getString(cur.getColumnIndexOrThrow("tc_infwno001"));
            String g_tc_infwno002 = cur.getString(cur.getColumnIndexOrThrow("tc_infwno002"));
            String g_tc_infwno003 = cur.getString(cur.getColumnIndexOrThrow("tc_infwno003"));
            String g_tc_infwno004 = cur.getString(cur.getColumnIndexOrThrow("tc_infwno004"));
            String g_tc_infwno005 = cur.getString(cur.getColumnIndexOrThrow("tc_infwno005"));
            String g_tc_infwno006 = cur.getString(cur.getColumnIndexOrThrow("tc_infwno006"));
            String g_tc_infwno007 = cur.getString(cur.getColumnIndexOrThrow("tc_infwno007"));
            String g_tc_infwno008 = cur.getString(cur.getColumnIndexOrThrow("tc_infwno008"));
            String g_tc_infwno009 = cur.getString(cur.getColumnIndexOrThrow("tc_infwno009"));
            String g_tc_infwno010 = cur.getString(cur.getColumnIndexOrThrow("tc_infwno010"));
            String g_tc_infwno011 = cur.getString(cur.getColumnIndexOrThrow("tc_infwno011"));
            String g_tc_infwno012 = cur.getString(cur.getColumnIndexOrThrow("tc_infwno012"));
            String g_tc_infwno013 = cur.getString(cur.getColumnIndexOrThrow("tc_infwno013"));
            data_model_detail.add(new DataPO(g_tc_infwno001, g_tc_infwno002, g_tc_infwno003, g_tc_infwno004, g_tc_infwno005, g_tc_infwno006, g_tc_infwno007, g_tc_infwno008, g_tc_infwno009, g_tc_infwno010, g_tc_infwno011, g_tc_infwno012, g_tc_infwno013));
            cur.moveToNext();
        }
      }
    private void showDialogDetail(DataPO item, String id) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_datecode_image, null);
        LinearLayout linearLayout = view.findViewById(R.id.contentLayoutdatecode);
        linearLayout.setBackground(ContextCompat.getDrawable(requireContext().getApplicationContext(), R.drawable.curved_background_checkimage));
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setView(view);
        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable());
        }
        dialog.setCanceledOnTouchOutside(false);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        imageDatecode= view.findViewById(R.id.image_datecode);
        Button take_photo_datecode = view.findViewById(R.id.btn_take_datecode);
        take_photo_datecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(),
                            new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
                } else {
                    // Gọi hàm chụp ảnh
                    capturePhoto();

                }
            }
        });
        imageDatecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable drawable = (BitmapDrawable) imageDatecode.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                showFullScreenDialog(bitmap, -1, "top" ,item,id);

            }
        });


        imageList = new ArrayList<>();
        imagedatecode = new ArrayList<>();
//        List<String> imageFileNames = getImageNamesFromDatabase(item);
        recyclerViewImagesCheck = view.findViewById(R.id.rcv_datecode_check);
        recyclerViewImagesCheck.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        if (id == "0") {
            imageList = new ArrayList<>();


            maxIsNo = tableImageDateCode.getmaxisno(item.getG_tc_infwno001(), item.getG_tc_infwno002());
            if (maxIsNo != null) {
                solan = Integer.parseInt(maxIsNo) + 1;

            }

            adapter = new ImageCheckAdapter(imageList,id, new OnItemClickListener() {
                @Override
                public void onItemClick(Bitmap image, int   position) {

                    showFullScreenDialog(image, position, "",item,id);


                }

                @Override
                public void onDeleteClick(int position) {

                    imageList.remove(position);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position, imageList.size());
                }
            });
            recyclerViewImagesCheck.setAdapter(adapter);

            Button btnTakePhoto = view.findViewById(R.id.btn_take_photo_check);
            btnTakePhoto.setOnClickListener(v -> {
                if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
                } else {
                    openCamera();
                }
            });

            Button btnAchieved = view.findViewById(R.id.btnAchieved_datecode);
            btnAchieved.setOnClickListener(v -> {
                if (imageList.isEmpty()) {
                    Toast.makeText(view.getContext(), "Vui lòng chụp ảnh trước", Toast.LENGTH_SHORT).show();
                    return;
                }
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(new Date());
                for (Bitmap imagedate : imagedatecode) {
                    saveImageToGallery(imagedate, item,"Y","real",solan);
                    imageNumber++;
                }
                for (Bitmap image : imageList) {
                    saveImageToGallery(image, item,"Y","check",solan);

                    imageNumber++;
                }
                Toast.makeText(view.getContext(), "Đang lưu thông tin...", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                poAdapter.notifyDataSetChanged();
            });

            Button btnNotAchieved = view.findViewById(R.id.btnNoAchieved_datecode);
            btnNotAchieved.setOnClickListener(v -> {
                if (imageList.isEmpty()) {
                    Toast.makeText(view.getContext(), "Vui lòng chụp ảnh trước", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (Bitmap imagedate : imagedatecode) {
                    saveImageToGallery(imagedate, item,"N","real",solan);
                    imageNumber++;
                }
                for (Bitmap image : imageList) {
                    saveImageToGallery(image, item,"N","check",solan);

                    imageNumber++;
                }
                Toast.makeText(view.getContext(), "Đang lưu thông tin...", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                poAdapter.notifyDataSetChanged();

            });
            Button btnCancel = view.findViewById(R.id.btnCancel_datecode);
            btnCancel.setOnClickListener(v -> {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Xác nhận")
                        .setMessage("Bạn có chắc chắn muốn đóng hộp thoại?")
                        .setPositiveButton("Đồng ý", (dialogInterface, which) ->{
                            deleteAllImages();
                            dialog.dismiss();


                        } )
                        .setNegativeButton("Hủy", (dialogInterface, which) -> dialogInterface.dismiss())
                        .create().show();

            });

            dialog.show();





        }
        else if (id == "1") {
            maxIsNo = tableImageDateCode.getmaxisno(item.getG_tc_infwno001(), item.getG_tc_infwno002());
            if (maxIsNo == null)
            {
                Toast.makeText(view.getContext(), "Vui lòng kiểm tra lại thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            final int[] currentIndex = {0};
            List<String> Listdatecode = tableImageDateCode.getListDatecode(item.getG_tc_infwno001(), item.getG_tc_infwno002());



            imageList = new ArrayList<>();
            adapter = new ImageCheckAdapter(imageList,id, new OnItemClickListener() {
                @Override
                public void onItemClick(Bitmap image, int position) {

                    showFullScreenDialog(image, position,"", item,id);


                }

                @Override
                public void onDeleteClick(int position) {

                    imageList.remove(position);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position, imageList.size());
                }
            });
            recyclerViewImagesCheck.setAdapter(adapter);
            Button btnTakePhotodate = view.findViewById(R.id.btn_take_datecode);
            btnTakePhotodate.setVisibility(View.GONE);
            Button btnTakePhoto = view.findViewById(R.id.btn_take_photo_check);
            btnTakePhoto.setVisibility(View.GONE);
            Button btnAchieved = view.findViewById(R.id.btnAchieved_datecode);
            btnAchieved.setVisibility(View.GONE);

            Button btnNotAchieved = view.findViewById(R.id.btnNoAchieved_datecode);
            btnNotAchieved.setVisibility(view.GONE);
            Button btnCancel = view.findViewById(R.id.btnCancel_datecode);
            btnCancel.setOnClickListener(v -> {
                dialog.dismiss();

            });
            if (!Listdatecode.isEmpty()) {
                currentIndex[0] = 0; // Đặt vị trí mặc định là phần tử đầu tiên
                updateDialogData(view, Listdatecode.get(currentIndex[0]), item,Listdatecode.size());
            }

            GestureDetector gestureDetector = new GestureDetector(view.getContext(), new GestureDetector.SimpleOnGestureListener() {
                private static final int SWIPE_THRESHOLD = 100;
                private static final int SWIPE_VELOCITY_THRESHOLD = 100;

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    float diffX = e2.getX() - e1.getX();
                    float diffY = e2.getY() - e1.getY();

                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD && Math.abs(diffX) > Math.abs(diffY)) {

                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) { // Vuốt sang phải (quay lại PO trước)
                                if (currentIndex[0] > 0) {
                                    currentIndex[0]--;
                                    Log.d("GESTURE", "Vuốt phải, PO trước: " + currentIndex[0]);
                                    updateDialogData(view, Listdatecode.get(currentIndex[0]),item,Listdatecode.size());
                                }
                            } else { // Vuốt sang trái (tiến tới PO sau)
                                if (currentIndex[0] < Listdatecode.size() - 1) {
                                    currentIndex[0]++;
                                    updateDialogData(view, Listdatecode.get(currentIndex[0]),item,Listdatecode.size());
                                }
                            }
                            return true;
                        }
                    }
                    return false;
                }
            });


            // Gán GestureDetector vào View
            linearLayout.setOnTouchListener((v, event) -> {
                if (gestureDetector.onTouchEvent(event)) {
                    return true;
                }
                return v.performClick();  // Giữ lại hành vi mặc định của View
            });




            dialog.show();
        }

    }

    private final ActivityResultLauncher<Intent> takePhotoLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && photoURI != null) {
                    try {
                        Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), photoURI);
                        imageBitmap = rotateImageIfRequired(requireContext(), imageBitmap, photoURI);


                        imagedatecode.add(imageBitmap); // thêm vào danh sách nếu cần
                        imageDatecode.setImageBitmap(imageBitmap); // hiển thị lên ImageView
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Không thể đọc ảnh từ URI", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(), "Không chụp được ảnh", Toast.LENGTH_SHORT).show();
                }
            }
    );


    private void capturePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireContext().getPackageManager()) != null) {
            try {
                photoFile = createImageFile(); // tạo file thật để lưu
            } catch (IOException ex) {
                ex.printStackTrace();
                Toast.makeText(requireContext(), "Lỗi khi tạo file ảnh", Toast.LENGTH_SHORT).show();
                return;
            }

            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(
                        requireContext(),
                        "com.lelong.OQC.provider", // dùng đúng authority đã khai trong AndroidManifest
                        photoFile
                );
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePhotoLauncher.launch(takePictureIntent);
            }
        }
    }

    private List<String> getImageNamesFromDatabase(DataPO item, String index) {
        List<String> imageNames = new ArrayList<>();

        Cursor cursor = tableImageDateCode.getData("tc_imgdatecode_file","tc_datecode003", "tc_datecode001 = '" + item.getG_tc_infwno001() + "' AND tc_datecode002 = '" + item.getG_tc_infwno002() + "' AND tc_datecode009 = '" + index + "'" );
        if(cursor.moveToFirst()) {
            do {
                String imageName = cursor.getString(cursor.getColumnIndexOrThrow("tc_datecode003"));
                imageNames.add(imageName);
            } while(cursor.moveToNext());
        }
        cursor.close();
        return imageNames;
    }
    private Uri getImageUriByName(String imageName) {
        Uri mediaImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{

                MediaStore.Images.Media._ID
        };
        // Đảm bảo imageName chứa cả tên và phần mở rộng chẳng hạn như "image.png"
        String selection = MediaStore.Images.Media.DISPLAY_NAME + " = ?";
        String[] selectionArgs = new String[]{imageName};

        ContentResolver contentResolver =   requireContext().getContentResolver();
        Cursor cursor = contentResolver.query(mediaImageUri, projection, selection, selectionArgs, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                long id = cursor.getLong(idColumn);
                Uri imageUri = ContentUris.withAppendedId(mediaImageUri, id);
                cursor.close();
                return imageUri;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }
    private void showFullScreenDialog(Bitmap image, int position, String imageName, DataPO item,String id) {
        Dialog dialog = new Dialog(container.getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_full_image);

        ImageView fullImageView = dialog.findViewById(R.id.fullImageView);
        ImageView deleteImageView = dialog.findViewById(R.id.deleteImageView);
        fullImageView.setImageBitmap(image);

        // Đặt sự kiện onClick cho fullImageView để đóng dialog
        fullImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if (id == "1")
        {
            deleteImageView.setVisibility(View.GONE);
        }
        deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( imageName == "top"){
                    imagedatecode.remove(0);
                    imageDatecode.setImageResource(R.drawable.ic_placeholder);
                }else {
                    imageList.remove(position);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position, imageList.size());
                }
                dialog.dismiss();

            }

        });

        dialog.show();
    }
    private boolean deleteImage(Uri imageUri) {
        if (imageUri != null) {
            try {
                ContentResolver contentResolver = requireContext().getContentResolver();
                int deletedRows = contentResolver.delete(imageUri, null, null);
                return deletedRows > 0;
            } catch (SecurityException e) {
                Toast.makeText(container.getContext(), "DELETE IMAGES FAIL", Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }
    private void deleteAllImages() {
        // Thư mục mà ảnh được lưu
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_DCIM);

        if (storageDir != null) {
            // Lấy tất cả các file trong thư mục
            File[] files = storageDir.listFiles();

            if (files != null) {
                for (File file : files) {
                    // Đối với mỗi file, lấy URI tương ứng qua FileProvider
                    Uri imageUri = FileProvider.getUriForFile(container.getContext(),
                            "com.lelong.OQC.provider",
                            file);

                    // Xóa file sử dụng ContentResolver và URI
                    try {
                        requireContext().getContentResolver().delete(imageUri, null, null);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                        // Nếu có bất kỳ lỗi nào, hiển thị thông báo
                        Toast.makeText(container.getContext(), "Unable to delete image: " + file.getName(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireContext().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(
                        container.getContext(),
                        "com.lelong.OQC.provider",
                        photoFile
                );
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePhotoLauncherList.launch(takePictureIntent);
            }
        }
    }
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = timeStamp + "_";
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_DCIM);


        File image = File.createTempFile(
                imageFileName,
                ".png",
                storageDir
        );

        photoURI = Uri.fromFile(image);
        return image;
    }
    private final ActivityResultLauncher<Intent> takePhotoLauncherList = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && photoURI != null) {
                    try {
                        Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), photoURI);
                        imageBitmap = rotateImageIfRequired(requireContext(), imageBitmap, photoURI);
                        imageList.add(imageBitmap);
                        adapter.notifyDataSetChanged();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(container.getContext(), "Không thể đọc ảnh từ URI", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(container.getContext(), "Không chụp được ảnh", Toast.LENGTH_SHORT).show();
                }
            }
    );
    private void saveImageToGallery(Bitmap bitmapImage, DataPO item, String status, String type, int imageNumber) {
        ContentValues values = new ContentValues();

        String number = String.format("%02d", imageNumber);
        String nameimg = "";
        if (type.equals("real")){
            nameimg =  item.getG_tc_infwno001() + "_" + item.getG_tc_infwno002() +"_"+ number+"_"+ "reality"+"_"+ imageNumber+"_"+"datecode"+".png";
        }else {
            nameimg =  item.getG_tc_infwno001() + "_" + item.getG_tc_infwno002() +"_"+ number +"_"+ imageNumber+"_"+ "datecode"+".png";
        }
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(new Date());
        values.put(MediaStore.Images.Media.DISPLAY_NAME, nameimg);
        tableImageDateCode.insertData("tc_imgdatecode_file", "tc_datecode001 , tc_datecode002, tc_datecode003, tc_datecode004 , tc_datecode005 , tc_datecode006 , tc_datecode007 , tc_datecode008, tc_datecode009  ","'" + item.getG_tc_infwno001() + "'"+"," +"'"+item.getG_tc_infwno002()  + "'"+"," + "'"+nameimg+"'"+","+ "'"+timeStamp+"'"+","+ "'N'" +","+ "'"+ Constant_Class.UserID +"'" +","+ "'"+status+"',"+"'N'"+","+" '"+solan+"'");






        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        // Chú ý đến việc đặt IsPending với giá trị 1 nếu bạn đang thực hiện trên Android 10 hoặc cao hơn.
        values.put(MediaStore.Images.Media.IS_PENDING, 1);

        // Insert thông tin ảnh vào MediaStore
        ContentResolver resolver = requireContext().getContentResolver();
        Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        // Nếu Uri hợp lệ, tiến hành lưu ảnh
        if (uri != null) {
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmapImage, 1366, 768, false);
            try (OutputStream outputStream = resolver.openOutputStream(uri)) {
                // Nén và ghi dữ liệu bitmap vào outputStream thông qua Uri
                resizedBitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
                outputStream.flush();
                outputStream.close();
                deleteAllImages();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Cập nhật lại thông tin ảnh là không còn đang được "Pending"
            values.clear();
            values.put(MediaStore.Images.Media.IS_PENDING, 0);
            resolver.update(uri, values, null, null);



            // Gửi broadcast để cập nhật thư viện ảnh
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(uri);
            requireContext().sendBroadcast(mediaScanIntent);




        }
    }
    private void updateDialogData(View view, String index, DataPO item,Number size) {
        imageList.clear(); // Xóa danh sách ảnh cũ
        List<String> newImageFileNames = getImageNamesFromDatabase(item,index);
        for (String imageName : newImageFileNames) {
            try {
                Uri imageUri = getImageUriByName(imageName);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(view.getContext().getContentResolver(), imageUri);
                if (imageName.contains("reality")) {
                    imageDatecode.setImageBitmap(bitmap);
                }
                imageList.add(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(view.getContext(), "Không thể tải hình ảnh", Toast.LENGTH_SHORT).show();
            }
        }
        String status = tableImageDateCode.getstatus(item.getG_tc_infwno001(), item.getG_tc_infwno002(), index);
        String timecheck = tableImageDateCode.getTimeCheck(item.getG_tc_infwno001(), item.getG_tc_infwno002(), index);
        TextView id = view.findViewById(R.id.titlelist_datecode);
        TextView time = view.findViewById(R.id.titletimecheck_datecode);
        String stt = index + "/" + size;
        id.setText(stt);
        String timeText = "Thời gian kiểm tra: " + timecheck;
        time.setText(timeText);
        id.setVisibility(View.VISIBLE);
        time.setVisibility(View.VISIBLE);
        TextView title = view.findViewById(R.id.titleTextView_datecode);
        title.setText("Ảnh đã chụp: ");
        title.setVisibility(View.VISIBLE);
        ImageView imstatus = view.findViewById(R.id.Imagestatus_datecode);
        if (status.equals("Y")) {
            imstatus.setImageResource(R.drawable.confime);
            imstatus.setVisibility(View.VISIBLE);
        }else {
            imstatus.setImageResource(R.drawable.no_confime);
            imstatus.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged(); // Cập nhật RecyclerView
    }
    private Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {
        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei = new ExifInterface(input);

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }



}


