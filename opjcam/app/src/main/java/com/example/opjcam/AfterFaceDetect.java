package com.example.opjcam;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class AfterFaceDetect extends AppCompatActivity {
    String TAG="AFTER_FACE_DETECT";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_face_detect);

        Bitmap finaldetectface = (Bitmap) getIntent().getParcelableExtra("FaceImage");
        Log.d(TAG,"In After face detect activity");
        ImageView imgview = findViewById(R.id.imgview);
        imgview.setImageBitmap(finaldetectface);

    }


}