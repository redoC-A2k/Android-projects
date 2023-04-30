package com.example.opjcam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class CamActivity extends CameraActivity {

    String TAG="CAM ACTIVITY";
    int CAM_REQ_CODE = 102;
    CameraBridgeViewBase cameraBridgeViewBase;
    CascadeClassifier cscl;

    Mat rgb,grey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);

        getPermissions();
        cameraBridgeViewBase = findViewById(R.id.opcamview);
        cameraBridgeViewBase.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_FRONT);

        cameraBridgeViewBase.setCvCameraViewListener(new CameraBridgeViewBase.CvCameraViewListener2() {
            @Override
            public void onCameraViewStarted(int width, int height) {
                rgb = new Mat();
                grey = new Mat();
            }

            @Override
            public void onCameraViewStopped() {
                rgb.release();
                grey.release();
            }

            @Override
            public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
                rgb = inputFrame.rgba();
                grey = inputFrame.gray();
                if (cscl!=null){
                    MatOfRect facedects = new MatOfRect();
                    cscl.detectMultiScale(grey,facedects,1.2,4,0,new Size(),new Size());
                    // System.out.println("Detected "+facedects.toArray().length+" faces");
                    Rect[] rects = facedects.toArray();
                    if (rects.length==1){
                        Rect trect = rects[0];
                        Rect rectCrop = new Rect(
                                trect.x-trect.width/3,
                                trect.y-trect.height/4,
                                (3*trect.width+2*trect.width)/3,
                                trect.height+trect.height/2);
//                                (3*trect.height+2*trect.height)/3);
                        Imgproc.rectangle(
                                rgb,                                               // where to draw the box
                                new Point(rectCrop.x, rectCrop.y),                            // bottom left
                                new Point(rectCrop.x + rectCrop.width, rectCrop.y + rectCrop.height), // top right
                                new Scalar(0, 0, 255),
                                3                                                     // RGB colour
                        );
//                        cameraBridgeViewBase.disableView();
//                        cameraBridgeViewBase.setVisibility(SurfaceView.INVISIBLE);

                        Intent intent = new Intent();
                        intent.putExtra("Found",true);
                        setResult(MainActivity.TAKE_FACE_REQ,intent);
                        MainActivity.rgba = rgb.submat(rectCrop).clone();
                        finish();
                    }
                }
                return rgb;
            }
        });
        if (OpenCVLoader.initDebug()){
            Log.d(TAG,"Success in loading opencv4");
            cameraBridgeViewBase.enableView();
//                }
//            });
            try {
                InputStream ipst = getResources().openRawResource(R.raw.lbpcascade_frontalface);
                File file = new File(getDir("cascade",MODE_PRIVATE),"lbpcascade_frontalface.xml");
                FileOutputStream fout = new FileOutputStream(file);

                byte [] data = new byte[4096];
                int read_bytes;

                while ((read_bytes = ipst.read(data))!=-1){
                    fout.write(data,0,read_bytes);
                }

                cscl = new CascadeClassifier(file.getAbsolutePath());
                if (cscl.empty()) cscl = null;

                ipst.close();
                fout.close();
                file.delete();
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        else{
            Log.d(TAG, "Problem in loading opencv4");
        }
    }

    void getPermissions(){
        if (checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA},CAM_REQ_CODE);
        }
    }

    @Override
    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(cameraBridgeViewBase);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraBridgeViewBase.enableView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraBridgeViewBase.disableView();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraBridgeViewBase.disableView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length>0 && grantResults[0]!=PackageManager.PERMISSION_GRANTED){
            getPermissions();
        }
    }
}