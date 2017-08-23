package com.example.user.timekeeper;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User on 31-07-2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static int DATABASE_VERSION =1;
    private static String DATEBASE_NAME ="attendance.db";
    private static String TABLE_NAME ="attendanceTable";
    private static String PRIMARY_ID ="primaryId";
    public static String EVENT ="event";
    public static String TIME ="time";
    public static String LOCATION ="location";
    public static String HOURS_WORKED="hoursWorked";
    public static String PHOTO_PATH = "photoPath";

    private static final String TAG = "Time Keeper";
    public DBHelper(Context context) {
        super(context, DATEBASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE ="CREATE TABLE "
                + TABLE_NAME + "("
                + PRIMARY_ID + " INTEGER PRIMARY KEY, "
                + EVENT + " TEXT, "
                + TIME + " TEXT, "
                + LOCATION + " TEXT, "
                + HOURS_WORKED + " TEXT, "
                + PHOTO_PATH + " TEXT"
                + " )";
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public long insertCheckIn(String checkIn, String location,String imagePath){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EVENT,"CheckIn");
        contentValues.put(TIME,checkIn);
        contentValues.put(LOCATION,location);
        contentValues.put(PHOTO_PATH,imagePath);
        long rowInserted =database.insert(TABLE_NAME,null,contentValues);

        database.close();
        return rowInserted;
    }

    public long insertCheckOut(String checkOut,String location,String hoursWorked){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EVENT,"CheckOut");
        contentValues.put(TIME,checkOut);
        contentValues.put(LOCATION,location);
        contentValues.put(HOURS_WORKED,hoursWorked);
        long rowInserted = database.insert(TABLE_NAME,null,contentValues);

       database.close();
        return rowInserted;
    }

   public ArrayList<HashMap<String,String>> getAttendance(){

        String event ="null";
        String time ="null";
        String location ="null";
        String query = "SELECT * FROM " + TABLE_NAME ;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(query,null);
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                event = cursor.getString(cursor.getColumnIndex(EVENT));
                time =cursor.getString(cursor.getColumnIndex(TIME));
                location = cursor.getString(cursor.getColumnIndex(LOCATION));
                HashMap<String ,String > map = new HashMap<>();
                map.put(EVENT,event);
                map.put(TIME,time);
                map.put(LOCATION,location);
                list.add(map);

            }while (cursor.moveToNext());
        }
        cursor.close();
        return  list;
    }
   public Cursor getAttendanceCursor(){
        String query ="SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(query,null);
       return cursor;
    }

}
