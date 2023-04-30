package com.example.adminlock;

import static com.example.adminlock.MainActivity.prefsTimeMinKey;
import static com.example.adminlock.UpdateQueActivity.prefsQue1;
import static com.example.adminlock.UpdateQueActivity.prefsQue2;
import static com.example.adminlock.UpdateQueActivity.prefsQue3;
import static com.example.adminlock.UpdateQueActivity.prefsQue4;

import androidx.appcompat.app.AppCompatActivity;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class QueActivity extends AppCompatActivity {
    Button submitBtn ;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    static final String prefsAns1 = "ans1";
    static final String prefsAns2 = "ans2";
    static final String prefsAns3 = "ans3";
    static final String prefsAns4 = "ans4";

    EditText editque1 = findViewById(R.id.que1);
    EditText editque2 = findViewById(R.id.que2);
    EditText editque3 = findViewById(R.id.que3);
    EditText editque4 = findViewById(R.id.que4);
    EditText editans1 = findViewById(R.id.ans1);
    EditText editans2 = findViewById(R.id.ans2);
    EditText editans3 = findViewById(R.id.ans3);
    EditText editans4 = findViewById(R.id.ans4);

    String que1,que2,que3,que4,ans1,ans2,ans3,ans4,saveans1,saveans2,saveans3,saveans4 ;
    int timeMin;

    String TAG = "QUE ACTIVITY";
    DevicePolicyManager deviceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_que);

        submitBtn = findViewById(R.id.submitBtn);
        prefs = getSharedPreferences("AdminLock",MODE_PRIVATE);
        editor = prefs.edit();
        deviceManager = (DevicePolicyManager)
                getSystemService(Context. DEVICE_POLICY_SERVICE ) ;

        que1 = prefs.getString(prefsQue1,"");
        que2 = prefs.getString(prefsQue2,"");
        que3 = prefs.getString(prefsQue3,"");
        que4 = prefs.getString(prefsQue4,"");
        saveans1 = prefs.getString(saveans1,"");
        saveans2 = prefs.getString(saveans2,"");
        saveans3 = prefs.getString(saveans3,"");
        saveans4 = prefs.getString(saveans4,"");

        timeMin = prefs.getInt(prefsTimeMinKey, 15);

        if (que1.equals("") && que2.equals("") && que3.equals("") && que4.equals("")){
            Log.i(TAG,"Time in mins is "+timeMin);
            MyService.handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    deviceManager.lockNow();
                }
            },timeMin*1000);
            finish();
        }
        else {
            submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ans1 = editans1.getText().toString();
                    ans2 = editans2.getText().toString();
                    ans3 = editans3.getText().toString();
                    ans4 = editans4.getText().toString();
                    if (
                            (!que1.equals("") || ans1.equals(saveans1)) &&
                            (!que2.equals("") || ans2.equals(saveans2)) &&
                            (!que3.equals("") || ans3.equals(saveans3)) &&
                            (!que4.equals("") || ans4.equals(saveans4))
                    ){
                        Log.i(TAG,"Time in mins is "+timeMin);
                        MyService.handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                deviceManager.lockNow();
                            }
                        },timeMin*1000);
                        finish();
                    }
                }
            });
        }
    }
}