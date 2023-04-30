package com.example.bundleex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnNext = findViewById(R.id.btnNext);
        Intent iNext = new Intent(MainActivity.this,secactivity.class);

        iNext.putExtra("Name","Afshan Ahmed Khan");
        iNext.putExtra("RollNo", 3);
        int no[]={9,6,1,7,0,7,8,6,7,7};
        iNext.putExtra("Phone",no);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(iNext);
            }
        });
    }
}