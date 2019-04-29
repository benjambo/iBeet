package com.example.ibeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
private EditText name;
private EditText password;
private TextView correction;
private Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.btn_login);
        correction = (TextView) findViewById(R.id.better);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(name.getText().toString(), password.getText().toString()); //Getting inputs and cheching them
            }
        });

    }

    /**
     * Creating validation method for login
     * @param userName
     * @param userPassword
     */
    private void validate(String userName, String userPassword) {
        if (((!userName.matches("")) && (!userPassword.matches("")))) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            correction.setText("Try again!");
        }

    }
}
