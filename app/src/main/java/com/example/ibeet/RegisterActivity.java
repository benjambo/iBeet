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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    private TextView already;
    private Button registerButton;
    private EditText username, password, name, age;
    private DatabaseSQL db;
    private SharedPreferences myPrefs;
    private RadioGroup toggle;
    private Boolean sex = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        name = findViewById(R.id.nameText);
        age = findViewById(R.id.ageText);

        myPrefs = getSharedPreferences("com.example.ibeet.DATES", Context.MODE_PRIVATE);

        registerButton = findViewById(R.id.btn_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //inserting registered accounts to database
                db=new DatabaseSQL(RegisterActivity.this);

                //Get text
                String names = name.getText().toString();
                String usernames = username.getText().toString();
                String passwords = password.getText().toString();
                String ages = age.getText().toString();
                String gender = sex.toString();

                //When Registering following method also clears prior prefs
                TimeCalculator.getInstance().updateDate(RegisterActivity.this);

                //Saving userName, age and sex to shared preferences
                myPrefs = getSharedPreferences("com.example.ibeet.DATES", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = myPrefs.edit();
                editor.putString("nameKey", names);
                editor.putString("userKey", usernames);
                editor.putString("passKey", passwords);
                editor.putString("ageKey", ages);
                editor.putBoolean("sexKey", Boolean.valueOf(gender));
                editor.commit();

                User user = new User(usernames, passwords, names, ages, Boolean.valueOf(gender));

                //add the user to UserFile.
                FileHandler.getInstance().readUserFile(RegisterActivity.this).setNewUser(user);

                //add username and psw to db
                db.addUser(user);

                Log.d("KOOL", "works: " + usernames + " " + ages + " " + gender);

                if (name.getText().toString().trim().equals("") ||
                        username.getText().toString().trim().equals("") ||
                        password.getText().toString().trim().equals("") ||
                        age.getText().toString().trim().equals("")){
                    Toast.makeText(getBaseContext(),"Please Fill in All the Input Fields", Toast.LENGTH_LONG).show();
                }else{
                    //do what you want with the entered text
                    Intent profileActivity = new Intent(RegisterActivity.this, ProfileActivity.class);
                    startActivity(profileActivity);
                    finish();
                }
            }
        });


         //Making a certain area of text clickable!!!!

        already = findViewById(R.id.already);

        String text = "Already have an account? Login here";

        SpannableString ss = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                finish();
            }
        };

        ss.setSpan(clickableSpan, 25, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);    //Indicating which letters to make clickable
        already.setText(ss);
        already.setMovementMethod(LinkMovementMethod.getInstance());    //Making text clickable

        toggle = findViewById(R.id.radioGroup);

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
