package com.xxxgreen.mvx.drivekeeper.view;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xxxgreen.mvx.drivekeeper.R;

import com.xxxgreen.mvx.drivekeeper.data.DatabaseManager;
import com.xxxgreen.mvx.drivekeeper.data.TimeRecord;

import java.util.List;

/**
 * Created by MVX on 8/2/2017.
 */

public class RecordRecyclerAdapter
        extends RecyclerView.Adapter<RecordRecyclerAdapter.RecordHolder> {
    private List<TimeRecord> recordList;
    private Cursor mCursor;
    int dayColor, nightColor;
    private OnItemClickListener mItemClickListener;

    public RecordRecyclerAdapter(List<TimeRecord> recordList, Context context) {
        this.recordList = recordList;
        DatabaseManager dbm = DatabaseManager.getInstance(context);
        dayColor = ContextCompat.getColor(context, R.color.colorPrimary);
        nightColor = ContextCompat.getColor(context, R.color.colorBlack);
        this.mCursor = dbm.queryAllRecords();
        while(mCursor.moveToNext()) {
            TimeRecord record = new TimeRecord(mCursor);
            recordList.add(record);
        }
    }

    public class RecordHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public CardView card_view;
        public TextView t_date;
        public TextView t_time;
        public TextView t_timeOfDay;

        public RecordHolder(View itemView) {
            super(itemView);
            card_view = (CardView) itemView.findViewById(R.id.card_view);
            t_date = (TextView) itemView.findViewById(R.id.t_date);
            t_time = (TextView) itemView.findViewById(R.id.t_time);
            t_timeOfDay = (TextView) itemView.findViewById(R.id.t_timeOfDay);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            mItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public RecordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_record, parent, false);

        return new RecordHolder(view);
    }

    @Override
    public void onBindViewHolder(RecordRecyclerAdapter.RecordHolder holder, int position) {
        final TimeRecord record = recordList.get(position);
        if (record.timeOfDay.toUpperCase().equals("DAY")) {
            holder.card_view.setCardBackgroundColor(dayColor);
        } else {
            holder.card_view.setCardBackgroundColor(nightColor);
        }
        holder.t_date.setText(record.date);
        holder.t_time.setText(record.getTime());
        holder.t_timeOfDay.setText(record.timeOfDay);
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    public TimeRecord getItem(int position) {
        return recordList.get(position);
    }
}
