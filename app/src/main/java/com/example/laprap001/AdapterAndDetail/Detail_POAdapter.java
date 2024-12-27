package com.example.laprap001.AdapterAndDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laprap001.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import jcifs.CIFSContext;
import jcifs.context.SingletonContext;
import jcifs.smb.NtlmPasswordAuthenticator;
import jcifs.smb.SmbFile;

public class Detail_POAdapter  extends RecyclerView.Adapter<Detail_POAdapter.CardViewHolder> {
    private final List<Image_PO_Detail> detail_PO;
    private final Context context;
    private OnItemClickListener listener;
    public Detail_POAdapter(List<Image_PO_Detail> detail_PO, Context context) {
        this.detail_PO = detail_PO;
        this.context = context;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.detail_po_card, parent, false);
        return new CardViewHolder(view);
    }
    public interface OnItemClickListener {
        void onItemClick(Image_PO_Detail item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Image_PO_Detail item = detail_PO.get(position);
        holder.titleTextView.setText(item.getTitle());
        Picasso.get().cancelRequest(holder.imageView);
        String path_img = item.getImageUrl();
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
                item.setTempImageFile(tempFile);

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
        holder.cardViewImage.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item); // Truyền dữ liệu ảnh khi click
            }
        });
    }

    @Override
    public int getItemCount() {
        return detail_PO.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        CardView cardViewImage;
        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            cardViewImage = itemView.findViewById(R.id.cardViewImage);
        }
    }
}
