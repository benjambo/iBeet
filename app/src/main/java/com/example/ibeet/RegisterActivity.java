package com.example.ibeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {
    private TextView already;
    private Button registerButton;
    private EditText email, password, name, age, weight;
    private DatabaseSQL db;
    private SharedPreferences myPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        name = (EditText) findViewById(R.id.nameText);
        age = (EditText) findViewById(R.id.ageText);
        weight = (EditText) findViewById(R.id.weightText);

        myPrefs = getSharedPreferences("prefID", Context.MODE_PRIVATE);

        //IF NEEDED LATER ON
        String names = myPrefs.getString("nameKey","No name");
        int ages = myPrefs.getInt("ageKey",0);
        final int weights = myPrefs.getInt("weightKey", 0);

        registerButton = (Button) findViewById(R.id.btn_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //inserting registered accounts to database
                db=new DatabaseSQL(RegisterActivity.this);

                //Adding users email and password to database
                db.addUser(new User(email.getText().toString(), password.getText().toString()));

                //Saving name, age and weight to shared preferences
                myPrefs = getSharedPreferences("prefID", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = myPrefs.edit();
                editor.putString("nameKey", name.getText().toString());
                editor.putInt("ageKey", Integer.parseInt(age.getText().toString()));
                editor.putInt("weightKey", Integer.parseInt(weight.getText().toString()));
                editor.apply();
                Log.d("KOOL", "Works well");

                Intent mainActivity = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(mainActivity);
            }
        });

        /**
         * Making a certain area of text clickable!!!!
         */

        already = (TextView) findViewById(R.id.already);

        String text = "Already have an account? Login here";

        SpannableString ss = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent loginActivity = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginActivity);
            }
        };

        ss.setSpan(clickableSpan, 25, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);    //Indicating which letters to make clickable
        already.setText(ss);
        already.setMovementMethod(LinkMovementMethod.getInstance());    //Making text clickable
    }
}
