package com.client.safelyblue.safelyblueclient.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by albin on 3/12/15.
 */
public class PositionReportDB extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PositionReport.db";
    public static final String DATABASE_CREATE="CREATE TABLE POSITIONREPORTS (longitude REAL, latitude REAL, altitiude REAL, accuracy REAL, datetimestamp TEXT)";
    public static final String DATABASE_DROP="DROP TABLE POSITIONREPORTS";
    //public static final String DATABASE_DELETE="DELETE FROM POSITIONREPORTS";

    public PositionReportDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(DATABASE_DROP);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean insertPosition (ContentValues contentValues)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try {

            db.insert("POSITIONREPORTS", null, contentValues);

        }finally {
            db.close();
        }
        return true;
    }
    public JSONArray getAllPositionReports(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from POSITIONREPORTS", null );
        JSONArray jsonArray = new JSONArray();
        try{
            res.moveToFirst();


            while(res.isAfterLast() == false){
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("longitude",res.getFloat(0));
                    jsonObject.put("latitude",res.getFloat(1));
                    jsonObject.put("altitiude",res.getFloat(2));
                    jsonObject.put("accuracy",res.getFloat(3));
                    jsonObject.put("datetimestamp",res.getLong(4));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                jsonArray.put(jsonObject);
                res.moveToNext();
            }
            int deleteResult = db.delete("POSITIONREPORTS",null,null);
            //Log(TAG,deleteResult);
            //res.close();
            //db.close();

        }finally {
            if (res != null && !res.isClosed()) {
                res.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

        return jsonArray;
    }
}
