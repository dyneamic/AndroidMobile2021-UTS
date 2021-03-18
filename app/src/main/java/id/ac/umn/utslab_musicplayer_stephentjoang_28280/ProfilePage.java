package id.ac.umn.utslab_musicplayer_stephentjoang_28280;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class ProfilePage extends AppCompatActivity {
    private ImageView gambarProfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        gambarProfil = findViewById(R.id.gambarProfil);
        gambarProfil.setImageResource(R.drawable.fotoprofil);
    }
}