package com.example.appventas2;

import android.provider.BaseColumns;

public final class DatabaseContract {
    private DatabaseContract() {}

    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_FULL_NAME = "full_name";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_KEYWORD = "keyword";
    }
}
