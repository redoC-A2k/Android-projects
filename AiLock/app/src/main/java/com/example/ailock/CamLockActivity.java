package com.example.ailock;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
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
import java.util.Collections;
import java.util.List;

public class CamLockActivity extends CameraActivity {

    String TAG="CAM_ACTIVITY";
    int CAM_REQ_CODE=102;
    CameraBridgeViewBase cameraBridgeViewBase;
    CascadeClassifier cascadeClassifier;
    Mat gray;
    static Mat croppedMat ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);

        getPermissions();
        cameraBridgeViewBase = findViewById(R.id.opcamview);
        cameraBridgeViewBase.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_BACK);

        cameraBridgeViewBase.setCvCameraViewListener(new CameraBridgeViewBase.CvCameraViewListener2() {
            @Override
            public void onCameraViewStarted(int width, int height) {
                gray = new Mat();
            }

            @Override
            public void onCameraViewStopped() {
                gray.release();
            }


            @Override
            public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
//                Mat rgb = inputFrame.rgba();
                gray = inputFrame.gray();
                if(cascadeClassifier != null){
                    MatOfRect matOfRect = new MatOfRect();
                    cascadeClassifier.detectMultiScale(gray,matOfRect,1.2,4,0);
                    Rect[] rects = matOfRect.toArray();
                    if (rects.length == 1){
                        Rect rectCrop = new Rect (
                                rects[0].x-rects[0].width/2,
                                rects[0].y-rects[0].height/2,
                                rects[0].width+rects[0].width,
                                rects[0].height+rects[0].height
                        );
                        Imgproc.rectangle(
                                gray,
                                new Point(rectCrop.x,rectCrop.y),
                                new Point(rectCrop.x+rectCrop.width,rectCrop.y+rectCrop.height),
                                new Scalar(0, 0, 255),
                                2);
                        Mat croppedimg ;
                        if (
                                0 <= rectCrop.x &&
                                0 <= rectCrop.width &&
                                rectCrop.x + rectCrop.width <= gray.cols() &&
                                0 <= rectCrop.y &&
                                0 <= rectCrop.height &&
                                rectCrop.y + rectCrop.height <= gray.rows()
                        ) {
                            croppedimg = gray.submat(rectCrop).clone();
                            MatOfRect tempMatOfRect = new MatOfRect();
                            cascadeClassifier.detectMultiScale(croppedimg,tempMatOfRect,1.2,4,0);
                            Rect[] croppedRects = tempMatOfRect.toArray();
                            if(croppedRects.length == 1) {
                                Rect croppedRect = croppedRects[0];
                                if (
                                        0 <= croppedRect.x &&
                                        0 <= croppedRect.width &&
                                        croppedRect.x + croppedRect.width <= croppedimg.cols() &&
                                        0 <= croppedRect.y &&
                                        0 <= croppedRect.height &&
                                        croppedRect.y + croppedRect.height <= croppedimg.rows()
                                ) {
                                    Rect tempRectCrop = new Rect(
                                            croppedRect.x,
                                            croppedRect.y,
                                            croppedRect.width,
                                            croppedRect.height
                                    );
//                                    MainActivity.gray = croppedimg.submat(tempRectCrop).clone();
                                    croppedMat = croppedimg.submat(tempRectCrop).clone();
//                                    DetectAge
                                    finish();
                                }
                            }
                        }
                    }
                }
                return gray;
            }
        });
        // When opencv loads
        if (OpenCVLoader.initDebug()){
            Log.d(TAG,"Success in loading opencv4");
            cameraBridgeViewBase.enableView();
            String classifierfilename =  "lbpcascade_frontalface.xml";

            try {
                InputStream inputStream = getResources().openRawResource(R.raw.lbpcascade_frontalface);
                File file = new File(getDir("cascade",MODE_PRIVATE),classifierfilename);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                byte[] data = new byte[4096];
                int read_bytes;
                while ((read_bytes = inputStream.read(data))!=-1){
                    fileOutputStream.write(data,0,read_bytes);
                }
                cascadeClassifier = new CascadeClassifier(file.getAbsolutePath());
                if (cascadeClassifier.empty()) cascadeClassifier = null;
                inputStream.close();
                fileOutputStream.close();
                file.delete();
            }
            catch (FileNotFoundException f){
                f.printStackTrace();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        } else {
            Log.d(TAG,"Problem in loading opencv4");
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length>0 && grantResults[0]!=PackageManager.PERMISSION_GRANTED){
            getPermissions();
        }
    }
}