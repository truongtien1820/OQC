package com.example.laprap001.PageFragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laprap001.AdapterAndDetail.Data_PO;
import com.example.laprap001.AdapterAndDetail.POAdapter;
import com.example.laprap001.Constant_Class;
import com.example.laprap001.Database.Create_Table_main;
import com.example.laprap001.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PageSearchFragment extends Fragment  {
    private ViewGroup container;
    private String POIDOLD, POID;
    private SearchView searchView;
    private RecyclerView recyclerView_PO;
    private Handler handler ;
    private Runnable searchTask;
    private List<Data_PO> data_model_detail = new ArrayList<>();
    private POAdapter poAdapter;
    private static Create_Table_main Cre_db = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pagesearch, container, false);
        this.container = container;



        addControls(view);
        addEvents();

        return view;
    }

    private void addEvents() {
        handler = new Handler();
        final long delay = 1000; // Thời gian đợi 0.5 giây
        searchTask = null; // Tác vụ tìm kiếm
        if (container != null) {
            Cre_db = new Create_Table_main(container.getContext());
            Cre_db.open();
            Cre_db.openTable();

        }
        POIDOLD = getSelectedFactory();
        curDesplay(POIDOLD);
        poAdapter.notifyDataSetChanged();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Thực hiện tìm kiếm ngay khi người dùng nhấn Enter
                handler.removeCallbacks(searchTask);
                handleSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Hủy các tác vụ đang chờ xử lý
                if (searchTask != null) {
                    handler.removeCallbacks(searchTask);
                }

                // Đặt lại tác vụ sau 0.5 giây
                searchTask = new Runnable() {
                    @Override
                    public void run() {
                        handleSearch(newText);
                    }
                };
                handler.postDelayed(searchTask, delay);

                return true;
            }
        });
    }

    private void addControls(View view) {
        recyclerView_PO = view.findViewById(R.id.recyclerView);
        recyclerView_PO.setLayoutManager(new LinearLayoutManager(getContext()));
        poAdapter = new POAdapter(data_model_detail);
        recyclerView_PO.setAdapter(poAdapter);
        searchView = view.findViewById(R.id.action_search);
        searchView.clearFocus();

    }
    private void handleSearch(String query) {
        // Xử lý logic tìm kiếm, ví dụ: lọc dữ liệu trong RecyclerView
        if (query.isEmpty()) {

        } else {
            if (!isValidFormat(query) ){
                Toast.makeText(getContext(), "Định dạng không hợp lệ. Vui lòng nhập đúng định dạng (VD: AA321-).", Toast.LENGTH_SHORT).show();
                return;
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        POID = query.trim();
                    Integer count  =  Cre_db.get_tc_infwno001( POID );
                    if (count > 0) {

                        curDesplay(POID);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Cập nhật giao diện
                                saveFactoryChoice(POID);
                                poAdapter.notifyDataSetChanged();
                            }
                        });;
                            return;
                        }
                     URL url = new URL(Constant_Class.mbaseUrl    + "/getPOID.php?POID=" + POID);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(10000);
                    conn.setReadTimeout(10000);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    //conn.connect();
                    // Kiểm tra kết nối trước khi thực hiện kết nối
                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        String result = reader.readLine();
                        reader.close();
                        try {
                            JSONArray jsonarray = new JSONArray(result);
                            if (jsonarray.length() > 0) {
                                data_model_detail.clear();
                            }
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonObject = jsonarray.getJSONObject(i);
                                String g_tc_infwno001 = jsonObject.getString("tc_infwno001");
                                String g_tc_infwno002 = jsonObject.getString("tc_infwno002");
                                String g_tc_infwno003 = jsonObject.getString("tc_infwno003");
                                String g_tc_infwno004 = jsonObject.getString("tc_infwno004");
                                String g_tc_infwno005 = jsonObject.getString("tc_infwno005");
                                String g_tc_infwno006 = jsonObject.getString("tc_infwno006");
                                String g_tc_infwno007 = jsonObject.getString("tc_infwno007");
                                String g_tc_infwno008 = jsonObject.getString("tc_infwno008");
                                String g_tc_infwno009 = jsonObject.getString("tc_infwno009");
                                String g_tc_infwno010 = jsonObject.getString("tc_infwno010");
                                String g_tc_infwno011 = jsonObject.getString("tc_infwno011");
                                String g_tc_infwno012 = jsonObject.getString("tc_infwno012");
                                String g_tc_infwno013 = jsonObject.getString("tc_infwno013");
                                Cre_db.insert_tc_infwno(g_tc_infwno001, g_tc_infwno002, g_tc_infwno003, g_tc_infwno004, g_tc_infwno005, g_tc_infwno006, g_tc_infwno007,g_tc_infwno008,g_tc_infwno009,g_tc_infwno010,g_tc_infwno011,g_tc_infwno012,g_tc_infwno013);
                                data_model_detail.add(new Data_PO(g_tc_infwno001, g_tc_infwno002, g_tc_infwno003, g_tc_infwno004, g_tc_infwno005, g_tc_infwno006 ,g_tc_infwno007, g_tc_infwno008, g_tc_infwno009, g_tc_infwno010, g_tc_infwno011, g_tc_infwno012, g_tc_infwno013));
                                selectImg(g_tc_infwno002,g_tc_infwno011);
                                saveFactoryChoice(POID);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Cập nhật giao diện
                                poAdapter.notifyDataSetChanged();
                            }
                        });

                    }


                }catch (Exception e){
                e.printStackTrace();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(container.getContext(), "Có lỗi xảy ra khi thực hiện tìm kiếm.", Toast.LENGTH_SHORT).show();
                            }
                        });
                }
                }
            }).start();




        }
        }

    private void selectImg( String Hangmuc, String nhanhieu) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    URL url = new URL(Constant_Class.mbaseUrl    + "/getIMG.php?POID=" + POID + "&HANGMUC=" + Hangmuc + "&NHANHIEU=" + nhanhieu);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(10000);
                    conn.setReadTimeout(10000);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    //conn.connect();
                    // Kiểm tra kết nối trước khi thực hiện kết nối
                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        String result = reader.readLine();
                        reader.close();
                        try {
                            JSONArray jsonarray = new JSONArray(result);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonObject = jsonarray.getJSONObject(i);
                                String g_tc_tc_img001 = jsonObject.getString("TC_IMG001");
                                String g_tc_tc_img002 = jsonObject.getString("TC_IMG002");
                                String g_tc_tc_img003 = jsonObject.getString("TC_IMG003");
                                String g_tc_tc_img004 = jsonObject.getString("TC_IMG004");
                                String g_tc_tc_img005 = jsonObject.getString("TC_IMG005");
                                String g_tc_tc_img006 = jsonObject.getString("TC_IMG006");
                                String g_tc_tc_img007 = jsonObject.getString("TC_IMG007");
                                String g_tc_tc_img008 = jsonObject.getString("TC_IMG008");
                                String g_tc_tc_img009 = jsonObject.getString("TC_IMG009");
                                String g_tc_tc_img010 = jsonObject.getString("TC_IMG010");
                                String g_tc_tc_img011 = jsonObject.getString("TC_IMG011");
                                Cre_db.insert_tc_img(g_tc_tc_img001, g_tc_tc_img002, g_tc_tc_img003, g_tc_tc_img004, g_tc_tc_img005, g_tc_tc_img006, g_tc_tc_img007, g_tc_tc_img008, g_tc_tc_img009, g_tc_tc_img010, g_tc_tc_img011);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }catch (Exception e){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(container.getContext(), "Có lỗi xảy ra khi thực hiện tìm kiếm.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private boolean isValidFormat(String query) {
        // Kiểm tra định dạng: AA321-1901000047, BA312-1903000013,...
        String regex = "^[A-B]{2}\\d{3}-[0-9,*]{10}$";
        return query.matches(regex);
        }
    private void saveFactoryChoice(String poID) {
        SharedPreferences preferences = container.getContext().getSharedPreferences("Poid", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("SelectedPo", poID);
        editor.apply();
    }
    private String getSelectedFactory() {
        SharedPreferences preferences = container.getContext().getSharedPreferences("Poid", Context.MODE_PRIVATE);
        return preferences.getString("SelectedPo", "");
    }

    private void curDesplay(String poid){
        Cursor cur = Cre_db.get_tc_infwno(poid);
        cur.moveToFirst();
        int num = cur.getCount();
        data_model_detail.clear();
        for (int i = 0; i < num; i++) {
            String g_tc_infwno001 = cur.getString(cur.getColumnIndexOrThrow("tc_infwno001"));
            String g_tc_infwno002 = cur.getString(cur.getColumnIndexOrThrow("tc_infwno002"));
            String g_tc_infwno003 = cur.getString(cur.getColumnIndexOrThrow("tc_infwno003"));
            String g_tc_infwno004 = cur.getString(cur.getColumnIndexOrThrow("tc_infwno004"));
            String g_tc_infwno005 = cur.getString(cur.getColumnIndexOrThrow("tc_infwno005"));
            String g_tc_infwno006 = cur.getString(cur.getColumnIndexOrThrow("tc_infwno006"));
            String g_tc_infwno007 = cur.getString(cur.getColumnIndexOrThrow("tc_infwno007"));
            String g_tc_infwno008 = cur.getString(cur.getColumnIndexOrThrow("tc_infwno008"));
            String g_tc_infwno009 = cur.getString(cur.getColumnIndexOrThrow("tc_infwno009"));
            String g_tc_infwno010 = cur.getString(cur.getColumnIndexOrThrow("tc_infwno010"));
            String g_tc_infwno011 = cur.getString(cur.getColumnIndexOrThrow("tc_infwno011"));
            String g_tc_infwno012 = cur.getString(cur.getColumnIndexOrThrow("tc_infwno012"));
            String g_tc_infwno013 = cur.getString(cur.getColumnIndexOrThrow("tc_infwno013"));
            data_model_detail.add(new Data_PO(g_tc_infwno001, g_tc_infwno002, g_tc_infwno003, g_tc_infwno004, g_tc_infwno005, g_tc_infwno006, g_tc_infwno007, g_tc_infwno008, g_tc_infwno009, g_tc_infwno010, g_tc_infwno011, g_tc_infwno012, g_tc_infwno013));
            cur.moveToNext();
        }
      }

}


