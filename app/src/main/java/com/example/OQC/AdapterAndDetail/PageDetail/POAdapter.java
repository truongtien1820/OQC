package com.example.OQC.AdapterAndDetail.PageDetail;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.OQC.AdapterAndDetail.PageSearch.DataPO;
import com.example.OQC.AdapterAndDetail.PageSearch.ImageAdapter;
import com.example.OQC.Database.TableMain;
import com.example.OQC.Database.TableImageTemp;
import com.example.OQC.Interface.Change_fragment;
import com.example.OQC.R;

import java.util.ArrayList;
import java.util.List;

public class POAdapter extends RecyclerView.Adapter<POAdapter.DataViewHolder>  {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private List<DataPO> dataList;
    private List<DataImageDetail> imageList = new ArrayList<>();
    private ImageAdapter imageAdapter;
    private TableMain Cre_tb;
    private TableImageTemp tempTableManager;
    private Change_fragment change_fragment;
    private POAdapter.OnItemClickListenerDetial listenerdetail;
    private Integer id;

    public POAdapter(List<DataPO> dataList) {
        this.dataList = dataList;

    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.po_card, parent, false);


        Cre_tb = new TableMain(parent.getContext());
        Cre_tb.open();
        tempTableManager = new TableImageTemp(parent.getContext());


        try{

            String table_name = "tc_img_temp";
            String table_schema = "TC_IMG001 TEXT, TC_IMG002 TEXT, TC_IMG003 TEXT, TC_IMG004 TEXT, TC_IMG005 TEXT, TC_IMG006 TEXT";
 // Xóa bảng nếu tồn tại
            tempTableManager.createTemporaryTable(table_name, table_schema);

        }catch (Exception e){
            e.printStackTrace();
        }


        change_fragment = (Change_fragment) parent.getContext();
        return  new  DataViewHolder(view);
    }
    public interface OnItemClickListenerDetial {
        void onItemClick(DataPO item, String id);
    }
    public void setOnItemClickListenerDetial(POAdapter.OnItemClickListenerDetial listener) {
        this.listenerdetail = listener;

    }
    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        DataPO data = dataList.get(position);
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
        List<DataImageDetail> newImageList = new ArrayList<>();
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
                    String mavlnho = cursor.getString(cursor.getColumnIndexOrThrow("TC_IMG003"));
                    newImageList.add(new DataImageDetail(g_tc_img001, g_tc_img002, g_tc_img003, g_tc_img004, g_tc_img005));
                    if (tempTableManager.isRowExists("tc_img_temp", "TC_IMG001 = '" + g_tc_img001 + "' AND TC_IMG002 = '" + g_tc_img002 + "' AND TC_IMG003 = '" + g_tc_img003+"'")){

                    }else {
                        tempTableManager.insertData("tc_img_temp", "TC_IMG001, TC_IMG002, TC_IMG003, TC_IMG004, TC_IMG005, TC_IMG006",
                                "'" + g_tc_img001 + "','" + g_tc_img002 + "','" + g_tc_img003 + "','" + g_tc_img004+"/"+ g_tc_img005+ "','" + g_tc_img006 + "','" + mavlnho + "'");}
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

        holder.linear_PO.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(holder.itemView.getContext(), holder.linear_PO);
            popup.getMenu().add(Menu.NONE, 0, 0, "Thiết lập datecode");
            popup.getMenu().add(Menu.NONE, 1, 1, "Xem lại thiết lập datecode");
            popup.getMenu().add(Menu.NONE, 2, 2, "Kiểm tra ngoại vi");
            popup.setOnMenuItemClickListener(item1 ->{
                    switch (item1.getItemId()) {
                            case 0:
                                listenerdetail.onItemClick(data,"0"); //Thiết lập datecode
                                return true;
                            case 1:
                                listenerdetail.onItemClick(data,"1"); // Xem lại thiết lập datecode
                                return true;
                            case 2:
                                change_fragment.ChangeFragment("3", data.getG_tc_infwno001(), data.getG_tc_infwno002());
                                return true;
                            default:
                                return false;
                        }
                    });
            popup.show();
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
        LinearLayout linear_PO;
        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            linear_PO = itemView.findViewById(R.id.linear_PO);
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
        }
    }



}
