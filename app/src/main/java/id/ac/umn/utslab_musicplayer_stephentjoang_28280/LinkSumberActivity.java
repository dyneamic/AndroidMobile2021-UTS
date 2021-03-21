package id.ac.umn.utslab_musicplayer_stephentjoang_28280;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LinkSumberActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_sumber);

        TextView sumber = findViewById(R.id.tvLinkSumber);
        sumber.setMovementMethod(LinkMovementMethod.getInstance());

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setBackgroundTintList(null);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LinkSumberActivity.this, ProfilePage.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LinkSumberActivity.this, ProfilePage.class);
        startActivity(intent);
    }

}