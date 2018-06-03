package com.xxxgreen.mvx.drivekeeper.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

import static com.xxxgreen.mvx.drivekeeper.data.DatabaseSchema.Schema.COL_1;
import static com.xxxgreen.mvx.drivekeeper.data.DatabaseSchema.Schema.COL_2;
import static com.xxxgreen.mvx.drivekeeper.data.DatabaseSchema.Schema.COL_3;
import static com.xxxgreen.mvx.drivekeeper.data.DatabaseSchema.Schema.COL_4;
import static com.xxxgreen.mvx.drivekeeper.data.DatabaseSchema.Schema.COL_5;
import static com.xxxgreen.mvx.drivekeeper.data.DatabaseSchema.Schema.COL_6;
import static com.xxxgreen.mvx.drivekeeper.data.DatabaseSchema.Schema.COL_7;
import static com.xxxgreen.mvx.drivekeeper.data.DatabaseSchema.Schema.TABLE_NAME;

/**
 * Created by MVX on 8/1/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "timeRecords.db";
    private static final int DATABASE_VERSION = 6;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COL_2 + " TEXT," +
                    COL_3 + " INTEGER," +
                    COL_4 + " TEXT," +
                    COL_5 + " TEXT," +
                    COL_6 + " TEXT," +
                    COL_7 + " TEXT)";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final String SQL_DELETE_ENTRY =
            "DELETE FROM " + TABLE_NAME + " WHERE ID = ";
    private Resources mResources;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mResources = context.getResources();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    protected void deleteEntry(SQLiteDatabase db, int i) {
        db.execSQL(SQL_DELETE_ENTRY + i);
    }

}
