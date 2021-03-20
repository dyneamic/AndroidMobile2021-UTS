package id.ac.umn.utslab_musicplayer_stephentjoang_28280;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListLaguActivity extends AppCompatActivity implements Serializable {
    RecyclerView listLaguView;
    ListLaguAdapter listLaguAdapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_lagu);

        //pemeriksaan permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                return;
            }
        }

        Bundle extras = getIntent().getExtras();
        if (extras == null){
            AlertDialog.Builder alertAwalBuilder = new AlertDialog.Builder(this);
            alertAwalBuilder.setTitle("Selamat Datang");
            alertAwalBuilder.setMessage("Stephen Tjoang\n00000028280");
            alertAwalBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog alertAwal = alertAwalBuilder.create();
            alertAwal.show();
        }


        getSongs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menuProfil:
                Intent intentProfil = new Intent(this, ProfilePage.class);
                startActivity(intentProfil);
                return true;
            case R.id.menuLogOut:
                Intent intentLogOut = new Intent(this, MainActivity.class);
                startActivity(intentLogOut);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getSongs() {
        context = ListLaguActivity.this;
        listLaguView = findViewById(R.id.listLaguView);

        List listSemuaLagu = getLaguFromDevice(context);

        listLaguAdapter = new ListLaguAdapter(context, listSemuaLagu);
        listLaguView.setLayoutManager(new LinearLayoutManager(context));
        listLaguView.setAdapter(listLaguAdapter);
    }

    public List getLaguFromDevice (final Context context) {
        final List tempListSemuaLagu = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] dataLagu = { MediaStore.Audio.AudioColumns.DATA,
                              MediaStore.Audio.AudioColumns.ALBUM,
                              MediaStore.Audio.ArtistColumns.ARTIST};

        Cursor cursor = context.getContentResolver().query(uri, dataLagu, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                ModelLagu modelLagu = new ModelLagu();

                String path = cursor.getString(0);
                String album = cursor.getString(1);
                String artist = cursor.getString(2);

                String name = path.substring(path.lastIndexOf("/") + 1);

                modelLagu.setLaguPath(path);
                modelLagu.setLaguAlbum(album);
                modelLagu.setLaguName(name);
                modelLagu.setLaguArtist(artist);

                tempListSemuaLagu.add(modelLagu);
            }
            cursor.close();
        }
        return tempListSemuaLagu;
    }

    //menghandle callback
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getSongs();
                    Toast.makeText(context,"Permission granted",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context,"Permission denied",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Informasi");
        alertBuilder.setMessage("Untuk keluar, silakan Log Out");
        alertBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertAwal = alertBuilder.create();
        alertAwal.show();
    }
}