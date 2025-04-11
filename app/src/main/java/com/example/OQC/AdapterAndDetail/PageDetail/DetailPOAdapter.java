package com.example.OQC.AdapterAndDetail.PageDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.OQC.Database.TableImageOQC;
import com.example.OQC.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import jcifs.CIFSContext;
import jcifs.context.SingletonContext;
import jcifs.smb.NtlmPasswordAuthenticator;
import jcifs.smb.SmbFile;

public class DetailPOAdapter extends RecyclerView.Adapter<DetailPOAdapter.CardViewHolder> {
    private final List<ImagePODetail> detail_PO;
    private final Context context;
    private OnItemClickListener listener;
    private TableImageOQC tableImageOQC;
    private String PO_ID,PO_NO;
    public DetailPOAdapter(List<ImagePODetail> detail_PO, Context context, String PO_ID, String PO_NO) {
        this.detail_PO = detail_PO;
        this.context = context;
        this.PO_ID = PO_ID;
        this.PO_NO = PO_NO;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.detail_po_card, parent, false);
        tableImageOQC = new TableImageOQC(context);

        return new CardViewHolder(view);
    }
    public interface OnItemClickListener {
        void onItemClick(ImagePODetail item, String id);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        ImagePODetail item = detail_PO.get(position);
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
                String OQC_NO = PO_ID + "_" + PO_NO + "_" + item.getMvlnho();
                String status = tableImageOQC.getstatus( OQC_NO, PO_ID, PO_NO, item.getMvlnho(),tableImageOQC.getmaxisno(OQC_NO, PO_ID, PO_NO, item.getMvlnho()));
                ((android.app.Activity) context).runOnUiThread(() -> {
                    if (status.equals("Y")) {
                        holder.imageconfime.setVisibility(View.VISIBLE);
                        holder.imageconfime.setImageResource(R.drawable.confime);
                    } else if (status.equals("N")){
                        holder.imageconfime.setVisibility(View.VISIBLE);
                        holder.imageconfime.setImageResource(R.drawable.no_confime);
                    }
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
                PopupMenu popup = new PopupMenu(holder.itemView.getContext(), holder.cardViewImage);
                popup.getMenu().add(Menu.NONE, 0, 0, "Kiểm tra");
                popup.getMenu().add(Menu.NONE, 1, 1, "Xem Lại ");
                popup.setOnMenuItemClickListener(item1 ->{
                    switch (item1.getItemId()) {
                        case 0:
                            listener.onItemClick(item, "0"); // Kiểm tra
                            return true;
                        case 1:
                            listener.onItemClick(item, "1"); // Xem lai
                            return true;
                        default:
                            return false;
                    }
                });
                popup.show();



            }
        });
    }
    @Override
    public int getItemCount() {
        return detail_PO.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,imageconfime;
        TextView titleTextView;
        CardView cardViewImage;
        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            imageconfime = itemView.findViewById(R.id.Imageconfimed);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            cardViewImage = itemView.findViewById(R.id.cardViewImage);
        }
    }
}
