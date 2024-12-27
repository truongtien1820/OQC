package com.example.laprap001.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteTemporaryTableManager {

    private SQLiteDatabase database;
    private String tc_img_tmp = "tc_img_tmp";
    // Constructor mở kết nối cơ sở dữ liệu
    public SQLiteTemporaryTableManager(Context context) {
        SQLiteOpenHelper dbHelper = new SQLiteOpenHelper(context, "temp_database.db", null, 1) {
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
}

