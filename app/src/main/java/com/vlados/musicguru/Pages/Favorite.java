package com.vlados.musicguru.Pages;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.vlados.musicguru.Constants.SQliteConstants;
import com.vlados.musicguru.SQLite.DBAdapter;
import com.vlados.musicguru.R;
/*
 * Класс для работы с артистами, которых пользователь добавил в избранное
 */
public class Favorite extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ListView lvArtist;
    private SimpleCursorAdapter cursorAdapter;
    DBAdapter db;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_favorite);
        lvArtist = (ListView) findViewById(R.id.lvArtistFav);
        db = new DBAdapter(this);
        db.open();
        cursor = db.getAllData();
        startManagingCursor(cursor);
        String[] from = new String[] {SQliteConstants.NAME, SQliteConstants.GENRES,
                SQliteConstants.CONTENT};
        int[] to = new int[] {R.id.tvName_fav, R.id.tvGenres_fav,
                R.id.tvContent_fav};
        cursorAdapter = new SimpleCursorAdapter(this, R.layout.row_fav, cursor, from, to);
        lvArtist.setAdapter(cursorAdapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorite, menu);
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
                Toast.makeText(Favorite.this, getString(R.string.artists), Toast.LENGTH_SHORT).show();
                Intent intent_artistst = new Intent(this, Artists.class);
                startActivity(intent_artistst);
                overridePendingTransition(R.anim.appearance, R.anim.disappearance);
                break;
            case R.id.favorite:
                Toast.makeText(Favorite.this, getString(R.string.favorite), Toast.LENGTH_SHORT).show();
                break;
            case R.id.info:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("О программе")
                        .setMessage("Приложение выполнил Перескоков Владислав\n" +
                                "Студент МГТУ им. Н.Э.Баумана\n" +
                                "Факультета информатики и систем управления\n" +
                                "Кафедры ИУ8 - информационная безопасность")
                        .setCancelable(false)
                        .setNegativeButton("Закрыть",
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
