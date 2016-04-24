package com.vlados.musicguru.Pages;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.vlados.musicguru.Constants.SQliteConstants;
import com.vlados.musicguru.R;
import com.vlados.musicguru.SQLite.SQLite;
/*
 * Класс для работы с отдельным артистом в отедльном активити
 */
public class Detail extends AppCompatActivity {
    private ImageView ivIconBg;
    private TextView tvGenres;
    private TextView tvContent;
    private TextView tvDescription;
    private TextView tvLink;
    private TextView tvLabel;
    private String name;
    private String genres;
    private String smallCover;
    private String content;
    private int id;
    private SQLite dHelper;
    private SQLiteDatabase database;
    private Cursor cursor;
    private ContentValues contentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_artists);

        dHelper = new SQLite(this);
        database = dHelper.getWritableDatabase();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setUpUIViews();
        /*
         * Извлекаем id артиста, переданного из класса Artists.class
         */
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getInt("artistDetail");
            if (id != -1) {
                try {
                    /*
                     * Заполняем активити данными артиста
                     */
                    adapter(id);
                } catch (NullPointerException e) {
                    this.onBackPressed();
                    e.printStackTrace();
                }
            }
        }
    }

    private void setUpUIViews() {
        ivIconBg = (ImageView) findViewById(R.id.ivIconBg);
        tvGenres = (TextView) findViewById(R.id.tvGenres_det);
        tvContent = (TextView) findViewById(R.id.tvContent_det);
        tvDescription = (TextView) findViewById(R.id.tvDescription_det);
        tvLink = (TextView) findViewById(R.id.tvLink_det);
        tvLabel = (TextView) findViewById(R.id.toolbar_text_det);
    }

    private void adapter(int id) throws NullPointerException {
        cursor = database.query(SQliteConstants.TABLE1, null, SQliteConstants.ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            int bigCoverId = cursor.getColumnIndex("bigCover");
            name = cursor.getString(cursor.getColumnIndex("name"));
            genres = cursor.getString(cursor.getColumnIndex("genres"));
            content = cursor.getInt(cursor.getColumnIndex("albums")) + " альбомов • "
                    + cursor.getInt(cursor.getColumnIndex("tracks")) + " песни";
            smallCover = cursor.getString(cursor.getColumnIndex("smallCover"));
            String bigIcon = cursor.getString(bigCoverId);
            if (bigIcon != null) {
                ImageLoader.getInstance().displayImage(bigIcon, ivIconBg);
            }
            tvGenres.setText(genres);
            tvContent.setText(content);
            tvDescription.setText(cursor.getString(cursor.getColumnIndex("description")));
            tvLink.setText(cursor.getString(cursor.getColumnIndex("link")));
            tvLabel.setText(name);
        }
        cursor.close();
        database.close();
    }
    /*
     * Добавление артиста в новую таблицу(для избранных артистов)
     */
    private void addTable2() {
        database = dHelper.getWritableDatabase();
        database.beginTransaction();
        contentValues = new ContentValues();
        contentValues.put(SQliteConstants.ID, id);
        contentValues.put(SQliteConstants.NAME, name);
        contentValues.put(SQliteConstants.GENRES, genres);
        contentValues.put(SQliteConstants.CONTENT, content);
        database.insert(SQliteConstants.TABLE2, null, contentValues);
        database.setTransactionSuccessful();
        database.endTransaction();
        database.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_artist_det, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.newArtist:
                addTable2();
                Toast.makeText(getApplicationContext(), name + " добавлен в избранное", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}