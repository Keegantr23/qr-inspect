package com.lonelyjones.qrandbarcodescanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class CodeItemHandler extends DatabaseHelper{

    public CodeItemHandler(Context context){
        super(context);
    }

    public boolean create(CodeItem codeItem){
        ContentValues values = new ContentValues();
        values.put("codeString", codeItem.getCodeScanned());
        values.put("dateScanned", codeItem.getDateScanned());
        values.put("qr_image", codeItem.getImg_code_gen());

        SQLiteDatabase db = this.getWritableDatabase();
        boolean isSuccessful = db.insert("CodeItem", null, values) > 0;
        db.close();
        return isSuccessful;
    }

    public ArrayList<CodeItem> readAllItems(){
        ArrayList<CodeItem> returnArray = new ArrayList<>();

        String sqlQuery = "SELECT * FROM CodeItem ORDER BY id ASC";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery, null);

        if(cursor.moveToFirst()){
            do{
                int id =Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String code_result_string = cursor.getString(cursor.getColumnIndex("codeString"));
                String date_scanned_string = cursor.getString(cursor.getColumnIndex("dateScanned"));
                byte[] qr_code_image = cursor.getBlob(cursor.getColumnIndex("qr_image"));

                CodeItem codeItemTemp =new CodeItem(code_result_string, date_scanned_string, qr_code_image);
                codeItemTemp.setId(id);
                returnArray.add(codeItemTemp);

            }while(cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return returnArray;
    }

    public CodeItem readSingleItem(int id){
        CodeItem codeItemTemp =null;

        String sqlQuery = "SELECT * FROM CodeItem WHERE id="+id;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery, null);

        if(cursor.moveToFirst()){
            int itemID =Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
            String code_result_string = cursor.getString(cursor.getColumnIndex("codeString"));
            String date_scanned_string = cursor.getString(cursor.getColumnIndex("dateScanned"));
            byte[] qr_code_image = cursor.getBlob(cursor.getColumnIndex("qr_image"));

            codeItemTemp = new CodeItem(code_result_string, date_scanned_string, qr_code_image);
            codeItemTemp.setId(itemID);
        }
        cursor.close();
        db.close();
        return codeItemTemp;
    }

    public boolean delete(int id){
        boolean isDeleted;
        SQLiteDatabase db = getWritableDatabase();
        isDeleted = db.delete("CodeItem", "id='"+id+"'", null) > 0;
        db.close();
        return isDeleted;
    }


}
