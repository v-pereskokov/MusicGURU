package com.vlados.musicguru.Pages;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.vlados.musicguru.Constants.Constants;
import com.vlados.musicguru.Constants.SQliteConstants;
import com.vlados.musicguru.Deserializer.ArtistDeserializer;
import com.vlados.musicguru.Adapters.Adapter;
import com.vlados.musicguru.Model.Artist;
import com.vlados.musicguru.Model.ArtistEntity;
import com.vlados.musicguru.R;
import com.vlados.musicguru.SQLite.SQLite;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;

/*
 * Класс для вывода всех артистов
 */
public class Artists extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private ListView lvArtists;
    private SQLite dHelper;
    private ContentValues contentValues;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_artists);

        lvArtists = (ListView) findViewById(R.id.lvArtist);

        /*
         * Настройка загрузки фото по ссылке
         */
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        dHelper = new SQLite(this);
        /*
         * Запуск парсера json
         */
        new Parser().execute(Constants.URL_JSON);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*
     * Класс парсера json
     * Приватный и внутри класса activity, потому что нигде более не нужен и не должен нигде использоваться
     */
    private class Parser extends AsyncTask<String, String, ArtistEntity> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArtistEntity doInBackground(String... params) {
            HttpURLConnection connection = null;
            JsonReader reader = null;
            ArtistEntity artistEntity = null;
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(ArtistEntity.class, new ArtistDeserializer())
                    .create();
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                if (connection != null) { // Проверка на наличие сети
                    connection.connect();
                    reader = new JsonReader(new InputStreamReader(connection.getInputStream()));
                    artistEntity = gson.fromJson(reader, ArtistEntity.class);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return artistEntity;
        }

        @Override
        protected void onPostExecute(final ArtistEntity artists) {
            super.onPostExecute(artists);
            createSQLiteDB(artists);
            if (artists != null) { // Сортировка по именам артистов
                Collections.sort(artists.getArtistList(), new Comparator<Artist>() {
                    @Override
                    public int compare(Artist lhs, Artist rhs) {
                        return lhs.getName().compareTo(rhs.getName());
                    }
                });
                final Adapter adapter = new Adapter(getApplicationContext(), R.layout.row, artists.getArtistList());
                lvArtists.setAdapter(adapter);
                lvArtists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Artist artist = artists.getArtistList().get(position);
                        Intent intent = new Intent(Artists.this, Detail.class);
                        intent.putExtra("artistDetail", artist.getId()); // Передача id артиста в другое активити
                        startActivity(intent);
                        overridePendingTransition(R.anim.appearance, R.anim.disappearance); // Анимация появления активити
                    }
                });
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Проверьте подключение к интернету", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }
    /*
     * Добавление всех артистов в первую таблицу
     */
    private void createSQLiteDB(ArtistEntity artists) {
        database = dHelper.getWritableDatabase();
        database.beginTransaction();
        for (Artist artist : artists.getArtistList()) {
            contentValues = new ContentValues();
            contentValues.put(SQliteConstants.ID, artist.getId());
            contentValues.put(SQliteConstants.NAME, artist.getName());
            contentValues.put(SQliteConstants.GENRES, artist.getGenres());
            contentValues.put(SQliteConstants.TRACKS, artist.getTracks());
            contentValues.put(SQliteConstants.ALBUMS, artist.getAlbums());
            contentValues.put(SQliteConstants.DESCRIPTION, artist.getDescription());
            contentValues.put(SQliteConstants.LINK, artist.getLink());
            contentValues.put(SQliteConstants.BIG_COVER, artist.getCovers().getBigCover());
            contentValues.put(SQliteConstants.SMALL_COVER, artist.getCovers().getSmallCover());
            if (database.insert(SQliteConstants.TABLE1, null, contentValues) == -1) {
                break;
            }
        }
        database.setTransactionSuccessful();
        database.endTransaction();
        database.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_artists, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.artists:
                new Parser().execute(Constants.URL_JSON);
                Toast.makeText(Artists.this, getString(R.string.artists), Toast.LENGTH_SHORT).show();
                break;
            case R.id.favorite:
                Toast.makeText(Artists.this, getString(R.string.favorite), Toast.LENGTH_SHORT).show();
                Intent intent_favorite = new Intent(this, Favorite.class);
                startActivity(intent_favorite);
                overridePendingTransition(R.anim.appearance, R.anim.disappearance);
                break;
            case R.id.info:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.about))
                        .setMessage("Приложение выполнил Перескоков Владислав\n" +
                                "Студент МГТУ им. Н.Э.Баумана\n" +
                                "Факультета информатики и систем управления\n" +
                                "Кафедры ИУ8 - информационная безопасность")
                        .setCancelable(false)
                        .setNegativeButton(getString(R.string.close),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}