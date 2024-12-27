package com.example.laprap001.PageFragment;

import static android.content.Intent.getIntent;

import android.app.Dialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.laprap001.Connection.Connect_Database;
import com.example.laprap001.Constant_Class;
import com.example.laprap001.Database.Create_Table_main;
import com.example.laprap001.Interface.ApiInterface;
import com.example.laprap001.Interface.Call_interface;
import com.example.laprap001.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.bouncycastle.jcajce.provider.symmetric.Threefish;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PageHomeFragment extends Fragment implements Call_interface {
    private static Create_Table_main Cre_db = null;
    private String[] tasks = {"usertable"};
    private int currentIndex = 0;
    public TextView id_user ,id_deparment,action_update,action_delete;
    private Dialog dialog;
    private TextView tv_syncName;
    private ProgressBar progressBar;
    public  ViewGroup container;
    private String ID ;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_pagehome, container, false);
        this.container = container;
        // Khởi tạo Create_Table_main với Context từ container

        ID = getArguments().getString("ID");

        addControls(view);
        addEvents();

        return view;

    }
    private void addControls(View v) {
        id_user = v.findViewById(R.id.id_user);
        id_deparment = v.findViewById(R.id.id_department);
        action_update = v.findViewById(R.id.action_update);
        action_delete = v.findViewById(R.id.action_delete);

    }
    private void addEvents() {
        if (container != null) {
            Cre_db = new Create_Table_main(container.getContext());
            Cre_db.open();
            Cre_db.openTable();
            new IDname().execute("http://172.16.40.20/" + Constant_Class.server + "/");
        }

        action_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.data_sync_layout);
                tv_syncName = dialog.findViewById(R.id.tv_syncName);
                progressBar = dialog.findViewById(R.id.impotDataProgressBar);
                tv_syncName.setText("");
                currentIndex = 0;
                runNextTask();
                dialog.show();

            }

        });
        action_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cre_db.delete_table();

                Toast.makeText(container.getContext(), "Xóa dữ liệu hoàn tất ", Toast.LENGTH_SHORT).show();
            }

        });
    }




    private void runNextTask() {
        if (currentIndex < tasks.length) {
            tv_syncName.setText(tasks[currentIndex]);
            Import_Data(tasks[currentIndex]);
        } else {
            Toast.makeText(container.getContext(), "Cập nhật hoàn tất", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }
    private void Import_Data(String g_table) {
        String baseUrl = "http://172.16.40.20/" + Constant_Class.server + Connect_Database.name_folder;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<JsonArray> call = apiInterface.getDataTable(baseUrl + Connect_Database.name_getdata + g_table);

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    JsonArray jsonArray = response.body();
                    //Cre_db.insertData(g_table,jsonArray);
                    Cre_db.setInsertCallback(PageHomeFragment.this);
                    Create_Table_main.InsertDataTask insertDataTask = Cre_db.new InsertDataTask(progressBar);
                    insertDataTask.execute(g_table, String.valueOf(jsonArray));
                } else {
                    // Xử lý trường hợp response không thành công
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                // Xử lý trường hợp lỗi
            }
        });
    }

    @Override
    public void ImportData_onInsertComplete(String status) {
        if (status.equals("OK")) {
            currentIndex++;
            runNextTask();
        }
    }
    private class IDname extends AsyncTask<String, Integer, String> {
        String result = "";

        @Override
        protected String doInBackground(String... params) {
            try {
                String baseUrl = params[0];
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiInterface apiInterface = retrofit.create(ApiInterface.class);
                Call<List<JsonObject>> call = apiInterface.getData(baseUrl + "getidJson.php?ID=" + ID);
                Response<List<JsonObject>> response = call.execute();

                if (response.isSuccessful()) {
                    List<JsonObject> jsonObjects = response.body();
                    if (jsonObjects != null && jsonObjects.size() > 0) {
                        JsonObject jsonObject = jsonObjects.get(0);
                        result = jsonObject.toString(); // Convert JsonObject to a string
                    } else {
                        result = "FALSE";
                    }
                } else {
                    result = "FALSE";
                }
            } catch (Exception e) {
                result = "FALSE";
            }
            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        protected void onPostExecute(String result) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (!result.equals("FALSE")) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            Constant_Class.UserID = ID;
                            Constant_Class.UserName_zh = jsonObject.getString("CPF02");
                            Constant_Class.UserName_vn = jsonObject.getString("TA_CPF001");
                            Constant_Class.UserDepID = jsonObject.getString("CPF29");
                            Constant_Class.UserDepName = jsonObject.getString("GEM02");
                            Constant_Class.UserFactory = jsonObject.getString("CPF281");
                            //menuID.setText(ID + " " + jsonObject.getString("TA_CPF001") + "\n" + jsonObject.getString("GEM02"));
                            id_user.setText(ID + Constant_Class.UserName_zh);
                            id_deparment.setText(Constant_Class.UserDepName);;
                        } catch (JSONException e) {
                            Toast alert = Toast.makeText(container.getContext(), e.toString(), Toast.LENGTH_LONG);
                            alert.show();
                        }
                    } else {
                        Cursor cursor = Cre_db.getUserData(ID);
                        if (cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            Constant_Class.UserID = ID;
                            Constant_Class.UserName_zh = cursor.getString(cursor.getColumnIndexOrThrow("cpf02"));
                            Constant_Class.UserName_vn = cursor.getString(cursor.getColumnIndexOrThrow("ta_cpf001"));
                            Constant_Class.UserDepID = cursor.getString(cursor.getColumnIndexOrThrow("cpf29"));
                            Constant_Class.UserDepName = cursor.getString(cursor.getColumnIndexOrThrow("gem02"));
                            Constant_Class.UserFactory = cursor.getString(cursor.getColumnIndexOrThrow("cpf281"));
                            //menuID.setText(ID + " " + Constant_Class.UserName_vn + "\n" + Constant_Class.UserDepName);
                            id_user.setText(ID + Constant_Class.UserName_vn);
                            id_deparment.setText(Constant_Class.UserDepName);
                        }
                    }
                }
            });
        }
    }

}
