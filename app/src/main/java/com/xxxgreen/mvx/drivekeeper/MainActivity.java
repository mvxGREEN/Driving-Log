package com.xxxgreen.mvx.drivekeeper;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Chronometer;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.xxxgreen.mvx.drivekeeper.data.DatabaseManager;
import com.xxxgreen.mvx.drivekeeper.data.TimeRecord;
import com.xxxgreen.mvx.drivekeeper.view.ButtonPanel;
import com.xxxgreen.mvx.drivekeeper.view.RecordRecyclerAdapter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    public Chronometer chronometer;
    public RecyclerView recyclerView;
    private RecordRecyclerAdapter adapter;
    public long resumeTime;
    public InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources res = getResources();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7417392682402637/6671090809");
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });
        requestNewInterstitial();

        //buttonpanel
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int squareSide = displayMetrics.widthPixels / 4;
        final ButtonPanel bp = (ButtonPanel) findViewById(R.id.bp_layout);
        bp.measure(squareSide, squareSide);

        bp.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "played");
                play();
            }
        });
        bp.pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "paused");
                pause();
            }
        });
        bp.stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "stopped");
                try {
                    stop();
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                }
            }
        });
        bp.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "add");
                Intent intent = new Intent(v.getContext(), WriteActivity.class);
                startActivity(intent);
            }
        });

        //timer
        resumeTime = 0;

        //recycler view
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<TimeRecord> recordList = new ArrayList<>();
        adapter = new RecordRecyclerAdapter(recordList, this);
        adapter.setOnItemClickListener(new RecordRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                TimeRecord record = adapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("record", record);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        //request permission to write to storage
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

    }


    @Override
    public void onResume() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<TimeRecord> nRecordList = new ArrayList<>();
        adapter = new RecordRecyclerAdapter(nRecordList, this);
        adapter.setOnItemClickListener(new RecordRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                TimeRecord record = adapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("record", record);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }

        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_log) {
            Intent intent = new Intent(this, LogActivity.class);
            startActivity(intent);

            return true;
        }
        if (id == R.id.action_export) {
            DatabaseManager dbm = DatabaseManager.getInstance(this);
            AlertDialog.Builder alertDialogBuilder
                    = new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilder.setTitle("Text File");
            alertDialogBuilder.setMessage("Your text file is here:\n" + dbm.backup())
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            return true;
        }
        if (id == R.id.action_reset) {
            AlertDialog.Builder alertDialogBuilder
                    = new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilder.setTitle("Reset");
            alertDialogBuilder.setMessage("Permanently delete data?")
                    .setCancelable(false)
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            DatabaseManager dbm = DatabaseManager.getInstance(MainActivity.this);
                            dbm.deleteTable();
                            dbm.close();

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void play() {
        if (chronometer == null || resumeTime != 0) {
            chronometer = (Chronometer) findViewById(R.id.chronometer);
            chronometer.setBase(SystemClock.elapsedRealtime() - resumeTime);

            chronometer.start();
        }
    }

    public void pause() {
        if (chronometer != null) {
            chronometer.stop();
            resumeTime = (SystemClock.elapsedRealtime() - chronometer.getBase());
        }
    }

    public void stop() throws ParseException {
        if (chronometer != null) {
            chronometer.stop();
            CharSequence resetText = "00:00";
            chronometer.setText(resetText);
            resumeTime = (SystemClock.elapsedRealtime() - chronometer.getBase());
            chronometer = null;
        }
        Intent intent = new Intent(this, WriteActivity.class);
        intent.putExtra("time", resumeTime);
        startActivity(intent);
        resumeTime = 0;
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("82016B6123805188F7A9D88FE463A95C")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

}
