package com.example.bmicalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText edit_height_i,edit_height_f,edit_weight;
        Button btn_bmi_calc;
        TextView txt_result ;
        LinearLayout llayout = findViewById(R.id.llayout);

        edit_weight = findViewById(R.id.edit_weight);
        edit_height_i = findViewById(R.id.edit_heighti);
        edit_height_f = findViewById(R.id.edit_heightf);
        btn_bmi_calc = findViewById(R.id.btn_calc_bmi);
        txt_result = findViewById(R.id.txt_result);

        btn_bmi_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int wt = Integer.parseInt(edit_weight.getText().toString());
                int htf = Integer.parseInt(edit_height_f.getText().toString());
                int hti = Integer.parseInt(edit_height_i.getText().toString());

                int totalIn = htf*12 + hti;
                double totalCm = totalIn*2.53;
                double totalM = totalCm/100;
                double bmi = wt/(totalM*totalM);

                if(bmi>25){
                    txt_result.setText("You are overweight");
                    llayout.setBackgroundColor(getResources().getColor(R.color.overw));
                }
                else if(bmi < 18){
                    txt_result.setText("You are underweight");
                    llayout.setBackgroundColor(getResources().getColor(R.color.underw));
                }
                else {
                    txt_result.setText("You are healthy");
                    llayout.setBackgroundColor(getResources().getColor(R.color.healthy));
                }
            }
        });

    }
}