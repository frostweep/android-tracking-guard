package com.frostweepgames.apptracker.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import com.frostweepgames.apptracker.settings.Constants;

/**-------------------------------
 * Created by artem on 21.02.2018.
 * Powered by Frostweep Games
 * All rights reserved!
 * (c)2013-2018
 * www.frostweepgames.com
 ---------------------------------*/

public class SQLiteManager extends SQLiteOpenHelper {

    private SQLiteDatabase _readableMainDataBase;
    private SQLiteDatabase _writableMainDataBase;

    public SQLiteManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        _writableMainDataBase = getWritableDatabase();
        _readableMainDataBase = getReadableDatabase();
    }


    public void addElementInDB(String table, ContentValues values) {
        _writableMainDataBase = getWritableDatabase();
        _writableMainDataBase.insertWithOnConflict(table, null, values, SQLiteDatabase.CONFLICT_REPLACE);
      //  _writableMainDataBase.close();
    }

    public void removeElementInDB(String table, Integer elementId, Boolean all) {
        _writableMainDataBase = getWritableDatabase();

        if (!all)
            _writableMainDataBase.delete(table, Constants.TABLE_KEY_ID + "=?", new String[]{elementId.toString()});
        else
            _writableMainDataBase.delete(table, null, null);

      //  _writableMainDataBase.close();
    }

    public ArrayList<SQLiteLineModel> getObjectsFromDB(String table, Integer limit, String what, String where) {
        _readableMainDataBase = getReadableDatabase();

        if(what == null || what == "")
            what =  "*";

        String query = "SELECT " + what + " FROM " + table;

        if(where != null && where.length() > 0)
            query += " WHERE " + where;

        if(limit != null && limit > 0)
            query += " LIMIT " + limit.toString();

        Cursor cursor = _readableMainDataBase.rawQuery(query, null);

        ArrayList<SQLiteLineModel> objects = new ArrayList<>();
        SQLiteLineModel modelObject;
        while (cursor.moveToNext()) {

            modelObject = new SQLiteLineModel();
            modelObject.Id = cursor.getInt(0);
            modelObject.Data = cursor.getString(1);
            objects.add(modelObject);
        }

       // _readableMainDataBase.close();

        return objects;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(Constants.CREATE_TABLE_CONTACTS_SQL);
        sqLiteDatabase.execSQL(Constants.CREATE_TABLE_SMS_SQL);
        sqLiteDatabase.execSQL(Constants.CREATE_TABLE_APPS_SQL);
        sqLiteDatabase.execSQL(Constants.CREATE_TABLE_CALLS_SQL);
        sqLiteDatabase.execSQL(Constants.CREATE_TABLE_GPS_SQL);
        sqLiteDatabase.execSQL(Constants.CREATE_TABLE_SYSTEM_INF0_SQL);
        sqLiteDatabase.execSQL(Constants.CREATE_TABLE_BROWSER_HISTORY_SQL);
        sqLiteDatabase.execSQL(Constants.CREATE_TABLE_APP_SETTINGS_SQL);
        sqLiteDatabase.execSQL(Constants.CREATE_TABLE_SYNC_SETTINGS_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL(Constants.DROP_TABLE_CONTACTS_SQL);
        sqLiteDatabase.execSQL(Constants.DROP_TABLE_SMS_SQL);
        sqLiteDatabase.execSQL(Constants.DROP_TABLE_APPS_SQL);
        sqLiteDatabase.execSQL(Constants.DROP_TABLE_CALLS_SQL);
        sqLiteDatabase.execSQL(Constants.DROP_TABLE_GPS_SQL);
        sqLiteDatabase.execSQL(Constants.DROP_TABLE_SYSTEM_INF0_SQL);
        sqLiteDatabase.execSQL(Constants.DROP_TABLE_BROWSER_HISTORY_SQL);
        sqLiteDatabase.execSQL(Constants.DROP_TABLE_APP_SETTINGS_SQL);
        sqLiteDatabase.execSQL(Constants.DROP_TABLE_SYNC_SETTINGS_SQL);

        onCreate(sqLiteDatabase);
    }

    @Override
    public void onOpen(SQLiteDatabase sqLiteDatabase) {

        _writableMainDataBase = sqLiteDatabase;
        _readableMainDataBase = sqLiteDatabase;
    }


    public class SQLiteLineModel
    {
        public int Id;
        public String Data;
    }
}