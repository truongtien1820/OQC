package com.example.laprap001.AdapterAndDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;




import com.example.laprap001.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import com.squareup.picasso.Picasso;

import jcifs.CIFSContext;
import jcifs.context.SingletonContext;
import jcifs.smb.NtlmPasswordAuthenticator;
import jcifs.smb.SmbFile;
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context context;
    private List<Data_Image_Detail> imageList; // Danh sách ảnh (resource ID hoặc URL)

    public ImageAdapter(Context context, List<Data_Image_Detail> imageList) {
        this.context = context;
        this.imageList = imageList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        if (imageList == null || imageList.size() <= position) {
            return; // Bỏ qua nếu vị trí không hợp lệ
        }

        Data_Image_Detail image = imageList.get(position);
        if (image == null) {
            return; // Bỏ qua nếu dữ liệu ảnh null
        }

        // Xóa trạng thái cũ của ImageView trước khi tải ảnh mới
        holder.imageView.setImageDrawable(null);
        Picasso.get().cancelRequest(holder.imageView);

        // Đường dẫn ảnh SMB
        String path_img = image.getG_tc_img003();
        String smbUrl = "smb://172.16.40.17/圖檔/包裝材圖片檔/LK/" + path_img + ".jpg";

        new Thread(() -> {
            try {
                CIFSContext baseContext = SingletonContext.getInstance();
                CIFSContext authContext = baseContext.withCredentials(
                        new NtlmPasswordAuthenticator("LELONGDH\\install", "install")
                );
                SmbFile smbFile = new SmbFile(smbUrl, authContext);

                InputStream inputStream = smbFile.getInputStream();
                File tempFile = File.createTempFile("temp_image", ".jpg", context.getCacheDir());
                FileOutputStream fos = new FileOutputStream(tempFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
                fos.close();
                inputStream.close();

                // Dùng Picasso để hiển thị ảnh
                holder.imageView.post(() -> {
                    Picasso.get()
                            .load(tempFile)
                            .placeholder(R.drawable.ic_placeholder) // Hiển thị tạm thời
                            //.error(R.drawable.error)             // Hiển thị nếu lỗi
                            .into(holder.imageView);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_item);
        }
    }
    public void updateData(List<Data_Image_Detail> newImageList) {
        if (this.imageList != null && !this.imageList.equals(newImageList)) {
            this.imageList.clear();
        }
        this.imageList.addAll(newImageList);
        notifyDataSetChanged();
    }

//    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
//        private ImageView imageView;
//
//        public LoadImageTask(ImageView imageView) {
//            this.imageView = imageView; // Gán ImageView từ ViewHolder
//        }
//        @Override
//        protected Bitmap doInBackground(String... urls) {
//            Bitmap bitmap = null;
//            try {
//
//                CIFSContext baseContext = SingletonContext.getInstance();
//                CIFSContext authContext = baseContext.withCredentials(new NtlmPasswordAuthenticator("LELONGDH\\install", "install"));
//                SmbFile file = new SmbFile(urls[0],authContext);
//                InputStream in = file.getInputStream();
//                bitmap = BitmapFactory.decodeStream(in); // Đọc InputStream thành Bitmap
//                in.close();
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return bitmap;
//        }

//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            if (bitmap != null) {
//                imageView.setImageBitmap(bitmap); // Hiển thị ảnh lên ImageView
//            } else {
//                // Xử lý nếu ảnh không tải được
//            }
//        }


    //}
}
