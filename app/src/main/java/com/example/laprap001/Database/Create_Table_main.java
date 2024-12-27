package com.example.laprap001.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import com.example.laprap001.Interface.Call_interface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Create_Table_main {
    private Call_interface callInterface;
    private Context mCtx = null;
    String DATABASE_NAME = "OQCXH.db";
    public SQLiteDatabase db = null;

    String TABLE_NAME_INFWNO = "tc_infwno_file"; //Bảng tiêu chuẩn các vật liệu
    String TABLE_NAME_TC_IMG = "tc_img_file"; //Bảng tên các tiêu chuẩn
    String TABLE_NAME_USERTABLE = "usertable"; //TB Dữ liệu nhân viên kiểm tra

    String TABLE_NAME_TC_QDD = "tc_qdd_file"; //TB Mã bộ phận
    String TABLE_NAME_TC_QDE = "tc_qde_file"; //TB Dữ liệu đã kiểm tbvra
    String TABLE_NAME_TC_QDF = "tc_qdf_file"; //TB Dữ liệu thông tin ảnh lỗi
    String TABLE_NAME_TC_QDR = "tc_qdr_file"; //TB Dữ liệu kiểm tra
    String TABLE_NAME_TC_QDS = "tc_qds_file"; //TB Dữ liệu ảnh kiểm tra

    String CREATE_TABLE_TC_INFWNO = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_INFWNO + " (tc_infwno001 TEXT, tc_infwno002 TEXT, tc_infwno003 TEXT, tc_infwno004 TEXT, tc_infwno005 TEXT, tc_infwno006 TEXT, tc_infwno007 TEXT, tc_infwno008 TEXT, tc_infwno009 TEXT, tc_infwno010 TEXT,tc_infwno011 TEXT,tc_infwno012 TEXT, tc_infwno013 TEXT)";
    String CREATE_TABLE_TC_IMG = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_TC_IMG + " (TC_IMG001 TEXT, TC_IMG002 TEXT,TC_IMG003 TEXT,TC_IMG004 TEXT,TC_IMG005 TEXT, TC_IMG006 TEXT, TC_IMG007 TEXT, TC_IMG008 TEXT, TC_IMG009 TEXT,TC_IMG010 TEXT, TC_IMG011 TEXT)";
    String CREATE_TABLE_TC_QDD = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_TC_QDD + " (tc_qdd001 TEXT, tc_qdd002 TEXT, tc_qdd003 TEXT, tc_qdd004 TEXT, tc_qdd005 TEXT, tc_qdd006 TEXT )";
    String CREATE_TABLE_TC_QDE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_TC_QDE + " (tc_qde001 TEXT, tc_qde002 TEXT, tc_qde003 TEXT, tc_qde004 TEXT, tc_qde005 TEXT, tc_qde006 TEXT, tc_qde007 TEXT, tc_qde008 TEXT , tc_qdepost TEXT )";
    String CREATE_TABLE_USERTABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_USERTABLE + " (USERTABLE001 TEXT, cpf02 TEXT, ta_cpf001 TEXT, cpf29 TEXT, gem02 TEXT, cpf281 TEXT)";
    String CREATE_TABLE_TC_QDF = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_TC_QDF + " (tc_qdf001 TEXT, tc_qdf002 TEXT, tc_qdf003 TEXT, tc_qdf004 TEXT, tc_qdf005 TEXT, tc_qdfpost TEXT)";
    String CREATE_TABLE_TC_QDR = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_TC_QDR + " (tc_qdr001 TEXT, tc_qdr002 TEXT, tc_qdr003 TEXT, tc_qdr004 TEXT, tc_qdr005 TEXT, tc_qdr006 TEXT, tc_qdr007 TEXT, tc_qdr008 TEXT, tc_qdr009 TEXT,tc_qdr010 TEXT,tc_qdr011 TEXT,tc_qdr012 TEXT,tc_qdruser TEXT,tc_qdrpost TEXT)";
    String CREATE_TABLE_TC_QDS = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_TC_QDS + " (tc_qds001 TEXT, tc_qds002 TEXT,tc_qds003 TEXT, tc_qds004 TEXT)";
    public void setInsertCallback(Call_interface callback) {
        this.callInterface = callback;
    }

    public Create_Table_main(Context ctx) {
        this.mCtx = ctx;
    }

    public void open() throws SQLException {
        db = mCtx.openOrCreateDatabase(DATABASE_NAME, 0, null);
    }

    public void openTable() {
        try {
            db.execSQL(CREATE_TABLE_TC_INFWNO);
            db.execSQL(CREATE_TABLE_TC_IMG);
            db.execSQL(CREATE_TABLE_TC_QDD);
            db.execSQL(CREATE_TABLE_TC_QDE);
            db.execSQL(CREATE_TABLE_USERTABLE);
            db.execSQL(CREATE_TABLE_TC_QDF);
            db.execSQL(CREATE_TABLE_TC_QDR);
            db.execSQL(CREATE_TABLE_TC_QDS);
        } catch (Exception e) {

        }
    }

    public void close() {
        try {
            String DROP_TABLE_NAME_INFWNO = "DROP TABLE IF EXISTS " + TABLE_NAME_INFWNO;
            String DROP_TABLE_NAME_TC_IMG = "DROP TABLE IF EXISTS " + TABLE_NAME_TC_IMG;
            String DROP_TABLE_NAME_TC_QDD = "DROP TABLE IF EXISTS " + TABLE_NAME_TC_QDD;
            String DROP_TABLE_NAME_TC_QDE = "DROP TABLE IF EXISTS " + TABLE_NAME_TC_QDE;
            String DROP_TABLE_NAME_USERTABLE = "DROP TABLE IF EXISTS " + TABLE_NAME_USERTABLE;
            String DROP_TABLE_NAME_TC_QDF = "DROP TABLE IF EXISTS " + TABLE_NAME_TC_QDF;
            String DROP_TABLE_NAME_TC_QDR = "DROP TABLE IF EXISTS " + TABLE_NAME_TC_QDR;
            String DROP_TABLE_NAME_TC_QDS = "DROP TABLE IF EXISTS " + TABLE_NAME_TC_QDS;
            db.execSQL(DROP_TABLE_NAME_INFWNO);
            db.execSQL(DROP_TABLE_NAME_TC_IMG);
            db.execSQL(DROP_TABLE_NAME_TC_QDD);
            db.execSQL(DROP_TABLE_NAME_TC_QDE);
            db.execSQL(DROP_TABLE_NAME_USERTABLE);
            db.execSQL(DROP_TABLE_NAME_TC_QDF);
            db.execSQL(DROP_TABLE_NAME_TC_QDR);
            db.execSQL(DROP_TABLE_NAME_TC_QDS);
            db.close();
        } catch (Exception e) {
        }
    }

    public void delete_table() {
        db.delete(TABLE_NAME_INFWNO, null, null);
        db.delete(TABLE_NAME_TC_IMG, null, null);
        db.delete(TABLE_NAME_TC_QDD, null, null);
        db.delete(TABLE_NAME_USERTABLE, null, null);
        db.delete(TABLE_NAME_TC_QDR, null, null);
        db.delete(TABLE_NAME_TC_QDS, null, null);
    }

    public void delete_DataTable() {
        db.delete(TABLE_NAME_INFWNO, null, null);
        db.delete(TABLE_NAME_TC_IMG, null, null);
        db.delete(TABLE_NAME_TC_QDD, null, null);
        db.delete(TABLE_NAME_USERTABLE, null, null);
        db.delete(TABLE_NAME_TC_QDR, null, null);
        db.delete(TABLE_NAME_TC_QDS, null, null);
    }

    public class InsertDataTask extends AsyncTask<String, Integer, Integer> {
        int progress;
        private ProgressBar progressBar;

        public InsertDataTask(ProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        @Override
        protected Integer doInBackground(String... params) {
            String g_table = params[0];
            String jsonData = params[1];
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                int totalItems = jsonArray.length();
                for (int i = 0; i < totalItems; i++) {
                    JSONObject jsonObject;
                    // Thực hiện insert dữ liệu từ jsonObject
                    switch (g_table) {
                        case "tc_infwno": {
                            try {
                                jsonObject = jsonArray.getJSONObject(i);
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
                                insert_tc_infwno(g_tc_infwno001, g_tc_infwno002, g_tc_infwno003, g_tc_infwno004, g_tc_infwno005, g_tc_infwno006, g_tc_infwno007,g_tc_infwno008,g_tc_infwno009,g_tc_infwno010,g_tc_infwno011,g_tc_infwno012,g_tc_infwno013);

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        }

                        case "TC_IMG": {
                            try {
                                jsonObject = jsonArray.getJSONObject(i);
                                String g_TC_IMG001 = jsonObject.getString("TC_IMG001");
                                String g_TC_IMG002 = jsonObject.getString("TC_IMG002");
                                String g_TC_IMG003= jsonObject.getString("TC_IMG003");
                                String g_TC_IMG004= jsonObject.getString("TC_IMG004");
                                String g_TC_IMG005= jsonObject.getString("TC_IMG005");
                                String g_TC_IMG006= jsonObject.getString("TC_IMG006");
                                String g_TC_IMG007= jsonObject.getString("TC_IMG007");
                                String g_TC_IMG008= jsonObject.getString("TC_IMG008");
                                String g_TC_IMG009= jsonObject.getString("TC_IMG009");
                                String g_TC_IMG010= jsonObject.getString("TC_IMG010");
                                String g_TC_IMG011= jsonObject.getString("TC_IMG011");
                                insert_tc_img(g_TC_IMG001, g_TC_IMG002, g_TC_IMG003,g_TC_IMG004,g_TC_IMG005,g_TC_IMG006,g_TC_IMG007,g_TC_IMG008,g_TC_IMG009,g_TC_IMG010,g_TC_IMG011);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        }

//                        case "tc_qdc": {
//                            try {
//                                jsonObject = jsonArray.getJSONObject(i);
//                                String g_tc_qdc001 = jsonObject.getString("TC_QDC001");
//                                String g_tc_qdc002 = jsonObject.getString("TC_QDC002");
//                                String g_tc_qdc003 = jsonObject.getString("TC_QDC003");
//                                String g_tc_qdc004 = jsonObject.getString("TC_QDC004");
//                                String g_tc_qdc005 = jsonObject.getString("TC_QDC005");
//                                String g_tc_qdc006 = jsonObject.getString("TC_QDC006");
//                                String g_tc_qdc007 = jsonObject.getString("TC_QDC007");
//                                String g_tc_qdc008 = jsonObject.getString("TC_QDC008");
//                                String g_tc_qdc009 = jsonObject.getString("TC_QDC009");
//                                String g_tc_qdc010 = jsonObject.getString("TC_QDC010");
//                                String g_tc_qdc011 = jsonObject.getString("TC_QDC011");
//                                String g_tc_qdc012 = jsonObject.getString("TC_QDC012");
//                                String g_tc_qdc013 = jsonObject.getString("TC_QDC013");
//                                String g_tc_qdc014 = jsonObject.getString("TC_QDC014");
//                                String g_tc_qdc015 = jsonObject.getString("TC_QDC015");
//                                String g_tc_qdc016 = jsonObject.getString("TC_QDC016");
//                                String g_tc_qdc017 = jsonObject.getString("TC_QDC017");
//                                String g_tc_qdc018 = jsonObject.getString("TC_QDC018");
//                                String g_tc_qdc019 = jsonObject.getString("TC_QDC019");
//                                String g_tc_qdc020 = jsonObject.getString("TC_QDC020");
//                                String g_tc_qdc021 = jsonObject.getString("TC_QDC021");
//                                String g_tc_qdc022 = jsonObject.getString("TC_QDC022");
//                                String g_tc_qdc023 = jsonObject.getString("TC_QDC023");
//                                String g_tc_qdc024 = jsonObject.getString("TC_QDC024");
//                                String g_tc_qdc025 = jsonObject.getString("TC_QDC025");
//                                String g_tc_qdc026 = jsonObject.getString("TC_QDC026");
//                                String g_tc_qdc027 = jsonObject.getString("TC_QDC027");
//                                String g_tc_qdc028 = jsonObject.getString("TC_QDC028");
//                                String g_tc_qdc029 = jsonObject.getString("TC_QDC029");
//                                String g_tc_qdc030 = jsonObject.getString("TC_QDC030");
//                                String g_tc_qdc031 = jsonObject.getString("TC_QDC031");
//                                String g_tc_qdc032 = jsonObject.getString("TC_QDC032");
//
//                                insert_qdc(g_tc_qdc001, g_tc_qdc002, g_tc_qdc003, g_tc_qdc004, g_tc_qdc005, g_tc_qdc006, g_tc_qdc007, g_tc_qdc008, g_tc_qdc009, g_tc_qdc010, g_tc_qdc011, g_tc_qdc012, g_tc_qdc013, g_tc_qdc014, g_tc_qdc015, g_tc_qdc016, g_tc_qdc017, g_tc_qdc018, g_tc_qdc019, g_tc_qdc020, g_tc_qdc021, g_tc_qdc022, g_tc_qdc023, g_tc_qdc024, g_tc_qdc025, g_tc_qdc026, g_tc_qdc027, g_tc_qdc028, g_tc_qdc029, g_tc_qdc030, g_tc_qdc031, g_tc_qdc032);
//                            } catch (JSONException e) {
//                                throw new RuntimeException(e);
//                            }
//                            break;
//                        }

                        case "tc_qdd": {
                            try {
                                jsonObject = jsonArray.getJSONObject(i);
                                String g_tc_qdd001 = jsonObject.getString("TC_QDD001");
                                String g_tc_qdd002 = jsonObject.getString("TC_QDD002");
                                String g_tc_qdd003 = jsonObject.getString("TC_QDD003");
                                String g_tc_qdd004 = jsonObject.getString("TC_QDD004");
                                String g_tc_qdd005 = jsonObject.getString("TC_QDD005");
                                String g_tc_qdd006 = jsonObject.getString("TC_QDD006");

                                insert_qdd(g_tc_qdd001, g_tc_qdd002, g_tc_qdd003, g_tc_qdd004, g_tc_qdd005, g_tc_qdd006);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        }

                        case "usertable": {
                            try {
                                jsonObject = jsonArray.getJSONObject(i);
                                String g_USERTABLE001 = jsonObject.getString("USERTABLE001");
                                String g_cpf02 = jsonObject.getString("CPF02");
                                String g_ta_cpf001 = jsonObject.getString("TA_CPF001");
                                String g_cpf29 = jsonObject.getString("CPF29");
                                String g_gem02 = jsonObject.getString("GEM02");
                                String g_cpf281 = jsonObject.getString("CPF281");

                                insert_usertable(g_USERTABLE001, g_cpf02, g_ta_cpf001, g_cpf29, g_gem02, g_cpf281);

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        }
                        case "tc_qdr": {
                            try {
                                jsonObject = jsonArray.getJSONObject(i);
                                String g_tc_qdr001 = jsonObject.getString("TC_QDR001");
                                String g_tc_qdr002 = jsonObject.getString("TC_QDR002");
                                String g_tc_qdr003 = jsonObject.getString("TC_QDR003");
                                String g_tc_qdr004 = jsonObject.getString("TC_QDR004");
                                String g_tc_qdr005 = jsonObject.getString("TC_QDR005");
                                String g_tc_qdr006 = jsonObject.getString("TC_QDR006");
                                String g_tc_qdr007 = jsonObject.getString("TC_QDR007");
                                String g_tc_qdr008 = jsonObject.getString("TC_QDR008");
                                String g_tc_qdr009 = jsonObject.getString("TC_QDR009");
                                String g_tc_qdr010 = jsonObject.getString("TC_QDR010");
                                String g_tc_qdr011 = jsonObject.getString("TC_QDR011");
                                String g_tc_qdr012 = jsonObject.getString("TC_QDR012");
                                String g_tc_qdruser = jsonObject.getString("TC_QDRUSER");
                                String g_tc_qdrpost = jsonObject.getString("TC_QDRPOST");
                                insert_qdr(g_tc_qdr001, g_tc_qdr002, g_tc_qdr003, g_tc_qdr004, g_tc_qdr005, g_tc_qdr006, g_tc_qdr007, g_tc_qdr008, g_tc_qdr009, g_tc_qdr010, g_tc_qdr011, g_tc_qdr012,g_tc_qdruser,g_tc_qdrpost);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        }
                        case "tc_qds": {
                            try {
                                jsonObject = jsonArray.getJSONObject(i);
                                String g_tc_qds001 = jsonObject.getString("TC_QDS001");
                                String g_tc_qds002 = jsonObject.getString("TC_QDS002");
                                String g_tc_qds003= jsonObject.getString("TC_QDS003");
                                String g_tc_qds004= jsonObject.getString("TC_QDS004");

                                insert_qds(g_tc_qds001, g_tc_qds002,g_tc_qds003,g_tc_qds004);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        }
                    }
                    // Cập nhật tiến độ
                    progress = (int) (((i + 1) / (float) totalItems) * 100);
                    publishProgress(progress);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return progress;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progress = values[0];
            // Cập nhật tiến độ insert dữ liệu trên giao diện
            progressBar.setProgress(progress); // Cập nhật tiến trình trên ProgressBar
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Hoàn thành quá trình insert
            if (result == 100) {
                callInterface.ImportData_onInsertComplete("OK");
            }
        }
    }

    public void call_insertPhotoData(String selectedDetail, String selectedDate, String selectedDepartment, String g_user, String fileName) {

        try {
            ContentValues args = new ContentValues();
            args.put("tc_qdf001", selectedDetail);
            args.put("tc_qdf002", selectedDate);
            args.put("tc_qdf003", selectedDepartment);
            args.put("tc_qdf004", g_user);
            args.put("tc_qdf005", fileName);
            args.put("tc_qdfpost", "N");
            db.insert(TABLE_NAME_TC_QDF, null, args);

            //Cập nhật table chứ dữ liệu đã kiểm tra
            call_uqdate_tc_qde(selectedDetail, selectedDate, selectedDepartment, g_user);
        } catch (Exception e) {
        }
    }

    public void uqdate_tc_qdspost(String image_no, String image_date, String image_dept, String image_employ, String image_name) {
        try {
            db.execSQL(" UPDATE tc_qdf_file SET tc_qdfpost = 'Y' " +
                    " WHERE tc_qdf001 ='" + image_no + "' " +
                    " AND tc_qdf002 ='" + image_date + "'" +
                    " AND tc_qdf003 = '" + image_dept + "'" +
                    " AND tc_qdf004 = '" + image_employ + "' " +
                    " AND tc_qdf005 = '" + image_name + "' ");
        } catch (Exception e) {
        }
    }

    private void call_uqdate_tc_qde(String selectedDetail, String selectedDate, String selectedDepartment, String g_user) {
        String g_tc_qde008;
        Cursor c = db.rawQuery(" SELECT tc_qde008 FROM tc_qde_file " +
                " WHERE tc_qde001 ='" + selectedDetail + "' " +
                " AND tc_qde002 ='" + selectedDate + "' " +
                " AND tc_qde003 ='" + g_user + "' " +
                " AND tc_qde004 = '" + selectedDepartment + "' ", null);
        ;
        if (c.moveToFirst()) {
            g_tc_qde008 = String.valueOf(Integer.parseInt(c.getString(0)) + 1);

            db.execSQL(" UPDATE tc_qde_file SET tc_qde008 = '" + g_tc_qde008 + "' , tc_qde006 = 'true' " +
                    " WHERE tc_qde001 ='" + selectedDetail + "' " +
                    " AND tc_qde002 ='" + selectedDate + "'" +
                    " AND tc_qde003 = '" + g_user + "'" +
                    " AND tc_qde004 = '" + selectedDepartment + "' ");
        } else {
            //'1' : Số lượng ảnh lỗi đầu tiên
            //'N' : Trạng thái chưa chuyển đến server
            db.execSQL(" INSERT INTO tc_qde_file VALUES('" + selectedDetail + "', '" + selectedDate + "', '" + g_user + "','" + selectedDepartment + "','false','true','','1','N')");
        }
        c.close();
    }

    private void insert_usertable(String g_USERTABLE001, String g_cpf02, String g_ta_cpf001, String g_cpf29, String g_gem02, String g_cpf281) {
        try {
            ContentValues args = new ContentValues();
            args.put("USERTABLE001", g_USERTABLE001);
            args.put("cpf02", g_cpf02);
            args.put("ta_cpf001", g_ta_cpf001);
            args.put("cpf29", g_cpf29);
            args.put("gem02", g_gem02);
            args.put("cpf281", g_cpf281);
            db.insert(TABLE_NAME_USERTABLE, null, args);
        } catch (Exception e) {
        }
    }

    private void insert_qdd(String g_tc_qdd001, String g_tc_qdd002, String g_tc_qdd003,
                            String g_tc_qdd004, String g_tc_qdd005, String g_tc_qdd006) {
        try {
            ContentValues args = new ContentValues();
            args.put("tc_qdd001", g_tc_qdd001);
            args.put("tc_qdd002", g_tc_qdd002);
            args.put("tc_qdd003", g_tc_qdd003);
            args.put("tc_qdd004", g_tc_qdd004);
            args.put("tc_qdd005", g_tc_qdd005);
            args.put("tc_qdd006", g_tc_qdd006);
            db.insert(TABLE_NAME_TC_QDD, null, args);
        } catch (Exception e) {
        }
    }

//    private void insert_qdc(String g_tc_qdc001, String g_tc_qdc002, String g_tc_qdc003, String g_tc_qdc004,
//                            String g_tc_qdc005, String g_tc_qdc006, String g_tc_qdc007, String g_tc_qdc008,
//                            String g_tc_qdc009, String g_tc_qdc010,String g_tc_qdc011,String g_tc_qdc012,
//                            String g_tc_qdc013, String g_tc_qdc014, String g_tc_qdc015,String g_tc_qdc016,String g_tc_qdc017,String g_tc_qdc018,String g_tc_qdc019,String g_tc_qdc020,String g_tc_qdc021,
//                            String g_tc_qdc022,String g_tc_qdc023,String g_tc_qdc024,String g_tc_qdc025,String g_tc_qdc026,String g_tc_qdc027,String g_tc_qdc028,String g_tc_qdc029,String g_tc_qdc030,String g_tc_qdc031,String g_tc_qdc032) {
//        try {
//            ContentValues args = new ContentValues();
//            args.put("tc_qdc001", g_tc_qdc001);//      tc_qdc001/Mã vật liệu
//            args.put("tc_qdc002", g_tc_qdc002);//            tc_qdc002/màu sắc
//            args.put("tc_qdc003", g_tc_qdc003);//            tc_qdc003/Mã số khuôn
//            args.put("tc_qdc004", g_tc_qdc004);//            tc_qdc004/mã số máy
//            args.put("tc_qdc005", g_tc_qdc005);//            tc_qdc005/Thời gian kiểm tra
//            args.put("tc_qdc006", g_tc_qdc006);//            tc_qdc006/Tay sản phẩm
//            args.put("tc_qdc007", g_tc_qdc007);//            tc_qdc007/kích thước dày 1
//            args.put("tc_qdc008", g_tc_qdc008);//            tc_qdc008/kích thước dày 2
//            args.put("tc_qdc009", g_tc_qdc009);//            tc_qdc009/kích thước dày 3
//            args.put("tc_qdc010", g_tc_qdc010);//            tc_qdc010/kích thước dày 4
//            args.put("tc_qdc011", g_tc_qdc011);//            tc_qdc011/kích thước dày 5
//            args.put("tc_qdc012", g_tc_qdc012);//            tc_qdc012/kích thước dày 6
//            args.put("tc_qdc013", g_tc_qdc013);//            tc_qdc013/kích thước dày 7
//            args.put("tc_qdc014", g_tc_qdc014);//            tc_qdc014/kích thước dày 8
//            args.put("tc_qdc015", g_tc_qdc015);//            tc_qdc015/kích thước dày 9
//            args.put("tc_qdc016", g_tc_qdc016);//            tc_qdc016/Đường kính điện cực
//            args.put("tc_qdc017", g_tc_qdc017);//            tc_qdc017/Đường kính vòng ron
//            args.put("tc_qdc018", g_tc_qdc018);//            tc_qdc018/Độ cao đường dán miếng mỏng
//            args.put("tc_qdc019", g_tc_qdc019);//            tc_qdc019/Độ dày chân lộ ACID
//            args.put("tc_qdc020", g_tc_qdc020);//            tc_qdc020/Độ dày nắp
//            args.put("tc_qdc021", g_tc_qdc021);//            tc_qdc021/Độ dày đường miếng mỏng
//            args.put("tc_qdc022", g_tc_qdc022);//            tc_qdc022/Trọng lượng QA
//            args.put("tc_qdc023", g_tc_qdc023);//            tc_qdc023/Trọng lượng NVSX
//            args.put("tc_qdc024", g_tc_qdc024);//            tc_qdc024/Tính va đập
//            args.put("tc_qdc025", g_tc_qdc025);//            tc_qdc025/tính chịu lực bên trong
//            args.put("tc_qdc026", g_tc_qdc026);//            tc_qdc026/KT lắp rắp
//            args.put("tc_qdc027", g_tc_qdc027);//            tc_qdc027/Biến dạng
//            args.put("tc_qdc028", g_tc_qdc028);//            tc_qdc028/Thiếu liệu
//            args.put("tc_qdc029", g_tc_qdc029);//            tc_qdc029/Bít lổ
//            args.put("tc_qdc030", g_tc_qdc030);//            tc_qdc030/màu sắc
//            args.put("tc_qdc031", g_tc_qdc031);//            tc_qdc031/Dán PE
//            args.put("tc_qdc032", g_tc_qdc032);//            tc_qdc032/Ngoại quan
//
//
////            db.insert(TABLE_NAME_TC_QDC, null, args);
//        } catch (Exception e) {
//        }
//    }

    public void insert_tc_img(String g_TC_IMG001, String g_TC_IMG002, String g_TC_IMG003, String g_TC_IMG004, String g_TC_IMG005, String g_TC_IMG006, String g_TC_IMG007, String g_TC_IMG008, String g_TC_IMG009, String g_TC_IMG010,String g_TC_IMG011) {
        try {
            ContentValues args = new ContentValues();
            args.put("TC_IMG001", g_TC_IMG001);
            args.put("TC_IMG002", g_TC_IMG002);
            args.put("TC_IMG003", g_TC_IMG003);
            args.put("TC_IMG004", g_TC_IMG004);
            args.put("TC_IMG005", g_TC_IMG005);
            args.put("TC_IMG006", g_TC_IMG006);
            args.put("TC_IMG007", g_TC_IMG007);
            args.put("TC_IMG008", g_TC_IMG008);
            args.put("TC_IMG009", g_TC_IMG009);
            args.put("TC_IMG010", g_TC_IMG010);
            args.put("TC_IMG011", g_TC_IMG011);
            db.insert(TABLE_NAME_TC_IMG, null, args);
        } catch (Exception e) {
        }
    }

   public void insert_tc_infwno(String g_tc_infwno001, String g_tc_infwno002, String g_tc_infwno003, String g_tc_infwno004, String g_tc_infwno005, String g_tc_infwno006, String g_tc_infwno007, String g_tc_infwno008,String g_tc_infwno009,String g_tc_infwno010, String g_tc_infwno011, String g_tc_infwno012, String g_tc_infwno013) {
        try {
            ContentValues args = new ContentValues();   //
            args.put("tc_infwno001", g_tc_infwno001); // Đơn đặt hàng
            args.put("tc_infwno002", g_tc_infwno002); // Hạng mục
            args.put("tc_infwno003", g_tc_infwno003); // Mã khách hàng giao hàng
            args.put("tc_infwno004", g_tc_infwno004); // Tên khách hàng
            args.put("tc_infwno005", g_tc_infwno005); // Ngày đông ý giao hàng
            args.put("tc_infwno006", g_tc_infwno006); // Số lượng
            args.put("tc_infwno007", g_tc_infwno007); // Mã vật liệu
            args.put("tc_infwno008", g_tc_infwno008); // tên tiếng việt
            args.put("tc_infwno009", g_tc_infwno009); // Quy cách
            args.put("tc_infwno010", g_tc_infwno010); // Mã đơn đặt hàng khách hàng
            args.put("tc_infwno011", g_tc_infwno011); // MS nhãn hiệu
            args.put("tc_infwno012", g_tc_infwno012); // Mã Khuôn in
            args.put("tc_infwno013", g_tc_infwno013); // Mã đơn công
            db.insert(TABLE_NAME_INFWNO, null, args);
        } catch (Exception e) {
        }
    }

    public Cursor getUserData(String g_UserID) {
        String selectQuery = "SELECT * FROM usertable WHERE USERTABLE001 = '" + g_UserID + "' ";
        return db.rawQuery(selectQuery, null);
    }

    public int checkUserData(String g_UserID) {
        Cursor c = db.rawQuery("SELECT count(*) FROM usertable WHERE USERTABLE001 = '" + g_UserID + "'", null);
        c.moveToFirst();
        Integer tcount = c.getInt(0);
        c.close();
        return tcount;
    }

    public Cursor getdata_tc_qdd(String g_factory) {
        String g_dk;
        if (g_factory.equals("DH")) {
            g_dk = "'01','02','03'";
        } else {
            g_dk = "'04'";
        }

        String selectQuery = "SELECT * FROM tc_qdd_file " +
                " WHERE tc_qdd001 in (" + g_dk + ") " +
                " ORDER BY tc_qdd001,tc_qdd002 ";

        return db.rawQuery(selectQuery, null);
    }

    public Cursor departmentCheckedData() {
        //String selectQuery = " SELECT 0,tc_qdd004||' '||tc_qdd005 AS donvi ,tc_qdd003, SUM((CASE WHEN tc_qde006 = 'true' THEN 1 ELSE 0 end)) slerr  ,tc_qde002,tc_qde004 " +
        String selectQuery = " SELECT 0,(CASE WHEN tc_qdd005 ='null' THEN tc_qdd004 || ' ' || '' ELSE tc_qdd004 || ' ' || tc_qdd005 END) AS donvi ,tc_qdd003, SUM((CASE WHEN tc_qde006 = 'true' THEN 1 ELSE 0 end)) slerr  ,tc_qde002,tc_qde004 " +
                " FROM tc_qde_file,tc_qdd_file " +
                " WHERE tc_qdd006=tc_qde004 " +
                " GROUP BY  tc_qdd004||' '||tc_qdd005,tc_qdd003,tc_qde002,tc_qde004 " +
                " ORDER BY tc_qde002 DESC,tc_qde004,tc_qde003 ";

        return db.rawQuery(selectQuery, null);
    }

    public Cursor getHangMucLon() {
        String selectQuery = " SELECT TC_IMG003,TC_IMG004,TC_IMG005  FROM TC_IMG_file WHERE TC_IMG003 BETWEEN '01' AND '08' ORDER BY TC_IMG003 ";
        return db.rawQuery(selectQuery, null);
    }

    public Cursor getHangMucChiTiet(int g_position, String g_ngay, String g_maBP, String userID) {
        String g_hangmuc = String.format("%02d", g_position + 1);

        String selectQuery = " SELECT tc_qdc004, tc_qdc005, tc_qdc006, tc_qdc007, tc_qdc008, tc_qde007,  COALESCE(SUM(tc_qde008), 0 )  AS tc_qde008, " +
                "    CASE WHEN tc_qde006 = 'true' THEN 'false' ELSE 'true' END AS tc_qde006 " +
                " FROM    tc_qdc_file" +
                " LEFT JOIN    tc_qde_file ON tc_qdc005 = tc_qde001 AND tc_qde004 = '" + g_maBP + "' AND tc_qde002 = '" + g_ngay + "'  " +
                " WHERE    tc_qdc003 = '" + g_hangmuc + "'" +
                " GROUP BY tc_qdc004, tc_qdc005, tc_qdc006, tc_qdc007, tc_qdc008, tc_qde007 " +
                " ORDER BY  tc_qdc005";
        return db.rawQuery(selectQuery, null);
    }

    public void uqd_GhiChu(String g_ngay, String g_maBP, String g_tc_qdc005, String g_User, String inputData) {
        Cursor c = db.rawQuery("SELECT count(*) FROM tc_qde_file " +
                " WHERE tc_qde001 = '" + g_tc_qdc005 + "' " +
                " AND tc_qde002 = '" + g_ngay + "' " +
                " AND tc_qde003 = '" + g_User + "' " +
                " AND tc_qde004 = '" + g_maBP + "' ", null);
        c.moveToFirst();
        Integer tcount = c.getInt(0);
        c.close();

        if (tcount == 0) {
            db.execSQL(" INSERT INTO tc_qde_file VALUES('" + g_tc_qdc005 + "', '" + g_ngay + "', '" + g_User + "','" + g_maBP + "','false','false','" + inputData + "','0','N')");
        } else {
            db.execSQL(" UPDATE tc_qde_file SET tc_qde007 = '" + inputData + "' " +
                    " WHERE tc_qde001 = '" + g_tc_qdc005 + "' " +
                    " AND tc_qde002 = '" + g_ngay + "' " +
                    " AND tc_qde003 = '" + g_User + "' " +
                    " AND tc_qde004 = '" + g_maBP + "' ");
        }
    }

    public Cursor getTc_qde_Upload(String input_bdate, String input_edate, String input_department) {
        //Lấy dữ liệu tc_qde_file uqdate tới máy chủ Oracle
        String selectQuery = " SELECT * FROM tc_qde_file WHERE 1=1 ";
        if (input_bdate.isEmpty() && input_edate.isEmpty() && input_department.isEmpty()) {
            selectQuery += " AND tc_qdepost = 'N' ";
        }
        if (!input_department.isEmpty()) {
            selectQuery += " AND tc_qde004 = '" + input_department + "' ";
        }
        if (!input_bdate.isEmpty() && !input_edate.isEmpty()) {
            selectQuery += " AND tc_qde002 BETWEEN '" + input_bdate + "' AND '" + input_edate + "'";
        }
        selectQuery += " ORDER BY tc_qde002,tc_qde004,tc_qde001 ";

        return db.rawQuery(selectQuery, null);
    }

    public Cursor getTc_qdf_Upload(String input_bdate, String input_edate, String input_department) {
        //Lấy dữ liệu tc_qdf_file uqdate tới máy chủ Oracle
        String selectQuery = " SELECT * FROM tc_qdf_file WHERE 1=1 ";
        if (input_bdate.isEmpty() && input_edate.isEmpty() && input_department.isEmpty()) {
            selectQuery += " AND tc_qdfpost = 'N' ";
        }
        if (!input_department.isEmpty()) {
            selectQuery += " AND tc_qdf003 = '" + input_department + "' ";
        }
        if (!input_bdate.isEmpty() && !input_edate.isEmpty()) {
            selectQuery += " AND tc_qdf002 BETWEEN '" + input_bdate + "' AND '" + input_edate + "'";
        }
        selectQuery += " ORDER BY tc_qdf002,tc_qdf003,tc_qdf001 ";

        return db.rawQuery(selectQuery, null);
    }

    public void delete_Image(String name) {
        try {
            db.execSQL("DELETE FROM tc_qdf_file WHERE tc_qdf005 = '" + name + "' ");
        } catch (Exception e) {
            String ex = e.getMessage().toString();
        }
    }

    public Cursor getGroup(String position,String date, String bophan, String hangmuc) {
        String selectQuery = " SELECT DISTINCT tc_qdf001,tc_qdf002,tc_qdf003,tc_qdd004,tc_qdd005,tc_qdc006,tc_qdc007 FROM tc_qdf_file,tc_qdd_file,tc_qdc_file WHERE tc_qdc005 = tc_qdf001 AND tc_qdd006 = tc_qdf003 ";
        if (date.isEmpty() && bophan.isEmpty() && hangmuc.isEmpty()) {
            //selectQuery += " AND tc_qdfpost = 'N' ";
        }
        if (!position.isEmpty()) {
            String g_position = String.format("%02d", Integer.parseInt(position) + 1);
            selectQuery += " AND tc_qdc003 = '" + g_position + "' ";
        }
        if (!bophan.isEmpty()) {
            selectQuery += " AND tc_qdf001 = '" + bophan + "' ";
        }
        if (!date.isEmpty()) {
            selectQuery += " AND tc_qdf002 = '" + date + "'";
        }
        if (!hangmuc.isEmpty()) {
            selectQuery += " AND tc_qdf003 = '" + hangmuc + "'";
        }
        selectQuery += " ORDER BY tc_qdf002,tc_qdf003,tc_qdf001 ";
        return db.rawQuery(selectQuery, null);
    }

    public Cursor get_data_tc_img(String tc_infwno001, String tc_infwno002) {
        String selectQuery = " SELECT * FROM tc_img_file WHERE tc_img001 = '"+tc_infwno001+"' AND tc_img002 = '"+tc_infwno002+"'";
        return db.rawQuery(selectQuery, null);
    }

    public void uqdate_imagecount(String selectedDetail, String selectedDate, String selectedDepartment, String g_user) {
        String g_tc_qde006 = "true",g_tc_qde008;
        Cursor c = db.rawQuery(" SELECT tc_qde008 FROM tc_qde_file " +
                " WHERE tc_qde001 ='" + selectedDetail + "' " +
                " AND tc_qde002 ='" + selectedDate + "' " +
                " AND tc_qde003 ='" + g_user + "' " +
                " AND tc_qde004 = '" + selectedDepartment + "' ", null);
        c.moveToFirst();
        g_tc_qde008 = String.valueOf(Integer.parseInt(c.getString(0)) - 1);

        if (g_tc_qde008.equals("0")) {
            db.execSQL(" Delete FROM tc_qde_file   " +
                    " WHERE tc_qde001 ='" + selectedDetail + "' " +
                    " AND tc_qde002 ='" + selectedDate + "'" +
                    " AND tc_qde003 = '" + g_user + "'" +
                    " AND tc_qde004 = '" + selectedDepartment + "' ");
        }else {
            db.execSQL(" UPDATE tc_qde_file SET tc_qde008 = '" + g_tc_qde008 + "' , tc_qde006 = '" + g_tc_qde006 + "'   " +
                    " WHERE tc_qde001 ='" + selectedDetail + "' " +
                    " AND tc_qde002 ='" + selectedDate + "'" +
                    " AND tc_qde003 = '" + g_user + "'" +
                    " AND tc_qde004 = '" + selectedDepartment + "' ");
        }
        

        c.close();
    }

    public Cursor get_Thongtin_Anh(String name) {
        String selectQuery = " SELECT tc_qdf001,tc_qdf002,tc_qdf003,tc_qdf004 FROM tc_qdf_file WHERE 1=1 ";
        if (!name.isEmpty()) {
            selectQuery += " AND tc_qdf005 = '" + name + "' ";
        }
        selectQuery += " ORDER BY tc_qdf002,tc_qdf003,tc_qdf001 ";
        return db.rawQuery(selectQuery, null);
    }

    public Cursor get_hangmucchitiet(String g_positionlon, String g_positioncon) {
        String selectQuery = " SELECT tc_qdc005,tc_qdc006,tc_qdc007 FROM tc_qdc_file WHERE 1=1  ";
        if (!g_positionlon.isEmpty()) {
            String g_hangmuc = String.format("%02d", Integer.parseInt(g_positionlon) + 1);
            selectQuery += " AND tc_qdc003 = '" + g_hangmuc + "' ";
        }
        if (!g_positioncon.isEmpty()) {
            String g_hangmuccon = String.format("%02d", Integer.parseInt(g_positioncon) + 1);
            selectQuery += " AND tc_qdc004 = '" + g_hangmuccon + "' ";
        }
        selectQuery += " ORDER BY  tc_qdc005 ";
        return db.rawQuery(selectQuery, null);
    }

    public void uqdate_MoveImage(String nameold, String namenew, String chitiet, String bophan) {
        db.execSQL("UPDATE tc_qdf_file SET tc_qdf001 = '" + chitiet + "' , tc_qdf003 = '" + bophan + "', " +
                " tc_qdf005 = '" + namenew + "' WHERE tc_qdf005='" + nameold + "' ");
    }

    public void uqdate_move(String selectedDetail, String selectedDate, String selectedDepartment, String g_user) {
        String g_tc_qde008;
        Cursor c = db.rawQuery(" SELECT tc_qde008 FROM tc_qde_file " +
                " WHERE tc_qde001 ='" + selectedDetail + "' " +
                " AND tc_qde002 ='" + selectedDate + "' " +
                " AND tc_qde003 ='" + g_user + "' " +
                " AND tc_qde004 = '" + selectedDepartment + "' ", null);
        ;
        if (c.moveToFirst()) {
            g_tc_qde008 = String.valueOf(Integer.parseInt(c.getString(0)) - 1);
            if (Integer.parseInt(g_tc_qde008) == 0) {
                db.execSQL("DELETE FROM tc_qde_file " +
                        " WHERE tc_qde001 ='" + selectedDetail + "' " +
                        " AND tc_qde002 ='" + selectedDate + "'" +
                        " AND tc_qde003 = '" + g_user + "'" +
                        " AND tc_qde004 = '" + selectedDepartment + "' ");
            } else {
                db.execSQL(" UPDATE tc_qde_file SET tc_qde008 = '" + g_tc_qde008 + "' " +
                        " WHERE tc_qde001 ='" + selectedDetail + "' " +
                        " AND tc_qde002 ='" + selectedDate + "'" +
                        " AND tc_qde003 = '" + g_user + "'" +
                        " AND tc_qde004 = '" + selectedDepartment + "' ");
            }
        }
        c.close();
    }
    public String get_tc_0501_img(String g_tc_img001 , String g_tc_img002 ) {
        String selectQuery = " SELECT tc_img003 FROM tc_img_file WHERE tc_img001 = '"+g_tc_img001+"' AND tc_img002 = '"+g_tc_img002+"' AND tc_img003 LIKE '0501%'";
        Cursor a = db.rawQuery(selectQuery, null);
        String result = null;

        if (a != null && a.moveToFirst()) {
            if (!a.isNull(0)) {
                result = a.getString(0);
            }
        }
        if (a != null) {
            a.close();
        }
        return result;
    }
    public void uqdate_movenewImage(String selectedDetail, String selectedDate, String selectedDepartment, String g_user) {
        String g_tc_qde008;
        Cursor c = db.rawQuery(" SELECT tc_qde008 FROM tc_qde_file " +
                " WHERE tc_qde001 ='" + selectedDetail + "' " +
                " AND tc_qde002 ='" + selectedDate + "' " +
                " AND tc_qde003 ='" + g_user + "' " +
                " AND tc_qde004 = '" + selectedDepartment + "' ", null);
        ;
        if (c.moveToFirst()) {
            g_tc_qde008 = String.valueOf(Integer.parseInt(c.getString(0)) + 1);

            db.execSQL(" UPDATE tc_qde_file SET tc_qde008 = '" + g_tc_qde008 + "' , tc_qde006 = 'true' " +
                    " WHERE tc_qde001 ='" + selectedDetail + "' " +
                    " AND tc_qde002 ='" + selectedDate + "'" +
                    " AND tc_qde003 = '" + g_user + "'" +
                    " AND tc_qde004 = '" + selectedDepartment + "' ");
        } else {
            //'1' : Số lượng ảnh lỗi đầu tiên
            //'N' : Trạng thái chưa chuyển đến server
            db.execSQL(" INSERT INTO tc_qde_file VALUES('" + selectedDetail + "', '" + selectedDate + "', '" + g_user + "','" + selectedDepartment + "','false','true','','1','N')");
        }
        c.close();
    }

    public Cursor get_ImageInfo(String name) {
        String selectQuery = " SELECT TC_IMG004,TC_IMG005,tc_qdc005,tc_qdc006,tc_qdc007,tc_qdd003," +
                " tc_qdd004,tc_qdd005,tc_qdf002,tc_qdf005,USERTABLE001,cpf02,ta_cpf001  FROM tc_qdc_file,tc_qdf_file," +
                " TC_IMG_file,tc_qdd_file,usertable  WHERE tc_qdc005=tc_qdf001 AND TC_IMG001=tc_qdc001" +
                " AND tc_qdd006 = tc_qdf003 AND TC_IMG002=tc_qdc002 AND TC_IMG003= tc_qdc003 AND USERTABLE001 = tc_qdf004 ";
        if (!name.isEmpty()) {
            selectQuery += " AND tc_qdf005='" + name + "' ";
        }
        selectQuery += " ORDER BY  tc_qdc005 ";
        return db.rawQuery(selectQuery, null);
    }

    public Cursor getChartCompleteData() {
        //select max(tc_qdd002) max_tc_qdd002  from tc_qdd_filemColumns = {String[1]@33400} ["g_count"]
        String selectQuery = " select tc_qde004, tc_qdd004,tc_qdd005, " +
                "( case when ( select sum(tc_qde008) total from tc_qde_file where tc_qde002 = (select max(tc_qde002) from tc_qde_file)) = 0 then 0 else   round((CAST(sum(tc_qde008) as REAL) / (select sum(tc_qde008) total from tc_qde_file where tc_qde002 = (select max(tc_qde002) from tc_qde_file)) ) * 100   ,2)  end ) g_count   " +
                " from tc_qde_file ,tc_qdd_file " +
                " where tc_qdd006= tc_qde004 and tc_qde002 = (select max(tc_qde002) from tc_qde_file)  group by tc_qde004  ";

        return db.rawQuery(selectQuery, null);
    }

    public void call_uqd_tc_qdepost(Cursor c_getTc_qde) {
        if (c_getTc_qde.getCount() > 0) {
            c_getTc_qde.moveToFirst();
            for (int i = 0; i < c_getTc_qde.getCount(); i++) {
                String g_tc_qde001 = c_getTc_qde.getString(c_getTc_qde.getColumnIndexOrThrow("tc_qde001"));
                String g_tc_qde002 = c_getTc_qde.getString(c_getTc_qde.getColumnIndexOrThrow("tc_qde002"));
                String g_tc_qde003 = c_getTc_qde.getString(c_getTc_qde.getColumnIndexOrThrow("tc_qde003"));
                String g_tc_qde004 = c_getTc_qde.getString(c_getTc_qde.getColumnIndexOrThrow("tc_qde004"));

                db.execSQL(" UPDATE tc_qde_file SET tc_qdepost = 'Y' " +
                        " WHERE tc_qde001 ='" + g_tc_qde001 + "' " +
                        " AND tc_qde002 ='" + g_tc_qde002 + "'" +
                        " AND tc_qde003 = '" + g_tc_qde003 + "'" +
                        " AND tc_qde004 = '" + g_tc_qde004 + "' ");
                c_getTc_qde.moveToNext();
            }
        }
    }
    public Cursor getDepartment_today(String date) {
        String selectQuery = " select distinct tc_qde004,tc_qdd003,tc_qdd004,tc_qdd005 from tc_qde_file,tc_qdd_file  where tc_qdd006 = tc_qde004 ";
        if (!date.isEmpty()) {
            selectQuery += " and  tc_qde002= '" + date + "'";
        }
        selectQuery += " ORDER BY tc_qde004 ";
        return db.rawQuery(selectQuery, null);
    }

    public Cursor getBatteryData() {
        String selectQuery = "SELECT tc_qdc001,tc_qdc002,tc_qdc003,tc_qdc004,tc_qdc005,tc_qdc007 FROM tc_qdc_file ORDER BY tc_qdc005";
        return db.rawQuery(selectQuery, null);
    }
    public Cursor getDataImage(String name) {
        String selectQuery = "SELECT TC_IMG005,tc_qdc007,tc_qdd004,tc_qdd005,tc_qdd001 FROM TC_IMG_file,tc_qdc_file,tc_qdd_file,tc_qdf_file" +
                " WHERE tc_qdf001 = tc_qdc005 AND tc_qdc001 = TC_IMG001 AND tc_qdc002 = TC_IMG002 AND tc_qdc003 = TC_IMG003" +
                " AND tc_qdf003 = tc_qdd006  " +
                " AND tc_qdf005='"+ name +"' ORDER BY TC_IMG005,tc_qdc007,tc_qdd004,tc_qdd005";
        return db.rawQuery(selectQuery, null);
    }

    public void UpdateData() {
        try {
            db.execSQL("DELETE FROM tc_qde_file WHERE tc_qde002 < DATE('now', '-2 months') ");
        } catch (Exception e) {
            String ex = e.getMessage().toString();
        }
    }
    public Cursor getData() {
        String selectQuery = "SELECT tc_qdf001,tc_qdf002,tc_qdf003,tc_qdf004,tc_qdf005 " +
                " FROM tc_qdf_file WHERE tc_qdf002 < DATE('now', '-2 months') ORDER BY tc_qdf005";
        return db.rawQuery(selectQuery, null);
    }

    public Cursor getAllBP() {
        String g_dk="'01','02','03','04'";
        String selectQuery = "SELECT * FROM tc_qdd_file " +
                " WHERE tc_qdd001 in (" + g_dk + ") " +
                " ORDER BY tc_qdd001,tc_qdd002 ";

        return db.rawQuery(selectQuery, null);
    }
    public Cursor get_content(String loai){
        String selectQuery = "";
        /* 01 = ACID
        *  02 = SDTF
        *  03 = SDCF
        *  04 = TLUU
        *  05 = Dgoi */
        if (loai.equals("ACID"))
        {
            selectQuery  = "SELECT tc_qdc004,tc_qdc005,tc_qdc007 FROM tc_qdc_file WHERE tc_qdc002 = '01' AND tc_qdc003='01'  ";
        }else if(loai.equals("SDTF")){
            selectQuery  = "SELECT tc_qdc004,tc_qdc005,tc_qdc007 FROM tc_qdc_file WHERE tc_qdc002 = '02' AND tc_qdc003='02'  ";
        }
        else if(loai.equals("SDCF")){
            selectQuery  = "SELECT tc_qdc004,tc_qdc005,tc_qdc007 FROM tc_qdc_file WHERE tc_qdc002 = '03' AND tc_qdc003='03'  ";
        }
        else if(loai.equals("TLUU")){
            selectQuery  = "SELECT tc_qdc004,tc_qdc005,tc_qdc007 FROM tc_qdc_file WHERE tc_qdc002 = '04' AND tc_qdc003='04'  ";
        }
        else if(loai.equals("DGOI")){
            selectQuery  = "SELECT tc_qdc004,tc_qdc005,tc_qdc007 FROM tc_qdc_file WHERE tc_qdc002 = '05' AND tc_qdc003='05'  ";
        }

        return db.rawQuery(selectQuery, null);
    }


    public void insert_qdr(String g_tc_qdr001, String g_tc_qdr002, String g_tc_qdr003, String g_tc_qdr004,
                           String g_tc_qdr005, String g_tc_qdr006, String g_tc_qdr007, String g_tc_qdr008, String g_tc_qdr009, String g_tc_qdr010, String g_tc_qdr011, String g_tc_qdr012, String g_tc_qdruser,String g_tc_qdrpost) {
        try {
            ContentValues args = new ContentValues();
            args.put("tc_qdr001", g_tc_qdr001);//Mã phiếu theo dõi
            args.put("tc_qdr002", g_tc_qdr002);//Xưởng
            args.put("tc_qdr003", g_tc_qdr003);//Trạm
            args.put("tc_qdr004", g_tc_qdr004);//Hạng mục
            args.put("tc_qdr005", g_tc_qdr005);//Ngày tháng
            args.put("tc_qdr006", g_tc_qdr006);//Giờ
            args.put("tc_qdr007", g_tc_qdr007);//Bộ phận
            args.put("tc_qdr008", g_tc_qdr008);//Thuyết minh
            args.put("tc_qdr009", g_tc_qdr009);//số lần
            args.put("tc_qdr010", g_tc_qdr010);//Trang thái E(Effective)/C(cancelled)
            args.put("tc_qdr011", g_tc_qdr011);//Theo dõi   Y/N  Không có đẩy lên tiptop
            args.put("tc_qdr012", g_tc_qdr012);//Trang thái đơn   E(Effective)/C(cancelled)
            args.put("tc_qdruser", g_tc_qdruser);//Người thao tác
            args.put("tc_qdrpost", g_tc_qdrpost);//Trang thái đẩy lên hay chưa Y/N   Không có đẩy lên tiptop
            db.insert(TABLE_NAME_TC_QDR, null, args);
        } catch (Exception e) {
        }
    }
    public void insert_qds(String g_tc_qds001, String g_tc_qds002,String g_tc_qds003,String g_tc_qds004) {
        try {
            ContentValues args = new ContentValues();
            args.put("tc_qds001", g_tc_qds001);//Mã phiếu theo dõi
            args.put("tc_qds002", g_tc_qds002);//Xuong
            args.put("tc_qds003", g_tc_qds003);//Số lần
            args.put("tc_qds004", g_tc_qds004);//Hình ảnh
            db.insert(TABLE_NAME_TC_QDS, null, args);
        } catch (Exception e) {
        }
    }
    public Cursor get_item_detial(String date,String bophan,String xuong, String tram, String tc_qdr004){
        String selectQuery = "";


        selectQuery= "SELECT DISTINCT tc_qdr001 FROM tc_qdr_file  WHERE tc_qdr002 = '"+xuong+"' AND tc_qdr003 = '"+tram+"'  AND tc_qdr004 = '"+tc_qdr004+"'  ";
        if (date == null){
            selectQuery = selectQuery + " AND 1=1 ";

        }else  if (date != null){
            selectQuery = selectQuery + " AND tc_qdr005 ='"+date+"' ";
        }
        if (bophan== null){
            selectQuery = selectQuery + " AND 1=1 ";
        }else if (bophan != null ) {
            selectQuery = selectQuery + " AND tc_qdr007 = '"+bophan+"' ";
        }
        selectQuery =  selectQuery + "  ORDER BY tc_qdr010 DESC,tc_qdr011 DESC, tc_qdr005";
        return db.rawQuery(selectQuery, null);
    }
    public Cursor get_item_detial_max(Cursor cursormadon) {

        String selectQuery = "sad";

        String g_tc_qdr001 = cursormadon.getString(cursormadon.getColumnIndexOrThrow("tc_qdr001"));
       selectQuery= "SELECT tc_qdr001,tc_qdr002,tc_qdr007,tc_qdr005,tc_qdr008,tc_qdr009,tc_qdr010,tc_qdr011 FROM tc_qdr_file WHERE  tc_qdr009 = (SELECT MAX(tc_qdr009) FROM tc_qdr_file  WHERE tc_qdr001 = '"+g_tc_qdr001 +"' AND tc_qdr012 <> 'C') AND tc_qdr001 = '"+g_tc_qdr001 +"' AND tc_qdr012 <> 'C' ORDER BY tc_qdr010 DESC,tc_qdr011 DESC,tc_qdr005 ";

        return db.rawQuery(selectQuery, null);
    }
//    public int count(){
//        Cursor c = db.rawQuery("SELECT count(*) FROM usertable WHERE USERTABLE001 = '" + g_UserID + "'", null);
//        c.moveToFirst();
//        Integer tcount = c.getInt(0);
//        c.close();
//        return tcount;
//    }
public void  uqdate_tc_qdr (String g_tc_qdr001,String g_tc_qdr002,String g_tc_qdr007,String g_tc_qdr008){
    try {

            db.execSQL(" UPDATE tc_qdr_file SET tc_qdr007 = '"+g_tc_qdr007+"', tc_qdr008 = '"+g_tc_qdr008+"' " +
                    " WHERE tc_qdr001 ='" + g_tc_qdr001 + "' " +
                    " AND tc_qdr002 ='" + g_tc_qdr002 + "'" );


    } catch (Exception e) {
    }
}
    public void  uqdate_tc_qdr_qdr011 (String g_tc_qdr001,String g_tc_qdr002,String g_tc_qdr009,String g_tc_qdr011){
        try {
            if (g_tc_qdr011.equals("Y")) {
            db.execSQL(" UPDATE tc_qdr_file SET tc_qdr011 = 'N' " +
                    " WHERE tc_qdr001 ='" + g_tc_qdr001 + "' " +
                    " AND tc_qdr002 ='" + g_tc_qdr002 + "'" +

                    " AND tc_qdr009 = '" + g_tc_qdr009 + "'"+
                    " AND tc_qdr011 ='" + g_tc_qdr011 + "'" );
            } else if (g_tc_qdr011.equals("N")) {
                db.execSQL(" UPDATE tc_qdr_file SET tc_qdr011 = 'Y' " +
                        " WHERE tc_qdr001 ='" + g_tc_qdr001 + "' " +
                        " AND tc_qdr002 ='" + g_tc_qdr002 + "'" +

                        " AND tc_qdr009 = '" + g_tc_qdr009 + "'"+
                        " AND tc_qdr011 ='" + g_tc_qdr011 + "'" );
            }

        } catch (Exception e) {
        }
    }
    public void  uqdate_tc_qdr_qdr010 (String g_tc_qdr001,String g_tc_qdr002,String g_tc_qdr010){

       try{
                if (g_tc_qdr010.equals("E")) {
                    db.execSQL(" UPDATE tc_qdr_file SET tc_qdr010 = 'C', tc_qdr011 = 'N' " +
                            " WHERE tc_qdr001 ='" + g_tc_qdr001 + "' " +
                            " AND tc_qdr002 ='" + g_tc_qdr002 + "'" +
                            " AND tc_qdr010 ='" + g_tc_qdr010 + "'" );
                } else if (g_tc_qdr010.equals("C")) {
                    db.execSQL(" UPDATE tc_qdr_file SET tc_qdr010 = 'E',tc_qdr011 = 'Y' " +
                            " WHERE tc_qdr001 ='" + g_tc_qdr001 + "' " +
                            " AND tc_qdr002 ='" + g_tc_qdr002 + "'" +
                            " AND tc_qdr010='" + g_tc_qdr010 + "'" );
                }

        } catch (Exception e) {
        }
    }
    public void  uqdate_tc_qdr_qdr012 (String g_tc_qdr001,String g_tc_qdr002,String g_tc_qdr009,String g_tc_qdr012){

        try{
            if (g_tc_qdr012.equals("E")) {
                db.execSQL(" UPDATE tc_qdr_file SET tc_qdr012 = 'C' " +
                        " WHERE tc_qdr001 ='" + g_tc_qdr001 + "' " +
                        " AND tc_qdr002 ='" + g_tc_qdr002 + "'" +
                        " AND tc_qdr009 ='" + g_tc_qdr009 + "'" +
                        " AND tc_qdr012 ='" + g_tc_qdr012 + "'" );
            } else if (g_tc_qdr012.equals("C")) {
                db.execSQL(" UPDATE tc_qdr_file SET tc_qdr012 = 'E'" +
                        " WHERE tc_qdr001 ='" + g_tc_qdr001 + "' " +
                        " AND tc_qdr002 ='" + g_tc_qdr002 + "'" +
                        " AND tc_qdr009 ='" + g_tc_qdr009 + "'" +
                        " AND tc_qdr012='" + g_tc_qdr012 + "'" );
            }

        } catch (Exception e) {
        }
    }
    public String get_tc_qdr_qdr009(String g_tc_qdr001,String g_tc_qdr002){
        String selectQuery = "SELECT MAX(tc_qdr009) FROM tc_qdr_file WHERE tc_qdr001='"+g_tc_qdr001+"' AND tc_qdr002 = '"+g_tc_qdr002+"' AND tc_qdr012 <> 'C' ";
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String res = cursor.getString(0);
        return res;
    }
    public Cursor get_tc_qds(String g_tc_qds001,String g_tc_qds002,String g_tc_qds003){
        String selectQuery = "SELECT tc_qds004 FROM tc_qds_file WHERE tc_qds001='"+g_tc_qds001+"' AND tc_qds002 = '"+g_tc_qds002+"' AND tc_qds003 = '"+g_tc_qds003+"'";
        return db.rawQuery(selectQuery, null);
    }
    public void delete_tc_qds(String g_tc_qds004){
        try {
            db.execSQL("DELETE FROM tc_qds_file WHERE tc_qds004 = '"+g_tc_qds004+"' ");
        } catch (Exception e) {
            String ex = e.getMessage().toString();
        }
    }
    public Cursor get_tc_qdr001( String madon,String xuong){
        String selectQuery = "SELECT tc_qdr009 FROM tc_qdr_file WHERE tc_qdr001='"+madon+"' AND tc_qdr002 = '"+xuong+"'";
        return db.rawQuery(selectQuery, null);
    }
    public String get_tc_qdr_qdr008(String g_tc_qdr001,String g_tc_qdr002,String g_tc_qdr009){
        String selectQuery = "SELECT tc_qdr008 FROM tc_qdr_file WHERE tc_qdr001='"+g_tc_qdr001+"' AND tc_qdr002 = '"+g_tc_qdr002+"' AND tc_qdr009 = '"+g_tc_qdr009+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String res = cursor.getString(0);
        return res;
    }
    public String get_tc_qdr_qdr007(String g_tc_qdr001,String g_tc_qdr002,String g_tc_qdr009){
        String selectQuery = "SELECT tc_qdr007 FROM tc_qdr_file WHERE tc_qdr001='"+g_tc_qdr001+"' AND tc_qdr002 = '"+g_tc_qdr002+"' AND tc_qdr009 = '"+g_tc_qdr009+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String res = cursor.getString(0);
        return res;
    }
    public String get_tc_qdr_qdr012(String g_tc_qdr001,String g_tc_qdr002,String g_tc_qdr009){
        String selectQuery = "SELECT tc_qdr012 FROM tc_qdr_file WHERE tc_qdr001='"+g_tc_qdr001+"' AND tc_qdr002 = '"+g_tc_qdr002+"' AND tc_qdr009 = '"+g_tc_qdr009+"' ";
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String res = cursor.getString(0);
        return res;
    }


    public Cursor getTc_qds_Upload(Cursor cursormadon) {
        //Lấy dữ liệu tc_qdf_file uqdate tới máy chủ Oracle
        String selectQuery = " SELECT * FROM tc_qds_file WHERE 1=1 ";
        String g_tc_qdr001 = cursormadon.getString(cursormadon.getColumnIndexOrThrow("tc_qdr001"));
        selectQuery = selectQuery + " AND tc_qds001 = '"+g_tc_qdr001+"' ";
        return db.rawQuery(selectQuery, null);
    }
    public Cursor getTc_qdr_Upload(String input_bdate, String input_edate, String input_department,String input_factory) {
        //Lấy dữ liệu tc_qde_file uqdate tới máy chủ Oracle
        String selectQuery = " SELECT * FROM tc_qdr_file WHERE 1=1 AND tc_qdr010 <> 'C' ";
        if (input_bdate.isEmpty() && input_edate.isEmpty() && input_department.isEmpty()) {
            selectQuery += " AND tc_qdrpost = 'N' ";
        }
        if (!input_department.isEmpty()) {
            selectQuery += " AND tc_qdr007 = '" + input_department + "' ";
        }
        if (!input_bdate.isEmpty() && !input_edate.isEmpty()) {
            selectQuery += " AND tc_qdr005 BETWEEN '" + input_bdate + "' AND '" + input_edate + "'";
        }
        if (!input_factory.isEmpty()) {
            selectQuery += " AND tc_qdr002 = '" + input_factory + "' ";
        }
        selectQuery += " ORDER BY  tc_qdr004,tc_qdr005  ";

        return db.rawQuery(selectQuery, null);
    }
    public void call_uqd_tc_qdrpost(Cursor c_getTc_qdr) {
        if (c_getTc_qdr.getCount() > 0) {
            c_getTc_qdr.moveToFirst();
            for (int i = 0; i < c_getTc_qdr.getCount(); i++) {
                String g_tc_qdr001 = c_getTc_qdr.getString(c_getTc_qdr.getColumnIndexOrThrow("tc_qdr001"));
                String g_tc_qdr002 = c_getTc_qdr.getString(c_getTc_qdr.getColumnIndexOrThrow("tc_qdr002"));
                String g_tc_qdr003 = c_getTc_qdr.getString(c_getTc_qdr.getColumnIndexOrThrow("tc_qdr003"));
                String g_tc_qdr004 = c_getTc_qdr.getString(c_getTc_qdr.getColumnIndexOrThrow("tc_qdr004"));

                db.execSQL(" UPDATE tc_qdr_file SET tc_qdrpost = 'Y' " +
                        " WHERE tc_qdr001 ='" + g_tc_qdr001 + "' " +
                        " AND tc_qdr002 ='" + g_tc_qdr002 + "'" +
                        " AND tc_qdr003 = '" + g_tc_qdr003 + "'" +
                        " AND tc_qdr004 = '" + g_tc_qdr004 + "' ");
                c_getTc_qdr.moveToNext();
            }
        }
    }
    public Integer get_tc_infwno001 (String g_tc_infwno001){
        String selectQuery= "SELECT COUNT(*) FROM tc_infwno_file WHERE tc_infwno001 = '"+g_tc_infwno001+"' ";
        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();
        Integer tcount = c.getInt(0);
        c.close();
        return tcount;
    }
    public Cursor get_tc_infwno (String g_tc_infwno001){
        String selectQuery= "SELECT distinct  * FROM tc_infwno_file WHERE tc_infwno001 = '"+g_tc_infwno001+"'";

        return db.rawQuery(selectQuery, null);
    }
    public String get_standard (String g_tc_infwno001, String g_tc_infwno002, String g_tc_infwno004){
        String selectQuery= "SELECT tc_infwno003 FROM tc_infwno_file WHERE tc_infwno001 = '"+g_tc_infwno001+"' AND tc_infwno002 = '"+g_tc_infwno002+"' AND tc_infwno004 = '"+g_tc_infwno004+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);

        String res = "";
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
             res= cursor.getString(0);
        }

        return res;
    }
}