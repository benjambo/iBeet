package com.example.ibeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
private EditText uname;
private EditText pswd;
private Button login;
private TextView register;
private DatabaseSQL db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        uname = (EditText) findViewById(R.id.username);
        pswd = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.btn_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = uname.getText().toString();
                String password = pswd.getText().toString();

                int id= checkUser(new User(name,password));
                if(id==-1)
                {
                    Toast.makeText(LoginActivity.this,"User Does Not Exist",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        db=new DatabaseSQL(LoginActivity.this);

        register = (TextView) findViewById(R.id.register);

        String text = "Register here";

        SpannableString ss = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent registerActivity = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerActivity);
            }
        };

        ss.setSpan(clickableSpan, 0, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        register.setText(ss);
        register.setMovementMethod(LinkMovementMethod.getInstance());

    }

    public int checkUser(User user)
    {
        return db.checkUser(user);
    }
}
