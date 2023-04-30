 package com.example.progocv;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

 public class MainActivity extends AppCompatActivity {

     ImageView imgview;
     Bitmap bp;
     Mat mat;
     int SELECT_CODE = 100;
     int CAMERA_CODE = 101;
     int CAMER_PERM_REQ_CODE = 102;
     Button selectbtn,camerabtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(OpenCVLoader.initDebug()){
            Log.d("LOADED","Success in open cv 4");
        }
        else{
            Log.d("LOADED", "Error while loading ");
        }

        getPermissions();

        camerabtn=findViewById(R.id.camerabtn);
        selectbtn=findViewById(R.id.selectbtn);
        imgview=findViewById(R.id.imgview);

        selectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_CODE);
            }
        });

        camerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_CODE);
            }
        });
    }

     @Override
     protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         if (requestCode==SELECT_CODE && data!=null){
             try {
                 bp = MediaStore.Images.Media.getBitmap(this.getContentResolver(),data.getData());
                 imgview.setImageBitmap(bp);

                 mat = new Mat();
                 Utils.bitmapToMat(bp,mat);

                 Imgproc.cvtColor(mat,mat,Imgproc.COLOR_RGB2GRAY);
                 Utils.matToBitmap(mat,bp);
                 imgview.setImageBitmap(bp);
             } catch (IOException e){
                 e.printStackTrace();
             }
         }
         if (requestCode==CAMERA_CODE && data!=null){
             bp = (Bitmap)data.getExtras().get("data");
             imgview.setImageBitmap(bp);

             mat=new Mat();
             Utils.bitmapToMat(bp,mat);
         }
     }

     void getPermissions(){
        if (checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA},CAMER_PERM_REQ_CODE);
        }
     }

     @Override
     public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
         super.onRequestPermissionsResult(requestCode, permissions, grantResults);
         if (requestCode == CAMER_PERM_REQ_CODE && grantResults.length>0){
             if (grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                 getPermissions();
             }
         }
     }
 }