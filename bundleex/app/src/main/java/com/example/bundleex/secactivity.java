package com.example.bundleex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Arrays;

public class secactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secactivity);

        Intent fromAct = getIntent();
        String name = fromAct.getStringExtra("Name");
        int []phone = fromAct.getIntArrayExtra("Phone");
        int rollNo = fromAct.getIntExtra("RollNo",0);
        String srno = Integer.toString(rollNo);

        String phoneno = Arrays.toString(phone);

        TextView tname = findViewById(R.id.name);
        TextView tphone = findViewById(R.id.phone);
        TextView trno = findViewById(R.id.rno);

        tname.setText(name);
        tphone.setText(phoneno);
        trno.setText(srno);
    }
}