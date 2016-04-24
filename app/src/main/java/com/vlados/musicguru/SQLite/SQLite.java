package com.vlados.musicguru.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vlados.musicguru.Constants.SQliteConstants;
/*
 * Класс для создания базы данных с двумя таблицами
 * Первая таблица - все артисты
 * Вторая таблица - избранные артисты
 */
public class SQLite extends SQLiteOpenHelper {
    private String allArtists = "CREATE TABLE " + SQliteConstants.TABLE1 + " (" + SQliteConstants.ID +  " INTEGER PRIMARY KEY, " +
            SQliteConstants.NAME + " TEXT NOT NULL, " + SQliteConstants.GENRES + " TEXT, "
            + SQliteConstants.TRACKS + " INTEGER, " + SQliteConstants.ALBUMS + " INTEGER, "
            + SQliteConstants.DESCRIPTION + " TEXT NOT NULL, " + SQliteConstants.LINK + " TEXT, "
            + SQliteConstants.BIG_COVER + " TEXT NOT NULL, " + SQliteConstants.SMALL_COVER + " TEXT NOT NULL" + ");";

    private String favoriteArtists = "CREATE TABLE " + SQliteConstants.TABLE2 + " (" + SQliteConstants.ID +  " INTEGER PRIMARY KEY, " +
            SQliteConstants.NAME + " TEXT NOT NULL, " + SQliteConstants.GENRES + " TEXT, "
            + SQliteConstants.CONTENT + " TEXT NOT NULL" + ");";


    public SQLite(Context context) {
        super(context, SQliteConstants.DBNAME, null, SQliteConstants.VERSION);
    }

    public SQLite(Context mCtx, String dbName, Object obj, int dbVersion) {
        super(mCtx, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(allArtists);
        db.execSQL(favoriteArtists);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}