package com.example.laprap001.AdapterAndDetail;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.laprap001.Database.Create_Table_main;
import com.example.laprap001.Database.SQLiteTemporaryTableManager;
import com.example.laprap001.Interface.Change_fragment;
import com.example.laprap001.R;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;

public class POAdapter extends RecyclerView.Adapter<POAdapter.DataViewHolder>  {

    private List<Data_PO> dataList;
    private List<Data_Image_Detail> imageList = new ArrayList<>();
    private ImageAdapter imageAdapter;
    private Create_Table_main Cre_tb;
    private SQLiteTemporaryTableManager tempTableManager;
    private Change_fragment change_fragment;
    public POAdapter(List<Data_PO> dataList) {
        this.dataList = dataList;

    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.po_card, parent, false);


        Cre_tb = new Create_Table_main(parent.getContext());
        Cre_tb.open();
        tempTableManager = new SQLiteTemporaryTableManager(parent.getContext());


        try{

            String table_name = "tc_img_temp";
            String table_schema = "TC_IMG001 TEXT, TC_IMG002 TEXT, TC_IMG003 TEXT, TC_IMG004 TEXT, TC_IMG005 TEXT";
 // Xóa bảng nếu tồn tại
            tempTableManager.createTemporaryTable(table_name, table_schema);

        }catch (Exception e){
            e.printStackTrace();
        }


        change_fragment = (Change_fragment) parent.getContext();
        return  new  DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        Data_PO data = dataList.get(position);
        if (holder.recyclerImage.getAdapter() == null) {
            StaggeredGridLayoutManager staggeredGridLayoutManager =
                    new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
            holder.recyclerImage.setLayoutManager(staggeredGridLayoutManager);
            imageAdapter = new ImageAdapter( holder.itemView.getContext(),imageList);
            holder.recyclerImage.setHasFixedSize(true);
            holder.recyclerImage.setAdapter(imageAdapter);

        }

        // Lấy adapter hiện tại từ RecyclerView
        ImageAdapter imageAdapter = (ImageAdapter) holder.recyclerImage.getAdapter();

