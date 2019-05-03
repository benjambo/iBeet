package com.example.ibeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    private TextView already;
    private Button registerButton;
    private EditText email, password, name, age;
    private DatabaseSQL db;
    private SharedPreferences myPrefs;
    private RadioGroup toggle;
    private RadioButton male;
    private Boolean sex = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        name = (EditText) findViewById(R.id.nameText);
        age = (EditText) findViewById(R.id.ageText);
        male = (RadioButton) findViewById(R.id.radioButtonMale);

        myPrefs = getSharedPreferences("com.example.ibeet.DATES", Context.MODE_PRIVATE);

        registerButton = (Button) findViewById(R.id.btn_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //inserting registered accounts to database
                db=new DatabaseSQL(RegisterActivity.this);

                //Adding users email and password to database
                db.addUser(new User(email.getText().toString(), password.getText().toString()));

                //Get text
                String names = name.getText().toString();
                String ages = age.getText().toString();
                String gender = male.getText().toString(); //Doesnt work yet

                //Saving name, age and sex to shared preferences
                myPrefs = getSharedPreferences(" com.example.ibeet.DATES", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = myPrefs.edit();
                editor.putString("nameKey", names);
                editor.putInt("ageKey", Integer.parseInt(ages));
                editor.putBoolean("sexKey", Boolean.valueOf(gender));
                editor.apply();

                Log.d("KOOL", "works: " + ages + names + gender);

                Intent mainActivity = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(mainActivity);
            }
        });


         //Making a certain area of text clickable!!!!

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

        toggle = (RadioGroup) findViewById(R.id.radioGroup);

        toggle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = toggle.findViewById(checkedId);
                int index = toggle.indexOfChild(radioButton);

                switch (index) {
                    case 0:
                        sex = true;
                        break;

                    case 1:
                        sex = false;
                        break;
                }
            }
        });
    }
}
