package com.vlados.musicguru.SQLite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vlados.musicguru.Constants.SQliteConstants;
/*
 * Адаптер для работы с SQLite
 */
public class DBAdapter {
    private final Context mCtx;
    private SQLite dHelper;
    private SQLiteDatabase database;

    public DBAdapter(Context ctx) {
        mCtx = ctx;
    }

    public void open() {
        dHelper = new SQLite(mCtx, SQliteConstants.DBNAME, null, SQliteConstants.VERSION);
        database = dHelper.getWritableDatabase();
    }

    public Cursor getAllData() {
        return database.query(SQliteConstants.TABLE2, null, null, null, null, null, null);
    }

    // закрыть подключение
    public void close() {
        if (dHelper!=null) {
            dHelper.close();
        }
    }
}