        //"\\172.16.40.17\圖檔\包裝材圖片檔\LK\05011328_1A1222_JPN0040_WP7.2-12_KLB0001A.jpg"
        Cursor cursor = Cre_tb.get_data_tc_img(data.getG_tc_infwno001(), data.getG_tc_infwno002());
        List<Data_Image_Detail> newImageList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                try {
                    String g_tc_img001 = cursor.getString(cursor.getColumnIndexOrThrow("TC_IMG001"));
                    String g_tc_img002 = cursor.getString(cursor.getColumnIndexOrThrow("TC_IMG002"));
                    String g_tc_img003 = cursor.getString(cursor.getColumnIndexOrThrow("TC_IMG003"));
                    String g_tc_img004 = cursor.getString(cursor.getColumnIndexOrThrow("TC_IMG004"));
                    String g_tc_img005 = cursor.getString(cursor.getColumnIndexOrThrow("TC_IMG005"));
                    String g_tc_img006 = cursor.getString(cursor.getColumnIndexOrThrow("TC_IMG006"));
                    String g_tc_img007 = cursor.getString(cursor.getColumnIndexOrThrow("TC_IMG007"));
                    String g_tc_img008 = cursor.getString(cursor.getColumnIndexOrThrow("TC_IMG008"));
                    String g_tc_img009 = cursor.getString(cursor.getColumnIndexOrThrow("TC_IMG009"));
                    String g_tc_img010 = cursor.getString(cursor.getColumnIndexOrThrow("TC_IMG010"));
                    String g_tc_img011 = cursor.getString(cursor.getColumnIndexOrThrow("TC_IMG011"));
                    if (g_tc_img003.substring(0,4).equals("0501") || g_tc_img003.substring(0,4).equals("0502")) {
                        String[] parts = g_tc_img011.split(","); // Tách chuỗi bằng dấu phẩy
                        String quycach = parts[2];

                        g_tc_img003 = g_tc_img003 + "_" + data.getG_tc_infwno007().substring(0,6) + "_" + data.getG_tc_infwno003() + "_" + quycach + "_" +data.getG_tc_infwno012();
                    }
                    //05070001_12_5_300_1A1222DD_05011328_05600002_WP7.2-12_JPN0040.jpg
                    //05070001_12_5_300_1A1222DD_JPN0040_05600002_WP7.2-12_KLB0001A
                    if (g_tc_img003.substring(0,4).equals("0507")) {
                        String[] parts = g_tc_img011.split(","); // Tách chuỗi bằng dấu phẩy
                        String quycach = parts[2];
                        String mvl0501 = Cre_tb.get_tc_0501_img(data.getG_tc_infwno001(), data.getG_tc_infwno002());
                        g_tc_img003 = g_tc_img003 + "_" + g_tc_img008 + "_" + g_tc_img009 + "_" +g_tc_img010+ "_" + data.getG_tc_infwno007() + "_" + mvl0501 + "_" + g_tc_img007 + "_" + quycach + "_" + data.getG_tc_infwno003();
                    }

                    newImageList.add(new Data_Image_Detail(g_tc_img001, g_tc_img002, g_tc_img003, g_tc_img004, g_tc_img005));
                    if (tempTableManager.isRowExists("tc_img_temp", "TC_IMG001 = '" + g_tc_img001 + "' AND TC_IMG002 = '" + g_tc_img002 + "' AND TC_IMG003 = '" + g_tc_img003+"'")){

                    }else {
                        tempTableManager.insertData("tc_img_temp", "TC_IMG001, TC_IMG002, TC_IMG003, TC_IMG004, TC_IMG005",
                                "'" + g_tc_img001 + "','" + g_tc_img002 + "','" + g_tc_img003 + "','" + g_tc_img004+"/"+ g_tc_img005+ "','" + g_tc_img006 + "'");}
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        imageAdapter.updateData(newImageList);


        holder.tc_infwno001_002.setText(data.getG_tc_infwno001()+"-"+data.getG_tc_infwno002());
        String customerInfo = holder.itemView.getContext().getString(R.string.customer_id)
                + ": " + data.getG_tc_infwno003()
                + "/" + data.getG_tc_infwno004();
        holder.tc_infwno003_004.setText(customerInfo);
        String dateInfo = holder.itemView.getContext().getString(R.string.Agreed_shipping_date)+ ": " +data.getG_tc_infwno005();
        holder.tc_infwno005.setText(dateInfo);
        String quantityInfo = holder.itemView.getContext().getString(R.string.quantity)+ ": " +data.getG_tc_infwno006();
        holder.tc_infwno006.setText(quantityInfo );
        String name_product = holder.itemView.getContext().getString(R.string.name_product)+ ": " +data.getG_tc_infwno007()+ "/"
                + data.getG_tc_infwno008();

        holder.tc_infwno007_008.setText(name_product);
        String specification_vn = holder.itemView.getContext().getString(R.string.specification_vn)+ ": " +data.getG_tc_infwno009();
        holder.tc_infwno009.setText(specification_vn);
        String PO_customer = holder.itemView.getContext().getString(R.string.PO_customer)+ ": " +data.getG_tc_infwno010();
        holder.tc_infwno010.setText(PO_customer);
        String Mold_number = holder.itemView.getContext().getString(R.string.Mold_number)+ ": " +data.getG_tc_infwno011() + "/"+ data.getG_tc_infwno012();
        holder.tc_infwno011_012.setText(Mold_number);
        String work_number = holder.itemView.getContext().getString(R.string.work_number)+ ": " +data.getG_tc_infwno013();
        holder.tc_infwno013.setText(work_number);

        holder.button_choice.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                change_fragment.ChangeFragment("3", data.getG_tc_infwno001(), data.getG_tc_infwno002());

            }
        });

    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        TextView tc_infwno001_002, tc_infwno003_004, tc_infwno005,
                tc_infwno006, tc_infwno007_008, tc_infwno009,
        tc_infwno010, tc_infwno011_012, tc_infwno013;
        RecyclerView recyclerImage;
        Button button_choice;
        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            tc_infwno001_002 = itemView.findViewById(R.id.tc_infwno001_002);
            tc_infwno003_004 = itemView.findViewById(R.id.tc_infwno003_004);
            tc_infwno005 = itemView.findViewById(R.id.tc_infwno005);
            tc_infwno006 = itemView.findViewById(R.id.tc_infwno006);
            tc_infwno007_008 = itemView.findViewById(R.id.tc_infwno007_008);
            tc_infwno009 = itemView.findViewById(R.id.tc_infwno009);
            tc_infwno010 = itemView.findViewById(R.id.tc_infwno010);
            tc_infwno011_012 = itemView.findViewById(R.id.tc_infwno011_012);
            tc_infwno013 = itemView.findViewById(R.id.tc_infwno013);
            recyclerImage = itemView.findViewById(R.id.recycler_img);
            button_choice = itemView.findViewById(R.id.btnAction_Choice);
        }
    }
}
