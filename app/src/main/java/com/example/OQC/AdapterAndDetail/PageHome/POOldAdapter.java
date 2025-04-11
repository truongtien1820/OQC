package com.example.OQC.AdapterAndDetail.PageHome;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.OQC.AdapterAndDetail.PageDetail.DetailPOAdapter;
import com.example.OQC.Interface.Change_fragment;
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

public class POOldAdapter extends RecyclerView.Adapter<POOldAdapter.HomeViewHolder>{
    private final List<POOldDetail> detail_PO;
    private final Context context;
    private POOldAdapter.OnItemClickListener listener;
    private Change_fragment change_fragment;

    public POOldAdapter(List<POOldDetail> detail_PO, Context context) {
        this.detail_PO = detail_PO;
        this.context = context;

    }

    public void setOnItemClickListener(POOldAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(POOldDetail  item);
    }



    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_old_po, parent, false);
        change_fragment = (Change_fragment) parent.getContext();
        return new HomeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        POOldDetail  item = detail_PO.get(position);
        String title = item.getPOID()+"-"+item.getPONO();
        holder.title.setText(title);
        Picasso.get().cancelRequest(holder.imagePO);
        String path_img = item.getImageUrlPOOld();
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
                ((android.app.Activity) context).runOnUiThread(() -> {
                    item.setTempImageFile(tempFile);
                    Picasso.get()
                            .load(tempFile)
                            .placeholder(R.drawable.ic_placeholder) // Hiển thị tạm thời
                            //.error(R.drawable.error)             // Hiển thị nếu lỗi
                            .into(holder.imagePO);

                    Picasso.get()
                            .load(getImageUriByName(item.getImageUrlCheck()))
                            .placeholder(R.drawable.ic_placeholder) // Hiển thị tạm thời
                            //.error(R.drawable.error)             // Hiển thị nếu lỗi
                            .into(holder.imagecheck);
                });



            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        if (  holder.cardViewcompare != null) {
            holder.cardViewcompare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    change_fragment.ChangeFragment("3", item.getPOID(), item.getPONO());

                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return detail_PO.size();
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder {
        ImageView imagePO,imagecheck;
        TextView title;
        CardView cardViewcompare;
        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
           imagePO = itemView.findViewById(R.id.imagePO);
           imagecheck = itemView.findViewById(R.id.imageCheck);
           title = itemView.findViewById(R.id.inf_PO_old);
            cardViewcompare = itemView.findViewById(R.id.cardViewcompare);
        }
    }
    private Uri getImageUriByName(String imageName) {
        Uri mediaImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{
                MediaStore.Images.Media._ID
        };
        // Đảm bảo imageName chứa cả tên và phần mở rộng chẳng hạn như "image.png"
        String selection = MediaStore.Images.Media.DISPLAY_NAME + " = ?";
        String[] selectionArgs = new String[]{imageName};

        ContentResolver contentResolver =   context.getContentResolver();
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
}
