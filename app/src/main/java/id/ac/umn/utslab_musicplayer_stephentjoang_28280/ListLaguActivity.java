package id.ac.umn.utslab_musicplayer_stephentjoang_28280;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListLaguActivity extends AppCompatActivity {
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

        getSongs();
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

        Uri uri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
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
        Log.w("HIHIHI", tempListSemuaLagu.toString());
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
}