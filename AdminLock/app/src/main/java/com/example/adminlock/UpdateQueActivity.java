package com.example.adminlock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UpdateQueActivity extends AppCompatActivity {
    Button udpateQueBtn;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    static final String prefsQue1 = "que1";
    static final String prefsQue2 = "que2";
    static final String prefsQue3 = "que3";
    static final String prefsQue4 = "que4";
    static final String prefsAns1 = "ans1";
    static final String prefsAns2 = "ans2";
    static final String prefsAns3 = "ans3";
    static final String prefsAns4 = "ans4";

    EditText editque1 ;
    EditText editque2 ;
    EditText editque3 ;
    EditText editque4 ;
    EditText editans1 ;
    EditText editans2 ;
    EditText editans3 ;
    EditText editans4 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_que);

        udpateQueBtn = findViewById(R.id.updateQueBtn);
        prefs = getSharedPreferences("AdminLock",MODE_PRIVATE);
        editor = prefs.edit();

        editque1 = findViewById(R.id.que1);
        editque1.setText(prefs.getString(prefsQue1,""));
        editque2 = findViewById(R.id.que2);
        editque2.setText(prefs.getString(prefsQue2,""));
        editque3 = findViewById(R.id.que3);
        editque3.setText(prefs.getString(prefsQue3,""));
        editque4 = findViewById(R.id.que4);
        editque4.setText(prefs.getString(prefsQue4,""));
        editans1 = findViewById(R.id.ans1);
        editans1.setText(prefs.getString(prefsAns1,""));
        editans2 = findViewById(R.id.ans2);
        editans2.setText(prefs.getString(prefsAns2,""));
        editans3 = findViewById(R.id.ans3);
        editans3.setText(prefs.getString(prefsAns3,""));
        editans4 = findViewById(R.id.ans4);
        editans4.setText(prefs.getString(prefsAns4,""));

        udpateQueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString(prefsQue1,editque1.getText().toString());
                editor.putString(prefsQue2,editque2.getText().toString());
                editor.putString(prefsQue3,editque3.getText().toString());
                editor.putString(prefsQue4,editque4.getText().toString());
                editor.putString(prefsAns1,editans1.getText().toString());
                editor.putString(prefsAns2,editans2.getText().toString());
                editor.putString(prefsAns3,editans3.getText().toString());
                editor.putString(prefsAns4,editans4.getText().toString());
                editor.apply();
            }
        });
    }
}