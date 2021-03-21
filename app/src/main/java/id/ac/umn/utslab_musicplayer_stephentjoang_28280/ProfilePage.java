package id.ac.umn.utslab_musicplayer_stephentjoang_28280;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ProfilePage extends AppCompatActivity {
    private ImageView gambarProfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        gambarProfil = findViewById(R.id.gambarProfil);
        gambarProfil.setImageResource(R.drawable.fotoprofil);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setBackgroundTintList(null);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button btnLink = findViewById(R.id.btnToLinks);
        btnLink.setBackgroundTintList(null);
        btnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this, LinkSumberActivity.class);
                startActivity(intent);
            }
        });
    }
}