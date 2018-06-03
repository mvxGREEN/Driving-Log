package com.xxxgreen.mvx.drivekeeper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.xxxgreen.mvx.drivekeeper.data.DatabaseManager;
import com.xxxgreen.mvx.drivekeeper.data.TimeRecord;

import java.util.Calendar;

/**
 * Created by MVX on 8/5/2017.
 */

public class EditActivity extends AppCompatActivity {
    private static final String TAG = "EditActivity";
    EditText et_date, et_time, et_tod, et_location, et_conditions, et_notes;
    String mDate, mTOD, mLocation, mConditions, mNotes;
    long mTime;
    TimeRecord mRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Edit Record");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mRecord = intent.getParcelableExtra("record");

        et_date = (EditText) findViewById(R.id.et_date);
        et_time = (EditText) findViewById(R.id.et_time);
        et_tod = (EditText) findViewById(R.id.et_tod);
        et_location = (EditText) findViewById(R.id.et_location);
        et_conditions = (EditText) findViewById(R.id.et_conditions);
        et_notes = (EditText) findViewById(R.id.et_notes);

        et_date.setText(mRecord.date);
        et_time.setText(mRecord.getTime());
        et_tod.setText(mRecord.timeOfDay);
        et_location.setText(mRecord.location);
        et_conditions.setText(mRecord.conditions);
        et_notes.setText(mRecord.initials);

        Button b_cancel = (Button) findViewById(R.id.b_cancel);
        b_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder
                        = new AlertDialog.Builder(EditActivity.this);
                alertDialogBuilder.setTitle("Delete");
                alertDialogBuilder.setMessage("Permanently delete this record?")
                        .setCancelable(false)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (mRecord != null) {
                                    DatabaseManager dbm = DatabaseManager.getInstance(EditActivity.this);
                                    dbm.deleteRecord(mRecord.id);
                                    dbm.close();
                                }

                                EditActivity.this.finish();
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
                dbm.replaceRecord(record, mRecord.id);
                dbm.close();

                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
