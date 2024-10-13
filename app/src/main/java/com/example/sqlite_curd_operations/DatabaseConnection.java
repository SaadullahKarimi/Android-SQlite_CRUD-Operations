package com.example.sqlite_curd_operations;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection extends SQLiteOpenHelper {
    public static final String CUSTOMER_NAME = "CUSTOMER_NAME";
    public static final String CUSTOMER_AGE = "CUSTOMER_AGE";
    public static final String ID = "ID";
    public static final String CUSTOMER_TABLE = "CUSTOMER_TABLE";

    public DatabaseConnection(@Nullable Context context) {
        super(context, "Student1.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CreateTableStatement = "CREATE TABLE " + CUSTOMER_TABLE +
                " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CUSTOMER_NAME + " TEXT, " + CUSTOMER_AGE + " INTEGER)";
        db.execSQL(CreateTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implement schema changes and data message here when upgrading
    }

    public boolean addOne(DataModel dataModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CUSTOMER_NAME, dataModel.getName());
        cv.put(CUSTOMER_AGE, dataModel.getAge());
        long insert = db.insert(CUSTOMER_TABLE, null, cv);
        db.close();
        return insert != -1;
    }

    public void deleteOne(DataModel dataModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = ID + "=?";
        String[] whereArgs = {String.valueOf(dataModel.getId())};
        db.delete(CUSTOMER_TABLE, whereClause, whereArgs);
        db.close();
    }

    public List<DataModel> getEveryOne() {
        List<DataModel> returnlist = new ArrayList<>();
        String queryString = "SELECT * FROM " + CUSTOMER_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int customerID = cursor.getInt(0);
                String customerName = cursor.getString(1);
                int customerAge = cursor.getInt(2);
                DataModel newDataModel = new DataModel(customerID, customerName, customerAge);
                returnlist.add(newDataModel);
            } while (cursor.moveToNext());
        }

        cursor.close();  // Always close the cursor
        db.close();  // Always close the database
        return returnlist;
    }

    public DataModel getCustomerById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + CUSTOMER_TABLE + " WHERE " + ID + " = ?";
        Cursor cursor = db.rawQuery(queryString, new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            int customerId = cursor.getInt(0);
            String customerName = cursor.getString(1);
            int customerAge = cursor.getInt(2);
            cursor.close();
            return new DataModel(customerId, customerName, customerAge);
        } else {
            cursor.close();
            return null;  // Customer not found
        }
    }

    public boolean updateOne(DataModel dataModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CUSTOMER_NAME, dataModel.getName());
        values.put(CUSTOMER_AGE, dataModel.getAge());

        String whereClause = ID + "=?";
        String[] whereArgs = {String.valueOf(dataModel.getId())};

        int result = db.update(CUSTOMER_TABLE, values, whereClause, whereArgs);
        db.close();
        return result != -1;
    }
}
