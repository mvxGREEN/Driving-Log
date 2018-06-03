package com.xxxgreen.mvx.drivekeeper;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.xxxgreen.mvx.drivekeeper.data.DatabaseManager;
import com.xxxgreen.mvx.drivekeeper.data.TimeRecord;
import com.xxxgreen.mvx.drivekeeper.view.RecordRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class LogActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    private RecordRecyclerAdapter adapter;
    private List<TimeRecord> mRecordList;
    private int dTotal = 0, nTotal = 0, gTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Log");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTotals(this);

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
                Intent intent = new Intent(LogActivity.this, EditActivity.class);
                intent.putExtra("record", record);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        setTotals(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<TimeRecord> nRecordList = new ArrayList<>();
        adapter = new RecordRecyclerAdapter(nRecordList, this);
        adapter.setOnItemClickListener(new RecordRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                TimeRecord record = adapter.getItem(position);
                Intent intent = new Intent(LogActivity.this, EditActivity.class);
                intent.putExtra("record", record);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        super.onResume();
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

    public void setTotals(Context context) {
        mRecordList = new ArrayList<>();
        DatabaseManager dbm = DatabaseManager.getInstance(this);
        Cursor cursor = dbm.queryAllRecords();
        while(cursor.moveToNext()) {
            TimeRecord record = new TimeRecord(cursor);
            mRecordList.add(record);
        }

        dTotal = 0;
        nTotal = 0;
        gTotal = 0;
        for (TimeRecord record : mRecordList) {
            if (record.timeOfDay.toUpperCase().equals("DAY")) {
                dTotal += record.time;
            } else if (record.timeOfDay.toUpperCase().equals("NIGHT")) {
                nTotal += record.time;
            }
            gTotal += record.time;
        }
        TextView t_daytime = (TextView) this.findViewById(R.id.t_daytime);
        TextView t_nighttime = (TextView) this.findViewById(R.id.t_nighttime);
        TextView t_grand = (TextView) this.findViewById(R.id.t_grand);

        t_daytime.setText(getTime(dTotal));
        t_nighttime.setText(getTime(nTotal));
        t_grand.setText(getTime(gTotal));
    }

    //convert time to formatted String
    public String getTime(long time) {
        int tMinutes = (int) time / 60000;
        String hours = String.valueOf(tMinutes / 60);
        String minutes = String.valueOf(tMinutes % 60);
        if (minutes.length() < 2) {
            minutes = "0" + minutes;
        }
        return hours + ":" + minutes;
    }
}
