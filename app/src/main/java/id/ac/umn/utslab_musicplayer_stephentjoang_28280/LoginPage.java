package id.ac.umn.utslab_musicplayer_stephentjoang_28280;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginPage extends AppCompatActivity {
    private EditText etUsername;
    private EditText etPassword;
    private TextView tvLoginError;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvLoginError = findViewById(R.id.tvLoginError);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valueUsername = etUsername.getText().toString();
                String valuePassword = etPassword.getText().toString();

                if ((!valueUsername.equals("uasmobile")) || (!valuePassword.equals("uasmobilegenap"))) {
                    tvLoginError.setVisibility(View.VISIBLE);
                }
                else {
                    tvLoginError.setVisibility(View.GONE);
                    Intent intentListLagu = new Intent(LoginPage.this, ListLaguActivity.class);
                    startActivity(intentListLagu);
                }
            }
        });

    }
}