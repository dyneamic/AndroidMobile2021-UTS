package id.ac.umn.utslab_musicplayer_stephentjoang_28280;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MusicPlayerActivity extends AppCompatActivity implements Serializable {
    MediaPlayer mediaPlayer;
    ModelLagu lagu;
    SeekBar seekBar;
    Button btnPlay;
    ImageView ivAlbumImage;
    int positionLagu;
    List laguList;
    TextView laguName, laguArtist, tvTotalTime, tvRunningTime;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        Intent intent = getIntent();
        //lagu = (ModelLagu) intent.getSerializableExtra("audio");
        laguList = (List) intent.getSerializableExtra("fullList");
        positionLagu = (int) intent.getSerializableExtra("position");

        laguName = findViewById(R.id.laguName);
        laguArtist = findViewById(R.id.laguArtist);
        tvTotalTime = findViewById(R.id.tvTotalTime);
        tvRunningTime = findViewById(R.id.tvRunningTime);
        ivAlbumImage = findViewById(R.id.albumImage);
        Button btnBack = findViewById(R.id.btnBack);
        Button btnPrev = findViewById(R.id.btnPrev);
        Button btnNext = findViewById(R.id.btnNext);
        btnPlay = findViewById(R.id.btnPlay);

        btnPlay.setBackgroundTintList(null);
        btnPrev.setBackgroundTintList(null);
        btnNext.setBackgroundTintList(null);
        btnBack.setBackgroundTintList(null);

        initNew(positionLagu);
        startPlay();
        enableSeekBar();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlay();
                Intent intent = new Intent(MusicPlayerActivity.this, ListLaguActivity.class);
                intent.putExtra("popup", 0);
                startActivity(intent);
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlay();
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlay();
                positionLagu--;
                if (positionLagu < 0) positionLagu = laguList.size() - 1;
                initNew(positionLagu);
                startPlay();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlay();
                positionLagu++;
                if (positionLagu == laguList.size()) positionLagu = 0;
                initNew(positionLagu);
                startPlay();
            }
        });
    }
    
    //tambahan
    public static Uri getArtUriFromMusicFile(Context context, ModelLagu lagu) {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] cursor_cols = { MediaStore.Audio.Media.ALBUM_ID };

        String condition = MediaStore.Audio.Media.IS_MUSIC + "=1 AND " + MediaStore.Audio.Media.DATA + " = '"
                + lagu.getLaguPath() + "'";
        Cursor cursor = context.getApplicationContext().getContentResolver().query(uri, cursor_cols, condition, null, null);
        /*
         * If the cusor count is greater than 0 then parse the data and get the art id.
         */
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            Long albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));

            Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
            Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);
            cursor.close();
            return albumArtUri;
        }
        return Uri.EMPTY;
    }

    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    //end of tambahan
    
    public void initNew(int positionLagu) {
        lagu = (ModelLagu) laguList.get(positionLagu);
        //nama lagu
        if (lagu != null) {
            String artist_name = "";
            if (lagu.getLaguArtist().equals("<unknown>")) artist_name = "Unknown Artist";
            else artist_name = lagu.getLaguArtist();
            laguName.setText(lagu.getLaguName());
            laguArtist.setText(artist_name);
        }
        else {
            laguName.setText("Filename not found.");
            laguArtist.setText("");
        }
        mediaPlayer = MediaPlayer.create(MusicPlayerActivity.this, Uri.parse(lagu.getLaguPath()));
        int durasi_lagu = mediaPlayer.getDuration();
        durasi_lagu /= 1000;
        int durasi_lagu_detik = durasi_lagu % 60;
        String durasi_lagu_detik_str = "";
        if ((durasi_lagu_detik) < 10) durasi_lagu_detik_str = "0" + durasi_lagu_detik;
        else durasi_lagu_detik_str = String.valueOf(durasi_lagu_detik);
        String time_str = (durasi_lagu / 60) + " : " + durasi_lagu_detik_str;
        tvTotalTime.setText(time_str);

        //album
        try {
            mContext = getApplicationContext();
            Uri album_image_uri = getArtUriFromMusicFile(mContext, lagu);
            File album_image_file = new File(getRealPathFromURI(album_image_uri));
            Drawable drawable_img = Drawable.createFromPath(album_image_file.getAbsolutePath());
            ivAlbumImage.setBackground(drawable_img);
        }
        catch(Exception e) {
        }
        enableSeekBar();
    }

    public void startPlay() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btnPlay.setBackground(getDrawable(R.drawable.play_button));
        }
        else if (mediaPlayer != null && !(mediaPlayer.isPlaying())) {
            mediaPlayer.start();
            btnPlay.setBackground(getDrawable(R.drawable.pause_button));
            enableSeekBar();
        }
    }

    public void stopPlay(){
        if(mediaPlayer!=null){
            mediaPlayer.stop();
        }
    }

    public void enableSeekBar() {
        seekBar =  findViewById(R.id.seekBar);
        seekBar.setMax(mediaPlayer.getDuration());
        int temp = 0;

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(mediaPlayer!=null && mediaPlayer.isPlaying()){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());

                    if (mediaPlayer.getCurrentPosition() > (mediaPlayer.getDuration() - 30)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnPlay.setBackground(getDrawable(R.drawable.play_button));
                            }
                        });
                    }

                    int curr_pos_lagu = mediaPlayer.getCurrentPosition();
                    curr_pos_lagu /= 1000;
                    int curr_pos_detik = curr_pos_lagu % 60;
                    String curr_pos_detik_str = "";
                    if ((curr_pos_detik) < 10) curr_pos_detik_str = "0" + curr_pos_detik;
                    else curr_pos_detik_str = String.valueOf(curr_pos_detik);
                    String curr_detik_str = (curr_pos_lagu / 60) + " : " + curr_pos_detik_str;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvRunningTime.setText(curr_detik_str);
                        }
                    });
                }
            }
        }, 0, 10);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                // Update the progress depending on seek bar
                if(fromUser){
                    mediaPlayer.seekTo(progress);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        stopPlay();
        //super.onBackPressed();
        Intent intent = new Intent(this, ListLaguActivity.class);
        intent.putExtra("popup", 0);
        startActivity(intent);
    }
}