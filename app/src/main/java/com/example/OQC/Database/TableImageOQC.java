package com.example.OQC.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class TableImageOQC {

    private SQLiteDatabase database;
    private String TC_OQC_tmp = "TC_OQC_file";
    // Constructor mở kết nối cơ sở dữ liệu
    public TableImageOQC(Context context) {
        SQLiteOpenHelper dbHelper = new SQLiteOpenHelper(context, "OQCTable.db", null, 1) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                // Không cần tạo bảng cố định trong onCreate
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                // Xử lý nâng cấp cơ sở dữ liệu (nếu cần)
            }
        };
        this.database = dbHelper.getWritableDatabase();
    }

    public void createTemporaryTable(String tableName, String tableSchema) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + tableSchema + ")";
        try{
            database.execSQL(createTableQuery);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // Phương thức chèn dữ liệu vào bảng tạm
    public void insertData(String tableName, String columns, String values) {
        String insertQuery = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + values + ")";
        database.execSQL(insertQuery);
    }

    // Phương thức lấy dữ liệu từ bảng tạm
    public Cursor getData(String tableName,String column,String value) {
        return database.rawQuery("SELECT "+ column+ " FROM " + tableName +" WHERE " + value,null);
    }

    // Phương thức xóa bảng tạm
    public void dropTemporaryTable(String tableName) {
        String dropQuery = "DROP TABLE IF EXISTS " + tableName;
        database.execSQL(dropQuery);
    }

    // Đóng kết nối cơ sở dữ liệu
    public void closeDatabase() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }
    public void deleteWhere(String tableName, String condition)  {
        String deleteQuery = "DELETE FROM " + tableName + " WHERE " + condition;
        database.execSQL(deleteQuery);
    }
    // Phương thức kiểm tra xem dòng dữ liệu đã tồn tại chưa
    public boolean isRowExists(String tableName, String condition) {
        boolean exists = false;
        Cursor cursor = null;
        try {
            String query = "SELECT 1 FROM " + tableName + " WHERE " + condition + " LIMIT 1";
            cursor = database.rawQuery(query, null);
            exists = (cursor != null && cursor.moveToFirst());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return exists;
    }
    public String getmaxisno(String TC_OQC001 ,String TC_OQC002 ,String TC_OQC003 ,String TC_OQC004) {
        Cursor cursor=  database.rawQuery("SELECT Max(TC_OQC009) FROM TC_OQC_file WHERE TC_OQC001 = ? AND TC_OQC002 = ? AND TC_OQC003 = ? AND TC_OQC004 = ?", new String []{TC_OQC001,TC_OQC002,TC_OQC003,TC_OQC004});
        cursor.moveToFirst();
        String maxisno = cursor.getString(0);
        return maxisno;
    }
    public String getstatus(String TC_OQC001 ,String TC_OQC002 ,String TC_OQC003 ,String TC_OQC004,String TC_OQC009) {
        try {
            Cursor cursor=  database.rawQuery("SELECT  TC_OQC006 FROM TC_OQC_file WHERE TC_OQC001 = ? AND TC_OQC002 = ? AND TC_OQC003 = ? AND TC_OQC004 = ? AND TC_OQC009 = ?  ", new String []{TC_OQC001,TC_OQC002,TC_OQC003,TC_OQC004,TC_OQC009});
            cursor.moveToFirst();
            String maxisno = cursor.getString(0);
            return maxisno;
        }catch (Exception e){
            return "";
        }


    }
    public String getTimeCheck(String TC_OQC001 ,String TC_OQC002 ,String TC_OQC003 ,String TC_OQC004,String TC_OQC009) {
        try {
            Cursor cursor=  database.rawQuery("SELECT  TC_OQC005 FROM TC_OQC_file WHERE TC_OQC001 = ? AND TC_OQC002 = ? AND TC_OQC003 = ? AND TC_OQC004 = ? AND TC_OQC009 = ?  ", new String []{TC_OQC001,TC_OQC002,TC_OQC003,TC_OQC004,TC_OQC009});
            cursor.moveToFirst();
            String maxisno = cursor.getString(0);
            return maxisno;
        }catch (Exception e){
            return "";
        }


    }
    public void call_upd_TC_img007(Cursor cGetTCFct) {
        String updateQuery = "UPDATE TC_OQC_file SET TC_OQC007 = 'Y' WHERE TC_OQC001 = ? AND TC_OQC002 = ? AND TC_OQC003 = ? AND TC_OQC004 = ? AND TC_OQC006 = ?";
        cGetTCFct.moveToFirst();
        do {
            String TC_OQC001 = cGetTCFct.getString(0);
            String TC_OQC002 = cGetTCFct.getString(1);
            String TC_OQC003 = cGetTCFct.getString(2);
            String TC_OQC004 = cGetTCFct.getString(3);
            String TC_OQC006 = cGetTCFct.getString(5);
            database.execSQL(updateQuery, new String[]{TC_OQC001, TC_OQC002, TC_OQC003, TC_OQC004, TC_OQC006});
        }while (cGetTCFct.moveToNext());


    }
    public List<String> getListPO( String TC_OQC001, String TC_OQC002, String TC_OQC003, String TC_OQC004) {
        List<String> listPO = new ArrayList<>();  // Khởi tạo list

        Cursor cursor = null;
        try {
            cursor = database.rawQuery(
                    "SELECT TC_OQC009 FROM TC_OQC_file WHERE TC_OQC001 = ? AND TC_OQC002 = ? AND TC_OQC003 = ? AND TC_OQC004 = ?",
                    new String[]{TC_OQC001, TC_OQC002, TC_OQC003, TC_OQC004}
            );

            if (cursor.moveToFirst()) {  // Kiểm tra nếu có dữ liệu
                do {
                    listPO.add(cursor.getString(0)); // Thêm dữ liệu vào list
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi ra Logcat
        } finally {
            if (cursor != null) {
                cursor.close(); // Đóng Cursor để tránh rò rỉ bộ nhớ
            }
        }

        return listPO; // Trả về danh sách
    }


    public Cursor getTC_OQC_Upload(Cursor cGetTCFct) {
        cGetTCFct.moveToFirst();
        String TC_OQC001 = cGetTCFct.getString(0);
        String TC_OQC002 = cGetTCFct.getString(1);
        String TC_OQC003 = cGetTCFct.getString(2);
        String TC_OQC004 = cGetTCFct.getString(3);
        String TC_OQC006 = cGetTCFct.getString(5);
        return database.rawQuery("SELECT TC_OQC005 FROM TC_OQC_file WHERE TC_OQC001 = ? AND TC_OQC002 = ? AND TC_OQC003 = ? AND TC_OQC004 = ? AND TC_OQC006 = ? AND TC_OQC007 = 'Y'",  new String[]{TC_OQC001,TC_OQC002, TC_OQC003, TC_OQC004, TC_OQC006});
    }
}

