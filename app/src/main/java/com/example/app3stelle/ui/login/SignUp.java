package com.example.app3stelle.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app3stelle.R;

public class SignUp extends AppCompatActivity {
    private TextView sin;
    private Button act;
    private EditText pswd,mail,pswd2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);
        LoginManager l = LoginManager.getInstance();
        sin =  findViewById(R.id.sin);
        act = findViewById(R.id.act);
        mail = findViewById(R.id.mal);
        pswd = findViewById(R.id.pswd);
        pswd2 = findViewById(R.id.pswd2);
        sin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Intent it = new Intent(SignUp.this, LoginActivity.class);
                startActivity(it);
            }
        });

        act.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(checkPasswords()){
                    l.createAccount(mail.getText().toString(),pswd.getText().toString(),SignUp.this);
                }else Toast.makeText(SignUp.this,"Le password non coincidono", Toast.LENGTH_SHORT).show();

            }
        });
    }
    public boolean checkPasswords(){
        if(pswd.getText().toString().equals(pswd2.getText().toString())){
            return true;
        }
        return false;

    }
}

