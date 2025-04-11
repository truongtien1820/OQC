package com.example.OQC.Interface;


import android.graphics.Bitmap;

public interface OnItemClickListener {
    void onItemClick(Bitmap image, int position);
    void onDeleteClick(int position);
}
