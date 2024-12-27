package com.example.laprap001.PageFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.laprap001.AdapterAndDetail.Detail_POAdapter;
import com.example.laprap001.AdapterAndDetail.Image_PO_Detail;
import com.example.laprap001.Database.Create_Table_main;
import com.example.laprap001.Database.SQLiteTemporaryTableManager;
import com.example.laprap001.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PageDetailPOFragment extends Fragment {
    private  String PO_ID ,PO_NO;
    private TextView inf_PO;
    private ViewGroup container;
    private RecyclerView recyclerView;
    private List<Image_PO_Detail> imagePoDetails = new ArrayList<>();
    private Detail_POAdapter detailPoAdapter;
    private static SQLiteTemporaryTableManager tb_tmp = null;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String poid;
    private OnPhotoTakenListener photoTakenListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pagedetail_po, container, false);
        this.container = container;
        // Khởi tạo Create_Table_main với Context từ container

        PO_ID = getArguments().getString("PO_ID");
        PO_NO = getArguments().getString("PO_NO");
        tb_tmp = new SQLiteTemporaryTableManager(container.getContext());
        addControls(view);
        addEvents();

        return view;
    }

    private void addEvents() {
        Cursor cur = tb_tmp.getData("tc_img_temp", "TC_IMG001,TC_IMG002, TC_IMG003, TC_IMG004, TC_IMG005",
                "TC_IMG001 = '" + PO_ID + "' AND TC_IMG002 = '" + PO_NO + "'");
        try {
            if (cur != null && cur.moveToFirst()) {
                imagePoDetails.clear();
                do {
                    String g_tc_img003 = cur.getString(cur.getColumnIndexOrThrow("TC_IMG003"));
                    String g_tc_img004 = cur.getString(cur.getColumnIndexOrThrow("TC_IMG004"));
                    String g_tc_img005 = cur.getString(cur.getColumnIndexOrThrow("TC_IMG005"));
                    imagePoDetails.add(new Image_PO_Detail(g_tc_img004 +" "+ g_tc_img005, g_tc_img003));
                } while (cur.moveToNext());
                detailPoAdapter.notifyDataSetChanged();
            }
        } finally {
            if (cur != null) {

                cur.close();
            }
        }
        detailPoAdapter.setOnItemClickListener(item -> showDialog(item));
    }


    private void addControls(View view) {
        recyclerView = view.findViewById(R.id.rcv_img); // Sử dụng biến toàn cục
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        detailPoAdapter = new Detail_POAdapter(imagePoDetails, view.getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(detailPoAdapter);
        inf_PO = view.findViewById(R.id.inf_PO);
        String infPO = PO_ID + "-" + PO_NO;
        inf_PO.setText(infPO);

    }
    private void showDialog(Image_PO_Detail item) {
        // Tạo dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_compare_image, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.curved_background_checkimage);
        }
        ImageView imageOriginal = dialogView.findViewById(R.id.image_original);
        Button btnTakePhoto = dialogView.findViewById(R.id.btn_take_photo);
        ImageView imageCaptured = dialogView.findViewById(R.id.image_captured);

        // Hiển thị ảnh gốc
        File tempImageFile = item.getTempImageFile();
        if (tempImageFile != null) {
            imageOriginal.setImageURI(Uri.fromFile(tempImageFile));
        } else {
            imageOriginal.setImageResource(R.drawable.ic_placeholder); // Placeholder nếu không có file
        }

        // Chụp ảnh khi nhấn nút
        btnTakePhoto.setOnClickListener(v -> {
            takePhoto(result -> {
                // Hiển thị ảnh chụp lên dialog
                imageCaptured.setImageBitmap(result);
                // Đánh giá kết quả so sánh ở đây
            });
        });

        // Hiển thị dialog
            dialog.show();
    }

    private void takePhoto(OnPhotoTakenListener listener) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

        // Nhận kết quả ảnh
        this.photoTakenListener = listener;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            if (photoTakenListener != null) {
                photoTakenListener.onPhotoTaken(imageBitmap);
            }
        }
    }

    interface OnPhotoTakenListener {
        void onPhotoTaken(Bitmap image);
    }

}
