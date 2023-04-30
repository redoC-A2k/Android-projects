package com.example.ailock;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import com.example.ailock.ml.AgeclassNew;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MainActivity extends AppCompatActivity {

    int imageSize = 200;
    static Mat gray = null;
    Bitmap bp;
    final static int TAKE_FACE_REQ = 101;
    final static String TAG = "MAIN_ACTIVITY";
    final static int SELECT_CODE = 102;

    static{
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
            Log.e(TAG,"Error in initialization");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button selectbtn = findViewById(R.id.selectimg);
        if (OpenCVLoader.initDebug()){
            Log.d(TAG,"Working now");
        }
        selectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_CODE);
            }
        });

        Button cambtn = findViewById(R.id.cambtn);
        cambtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //        final Thread[] t = new Thread[1];
                Intent intent = new Intent(MainActivity.this, CamLockActivity.class);
//        t[0] = new Thread(new Runnable() {
//            @Override
//            public void run() {
                startActivityForResult(intent,TAKE_FACE_REQ);
//            }
//        });
//        t[0].start();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_FACE_REQ){
            ImageView imageView = findViewById(R.id.imgview);
            Bitmap bp = convertMatToBitMap(gray);
            Imgproc.resize(gray,gray,new Size(imageSize,imageSize),0,0,Imgproc.INTER_AREA);
            imageView.setImageBitmap(bp);
            detectage(bp,gray);
        }
//        else if(requestCode == SELECT_CODE){
//            try {
////                imageView.setImageBitmap(bp);
//                this.bp = MediaStore.Images.Media.getBitmap(this.getContentResolver(),data.getData());
//                Log.d(TAG,"Image selected");
//                if(OpenCVLoader.initDebug()){
//                    bp = Bitmap.createScaledBitmap(bp,imageSize,imageSize,false);
//                    Mat mat = new Mat(bp.getWidth(),bp.getHeight(), CvType.CV_8UC1);
//                    Utils.bitmapToMat(bp,mat);
//                    ImageView imgview = findViewById(R.id.imgview);
//                    imgview.setImageBitmap(bp);
//                    Log.i(TAG,""+mat.rows()+" "+mat.cols());
//                    detectage(bp,mat);
//                }
//            } catch (IOException e){
//                e.printStackTrace();
//            }
//        }
    }

    private void detectage(Bitmap bp,Mat scaledmat){
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
            TextView txtview = findViewById(R.id.txtview);
            if(op1[0]<=0.5 && op2[0]>=0.5)
                txtview.setText("You are above 10 yrs");
            else if(op1[0]>0.5 && op2[0]<=0.5)
                txtview.setText("You are below 10 yrs");
            else if(op1[0]>0.5 && op1[0]>op2[0])
                txtview.setText("You are below 10 yrs");
            else if(op2[0]>0.5 && op2[0]>op1[0])
                txtview.setText("You are above 10 yrs");
            else if(op1[0]<0.5 && op2[0]<0.5)
                txtview.setText("Retake image");
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
            Log.e(TAG,"Exception in model"+e);
        }
    }

    private static Bitmap convertMatToBitMap(Mat input){
        Bitmap bmp = null;
        try {
            bmp = Bitmap.createBitmap(input.cols(), input.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(input, bmp);
        }
        catch (CvException e){
            Log.d("Exception",e.getMessage());
        }
        return bmp;
    }

    private void graydetectage(Bitmap bp){
        try {
            bp = Bitmap.createScaledBitmap(bp,imageSize,imageSize,false);
            AgeclassNew model = AgeclassNew.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, imageSize, imageSize, 1}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4*imageSize*imageSize*3);
            byteBuffer.order(ByteOrder.nativeOrder());
            int [] intValues = new int[imageSize*imageSize];
            bp.getPixels(intValues,0,bp.getWidth(),0,0,bp.getWidth(),bp.getHeight());
            for(int i=0;i<20;i++){
                Log.d(this.TAG,Integer.toString(intValues[i]));
            }
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }
}