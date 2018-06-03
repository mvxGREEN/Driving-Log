package com.xxxgreen.mvx.drivekeeper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xxxgreen.mvx.drivekeeper.data.DatabaseManager;
import com.xxxgreen.mvx.drivekeeper.data.TimeRecord;


public class WriteActivity extends AppCompatActivity {
    private static final String TAG = "WriteActivity";
    public EditText et_date, et_time, et_tod, et_location, et_conditions, et_notes;
    public String mDate, mTOD, mLocation, mConditions, mNotes;
    public long mTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Add Record");

        Intent intent = getIntent();
        String timeString = Utils.getTime(intent.getLongExtra("time", 0));

        try {
            mDate = Utils.getDate();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }

        et_date = (EditText) findViewById(R.id.et_date);
        et_date.setText(mDate);
        et_time = (EditText) findViewById(R.id.et_time);
        et_time.setText(timeString);
        et_tod = (EditText) findViewById(R.id.et_tod);
        et_location = (EditText) findViewById(R.id.et_location);
        et_conditions = (EditText) findViewById(R.id.et_conditions);
        et_notes = (EditText) findViewById(R.id.et_notes);

        Button b_cancel = (Button) findViewById(R.id.b_cancel);
        b_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder
                        = new AlertDialog.Builder(WriteActivity.this);
                alertDialogBuilder.setTitle("Delete");
                alertDialogBuilder.setMessage("Permanently delete this record?")
                        .setCancelable(false)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                WriteActivity.this.finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });
        Button b_save = (Button) findViewById(R.id.b_save);
        b_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDate = et_date.getText().toString();
                mTime = Utils.makeLong(et_time.getText().toString());
                mTOD = et_tod.getText().toString();
                mLocation = et_location.getText().toString();
                mConditions = et_conditions.getText().toString();
                mNotes = et_notes.getText().toString();

                DatabaseManager dbm = DatabaseManager.getInstance(v.getContext());
                TimeRecord record = new TimeRecord(mDate, mTime, mTOD, mLocation,
                        mConditions, mNotes);
                dbm.addRecord(record);
                dbm.close();

                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        boolean leave = false;
        AlertDialog.Builder alertDialogBuilder
                = new AlertDialog.Builder(WriteActivity.this);
        alertDialogBuilder.setTitle("Delete");
        alertDialogBuilder.setMessage("Permanently delete this record?")
                .setCancelable(false)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        WriteActivity.this.finish();
                        WriteActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
