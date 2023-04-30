package com.example.alarmman;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.alarmman.R;

public class MainActivity extends AppCompatActivity {
    static final int ALARM_REQ_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AlarmManager alrm = (AlarmManager) getSystemService(ALARM_SERVICE);

        findViewById(R.id.btnSet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int time = Integer.parseInt(((EditText)findViewById(R.id.editTime)).getText().toString());
                long trigTime = System.currentTimeMillis()+(time*1000);
                Intent ibroadcast = new Intent(MainActivity.this,MyReceiver.class);
                PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this,ALARM_REQ_CODE,ibroadcast,PendingIntent.FLAG_UPDATE_CURRENT);
                alrm.set (AlarmManager.RTC_WAKEUP,trigTime,pi);
            }
        });
    }
}