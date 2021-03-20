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
import java.util.Timer;
import java.util.TimerTask;

public class MusicPlayerActivity extends AppCompatActivity implements Serializable {
    MediaPlayer mediaPlayer;
    ModelLagu lagu;
    SeekBar seekBar;
    Button btnPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        Intent intent = getIntent();
        lagu = (ModelLagu) intent.getSerializableExtra("audio");

        TextView laguName = findViewById(R.id.laguName);
        Button btnViewAllSongs = findViewById(R.id.btnViewAllSongs);
        Button btnPrev = findViewById(R.id.btnPrev);
        Button btnNext = findViewById(R.id.btnNext);
        btnPlay = findViewById(R.id.btnPlay);

        if (lagu != null) {
            laguName.setText(lagu.getLaguName());
        }
        else {
            laguName.setText("Sample Lagu");
        }

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlay();
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnViewAllSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlay();
                startActivity(new Intent(getApplicationContext(), ListLaguActivity.class));
                //finish();
            }
        });
    }

    public void startPlay() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btnPlay.setBackground(getDrawable(R.drawable.ic_play_button));
        }
        else if (mediaPlayer != null && !(mediaPlayer.isPlaying())) {
            mediaPlayer.start();
            btnPlay.setBackground(getDrawable(R.drawable.ic_pause_button));
        }
        else if (lagu != null) {
            Toast.makeText(getApplicationContext(),"Playing from device; "+ lagu.getLaguPath(),Toast.LENGTH_SHORT).show();
            btnPlay.setBackground(getDrawable(R.drawable.ic_pause_button));
            mediaPlayer = MediaPlayer.create(MusicPlayerActivity.this, Uri.parse(lagu.getLaguPath()));
            mediaPlayer.start();
            enableSeekBar();
        }
        else{
            Toast.makeText(getApplicationContext(),"Playing from app",Toast.LENGTH_SHORT).show();
            try {
                AssetFileDescriptor afd = getAssets().openFd("piano.wav");
                btnPlay.setBackground(getDrawable(R.drawable.ic_pause_button));
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                afd.close();
                mediaPlayer.prepare();
                mediaPlayer.start();
                enableSeekBar();
            } catch (Exception e) {
                e.printStackTrace();
            }
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