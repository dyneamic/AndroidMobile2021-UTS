package id.ac.umn.utslab_musicplayer_stephentjoang_28280;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btnProfil;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnProfil = findViewById(R.id.btnProfil);
        btnLogin = findViewById(R.id.btnLogin);

        btnProfil.setBackgroundTintList(null);
        btnLogin.setBackgroundTintList(null);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(MainActivity.this, LoginPage.class);
                startActivity(intentLogin);
            }
        });

        btnProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentProfil = new Intent(MainActivity.this, ProfilePage.class);
                startActivity(intentProfil);
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Informasi");
        alertBuilder.setMessage("Tekan home button untuk kembali ke menu utama.");
        alertBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        AlertDialog alertAwal = alertBuilder.create();
        alertAwal.show();
    }
}