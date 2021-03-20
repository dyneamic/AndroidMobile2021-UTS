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

public class MusicPlayerActivity extends AppCompatActivity implements Serializable {
    MediaPlayer mediaPlayer;
    ModelLagu lagu;
    SeekBar seekBar;
    Button btnPlay;
    int positionLagu;
    List laguList;
    TextView laguName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        Intent intent = getIntent();
        //lagu = (ModelLagu) intent.getSerializableExtra("audio");
        laguList = (List) intent.getSerializableExtra("fullList");
        positionLagu = (int) intent.getSerializableExtra("position");

        laguName = findViewById(R.id.laguName);
        Button btnPrev = findViewById(R.id.btnPrev);
        Button btnNext = findViewById(R.id.btnNext);
        btnPlay = findViewById(R.id.btnPlay);

        btnPlay.setBackgroundTintList(null);
        btnPrev.setBackgroundTintList(null);
        btnNext.setBackgroundTintList(null);

        initNew(positionLagu);
        startPlay();
        enableSeekBar();

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
            laguName.setText(lagu.getLaguName());
        }
        else {
            laguName.setText("Filename not found.");
        }
        mediaPlayer = MediaPlayer.create(MusicPlayerActivity.this, Uri.parse(lagu.getLaguPath()));
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
        //enableSeekBar();
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