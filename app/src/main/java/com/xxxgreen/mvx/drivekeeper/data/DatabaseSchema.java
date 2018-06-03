package com.xxxgreen.mvx.drivekeeper.data;

import android.provider.BaseColumns;

/**
 * Created by MVX on 8/1/2017.
 */

public final class DatabaseSchema {
    // constructor is private to prevent instantiation
    private DatabaseSchema() {}

    // table contents
    public static class Schema implements BaseColumns {
        public static final String TABLE_NAME = "timeRecords";
        public static final String COL_1 = "id";
        public static final String COL_2 = "date";
        public static final String COL_3 = "time";
        public static final String COL_4 = "timeOfDay";
        public static final String COL_5 = "location";
        public static final String COL_6 = "conditions";
        public static final String COL_7 = "initials";
    }
}
