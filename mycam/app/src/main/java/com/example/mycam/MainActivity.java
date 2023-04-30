package com.example.mycam;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    ImageView imgCam;
    private  final int CAMERA_REQ_CODE = 100;

    ActivityResultLauncher<Intent> iCamResultLauncher = registerForActivityResult()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgCam = findViewById(R.id.ImgCam);
        Button cambtn = findViewById(R.id.CamBtn);

        cambtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent icam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(icam,CAMERA_REQ_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode==CAMERA_REQ_CODE){
                Bitmap img = (Bitmap)data.getExtras().get("data");
                imgCam.setImageBitmap(img);
            }
        }
    }
}