package com.example.lockai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("AppData",MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        EditText lvled = findViewById(R.id.lvled);
        lvled.setText(sharedPreferences.getInt("level",1));
        EditText queed = findViewById(R.id.queed);
        queed.setText(sharedPreferences.getInt("noofques",3));
        EditText timed = findViewById(R.id.timed);
        timed.setText(sharedPreferences.getInt("mins",10));

        Button updatebtn = findViewById(R.id.updatebtn);
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putInt("level",Integer.parseInt(lvled.getText().toString()));
                editor.putInt("noofques",Integer.parseInt(queed.getText().toString()));
                editor.putInt("mins",Integer.parseInt(timed.getText().toString()));
                editor.commit();
            }
        });
    }
}