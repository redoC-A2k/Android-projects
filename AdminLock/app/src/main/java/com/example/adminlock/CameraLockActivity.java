package com.example.adminlock;

import androidx.appcompat.app.AppCompatActivity;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.adminlock.ml.AgeclassNew;

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
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class CameraLockActivity extends CameraActivity {
    int imageSize = 200;
    int CAM_REQ_CODE=102;
    String TAG = "CAMERA LOCK ACTIVITY";
    CameraBridgeViewBase cameraBridgeViewBase;
    CascadeClassifier cascadeClassifier;
    Mat gray;
    // To keep track of activity's window focus
    boolean currentFocus;
    // To keep track of activity's foreground/background status
    boolean isPaused;
    static Mat croppedMat ;
    int output = -1;
    DevicePolicyManager deviceManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_lock);

        cameraBridgeViewBase = findViewById(R.id.opcamview);
        cameraBridgeViewBase.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_BACK);

        deviceManager = (DevicePolicyManager)
                getSystemService(Context. DEVICE_POLICY_SERVICE ) ;

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
                                    detectage(croppedMat);
                                    finish();
                                }
                            }
                        }
                    }
                }
                return gray;
            }
        });
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

    private void detectage(Mat scaledmat){
        try {
            AgeclassNew model = AgeclassNew.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 200, 200, 1}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4*imageSize*imageSize*1);
            byteBuffer.order(ByteOrder.nativeOrder());
            for(int i=0; i<scaledmat.rows();i++) {
                for (int j = 0; j < scaledmat.cols(); j++) {
                    byteBuffer.putFloat((float) (scaledmat.get(i,j)[0])/(1.0f));
                }
            }
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            AgeclassNew.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            TensorBuffer outputFeature1 = outputs.getOutputFeature1AsTensorBuffer();
            float op1[] = outputFeature0.getFloatArray();
            float op2[] = outputFeature1.getFloatArray();
            Log.i(TAG,"op1 "+op1[0]);//child
            Log.i(TAG,"op2 "+op2[0]);//adult
            // Releases model resources if no longer used.
            if(op1[0]<=0.5 && op2[0]>=0.5)
                output=1;
//                txtview.setText("You are above 10 yrs");
            else if(op1[0]>0.5 && op2[0]<=0.5)
                output=0;
//                txtview.setText("You are below 10 yrs");
            else if(op1[0]>0.5 && op1[0]>op2[0])
                output=0;
//                txtview.setText("You are below 10 yrs");
            else if(op2[0]>0.5 && op2[0]>op1[0])
                output=1;
//                txtview.setText("You are above 10 yrs");
            else if(op1[0]<0.5 && op2[0]<0.5)
                output = -1;
//                txtview.setText("Retake image");
            model.close();
            if (output==0){
                Intent intent = new Intent(CameraLockActivity.this, QueActivity.class);
                startActivity(intent);
            }
            else if (output==1){
                finish();
            }
            else {

            }
        } catch (IOException e) {
            // TODO Handle the exception
            Log.e(TAG,"Exception in model"+e);
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        // Activity's been resumed
//        Log.i(TAG,"Activity has been resumed");
//        isPaused = false;
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        // Activity's been paused
//        Log.i(TAG,"Activity has been paused");
//        isPaused = true;
//    }
//
//    @Override
//    protected void onUserLeaveHint() {
//        deviceManager.lockNow();
//        Log.i(TAG,"User has leaved");
//        super.onUserLeaveHint();
//    }
//
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        currentFocus = hasFocus;
//        if (!hasFocus) {
//            deviceManager.lockNow();
//        }
//        Log.i(TAG,"On window focus changed");
//    }

}