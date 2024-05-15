package com.example.app3stelle.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app3stelle.R;

public class LoginActivity extends AppCompatActivity {
    private TextView acc,sin, registrationTextView;
    private EditText mal,pswd;
    private Button btn;
    LoginManager log_manager = LoginManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        registrationTextView = (TextView)findViewById(R.id.sup);
        sin = (TextView)findViewById(R.id.sin);
        btn = findViewById(R.id.btnS);
        //acc = (TextView)findViewById(R.id.act);
        mal = findViewById(R.id.mal);
        pswd = findViewById(R.id.pswd);
        log_manager.logOut(LoginActivity.this);
        //log_manager.onStart(this);
        registrationTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(LoginActivity.this, SignUp.class);
                startActivity(it);
            }
        });

        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                log_manager.signIn(mal.getText().toString(),pswd.getText().toString(),LoginActivity.this);
            }
        });
    }
}
