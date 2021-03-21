package id.ac.umn.utslab_musicplayer_stephentjoang_28280;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
    int positionLagu;
    List laguList;
    TextView laguName, laguArtist, tvTotalTime, tvRunningTime;

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

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(mediaPlayer!=null && mediaPlayer.isPlaying()){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
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