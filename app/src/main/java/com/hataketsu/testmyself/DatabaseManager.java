package com.hataketsu.testmyself;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by hataketsu on 2/27/16.
 */
public class DatabaseManager {
    private static SQLiteDatabase database;

    public static SQLiteDatabase getDatabase() {
        if (database == null) {
            database = SQLiteDatabase.openDatabase(Environment.getExternalStorageDirectory().getPath() + "/Download/dethixx.db", null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
        }
        return database;
    }
    public static Cursor query(String sql) {
        return getDatabase().rawQuery(sql, null);
    }
}
