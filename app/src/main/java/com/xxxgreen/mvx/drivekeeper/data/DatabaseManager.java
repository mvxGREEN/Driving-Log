package com.xxxgreen.mvx.drivekeeper.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import static com.xxxgreen.mvx.drivekeeper.data.DatabaseSchema.Schema.COL_1;
import static com.xxxgreen.mvx.drivekeeper.data.DatabaseSchema.Schema.COL_2;
import static com.xxxgreen.mvx.drivekeeper.data.DatabaseSchema.Schema.COL_3;
import static com.xxxgreen.mvx.drivekeeper.data.DatabaseSchema.Schema.COL_4;
import static com.xxxgreen.mvx.drivekeeper.data.DatabaseSchema.Schema.COL_5;
import static com.xxxgreen.mvx.drivekeeper.data.DatabaseSchema.Schema.COL_6;
import static com.xxxgreen.mvx.drivekeeper.data.DatabaseSchema.Schema.COL_7;
import static com.xxxgreen.mvx.drivekeeper.data.DatabaseSchema.Schema.TABLE_NAME;

/**
 * Created by MVX on 8/2/2017.
 */

public class DatabaseManager {
    private static final String TAG = "DatabaseManager";
    private static DatabaseManager sInstance;
    private SQLiteDatabase mDatabase;

    public static synchronized DatabaseManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseManager(context.getApplicationContext());
        }
        return sInstance;
    }

    private DatabaseHelper myDbHelper;

    private DatabaseManager(Context context) {
        myDbHelper = new DatabaseHelper(context);
    }


    public Cursor queryAllRecords() {
        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        return db.rawQuery(query, null);
    }

    public void addRecord(TimeRecord record) {
        try {
            open();
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }

        ContentValues values = new ContentValues();
        values.put(COL_2, record.date);
        values.put(COL_3, record.time);
        values.put(COL_4, record.timeOfDay);
        values.put(COL_5, record.location);
        values.put(COL_6, record.conditions);
        values.put(COL_7, record.initials);

        long result = mDatabase.insert(TABLE_NAME, null, values);
        this.backup();
        mDatabase.close();
    }

    public void replaceRecord(TimeRecord record, int position) {
        try {
            open();
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }

        ContentValues values = new ContentValues();
        values.put(COL_1, position);
        values.put(COL_2, record.date);
        values.put(COL_3, record.time);
        values.put(COL_4, record.timeOfDay);
        values.put(COL_5, record.location);
        values.put(COL_6, record.conditions);
        values.put(COL_7, record.initials);

        myDbHelper.deleteEntry(mDatabase, position);
        long result = mDatabase.insert(TABLE_NAME, null, values);
        this.backup();
        mDatabase.close();
    }

    public void deleteTable() {
        try {
            open();
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }

        myDbHelper.onUpgrade(mDatabase, 0, 1);
        this.backup();
        mDatabase.close();
    }

    public void deleteRecord(int position) {
        try {
            open();
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
        myDbHelper.deleteEntry(mDatabase, position);
        this.backup();
        mDatabase.close();
    }

    public String backup() {
        String location = Environment.getExternalStorageDirectory() + "/mvxGREEN/backup.txt";
        if (isExternalStorageWritable()) {
            File backupDir = new File(Environment.getExternalStorageDirectory(), "/mvxGREEN/");

            //attempt to create internal directory
            if (!backupDir.exists()) {
                if (!backupDir.mkdirs()) {
                    Log.e(TAG, "Directory not created");
                } else {
                    Log.i(TAG, "Directory created at " + backupDir.getAbsolutePath());
                }
            } else {
                Log.i(TAG, "Directory exists at " + backupDir.getAbsolutePath());
            }

            File backupFile = new File(backupDir, "backup.txt");

            //attempt to create text file
            try {
                if (backupFile.createNewFile()) {
                    Log.i(TAG, "New file created");
                } else {
                    Log.i(TAG, "File exists");
                }
            } catch (IOException e) {
                Log.e(TAG, "File not created");
                e.printStackTrace();
            }

            //write records into a String
            Cursor cursor = this.queryAllRecords();
            StringBuilder builder = new StringBuilder();
            builder.append("DATE,\tLOCATION,\tCONDITIONS,\tTIME OF DAY,\tTIME,\tINITIALS");
            while (cursor.moveToNext()) {
                TimeRecord record = new TimeRecord(cursor);
                builder.append(record.toString()).append("\n");
            }
            cursor.close();
            String contents = builder.toString();

            //write String to text file
            try {
                setContents(backupFile, contents);
            } catch (IOException e) {
                Log.e(TAG, "Error backing up to text file");
                e.printStackTrace();
            }
        }

        return location;
    }

    public void open() throws SQLException {
        mDatabase = myDbHelper.getWritableDatabase();
    }

    public void close() {
        this.backup();
        if (mDatabase.isOpen()) {
            mDatabase.close();
        }
        this.myDbHelper.close();
    }

    private void setContents(File file, String contents)
            throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File should not be null.");
        }
        if (!file.exists()) {
            throw new FileNotFoundException("File does not exist: " + file);
        }
        if (!file.isFile()) {
            throw new IllegalArgumentException("Should not be a directory: " + file);
        }
        if (!file.canWrite()) {
            throw new IllegalArgumentException("File cannot be written: " + file);
        }

        //declared here only to make visible to finally clause; generic reference
        Writer output = null;
        try {
            //use buffering
            //FileWriter always assumes default encoding is OK!
            output = new BufferedWriter(new FileWriter(file));
            output.write(contents);
        } finally {
            //flush and close both "output" and its underlying FileWriter
            if (output != null) {
                output.close();
            }
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
