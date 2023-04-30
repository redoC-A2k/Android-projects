package com.example.opjcam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static Bitmap bp = null;
    static Mat rgba = null;
    final static int TAKE_FACE_REQ = 101;

    private static Bitmap convertMatToBitMap(Mat input){
        Bitmap bmp = null;
//        Mat rgb = new Mat();
//        Imgproc.cvtColor(input, rgb, Imgproc.COLOR_BGR2RGB);

        try {
            bmp = Bitmap.createBitmap(input.cols(), input.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(input, bmp);
        }
        catch (CvException e){
            Log.d("Exception",e.getMessage());
        }
        return bmp;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String TAG = "MAIN_ACTIVITY";
        Button btn = findViewById(R.id.goToCamBtn);
        final Thread[] t = new Thread[1];
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CamActivity.class);
                t[0] = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        startActivityForResult(intent,TAKE_FACE_REQ);
                    }
                });
                t[0].start();
            }
        });
//        Thread w = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (rgba==null){
////                    Thread y = Thread.currentThread();
//                    try {
//                        Thread.sleep(50);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//        while (rgba==null){
//            try {
//                Thread.sleep(50);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        if (rgba!=null){
//                    try {
//                        t[0].stop();
//                        t[0].interrupt();
//                    }
//                    catch (Exception e){
//                        e.printStackTrace();
//                        Log.w(TAG,"Exception occured");
//                    }
//                    Core.rotate(rgba,rgba,Core.ROTATE_90_COUNTERCLOCKWISE);
//                    bp = convertMatToBitMap(rgba);
//                    Log.d(TAG,"Bitmap is " + bp.toString());
//                    MediaStore.Images.Media.insertImage(getContentResolver(), bp, bp.toString() , "");
////                    imageView.refreshDrawableState();
//                }
//            }
//        });
//        w.start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_FACE_REQ){
            ImageView imageView = findViewById(R.id.imgview);
            imageView.setImageBitmap(convertMatToBitMap(rgba));
        }

    }
}