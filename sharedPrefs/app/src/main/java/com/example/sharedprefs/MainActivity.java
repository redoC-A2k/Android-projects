package com.example.sharedprefs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences pref = getSharedPreferences("login",MODE_PRIVATE);
                boolean check = pref.getBoolean("flag",false);
                Intent inext;
                if(check){
                    inext = new Intent(MainActivity.this,homeact.class);
                }else{
                    inext = new Intent(MainActivity.this,loginact.class);
                }
                startActivity(inext);
            }

        },4000);
    }
}