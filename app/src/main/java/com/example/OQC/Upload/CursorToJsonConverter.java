package com.example.OQC.Upload;

import android.database.Cursor;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class CursorToJsonConverter {
    public static JsonArray cursorToJson(Cursor cursor) {
        JsonArray jsonArray = new JsonArray();

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            int totalColumn = cursor.getColumnCount();
            JsonObject rowObject = new JsonObject();

            for (int i = 0; i < totalColumn; i++) {
                String columnName = cursor.getColumnName(i);
                String columnValue = cursor.getString(i);

                // Thêm cặp khóa-giá trị vào rowObject
                rowObject.add(columnName, new JsonPrimitive(columnValue));
            }

            jsonArray.add(rowObject);
            cursor.moveToNext();
        }

        //cursor.close();

        return jsonArray;
    }
}
