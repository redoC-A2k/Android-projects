package com.example.lockai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

public class mathsactivity extends AppCompatActivity {
    int level ;
    int noofques ;
    int mins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mathsactivity);


        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("AppData",MODE_PRIVATE);
        level = sharedPreferences.getInt("level",1);
        noofques = sharedPreferences.getInt("noofques",3);
        mins = sharedPreferences.getInt("mins", 10);

    }
}