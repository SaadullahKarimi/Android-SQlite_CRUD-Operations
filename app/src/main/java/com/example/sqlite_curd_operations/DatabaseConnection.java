package com.example.sqlite_curd_operations;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection  extends SQLiteOpenHelper {
    public static final String CUSTOMER_NAME = "CUSTOMER_NAME";
    public static final String CUSTOMER_AGE = "CUSTOMER_AGE";
    public static final String ID = "ID";
    public static final String CUSTOMER_TABLE = "Customer_Table";

    public DatabaseConnection(@Nullable Context context) {
        super(context, "Customer.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CreateTableStatement = "CREATE TABLE " + CUSTOMER_TABLE + "(" + ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CUSTOMER_NAME + " TEXT, " + CUSTOMER_AGE + " INTEGER )";
        db.execSQL(CreateTableStatement);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }

    public boolean addOne(DataModel dataModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CUSTOMER_NAME, dataModel.getName());
        cv.put(CUSTOMER_AGE, dataModel.getAge());
        long insert = db.insert(CUSTOMER_TABLE, null, cv);
        return insert != -1;

    }
    public void deleteOne(DataModel dataModel) {

        // find customerModel in the database. if it found, delete it and return true.
        // if it is not found, return false
        SQLiteDatabase db = this.getWritableDatabase();
        String queryStrings = " DELETE FROM " + CUSTOMER_TABLE + " WHERE " + ID + " = " + dataModel.getId();
        Cursor cursor;
        cursor = db.rawQuery(queryStrings,null);

        cursor.moveToNext();
    }

    public boolean updateOne(DataModel dataModel) {

        // find customerModel in the database. if it found, delete it and return true.
        // if it is not found, return false
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, dataModel.getId());
        values.put(CUSTOMER_NAME, dataModel.getName());
        values.put(CUSTOMER_AGE, dataModel.getAge());
        long update =    db.update(CUSTOMER_TABLE, values, ID + " = " +dataModel.getId(),null);

        return update != -1;
    }


    public List<DataModel> getEveryOne(){

        List<DataModel> returnlist = new ArrayList<>();
        //get data from the database
        String queryString = "SELECT * FROM  CUSTOMER_TABLE";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.rawQuery(queryString,null);
        if(cursor.moveToFirst()){
            do{
                int customerID = cursor.getInt(0);
                String customerName = cursor.getString(1);
                int customerAge = cursor.getInt(2);
                DataModel newdataModel = new DataModel(customerID,customerName,customerAge);
                returnlist.add(newdataModel);
            } while (cursor.moveToNext());
        }
        //failure. do not add anything to the list

        cursor.close();
        db.close();

        return returnlist;

    }

}
