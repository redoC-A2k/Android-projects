package com.example.notiandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    Intent intent ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.edit_text_input);
//        notificationManager.notify(NOTIFICATION_ID,notification);

        Button startservicebtn = findViewById(R.id.startservicebtn);
        Button stopservicebtn = findViewById(R.id.stopservicebtn);
        startservicebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(view);
            }
        });
        stopservicebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(view);
            }
        });
    }

    public void startService(View v){
        String input = editText.getText().toString();
        intent = new Intent(this,MyService.class);
        intent.putExtra("inputExtra",input);
        ContextCompat.startForegroundService(this,intent);
    }



    public void stopService(View v){
        stopService(intent);
    }
}