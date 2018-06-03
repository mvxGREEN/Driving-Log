package com.xxxgreen.mvx.drivekeeper.data;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MVX on 8/1/2017.
 */

public class TimeRecord implements Parcelable {
    public final int id;
    public final String date;
    public final long time;
    public final String timeOfDay;
    public final String location;
    public final String conditions;
    public final String initials;

    public TimeRecord(String date, long time, String timeOfDay, String location,
                      String conditions, String initials) {
        this.id = 0;
        this.date = date;
        this.time = time;
        this.timeOfDay = timeOfDay;
        this.location = location;
        this.conditions = conditions;
        this.initials = initials;
    }

    public TimeRecord(Cursor cursor) {
        this.id = cursor.getInt(0);
        this.date = cursor.getString(1);
        this.time = cursor.getLong(2);
        this.timeOfDay = cursor.getString(3);
        this.location = cursor.getString(4);
        this.conditions = cursor.getString(5);
        this.initials = cursor.getString(6);
    }

    protected TimeRecord(Parcel in) {
        this.id = in.readInt();
        this.date = in.readString();
        this.time = in.readLong();
        this.timeOfDay = in.readString();
        this.location = in.readString();
        this.conditions = in.readString();
        this.initials = in.readString();
    }

    public static final Creator<TimeRecord> CREATOR = new Creator<TimeRecord>() {
        @Override
        public TimeRecord createFromParcel(Parcel in) {
            return new TimeRecord(in);
        }

        @Override
        public TimeRecord[] newArray(int size) {
            return new TimeRecord[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(date);
        dest.writeLong(time);
        dest.writeString(timeOfDay);
        dest.writeString(location);
        dest.writeString(conditions);
        dest.writeString(initials);
    }

    //convert time to formatted String
    public String getTime() {
        int tMinutes = (int) this.time / 60000;
        String hours = String.valueOf(tMinutes / 60);
        String minutes = String.valueOf(tMinutes % 60);
        if (minutes.length() < 2) {
            minutes = "0" + minutes;
        }
        return hours + ":" + minutes;
    }

    @Override
    public String toString() {
        return "" + this.date + ",\t" + this.location + ",\t" + this.conditions + ",\t"
                + this.timeOfDay + ",\t" + this.getTime() + ",\t" + this.initials;
    }

}
