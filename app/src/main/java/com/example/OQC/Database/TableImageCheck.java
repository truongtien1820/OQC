package com.example.OQC.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TableImageCheck {

    private SQLiteDatabase database;
    private String tc_img_tmp = "tc_imgcheck_file";
    // Constructor mở kết nối cơ sở dữ liệu
    public TableImageCheck(Context context) {
        SQLiteOpenHelper dbHelper = new SQLiteOpenHelper(context, "database.db", null, 1) {
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
    public boolean isTableExists(String tableName) {
        Cursor cursor = database.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name=?",
                new String[]{tableName}
        );
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
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
    public Cursor getfiveoldpo() {
        return database.rawQuery("SELECT * FROM tc_imgcheck_file ORDER BY tc_img006 DESC LIMIT 5",null);
    }

    public void call_upd_tc_ìmg007(Cursor cGettcFct) {
        String updateQuery = "UPDATE tc_imgcheck_file SET tc_img007 = 'Y' WHERE tc_img001 = ? AND tc_img002 = ? AND tc_img003 = ? AND tc_img004 = ? AND tc_img006 = ?";
        cGettcFct.moveToFirst();
        do {
            String tc_img001 = cGettcFct.getString(0);
            String tc_img002 = cGettcFct.getString(1);
            String tc_img003 = cGettcFct.getString(2);
            String tc_img004 = cGettcFct.getString(3);
            String tc_img006 = cGettcFct.getString(5);
            database.execSQL(updateQuery, new String[]{tc_img001, tc_img002, tc_img003, tc_img004, tc_img006});
        }while (cGettcFct.moveToNext());


    }

    public Cursor gettc_img_Upload(Cursor cGettcFct) {
        cGettcFct.moveToFirst();
        String tc_img001 = cGettcFct.getString(0);
        String tc_img002 = cGettcFct.getString(1);
        String tc_img003 = cGettcFct.getString(2);
        String tc_img004 = cGettcFct.getString(3);
        String tc_img006 = cGettcFct.getString(5);
        return database.rawQuery("SELECT tc_img005 FROM tc_imgcheck_file WHERE tc_img001 = ? AND tc_img002 = ? AND tc_img003 = ? AND tc_img004 = ? AND tc_img006 = ? AND tc_img007 = 'Y'",  new String[]{tc_img001,tc_img002, tc_img003, tc_img004, tc_img006});
    }
}

