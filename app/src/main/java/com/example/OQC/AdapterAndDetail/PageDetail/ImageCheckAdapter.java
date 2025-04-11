package com.example.OQC.AdapterAndDetail.PageDetail;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.OQC.Interface.OnItemClickListener;
import com.example.OQC.R;

import java.util.List;

public class ImageCheckAdapter extends RecyclerView.Adapter<ImageCheckAdapter.ViewHolder> {

    private List<Bitmap> images;
    private OnItemClickListener listener;
    private String id;

    public ImageCheckAdapter(List<Bitmap> images,String id, OnItemClickListener listener) {
        this.images = images;
        this.listener = listener;
        this.id = id;
    }
    public ImageCheckAdapter(List<Bitmap> images) {
        this.images = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_check, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bitmap image = images.get(position);
        holder.imageView.setImageBitmap(image);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView deleteImage;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageViewPicture);
            deleteImage = view.findViewById(R.id.buttonDeleteImage);
            if(id.equals("1")){
                deleteImage.setVisibility(View.GONE);

            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(images.get(position), position);
                        }
                    }
                }
            });

            deleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }
}
