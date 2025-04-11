package com.example.OQC.PageFragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.app.Dialog;
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
import android.os.Bundle;
import android.os.Environment;
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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.OQC.AdapterAndDetail.PageDetail.DetailPOAdapter;
import com.example.OQC.AdapterAndDetail.PageDetail.ImageCheckAdapter;
import com.example.OQC.AdapterAndDetail.PageDetail.ImagePODetail;
import com.example.OQC.Constant_Class;
import com.example.OQC.Database.TableImageCheck;
import com.example.OQC.Database.TableImageOQC;
import com.example.OQC.Database.TableImageTemp;
import com.example.OQC.Database.TableMain;
import com.example.OQC.Interface.OnItemClickListener;
import com.example.OQC.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PageDetailPOFragment extends Fragment {
    private  String PO_ID ,PO_NO;
    private TextView inf_PO;
    private ViewGroup container;
    private RecyclerView recyclerView;
    private List<ImagePODetail> imagePoDetails = new ArrayList<>();
    private DetailPOAdapter detailPoAdapter;
    private RecyclerView recyclerViewImagesCheck;
    private ImageCheckAdapter adapter;
    private List<Bitmap> imageList, imageListedit;
    private static TableImageTemp tb_tmp = null;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String poid;
    private OnPhotoTakenListener photoTakenListener;
    private int imageNumber = 1,solan = 1;
    private Uri photoURI = null;
    private TableImageCheck tableImageCheck;
    private TableImageOQC tableImageOQC;
    private TextView title;
    private TableMain tableMain;
    private String mvlnho;
    private String status = "Free";
    private String OQC_NO =  null;
    private String maxIsNo = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pagedetail_po, container, false);
        this.container = container;
        // Khởi tạo Create_Table_main với Context từ container


        addControls(view);
        addEvents();

        return view;
    }

    private void addEvents() {
        Cursor cur = null;
        try {
             cur = tb_tmp.getData("tc_img_temp", "TC_IMG001,TC_IMG002, TC_IMG003, TC_IMG004, TC_IMG005, TC_IMG006",
                    "TC_IMG001 = '" + PO_ID + "' AND TC_IMG002 = '" + PO_NO + "'");
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            if (cur != null && cur.moveToFirst()) {
                imagePoDetails.clear();
                do {
                    String g_tc_img003 = cur.getString(cur.getColumnIndexOrThrow("TC_IMG003"));
                    String g_tc_img004 = cur.getString(cur.getColumnIndexOrThrow("TC_IMG004"));
                    String g_tc_img005 = cur.getString(cur.getColumnIndexOrThrow("TC_IMG005"));
                    String g_tc_img006 = cur.getString(cur.getColumnIndexOrThrow("TC_IMG006"));
                    imagePoDetails.add(new ImagePODetail(g_tc_img004 +" "+ g_tc_img005, g_tc_img003,g_tc_img006));
                } while (cur.moveToNext());
                detailPoAdapter.notifyDataSetChanged();
            }
        } finally {
            if (cur != null) {

                cur.close();
            }
        }
        detailPoAdapter.setOnItemClickListener(new DetailPOAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ImagePODetail item, String id) {
                showDialog(item, id);
            }
        });
    }


    private void addControls(View view) {
        PO_ID = getArguments().getString("PO_ID");
        PO_NO = getArguments().getString("PO_NO");
        if (PO_ID == null && PO_NO == null){
            PO_ID = getSelectedPoID();
            PO_NO = getSelectedPoNO();
        }else{
            savePOChoice(PO_ID,PO_NO);
        }

        tb_tmp = new TableImageTemp(container.getContext());
        tableMain = new TableMain(container.getContext());
        tableMain.open();
        recyclerView = view.findViewById(R.id.rcv_img); // Sử dụng biến toàn cục
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        detailPoAdapter = new DetailPOAdapter(imagePoDetails, view.getContext(),PO_ID,PO_NO);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(detailPoAdapter);
        inf_PO = view.findViewById(R.id.inf_PO);
        String infPO = PO_ID + "-" + PO_NO;
        inf_PO.setText(infPO);
        tableImageCheck = new TableImageCheck(container.getContext());
        try{

            String table_name = "tc_imgcheck_file";
            String table_schema = "TC_IMG001 TEXT, TC_IMG002 TEXT, TC_IMG003 TEXT, TC_IMG004 TEXT, TC_IMG005 TEXT, TC_IMG006 TEXT, TC_IMG007 TEXT , TC_IMG008 TEXT, TC_IMG009 TEXT, TC_IMG010 TEXT, TC_IMG011 TEXT";
            // Xóa bảng nếu tồn tại
            tableImageCheck.createTemporaryTable(table_name, table_schema);

        }catch (Exception e){
            e.printStackTrace();
        }
        tableImageOQC = new TableImageOQC(container.getContext());
        try{

            String table_name = "tc_OQC_file";
            String table_schema = "TC_OQC001 TEXT, TC_OQC002 TEXT, TC_OQC003 TEXT, TC_OQC004 TEXT, TC_OQC005 TEXT, TC_OQC006 TEXT, TC_OQC007 TEXT , TC_OQC008 TEXT  , TC_OQC009 TEXT  ";
            // Xóa bảng nếu tồn tại
            tableImageOQC.createTemporaryTable(table_name, table_schema);

        }catch (Exception e){
            e.printStackTrace();
        }


    }
    private void showDialog(ImagePODetail item, String id) {

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_compare_image, null);
        LinearLayout linearLayout = view.findViewById(R.id.contentLayout);
        linearLayout.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.curved_background_checkimage));

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setView(view);
        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable());
        }
        dialog.setCanceledOnTouchOutside(false); // Ngăn dialog bị tắt khi ấn ra ngoài
        ImageView imageOriginal = view.findViewById(R.id.image_original);
        File tempImageFile = item.getTempImageFile();
        if (tempImageFile != null) {
            imageOriginal.setImageURI(Uri.fromFile(tempImageFile));
        } else {
            imageOriginal.setImageResource(R.drawable.ic_placeholder); // Placeholder nếu không có file


        }
        imageOriginal.setOnClickListener(v -> {
            imageOriginal.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) imageOriginal.getDrawable()).getBitmap();
            showZoomableImage(bitmap);
        });
        imageNumber = 1;
        title = view.findViewById(R.id.titleTextView_check);
        title.setText(item.getTitle());
        mvlnho = item.getMvlnho();
        OQC_NO = PO_ID + "_" + PO_NO + "_" + mvlnho;
        recyclerViewImagesCheck = view.findViewById(R.id.recyclerViewImagesCheck);
        recyclerViewImagesCheck.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));

        if (id == "0"){
            imageList = new ArrayList<>();


            maxIsNo = tableImageOQC.getmaxisno(OQC_NO, PO_ID, PO_NO, mvlnho);
            if (maxIsNo != null)
            {
                solan = Integer.parseInt(maxIsNo)+1;

            }






            adapter = new ImageCheckAdapter(imageList,id, new OnItemClickListener() {
                @Override
                public void onItemClick(Bitmap image, int position) {

                    showFullScreenDialog(image, position, item,id);


                }

                @Override
                public void onDeleteClick(int position) {

                    imageList.remove(position);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position, imageList.size());
                }
            });
            recyclerViewImagesCheck.setAdapter(adapter);

            Button btnTakePhoto = view.findViewById(R.id.btn_take_photo);
            btnTakePhoto.setOnClickListener(v -> {
                if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
                } else {
                    openCamera();
                }
            });

            Button btnAchieved = view.findViewById(R.id.btnAchieved);
            btnAchieved.setOnClickListener(v -> {
                if (imageList.isEmpty()) {
                    Toast.makeText(view.getContext(), "Vui lòng chụp ảnh trước", Toast.LENGTH_SHORT).show();
                    return;
                }
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(new Date());
                tableImageOQC.insertData("tc_OQC_file", "TC_OQC001, TC_OQC002, TC_OQC003, TC_OQC004, TC_OQC005, TC_OQC006 , TC_OQC007, TC_OQC008, TC_OQC009", "'" + OQC_NO + "','" + PO_ID + "','" + PO_NO + "','" + mvlnho + "','" + timeStamp + "','Y','N','" + Constant_Class.UserID + "' ,'"+solan +"'");
                for (Bitmap image : imageList) {
                    saveImageToGallery(image, item, "Y", solan);
                    imageNumber++;
                }
                Toast.makeText(view.getContext(), "Đang lưu thông tin...", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                detailPoAdapter.notifyDataSetChanged();
            });

            Button btnNotAchieved = view.findViewById(R.id.btnNoAchieved);
            btnNotAchieved.setOnClickListener(v -> {
                if (imageList.isEmpty()) {
                    Toast.makeText(view.getContext(), "Vui lòng chụp ảnh trước", Toast.LENGTH_SHORT).show();
                    return;
                }
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(new Date());
//            if (maxIsNo != null) {
//
//                for (Bitmap imageEdit : imageListedit) {
//                    saveImageToGallery(imageEdit, item, "Y", solan);
//                    imageNumber++;
//                }
//            } else {
                tableImageOQC.insertData("tc_OQC_file", "TC_OQC001, TC_OQC002, TC_OQC003, TC_OQC004, TC_OQC005, TC_OQC006 , TC_OQC007, TC_OQC008, TC_OQC009", "'" + OQC_NO + "','" + PO_ID + "','" + PO_NO + "','" + mvlnho + "','" + timeStamp + "','N','N','" + Constant_Class.UserID + "' ,'"+solan +"'");
                for (Bitmap image : imageList) {
                    saveImageToGallery(image, item, "N", solan);
                    imageNumber++;
                }
//            }
                Toast.makeText(view.getContext(), "Đang lưu thông tin...", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                detailPoAdapter.notifyDataSetChanged();
            });
            Button btnCancel = view.findViewById(R.id.btnCancel);
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
        }else if (id == "1"){ // XEM

            maxIsNo = tableImageOQC.getmaxisno(OQC_NO, PO_ID, PO_NO, mvlnho);
            if (maxIsNo == null)
            {
                Toast.makeText(view.getContext(), "Vui lòng kiểm tra lại thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            final int[] currentIndex = {0};
            List<String> ListPO = tableImageOQC.getListPO(OQC_NO, PO_ID, PO_NO, mvlnho);

            imageList = new ArrayList<>();
            adapter = new ImageCheckAdapter(imageList,id, new OnItemClickListener() {
                @Override
                public void onItemClick(Bitmap image, int position) {

                    showFullScreenDialog(image, position, item,id);


                }

                @Override
                public void onDeleteClick(int position) {

                    imageList.remove(position);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position, imageList.size());
                }
            });
            recyclerViewImagesCheck.setAdapter(adapter);

            Button btnTakePhoto = view.findViewById(R.id.btn_take_photo);
            btnTakePhoto.setVisibility(View.GONE);
            Button btnAchieved = view.findViewById(R.id.btnAchieved);
            btnAchieved.setVisibility(View.GONE);

            Button btnNotAchieved = view.findViewById(R.id.btnNoAchieved);
            btnNotAchieved.setVisibility(view.GONE);
            Button btnCancel = view.findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(v -> {
                dialog.dismiss();

            });
            if (!ListPO.isEmpty()) {
                currentIndex[0] = 0; // Đặt vị trí mặc định là phần tử đầu tiên
                updateDialogData(view, ListPO.get(currentIndex[0]), mvlnho, item.getImageUrl(),ListPO.size());
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
                                    updateDialogData(view, ListPO.get(currentIndex[0]),mvlnho,item.getImageUrl(),ListPO.size());
                                }
                            } else { // Vuốt sang trái (tiến tới PO sau)
                                if (currentIndex[0] < ListPO.size() - 1) {
                                    currentIndex[0]++;
                                    updateDialogData(view, ListPO.get(currentIndex[0]),mvlnho,item.getImageUrl(),ListPO.size());
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







    private void takePhoto(OnPhotoTakenListener listener) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

        // Nhận kết quả ảnh
        this.photoTakenListener = listener;
    }



    private final ActivityResultLauncher<Intent> takePhotoLauncher = registerForActivityResult(
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


    interface OnPhotoTakenListener {
        void onPhotoTaken(Bitmap image);
    }
    private void showZoomableImage(Bitmap image) {
        Dialog dialog = new Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.full_image);

        PhotoView photoView = dialog.findViewById(R.id.fullImageView);
        photoView.setImageBitmap(image);

        photoView.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void showFullScreenDialog(Bitmap image, int position, ImagePODetail item,String id) {
        Dialog dialog = new Dialog(container.getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_full_image);

        ImageView fullImageView = dialog.findViewById(R.id.fullImageView);
        ImageView deleteImageView = dialog.findViewById(R.id.deleteImageView);
        if (id == "1"){
            deleteImageView.setVisibility(View.GONE);
        }
        fullImageView.setImageBitmap(image);

        // Đặt sự kiện onClick cho fullImageView để đóng dialog
        fullImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageList.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, imageList.size());

                dialog.dismiss();
            }

        });
        dialog.show();
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
                takePhotoLauncher.launch(takePictureIntent);
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
    private void saveImageToGallery(Bitmap bitmapImage, ImagePODetail item, String status, Integer sl) {
        ContentValues values = new ContentValues();

            String number = String.format("%02d", imageNumber);
            String nameimg = PO_ID+"_"+PO_NO+"_"+ mvlnho+ "_"+sl+ "_"+ number+ ".png";
            String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(new Date());
            values.put(MediaStore.Images.Media.DISPLAY_NAME, nameimg);

            tableImageCheck.insertData("tc_imgcheck_file", "TC_IMG001, TC_IMG002, TC_IMG003, TC_IMG004, TC_IMG005, TC_IMG006, TC_IMG007, TC_IMG008, TC_IMG009, TC_IMG010, TC_IMG011","'" + PO_ID +"' " + "," + "'" + PO_NO +"'" +","+ "'"+mvlnho+ "'"+"," +"'"+item.getImageUrl()+"'" + "," + "'"+nameimg+"'"+","+ "'"+timeStamp+"'"+","+ "'N'" +","+ "'"+ Constant_Class.UserID +"'" +","+ "'"+status+"'"+","+"'"+OQC_NO+"'"+","+"'"+sl+"'" );





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
    private List<String> getImageNamesFromDatabase(String mvlnho, String pathimage,String index) {
        List<String> imageNames = new ArrayList<>();

        Cursor cursor = tableImageCheck.getData("tc_imgcheck_file", "TC_IMG005", "TC_IMG003 = '" + mvlnho + "' AND TC_IMG001 = " + "'" + PO_ID + "' AND TC_IMG002 = '" + PO_NO + "' AND TC_IMG004 = '"+pathimage+"'  AND TC_IMG010 = '"+OQC_NO+"'   AND TC_IMG011 = '"+index+"'" );
        if(cursor.moveToFirst()) {
            do {
                String imageName = cursor.getString(cursor.getColumnIndexOrThrow("TC_IMG005"));
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
    private void savePOChoice(String poID, String poNO) {
        SharedPreferences preferences = container.getContext().getSharedPreferences("Podetail", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("SelectedPoID", poID);
        editor.putString("SelectedPoNO", poNO);
        editor.apply();
    }
    private String getSelectedPoID() {
        SharedPreferences preferences = container.getContext().getSharedPreferences("Podetail", Context.MODE_PRIVATE);
        return preferences.getString("SelectedPoID", "");

    }
    private String getSelectedPoNO() {
        SharedPreferences preferences = container.getContext().getSharedPreferences("Podetail", Context.MODE_PRIVATE);
        return preferences.getString("SelectedPoNO", "");

    }


    private void updateDialogData(View view, String index, String newMvlnho,String ImageUrl,Number size) {
        imageList.clear(); // Xóa danh sách ảnh cũ
        List<String> newImageFileNames = getImageNamesFromDatabase(newMvlnho, ImageUrl,index);
        String status = tableImageOQC.getstatus(OQC_NO, PO_ID, PO_NO, newMvlnho,index);
        String timecheck = tableImageOQC.getTimeCheck(OQC_NO, PO_ID, PO_NO, newMvlnho,index);
        TextView id = view.findViewById(R.id.titlelist);
        TextView time = view.findViewById(R.id.titletimecheck);
        String stt = index + "/" + size;
        id.setText(stt);
        String timeText = "Thời gian kiểm tra: " + timecheck;
        time.setText(timeText);
        id.setVisibility(View.VISIBLE);
        time.setVisibility(View.VISIBLE);
        ImageView imstatus = view.findViewById(R.id.Imagestatus);
        if (status.equals("Y")) {
            imstatus.setImageResource(R.drawable.confime);
            imstatus.setVisibility(View.VISIBLE);
        }else {
            imstatus.setImageResource(R.drawable.no_confime);
            imstatus.setVisibility(View.VISIBLE);
        }

        for (String fileName : newImageFileNames) {
            Uri imageUri = getImageUriByName(fileName);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(view.getContext().getContentResolver(), imageUri);
                if (bitmap != null) {
                    imageList.add(bitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(view.getContext(), "Không thể tải hình ảnh", Toast.LENGTH_SHORT).show();
            }

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
